package com.github.raa0121.spicat

import java.io.File

object Config {
    init {
        reload()
    }

    lateinit var kokoroioBotAccessToken: String
    lateinit var kokoroioChannelId: String

    fun reload() {
        File(SpicatInstance.dataFolder, "config.yml").let {
            if (!it.exists()) {
                copyFileFromJar(SpicatInstance.jarFile, "config_default.yml", it, false)
            }
        }

        SpicatInstance.reloadConfig()
        val config = SpicatInstance.config

        kokoroioBotAccessToken = config.getString("kokoroio-bot-access-token", "")
        kokoroioChannelId = config.getString("kokoroio-channel-id", "")
    }

}