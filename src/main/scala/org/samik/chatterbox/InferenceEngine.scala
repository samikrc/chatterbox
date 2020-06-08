package org.samik.chatterbox

import akka.actor.{Actor, ActorLogging}
import org.samik.chatterbox.utils.Json

/**
  * Class to run inferencing using ML and non-ML models
  */
class InferenceEngine(modelName: String) extends Actor with ActorLogging
{

    override def receive = ???
}
