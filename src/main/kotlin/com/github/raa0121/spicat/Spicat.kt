package com.github.raa0121.spicat

import com.github.raa0121.kokoroio.model.MessageEntity
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.concurrent.TimeUnit

class Spicat : JavaPlugin() {

    private val webserver = embeddedServer(Netty, 8080) {
        routing {
            get("/") {
                call.respondText("hi", ContentType.Text.Plain)
            }
            post("/") {
                val post = call.receive<MessageEntity>()
                server.broadcastMessage(
                        "[kokoro-io/%s] %s: %s".format(
                                post.channel?.channel_name,
                                post.profile?.screen_name,
                                post.raw_content
                        )
                )
                call.respond(mapOf("OK" to true))
            }
        }
    }

    override fun onEnable() {
        // Plugin startup logic
        webserver.start(wait = false)
        server.pluginManager.registerEvents(KokoroIoEventListener(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
        webserver.stop(gracePeriod = 100, timeout = 100, timeUnit = TimeUnit.SECONDS)
    }

    val jarFile: File
        get() = this.file
}
