package com.github.raa0121.spicat

import org.bukkit.plugin.java.JavaPlugin

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.receive
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.util.concurrent.TimeUnit

import com.github.raa0121.kokoroio.model.*

class Spicat : JavaPlugin() {

    private val webserver = embeddedServer(Netty, 8080, module = Application::module)
    override fun onEnable() {
        // Plugin startup logic
        webserver.start(wait = false)
        server.pluginManager.registerEvents(KokoroIoEventListener(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
        webserver.stop(gracePeriod = 100, timeout = 100, timeUnit = TimeUnit.SECONDS)
    }

}

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Routing) {
        get("/") {
            call.respondText("hi", ContentType.Text.Plain)
        }
        post("/") {
            val post = call.receive<MessageEntity>()

            call.respond(mapOf("OK" to true))
        }
    }
}