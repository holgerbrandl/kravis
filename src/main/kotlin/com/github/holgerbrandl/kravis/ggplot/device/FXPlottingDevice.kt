package com.github.holgerbrandl.kravis.ggplot.device

import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.embed.swing.JFXPanel
import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.*
import java.io.File
import javax.swing.JDialog
import javax.swing.WindowConstants


/**
 * @author Holger Brandl
 */

object FXPlottingDevice {

    init {
        createAndShowGUI()
    }

    fun createAndShowGUI() {
        val frame = JDialog()
        frame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE

        val wrapper = JFXPanel()
        frame.contentPane.add(wrapper)
        frame.pack()
        frame.isVisible = true

        // Init TornadoFX Application
        Platform.runLater {
            val stage = Stage()
            stage.width = 1000.0
            stage.height = 800.0
            val app = HelloWorldApp()
            app.start(stage)
        }
    }


    fun showImage(imageFile: File) {
        val imView = FX.find(PlottingPanel::class.java)
        imView.imageProperty.set(Image(imageFile.toURI().toURL().toString()))
        //        imView.imageProperty.set(Image("file:///Users/brandl/Dropbox/sharedDB/fotos/2017-07-01 14.35.05.jpg"))
    }
}

class PlottingPanel : View() {

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

class HelloWorldApp : App(PlottingPanel::class, InternalWindow.Styles::class)


fun main(args: Array<String>) {
    //    SwingUtilities.invokeLater { FXPlottingDevice.createAndShowGUI() }

    //    Thread.sleep(3000)

    //    FXPlottingDevice.hashCode()
    FXPlottingDevice.showImage(File("/Users/brandl/Dropbox/sharedDB/fotos/2017-07-01 14.35.05.jpg"))
}