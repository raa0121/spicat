package com.github.raa0121.spicat.modules

import com.github.raa0121.spicat.logger
import com.github.raa0121.spicat.playSound
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerToggleSneakEvent

class BearFallDamage : Listener {
    private val logger = logger()
    val playerTimeList = hashMapOf<Player, Long>()

    @EventHandler
    fun onPlayerToggleSneakEvent(event: PlayerToggleSneakEvent) {
        val currentTime = event.player.world.fullTime

        playerTimeList[event.player]?.let {
            if (currentTime - it < 20) return
        }
        playerTimeList[event.player] = currentTime
    }

    @EventHandler
    fun onEntityDamageEvent(event: EntityDamageEvent) {
        if (event.entity !is Player || event.cause != EntityDamageEvent.DamageCause.FALL) return

        val player = event.entity as Player
        playerTimeList[player]?.let { sneakedTime ->
            val timeDiff = event.entity.world.fullTime - sneakedTime
            if (timeDiff > 3) return

            // FIXME: Doesn't work
            // event.isCancelled = true

            event.damage -= 5
            if (event.damage < 0) event.damage = 0.0

            event.entity.location.playSound(Sound.ENTITY_CAT_AMBIENT, pitch = 1.2f)
        }

        playerTimeList[player] = 0
    }

}

