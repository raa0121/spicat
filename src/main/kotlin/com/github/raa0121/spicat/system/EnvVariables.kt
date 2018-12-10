package com.github.raa0121.spicat.system

val envVars = Variables(SystemEnvKeyVals())

//val secret = StrVar("KOKOROIO_SECRET")
//val channelId = StrVar("KOKOROIO_CHANNEL_ID")

private class SystemEnvKeyVals : KeyVals {
    override fun get(key: String): String? = System.getenv(key)
}