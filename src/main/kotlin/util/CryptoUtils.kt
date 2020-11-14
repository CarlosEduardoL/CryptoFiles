package util

import java.io.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import java.security.spec.KeySpec
import javax.crypto.CipherOutputStream
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

private val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
private val salt = "12345678".toByteArray()
private const val iterationCount = 1024
private const val keyStrength = 128

private fun String.toKey(): SecretKeySpec {
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
    val spec: KeySpec = PBEKeySpec(toCharArray(), salt, iterationCount, keyStrength)
    val tmp = factory.generateSecret(spec)
    return SecretKeySpec(tmp.encoded, "AES")
}

fun File.encrypt(key: String, out: FileOutputStream, progress:(Long)->Unit) {
    cipher.init(Cipher.ENCRYPT_MODE, key.toKey())
    inputStream().doCopy(CipherOutputStream(out, cipher),progress)
}

fun FileInputStream.decrypt(key: String, out: FileOutputStream, progress:(Long)->Unit) {
    cipher.init(Cipher.DECRYPT_MODE, key.toKey())
    doCopy(CipherOutputStream(out, cipher),progress)
}

@Throws(IOException::class)
fun InputStream.doCopy(os: OutputStream, progress:(Long)->Unit) {
    val bytes = ByteArray(2048*8)
    var numBytes: Int
    var accumulator: Int = 0

    while (read(bytes).also { numBytes = it }.also { accumulator += it } != -1) {
        progress(accumulator.toLong())
        os.write(bytes, 0, numBytes)
    }
    os.flush()
    os.close()
    close()
}
