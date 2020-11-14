package controller

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.stage.FileChooser
import util.calcSHA1
import util.decrypt
import util.encrypt
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.concurrent.thread

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
        file?.let{ selectButton!!.text = it.name }
    }

    @FXML
    private fun encrypt() {
        file?.let {
            thread {
                val sha1 = it.calcSHA1().toByteArray()
                val out = FileOutputStream(it.absolutePath + ".encrypt")
                out.write(sha1)
                out.flush()
                it.encrypt(password!!.text, out)
            }
            deselect()
        } ?: unspecifiedFile()

    }

    @FXML
    private fun decrypt() {
        file?.let {
            thread {
                val decryptFile = File(it.absolutePath.removeSuffix(".encrypt"))
                val inStream = it.inputStream()
                val sha1 = String(inStream.readNBytes(40))
                inStream.decrypt(password!!.text, decryptFile.outputStream())
                val calcSHA1 = decryptFile.calcSHA1()
                Platform.runLater {
                    if (calcSHA1 == sha1){
                        wellSHA1(sha1,calcSHA1)
                    }else {
                        wrongSHA1(sha1,calcSHA1)
                    }
                }
            }
            deselect()
        } ?: unspecifiedFile()

    }

    private fun deselect() {
        selectButton!!.text = "Seleccionar"
        file = null
    }

    private fun wellSHA1(expected: String, obtined: String): Optional<ButtonType> = Alert(Alert.AlertType.INFORMATION).apply {
        title = "Las SHA-1 no coinciden"
        headerText = null
        dialogPane.content = Label("""|El archivo ha sido correctamente desencriptado.
                                      |SHA-1 Esperada: $expected
                                      |SHA-1 Obtenida: $obtined
        """.trimMargin())
    }.showAndWait()

    private fun wrongSHA1(expected: String, obtined: String): Optional<ButtonType> = Alert(Alert.AlertType.ERROR).apply {
        title = "Las SHA-1 no coinciden"
        headerText = null
        dialogPane.content = Label("""|Contraseña incorrecta o Archivo no encriptado con este programa.
                                      |SHA-1 Esperada: $expected
                                      |SHA-1 Obtenida: $obtined
        """.trimMargin())
    }.showAndWait()

    private fun unspecifiedFile(): Optional<ButtonType> = Alert(Alert.AlertType.ERROR).apply {
        title = "Archivo no especificado"
        headerText = "Seleccione un archivo para efectuar esta operacion"
    }.showAndWait()

}