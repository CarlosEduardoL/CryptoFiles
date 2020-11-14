package controller

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.FileChooser

import util.calcSHA1
import util.decrypt
import util.encrypt
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths

class RootController {
    private val fileChooser = FileChooser()
    private var file: File? = null

    @FXML
    private var selectButton: Button? = null

    @FXML
    private var password: TextField? = null

    @FXML
    private fun openFileChooser() {
        file = fileChooser.showOpenDialog(selectButton!!.scene.window)
        if (file != null) {
            selectButton!!.text = file!!.name
        }
    }

    @FXML
    private fun encrypt() {
        file?.let {
            val sha1 = it.calcSHA1().toByteArray()
            val out = FileOutputStream(it.absolutePath + ".encrypt")
            out.write(sha1)
            out.flush()
            it.encrypt(password!!.text, out)
        } ?: {
            TODO("Show Error Message Here!")
        }()
    }

    @FXML
    private fun decrypt() {
        file?.let {
            val decryptFile = File(it.absolutePath.removeSuffix(".encrypt"))
            val inStream = FileInputStream(it)
            val sha1 = String(inStream.readNBytes(40))
            inStream.decrypt(password!!.text, FileOutputStream(decryptFile))
            println("sha1 original: $sha1")
            println("sha 1 nueva: ${decryptFile.calcSHA1()}")
            if (decryptFile.calcSHA1() == sha1){
                // Aqui mostar que se desencripto con exito y mostrar un mensaje por pantalla
            }else {
                // Hubo algun error o se intento desencriptar un archivo que no encriptamos nosotros
            }
        } ?: {
            TODO("Show Error Message Here!")
        }()
    }

}