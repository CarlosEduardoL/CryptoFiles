package util

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
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

fun File.encrypt(key: String, out: FileOutputStream) {
    cipher.init(Cipher.ENCRYPT_MODE, key.toKey())
    FileInputStream(this).doCopy(CipherOutputStream(out, cipher))
}

fun FileInputStream.decrypt(key: String, out: FileOutputStream) {
    cipher.init(Cipher.DECRYPT_MODE, key.toKey())
    doCopy(CipherOutputStream(out, cipher))
}
