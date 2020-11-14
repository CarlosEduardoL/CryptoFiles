package util

import javafx.scene.Node
import javafx.scene.control.Tooltip
import java.io.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


@Throws(FileNotFoundException::class, IOException::class, NoSuchAlgorithmException::class)
fun File.calcSHA1(): String {
    val sha1 = MessageDigest.getInstance("SHA-1")
    FileInputStream(this).use { input ->
        val buffer = ByteArray(8192)
        var len = input.read(buffer)
        while (len != -1) {
            sha1.update(buffer, 0, len)
            len = input.read(buffer)
        }
        return sha1.digest().toHexString()
    }
}

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }.toUpperCase()
fun installTooltip(tooltip: Tooltip, vararg nodes: Node) = nodes.forEach { Tooltip.install(it,tooltip) }
fun uninstallTooltips(tooltip: Tooltip,vararg nodes: Node) = nodes.forEach { Tooltip.uninstall(it,tooltip) }