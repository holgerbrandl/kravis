import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.embed.swing.JFXPanel
import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.*
import javax.swing.SwingUtilities

object SwingApp {
    private fun createAndShowGUI() {

        SwingUtilities.invokeLater {
            JFXPanel() // this will prepare JavaFX toolkit and environment
            Platform.runLater {
                val stage = Stage()
                val app = HelloWorldApp()
                app.start(stage)
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        createAndShowGUI()
    }
}

class PlottingPanel : View() {

    val imageProperty = SimpleObjectProperty<Image>()

    override val root = pane {
        label("foo")
    }
}

class HelloWorldApp : App(PlottingPanel::class, InternalWindow.Styles::class)
