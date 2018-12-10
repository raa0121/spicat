package com.github.raa0121.spicat

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.ServerCommandEvent

class KokoroIoEventListener : Listener {
    private val logger = logger()

    private val notifier: KokoroIoNotifier? =
            if (Config.kokoroioBotAccessToken.isNotBlank() && Config.kokoroioChannelId.isNotBlank()) {
                KokoroIoNotifier(Config.kokoroioBotAccessToken, Config.kokoroioChannelId)
            } else {
                logger.info("Token and channel id in Spicat/config.yml is empty, kokoro.io notification is disabled.")
                null
            }

    @EventHandler
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        notifier?.postMessage(stripColor(event.joinMessage)!!)
    }

    @EventHandler
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        notifier?.postMessage(stripColor(event.quitMessage)!!)
    }

    @EventHandler
    fun onPlayerDeathEvent(event: PlayerDeathEvent) {
        notifier?.postMessage(event.deathMessage)
    }

    @EventHandler
    fun onAsyncPlayerChatEvent(event: AsyncPlayerChatEvent) {
        notifier?.postMessage(String.format(event.format, event.player.displayName, event.message))
    }

    @EventHandler
    fun onServerCommandEvent(event: ServerCommandEvent) {
        if (event.command.indexOf("say") >= 0) {
            notifier?.postMessage("天の声：" + event.command.replace("say ", ""))
        }
    }
}