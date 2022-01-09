package kravis.device

import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.embed.swing.JFXPanel
import javafx.scene.image.Image
import javafx.stage.Stage
import kravis.GGPlot
import kravis.render.PlotFormat
import tornadofx.*
import java.awt.Dimension
import java.io.File
import javax.swing.SwingUtilities


/**
 * @author Holger Brandl
 */

object FxPngDevice : OutputDevice() {

    init {
        SwingUtilities.invokeLater {

            val wrapper = JFXPanel()

            // Init TornadoFX Application
            Platform.runLater {
                val stage = Stage()
                stage.width = 1000.0
                stage.height = 800.0
                val app = FxPngApp()
                app.start(stage)
            }
        }
    }

    override fun getPreferredFormat() = PlotFormat.PNG


    override fun getPreferredSize(): Dimension? {
        //        val imView = FX.find(PngViewPanel::class.java)
        //        imView.imageProperty
        //        ???
        return null
    }


    override fun show(plot: GGPlot) {
        val imageFile = plot.save(createTempFile(suffix = getPreferredFormat().toString()), getPreferredSize())
        require(imageFile.exists()) { "Visualization Failed. Could not render image." }

        showImage(imageFile)
        //        imView.imageProperty.set(Image("file:///Users/brandl/Dropbox/sharedDB/fotos/2017-07-01 14.35.05.jpg"))
    }

    fun showImage(imageFile: File) {
        val imView = FX.find(PngViewPanel::class.java)
        imView.imageProperty.set(Image(imageFile.toURI().toURL().toString()))
    }
}

class PngViewPanel : View() {

    val imageProperty = SimpleObjectProperty<javafx.scene.image.Image>()

    override val root = borderpane {
        top = toolbar() {
            button("foo")
        }

        // https://stackoverflow.com/questions/14682881/binding-image-in-javafx
        center = imageview {

            //            heightProperty().addListener(object: ChangeListener<Number?> {
            //                override fun changed(observable: ObservableValue<out Number?>?, oldValue: Number?, newValue: Number?) {
            //                    println("new dimension ${height}")
            //                }
            //            })

            imageProperty().bindBidirectional(imageProperty)

            // https://stackoverflow.com/questions/12630296/resizing-images-to-fit-the-parent-node
            fitWidthProperty().bind(widthProperty())
            fitHeightProperty().bind(heightProperty().minus(50))


            // set start image
            val image = Image("file:///Users/brandl/Dropbox/sharedDB/fotos/me.jpg")

            imageProperty.set(image)
        }
    }
}

class FxPngApp : App(PngViewPanel::class, InternalWindow.Styles::class)


fun main() {
    //    SwingUtilities.invokeLater { FXPlottingDevice.createAndShowGUI() }


    FxPngDevice.showImage(File("/Users/brandl/Dropbox/sharedDB/fotos/2017-07-01 14.35.05.jpg"))

    // requires     compile 'de.codecentric.centerdevice:javafxsvg:1.3.0'
    //    SvgImageLoaderFactory.install(PrimitiveDimensionProvider())
    //    SvgImageLoaderFactory.install()
    //    PngFxDevice.showImage(File("/Users/brandl/projects/kotlin/kravis/src/test/resources/kravis/boxplot_with_overlay.svg"))

    Thread.sleep(10000)
}