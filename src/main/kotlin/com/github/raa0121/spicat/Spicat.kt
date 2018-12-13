package com.github.raa0121.spicat

import com.github.raa0121.kokoroio.model.MessageEntity
import com.ryanharter.ktor.moshi.moshi
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.bukkit.plugin.java.JavaPlugin
import io.swagger.client.infrastructure.Serializer
import java.io.File
import java.lang.Exception
import java.util.concurrent.TimeUnit

class Spicat : JavaPlugin() {

    lateinit var notifier: KokoroIoNotifier

    private val webserver = embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            moshi(Serializer.moshi)
        }
        routing {
            get("/") {
                call.respondText("hi", ContentType.Text.Plain)
            }
            post("/") {
                try {
                    val post = call.receive<MessageEntity>()
                    if (post.channel?.channel_name == "spicat") {
                        if (post.raw_content == "/list") {
                            notifier.postMessage(server.onlinePlayers.joinToString(separator = ", ") { player -> player.displayName })
                        } else server.broadcastMessage(
                                "[kokoro-io/%s] %s: %s".format(
                                        post.channel?.channel_name,
                                        post.profile?.screen_name,
                                        post.raw_content
                                )
                        )
                    }
                } catch (e: Exception) {
                    logger.warning(e.toString())
                }
            }
        }
    }

    override fun onEnable() {
        // Plugin startup logic
        notifier = KokoroIoNotifier(Config.kokoroioBotAccessToken, Config.kokoroioChannelId)
        notifier.postMessage("Server starting.")
        webserver.start(wait = false)
        server.pluginManager.registerEvents(KokoroIoEventListener(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
        notifier.postMessage("Server stopping.")
        webserver.stop(gracePeriod = 10, timeout = 10, timeUnit = TimeUnit.SECONDS)
    }

    val jarFile: File
        get() = this.file
}
