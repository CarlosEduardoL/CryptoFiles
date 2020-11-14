package controller

import javafx.application.Platform
import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.shape.Circle
import javafx.stage.FileChooser
import util.calcSHA1
import util.decrypt
import util.encrypt
import util.installTooltip
import java.io.File
import java.io.FileOutputStream
import java.util.*

class RootController {
    private val fileChooser = FileChooser()
    private var file: File? = null

    @FXML
    private lateinit var selectButton: Button

    @FXML
    private lateinit var password: TextField

    @FXML
    private lateinit var image: ImageView

    @FXML
    private lateinit var circle: Circle

    @FXML
    private lateinit var progressBar: ProgressBar

    private val tooltip1 = Tooltip("Desencriptar archivo")
    private val tooltip2 = Tooltip("Encriptar archivo")

    private var enable = true

    @FXML
    private fun openFileChooser() {
        if(enable) file = fileChooser.showOpenDialog(selectButton.scene.window)
        file?.let {
            selectButton.text = it.name
            progressBar.progressProperty().unbind()
            progressBar.progressProperty().set(0.0)
            if (it.name.endsWith(".encrypt")) {
                image.image = Image(RootController::class.java.getResource("/images/open.png").toString())
                installTooltip(tooltip1,image,circle)
            } else {
                image.image = Image(RootController::class.java.getResource("/images/closed.png").toString())
                installTooltip(tooltip2,image,circle)
            }
        }
    }

    @FXML
    private fun doAction() {
        if(enable) file?.let {
            enable = false
            if (it.name.endsWith(".encrypt")) it.decrypt()
            else it.encrypt()
        } ?: unspecifiedFile()
    }

    private fun File.encrypt() {
        val size = length()
        val task = object : Task<Unit>() {
            override fun call() {
                val sha1 = calcSHA1().toByteArray()
                val out = FileOutputStream("$absolutePath.encrypt")
                out.write(sha1)
                out.flush()
                encrypt(password.text, out) { updateProgress(it, size) }
                enable = true
                Platform.runLater { finish() }
            }
        }
        progressBar.progressProperty().bind(task.progressProperty())
        Thread(task).start()
        deselect()
    }

    private fun File.decrypt() {
        val size = length()
        val task = object : Task<Unit>() {
            override fun call() {
                val decryptFile = File(absolutePath.removeSuffix(".encrypt"))
                val inStream = inputStream()
                val sha1 = String(inStream.readNBytes(40))
                inStream.decrypt(password.text, decryptFile.outputStream()) { updateProgress(it, size-40) }
                val calcSHA1 = decryptFile.calcSHA1()
                Platform.runLater {
                    if (calcSHA1 == sha1) {
                        wellSHA1(sha1, calcSHA1)
                    } else {
                        wrongSHA1(sha1, calcSHA1)
                    }
                }
                enable = true
            }
        }
        progressBar.progressProperty().bind(task.progressProperty())
        Thread(task).start()
        deselect()
    }

    private fun deselect() {
        selectButton.text = "Seleccionar"
        file = null
    }

    private fun finish(): Optional<ButtonType> = Alert(Alert.AlertType.INFORMATION).apply {
        title = "Finalizado"
        headerText = null
        contentText = "El proceso ha finalizado"
    }.showAndWait()

    private fun wellSHA1(expected: String, obtined: String): Optional<ButtonType> = Alert(Alert.AlertType.INFORMATION).apply {
        title = "Las SHA-1 coinciden"
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