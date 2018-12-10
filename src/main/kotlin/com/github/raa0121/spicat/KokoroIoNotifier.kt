package com.github.raa0121.spicat

import com.github.raa0121.kokoroio.client.BotApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class KokoroIoNotifier(val token: String, val channelId: String) {
    private val logger = logger()

    private val bot = BotApi()

    fun postMessage(text: String) {
        postMessage(channelId, text)
    }

    private fun postMessage(channelId: String, message: String) = GlobalScope.launch {
        bot.postV1BotChannelsChannelIdMessages(token, channelId, message, "spicat", false, false)
    }
}
