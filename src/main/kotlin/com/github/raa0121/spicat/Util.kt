package com.github.raa0121.spicat

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.io.*
import java.lang.reflect.InvocationTargetException
import java.util.jar.JarFile
import java.util.logging.Logger


val SpicatInstance : Spicat by lazy {
    Bukkit.getPluginManager().getPlugin("Spicat") as Spicat
}

/**
 * 現在動作中のCraftBukkitが、v1.7.8 以上かどうかを確認する
 * @return v1.7.8以上ならtrue、そうでないならfalse
 */
val isCB178orLater: Boolean by lazy {
    isUpperVersion(Bukkit.getBukkitVersion(), "1.7.8")
}

/**
 * 現在動作中のCraftBukkitが、v1.9 以上かどうかを確認する
 * @return v1.9以上ならtrue、そうでないならfalse
 */
val isCB19orLater: Boolean by lazy {
    isUpperVersion(Bukkit.getBukkitVersion(), "1.9")
}

/**
 * 現在接続中のプレイヤーを全て取得する
 * @return 接続中の全てのプレイヤー
 */
@Suppress("UNCHECKED_CAST")
val onlinePlayers: ArrayList<Player>
    get() {
        // CB179以前と、CB1710以降で戻り値が異なるため、
        // リフレクションを使って互換性を（無理やり）保つ。
        try {
            if (Bukkit::class.java.getMethod("getOnlinePlayers", *arrayOfNulls(0)).returnType == Collection::class.java) {
                val temp = Bukkit::class.java.getMethod("getOnlinePlayers", *arrayOfNulls(0))
                        .invoke(null, *arrayOfNulls(0)) as Collection<*>
                return ArrayList(temp as Collection<Player>)
            } else {
                val temp = Bukkit::class.java.getMethod("getOnlinePlayers", *arrayOfNulls(0))
                        .invoke(null, *arrayOfNulls(0)) as Array<Player>
                val players = ArrayList<Player>()
                for (t in temp) {
                    players.add(t)
                }
                return players
            }
        } catch (ex: NoSuchMethodException) {
            // never happen
        } catch (ex: InvocationTargetException) {
            // never happen
        } catch (ex: IllegalAccessException) {
            // never happen
        }

        return ArrayList()
    }

/**
 * 現在のサーバー接続人数を返します。
 * @return サーバー接続人数
 */
val onlinePlayersCount: Int
    get() = onlinePlayers.size

@Suppress("unused")
inline fun <reified T> T.logger(): Logger = Logger.getLogger(T::class.java.simpleName)

/**
 * jarファイルの中に格納されているファイルを、jarファイルの外にコピーするメソッド
 * @param jarFile jarファイル
 * @param targetFile コピー先
 * @param sourceFilePath コピー元
 * @param isBinary バイナリファイルかどうか
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
                            // CB190以降は、書き出すファイルエンコードにUTF-8を強制する。see issue #141.
                            if (isCB19orLater) OutputStreamWriter(outputStream, "UTF-8")
                            else OutputStreamWriter(outputStream)
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

/**
 * 文字列内のカラーコード候補（&a）を、カラーコード（§a）に置き換えする
 * @param source 置き換え元の文字列
 * @return 置き換え後の文字列
 */
fun replaceColorCode(source: String?): String? {
    return if (source == null) null else ChatColor.translateAlternateColorCodes('&', source)
}

/**
 * 文字列に含まれているカラーコード（§a）を除去して返す
 * @param source 置き換え元の文字列
 * @return 置き換え後の文字列
 */
fun stripColor(source: String?): String? {
    return if (source == null) null else ChatColor.stripColor(source)
}

/**
 * 指定された文字数のアスタリスクの文字列を返す
 * @param length アスタリスクの個数
 * @return 指定された文字数のアスタリスク
 */
fun getAstariskString(length: Int): String {
    val buf = StringBuilder()
    for (i in 0 until length) {
        buf.append("*")
    }
    return buf.toString()
}

/**
 * カラー表記の文字列を、ChatColorクラスに変換する
 * @param color カラー表記の文字列
 * @return ChatColorクラス
 */
fun changeToChatColor(color: String): ChatColor {

    return if (isValidColor(color)) {
        ChatColor.valueOf(color.toUpperCase())
    } else ChatColor.WHITE
}

/**
 * カラー表記の文字列を、カラーコードに変換する
 * @param color カラー表記の文字列
 * @return カラーコード
 */
fun changeToColorCode(color: String): String {

    return "&" + changeToChatColor(color).char
}

/**
 * ChatColorで指定可能な色かどうかを判断する
 * @param color カラー表記の文字列
 * @return 指定可能かどうか
 */
fun isValidColor(color: String): Boolean {

    for (c in ChatColor.values()) {
        if (c.name == color.toUpperCase()) {
            return true
        }
    }
    return false
}

/**
 * カラーコードかどうかを判断する
 * @param color カラー表記の文字列
 * @return 指定可能かどうか
 */
fun isValidColorCode(code: String?): Boolean {

    return code?.matches("&[0-9a-f]".toRegex()) ?: false
}

/**
 * 指定されたバージョンが、基準より新しいバージョンかどうかを確認する
 * @param version 確認するバージョン
 * @param border 基準のバージョン
 * @return 基準より確認対象の方が新しいバージョンかどうか<br></br>
 * ただし、無効なバージョン番号（数値でないなど）が指定された場合はfalseに、
 * 2つのバージョンが完全一致した場合はtrueになる。
 */
fun isUpperVersion(version: String, border: String): Boolean {
    @Suppress("NAME_SHADOWING") var version = version

    val hyphen = version.indexOf("-")
    if (hyphen > 0) {
        version = version.substring(0, hyphen)
    }

    val versionArray = version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val versionNumbers = IntArray(versionArray.size)
    for (i in versionArray.indices) {
        if (!versionArray[i].matches("[0-9]+".toRegex()))
            return false
        versionNumbers[i] = Integer.parseInt(versionArray[i])
    }

    val borderArray = border.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val borderNumbers = IntArray(borderArray.size)
    for (i in borderArray.indices) {
        if (!borderArray[i].matches("[0-9]+".toRegex()))
            return false
        borderNumbers[i] = Integer.parseInt(borderArray[i])
    }

    var index = 0
    while (versionNumbers.size > index && borderNumbers.size > index) {
        if (versionNumbers[index] > borderNumbers[index]) {
            return true
        } else if (versionNumbers[index] < borderNumbers[index]) {
            return false
        }
        index++
    }
    return borderNumbers.size == index
}

/**
 * 指定された名前のオフラインプレイヤーを取得する
 * @param name プレイヤー名
 * @return オフラインプレイヤー
 */
fun getOfflinePlayer(name: String): OfflinePlayer? {
    @Suppress("DEPRECATION") val player = Bukkit.getOfflinePlayer(name)
    return if (player == null || !player.hasPlayedBefore() && !player.isOnline) null else player
}

/**
 * 指定された名前のプレイヤーを取得する
 * @param name プレイヤー名
 * @return プレイヤー
 */
fun getPlayerExact(name: String): Player {
    return Bukkit.getPlayer(stripColor(name))
}

