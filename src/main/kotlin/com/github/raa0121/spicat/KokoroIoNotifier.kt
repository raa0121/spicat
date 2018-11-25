package com.github.raa0121.spicat

import kotlinx.coroutines.launch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Dispatchers
import java.util.logging.Logger
import com.github.raa0121.spicat.system.envVars
import com.github.raa0121.spicat.system.channel_id
import com.github.raa0121.kokoroio.client.*
import com.github.raa0121.spicat.system.secret

class KokoroIoNotifier {
    private val logger = Logger.getLogger(KokoroIoNotifier::javaClass.name)

    private val channel by lazy {
        envVars[channel_id]
    }
    private val secret_key by lazy {
        envVars[secret]
    }

    private val bot = BotApi()

    fun postMessage(text: String) {
        postMessage(channel, text)
    }

    private fun postMessage(channel_id: String, message: String) = GlobalScope.launch(Dispatchers.Unconfined) {
            bot.postV1BotChannelsChannelIdMessages(secret_key, channel_id, message, "spicat", false, false)
    }
}