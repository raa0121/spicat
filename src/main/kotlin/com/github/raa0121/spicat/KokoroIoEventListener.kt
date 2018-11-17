package com.github.raa0121.spicat

import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

class KokoroIoEventListener : Listener {
    @EventHandler
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        KokoroIoNotifier().postMassage(event.joinMessage)
    }
}