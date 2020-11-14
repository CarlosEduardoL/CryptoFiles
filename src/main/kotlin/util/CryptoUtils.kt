package util

import java.io.*
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * Objeto usado para hacer el cifrado del archivo
 */
private val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
private val salt = "12345678".toByteArray()
private const val iterationCount = 1024
private const val keyStrength = 128

/**
 * Método que transforma una contraseña [String] en formato UTF-8
 * y usando el algoritmo PBKDF2 la convierte en una llave de 128 bits
 * @receiver Contraseña en formato UTF-8
 * @return Llave [SecretKeySpec] de 128 bits lista para ser
 * usada por el algoritmo AES
 */
private fun String.toKey(): SecretKeySpec {
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
    val spec: KeySpec = PBEKeySpec(toCharArray(), salt, iterationCount, keyStrength)
    return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
}

/**
 * Método que toma la información sin encriptar y la encripta
 * guardando esta información en otro archivo con el mismo nombre pero con .encrypt al final
 * guarda el SHA-1 del archivo sin encriptar al inicio del archivo encriptado para probar
 * el checksum del archivo encriptado
 * @receiver archivo sin encriptar
 * @param key contraseña en forma de [String] codificada en UTF-8
 * @param progress función de actualización del progreso
 */
fun File.encrypt(key: String, progress: (Long) -> Unit) {
    val sha1 = calcSHA1().toByteArray() //Calcular el SHA-1 del archivo a encriptar
    val out = FileOutputStream("$absolutePath.encrypt")
    out.write(sha1)
    out.flush()
    cipher.init(Cipher.ENCRYPT_MODE, key.toKey())
    inputStream().doCopy(CipherOutputStream(out, cipher), progress)
}

/**
 * Toma la información de un archivo encriptado y la desencripta
 * guardando esta en otro archivo, calcula el SHA-1 de este y
 * retorna el SHA-1 esperado y el SHA-1 calculado
 * @receiver archivo encriptado
 * @param key contraseña en forma de [String] codificada en UTF-8
 * @param progress función de actualización del progreso
 */
fun File.decrypt(key: String, progress: (Long) -> Unit): Pair<String, String> {
    val decryptFile = File(absolutePath.removeSuffix(".encrypt"))
    val ins = inputStream()
    val sha1 = String(ins.readNBytes(40)) // Toma el SHA-1 almacenado en el archivo encriptado
    cipher.init(Cipher.DECRYPT_MODE, key.toKey()) // configura el Cipher en modo de desencripcion
    ins.doCopy(CipherOutputStream(decryptFile.outputStream(), cipher), progress) // desencripta el archivo
    val calcSHA1 = decryptFile.calcSHA1() // calcula la SHA-1 del archivo desencriptado
    return sha1 to calcSHA1
}

/**
 * Método que pasa la información el archivo a través de un [CipherOutputStream]
 * que cifra o decifra dependiendo de la configuración y lo guarda en otro archivo
 * @receiver recibe un Stream de datos de un archivo
 * @param os flujo de salida del archivo nuevo en modo cifrado o descifrado dependiendo la configuración del cipher
 * @param progress función de actualización del progreso
 */
fun FileInputStream.doCopy(os: CipherOutputStream, progress: (Long) -> Unit) {
    val bytes = ByteArray(2048 * 8)
    var numBytes: Int
    var accumulator: Long = 0

    while (read(bytes).also { numBytes = it }.also { accumulator += it } != -1) {
        progress(accumulator)
        os.write(bytes, 0, numBytes)
    }
    os.flush()
    os.close()
    close()
}
