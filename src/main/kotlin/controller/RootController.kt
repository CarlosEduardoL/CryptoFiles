package controller

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.FileChooser
import logic.decrypt
import logic.encrypt
import util.calcSHA1
import java.io.File
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
            val info = Files.readAllBytes(Paths.get(it.absolutePath))
            Files.write(Paths.get(it.absolutePath.removeSuffix(".encrypt")), info.decrypt(password!!.text))
        } ?: {
            TODO("Show Error Message Here!")
        }()
    }

}