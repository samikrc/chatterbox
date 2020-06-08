package org.samik.chatterbox

import akka.actor.{Actor, ActorLogging}

/**
  * Actor for logging messages to a message bus, e.g., Kafka
  */
class EventLogManager extends Actor with ActorLogging
{
    override def receive: Receive = ???
}
