package controller

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.shape.Circle
import javafx.stage.FileChooser
import util.*
import java.io.File
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
        if(enable) file = fileChooser.showOpenDialog(selectButton.stage)
        file?.let {
            selectButton.text = it.name
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
        val task = task {
            encrypt(password.text) { it(it, size) }
            onProcessFinish(::finishEncrypt)
        }
        progressBar.progressProperty().bind(task.progressProperty())
        Thread(task).start()
    }

    private fun File.decrypt() {
        val size = length()
        val task = task {
            val (sha1, calcSHA1) = decrypt(password.text) { it(it, size-40) }
            onProcessFinish { finishDecrypt(sha1, calcSHA1) }
        }
        progressBar.progressProperty().bind(task.progressProperty())
        Thread(task).start()
    }

    private fun deselect() {
        selectButton.text = "Seleccionar"
        file = null
    }

    private fun onProcessFinish(alert: () -> Optional<ButtonType>){
        enable = true
        Platform.runLater {
            alert()
            deselect()
            progressBar.progressProperty().unbind()
            progressBar.progressProperty().set(0.0)
        }
    }

    private fun finishEncrypt(): Optional<ButtonType> = Alert(Alert.AlertType.INFORMATION).apply {
        initOwner(circle.stage)
        title = "Finalizado"
        headerText = null
        contentText = "El proceso ha finalizado"
    }.showAndWait()

    private fun message(well: Boolean) = if (well )"El archivo ha sido correctamente desencriptado." else "Contraseña incorrecta o Archivo no encriptado con este programa."

    private fun finishDecrypt(expected: String, obtained: String): Optional<ButtonType> = (expected == obtained).let {
        Alert(if (it) Alert.AlertType.INFORMATION else Alert.AlertType.ERROR).apply {
            initOwner(circle.stage)
            title = "Las SHA-1 ${if (!it) "no " else ""}coinciden"
            headerText = null
            dialogPane.content = Label("""|${message(it)}
                                      |SHA-1 Esperada: $expected
                                      |SHA-1 Obtenida: $obtained
        """.trimMargin())
        }.showAndWait()
    }

    private fun unspecifiedFile(): Optional<ButtonType> = Alert(Alert.AlertType.ERROR).apply {
        title = "Archivo no especificado"
        headerText = "Seleccione un archivo para efectuar esta operacion"
    }.showAndWait()

}