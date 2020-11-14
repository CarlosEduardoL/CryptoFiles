package util

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

@Throws(IOException::class)
fun InputStream.doCopy(os: OutputStream) {
    val bytes = ByteArray(2048)
    var numBytes: Int

    while (read(bytes).also { numBytes = it } != -1) {
        os.write(bytes, 0, numBytes)
    }
    os.flush()
    os.close()
    close()
}