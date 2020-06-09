package org.samik.chatterbox

import akka.actor.{Actor, ActorLogging}
import org.samik.chatterbox.utils.Json

import scala.io.Source

/**
  * Class to execute a generic API endpoint and return a response.
  */
class APIExecutor(modelName: String) extends Actor with ActorLogging
{
    override def receive: Receive =
    {
        case apiDetails: APIDetails =>
        {
            val response = Source.fromURL(apiDetails.url).mkString
            sender() ! Json.parse(response)
        }
        case _ => log.info("Invalid message")
    }
}

case class APIDetails(url: String)
