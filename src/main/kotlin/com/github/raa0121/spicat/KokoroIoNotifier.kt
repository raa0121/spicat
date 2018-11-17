package com.github.raa0121.spicat

import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.logging.Logger
import com.github.raa0121.spicat.system.envVars
import com.github.raa0121.spicat.system.channel_id
import com.github.raa0121.spicat.system.seacret
import com.github.raa0121.kokoroio.client.*

class KokoroIoNotifier {
    private val logger = Logger.getLogger(KokoroIoNotifier::javaClass.name)

    private val channel by lazy {
        envVars[channel_id]
    }
    private val seacret_key by lazy {
        envVars[seacret]
    }

    private val bot = BotApi()

    fun postMessage(text: String) {
        postMessage(channel, text)
    }

    private fun postMessage(channel_id: String, message: String) = runBlocking<Unit> {
        launch(Dispatchers.Default){
            bot.postV1BotChannelsChannelIdMessages(channel_id, message, "spicat", false, false)
        }
    }
}