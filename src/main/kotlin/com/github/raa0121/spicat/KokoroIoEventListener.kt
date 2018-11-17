package com.github.raa0121.spicat

import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class KokoroIoEventListener : Listener {
    @EventHandler
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        KokoroIoNotifier().postMessage(event.joinMessage)
    }
    @EventHandler
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        KokoroIoNotifier().postMessage(event.quitMessage)
    }

    @EventHandler
    fun onPlayerDeathEvent(event: PlayerDeathEvent) {
        KokoroIoNotifier().postMessage(event.deathMessage)
    }

    @EventHandler
    fun onAsyncPlayerChatEvent(event: AsyncPlayerChatEvent) {
        KokoroIoNotifier().postMessage(String.format(event.format, event.player.displayName, event.message))
    }
}