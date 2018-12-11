package com.github.raa0121.spicat

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import java.io.*
import java.util.*
import java.util.jar.JarFile
import java.util.logging.Logger


val SpicatInstance : Spicat by lazy {
    Bukkit.getPluginManager().getPlugin("Spicat") as Spicat
}

@Suppress("unused")
inline fun <reified T> T.logger(): Logger = Logger.getLogger(T::class.java.simpleName)

fun <E> List<E>.random(): E? = if (size > 0) get(Random().nextInt(size)) else null
fun <E> List<E>.random(defVal: E): E = if (size > 0) get(Random().nextInt(size)) else defVal

fun stripColor(source: String?): String? =  if (source == null) null else ChatColor.stripColor(source)

/**
 * Generate file from jar
 * @param jarFile
 * @param sourceFilePath Filename in resources directory
 * @param targetFile
 * @param isBinary
 */
fun copyFileFromJar(
        jarFile: File, sourceFilePath: String, targetFile: File, isBinary: Boolean) {
    val parent = targetFile.parentFile
    if (!parent.exists()) {
        parent.mkdirs()
    }

    try {
        val jar = JarFile(jarFile)
        val zipEntry = jar.getEntry(sourceFilePath)

        jar.getInputStream(zipEntry)?.use { inputStream ->
            FileOutputStream(targetFile).use { outputStream ->
                if (isBinary) {
                    inputStream.copyTo(outputStream)
                } else {
                    BufferedWriter(
                            OutputStreamWriter(outputStream, "UTF-8")
                    ).use { writer ->
                        BufferedReader(InputStreamReader(inputStream, "UTF-8")).use { reader ->
                            reader.lines().forEach { writer.appendln(it) }
                        }
                        writer.flush()
                    }
                }

                outputStream.flush()
            }
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
