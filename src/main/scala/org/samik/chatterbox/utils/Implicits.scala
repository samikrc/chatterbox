package org.samik.chatterbox.utils

object Implicits
{
    implicit class JsonUtils(value: Json.Value)
    {
        def asStringArray = value.asArray.map(_.asString)
    }
}
