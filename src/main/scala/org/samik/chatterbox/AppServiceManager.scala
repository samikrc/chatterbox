package org.samik.chatterbox

import akka.actor.{Actor, ActorLogging}
import org.samik.chatterbox.utils._
import org.samik.chatterbox.utils.Implicits._

class AppServiceManager(appConfig: Json.Value) extends Actor with ActorLogging
{
    // Get the conversation nodes from the JSON config file
    protected val nodes = appConfig("nodes").asArray.map(v =>
    {
        val map = v.asMap
        Node(map.get("name").get.asString, map.get("models").map(_.asStringArray), map.get("models").map(_.asStringArray), map.get("models").map(_.asStringArray))
    })
    protected val models = appConfig("models").asArray.map(v => {
        val map = v.asMap
        Model(map.get("name").get.asString, map.get("description").map(_.asString), map.get("location").get.asString, map.get("url").map(_.asString))
    })
    protected val conditions = appConfig("conditions").asArray.map(v => Condition(v.asMap("name").asString, v.asMap("script").asString))


    override def receive: Receive =
    {
        case message: Message =>
        {

        }
    }
}

case class Node(name: String, models: Option[Array[String]], entities: Option[Array[String]], responseList: Option[Array[String]])
case class Model(name: String, description: Option[String], location: String, url: Option[String])
case class Condition(name: String, script: String)
/*
case class Node(data: Map[String, Json.Value])
{
    val name = data("name").asString
    val models = Option(data("models").asStringArray)
    val entities = Option(data("entities").asStringArray)
    val responseList = Option(data("responseList").asStringArray)
}

case class Model(data: Map[String, Json.Value])
{
    val name = data("name").asString
    val description = Option(data("description").asString)
    val location = data("location").asString
    val url = Option(data("url").asString)
}

case class Condition(data: Map[String, Json.Value])
{
    val name = data("name").asString
    val script = data("script").asString
}

case class AppFlow(data: Map[String, Json.Value], nodes: Array[Node])
{
    val conditions = data("conditions").asStringArray
    val ifPositiveNode = nodes.find(p => p.name == data("if-positive-node").asString).get
    val ifNegativeNode = nodes.find(p => p.name == data("if-negative-node").asString).get
}
*/