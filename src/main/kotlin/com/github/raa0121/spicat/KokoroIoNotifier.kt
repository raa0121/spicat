package com.github.raa0121.spicat

import com.github.raa0121.kokoroio.client.BotApi
import com.github.raa0121.spicat.system.channelId
import com.github.raa0121.spicat.system.envVars
import com.github.raa0121.spicat.system.secret
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.logging.Logger

class KokoroIoNotifier {
    private val logger = Logger.getLogger(KokoroIoNotifier::javaClass.name)

    private val channel by lazy {
        envVars[channelId]
    }
    private val secretKey by lazy {
        envVars[secret]
    }

    private val bot = BotApi()

    fun postMessage(text: String) {
        postMessage(channel, text)
    }

    private fun postMessage(channelId: String, message: String) = GlobalScope.launch {
        bot.postV1BotChannelsChannelIdMessages(secretKey, channelId, message, "spicat", false, false)
    }
}
