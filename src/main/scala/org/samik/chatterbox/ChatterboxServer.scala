package org.samik.chatterbox

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, FromRequestUnmarshaller, PredefinedFromEntityUnmarshallers}
import akka.routing.RoundRobinPool
import akka.stream.ActorMaterializer
import akka.util.Timeout
import org.samik.chatterbox.utils.Json

import scala.collection.mutable
import scala.io.Source

/**
  *
  */
object ChatterboxServer extends App
{
    implicit val system = ActorSystem("Chatterbox Server")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    import scala.concurrent.duration._
    // Timeout for future request
    implicit def timeout: Timeout = 5 seconds

    /**
      * Define helper directive for converting the map of form fields to an WebInput object
      */
    private val messageFromFormFields: Directive1[Message] = formFieldMap.map(fields => new Message(fields))

    private final val appSvcMgrInstanceCount: Int = 10

    private val appServiceManagers = new mutable.HashMap[String, ActorRef]()
    private val modelExecutors = new mutable.HashMap[String, ActorRef]()

    val route =
        path("messages")
        {
            (post & messageFromFormFields) { message: Message =>
                // Get or create an ActorRef for the appId of the message
                val msgProcessor = if(appServiceManagers.contains(message.appId))
                    appServiceManagers.get(message.appId).asInstanceOf[ActorRef]
                else
                {
                    // Get the corresponding app file from Resources
                    val appFileContent = Source.fromInputStream(this.getClass.getResourceAsStream(s"/apps/${message.appId}.json")).getLines().mkString
                    // Start an AppServiceManager with specific count of threads.
                    val processor = system.actorOf(Props(
                        new AppServiceManager(Json.parse(appFileContent))).withRouter(RoundRobinPool(appSvcMgrInstanceCount)), name = message.appId)
                    // Add to the HashMap
                    appServiceManagers(message.appId) = processor
                    processor
                }
                val responseF = (msgProcessor ? message).mapTo[String]
                onSuccess(responseF){ response =>
                    complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, response))
                }
                complete(StatusCodes.InternalServerError)
            }
        }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/")
}
