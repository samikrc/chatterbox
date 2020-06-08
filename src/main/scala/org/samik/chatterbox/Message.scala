package org.samik.chatterbox

/**
  * Class for processing messages from POST calls.
  */
class Message(input: Map[String, String])
{
    val sessionId = input("sessionId")
    val appId = input("appId")
    val body = input("body")
    val localTS = input("localTS")
}
