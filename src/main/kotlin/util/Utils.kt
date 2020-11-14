package util

import javafx.concurrent.Task
import javafx.scene.Node
import javafx.scene.control.Tooltip
import javafx.stage.Stage
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
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

val Node.stage: Stage
    get() = scene.window as Stage

fun installTooltip(tooltip: Tooltip, vararg nodes: Node) = nodes.forEach { Tooltip.install(it, tooltip) }

fun task(body: ((Long, Long) -> Unit) -> Unit) = object : Task<Unit>() {
    override fun call() = body { a, b -> updateProgress(a, b) }
}
