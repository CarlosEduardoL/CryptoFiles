package controller

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.FileChooser
import logic.decrypt
import logic.encrypt
import java.io.File
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
            val info = Files.readAllBytes(Paths.get(it.absolutePath))
            Files.write(Paths.get(it.absolutePath + ".encrypt"), info.encrypt(password!!.text))
        } ?: {
            TODO("Show Error Message Here!")
        }()
    }

    @FXML
    private fun decrypt() {
        file?.let {
            val info = Files.readAllBytes(Paths.get(it.absolutePath))
            Files.write(Paths.get(it.absolutePath.removeSuffix(".encrypt")), info.decrypt(password!!.text))
        } ?: {
            TODO("Show Error Message Here!")
        }()
    }

}