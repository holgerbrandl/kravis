package kravis.device

import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.web.WebView
import javafx.stage.Stage
import tornadofx.*
import java.io.File
import javax.swing.SwingUtilities


/**
 * @author Holger Brandl
 */

object FxSvgDevice {

    init {
        SwingUtilities.invokeLater {

            JFXPanel()

            // Init TornadoFX Application
            Platform.runLater {
                val stage = Stage()
                stage.width = 1000.0
                stage.height = 800.0
                val app = FxSvgApp()
                app.start(stage)
            }
        }
    }


    fun showImage(svgFile: File) {
        Platform.runLater {

            val webview = FX.find(SvgViewPanel::class.java).webview
            webview!!.engine.loadContent(svgFile.readText())
        }
    }
}


internal class SvgViewPanel : View() {
    var webview: WebView? = null

    override val root = borderpane {
        top = toolbar() {
            button("foo")
        }

        webview = webview {

            //            heightProperty().addListener(object: ChangeListener<Number?> {
            //                override fun changed(observable: ObservableValue<out Number?>?, oldValue: Number?, newValue: Number?) {
            //                    println("new dimension ${height}")
            //                }
            //            })


            //                        prefHeightProperty().bind(currentStage?.heightProperty())

            // https://stackoverflow.com/questions/12630296/resizing-images-to-fit-the-parent-node
            //                        prefHeightProperty().bind(currentStage?.heightProperty())
            //                        prefWidthProperty().bind(currentStage?.widthProperty())
        }
        center = webview
    }
}

class FxSvgApp : App(SvgViewPanel::class, InternalWindow.Styles::class)


fun main() {
    //    SwingUtilities.invokeLater { FXPlottingDevice.createAndShowGUI() }

    //    Thread.sleep(3000)

    //    FXPlottingDevice.hashCode()
    //    SvgImageLoaderFactory.install(PrimitiveDimensionProvider())
    //    SvgImageLoaderFactory.install()

    //    FXPlottingDevice.showImage(File("/Users/brandl/Dropbox/sharedDB/fotos/2017-07-01 14.35.05.jpg"))

    //    FXWebPlottingDevice.toString()
    FxSvgDevice.toString()
    Thread.sleep(2000)

    FxSvgDevice.showImage(File("/Users/brandl/projects/kotlin/kravis/src/test/resources/kravis/boxplot_with_overlay.svg"))

    println("done")
    Thread.sleep(10000)
}