package com.github.raa0121.spicat

import org.bukkit.plugin.java.JavaPlugin

class Spicat : JavaPlugin() {

    override fun onEnable() {
        // Plugin startup logic
        logger.info("hello kotlin")

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
