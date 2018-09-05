package com.github.holgerbrandl.kravis.device

import com.github.holgerbrandl.kravis.GGPlot
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JDialog
import javax.swing.WindowConstants


internal object DeviceAutodetect {

    val OUTPUT_DEVICE_DEFAULT by lazy {
        // todo autodetect environment and inform user about choide
        SwingPlottingDevice()

        try {
            Class.forName("org.jetbrains.kotlin.jupyter.KernelConfig")
            println("using jupyter device")
            JupyterDevice()
        } catch (e: ClassNotFoundException) {
            // it's not jupyter so default back to swing
            SwingPlottingDevice()

            // todo check if javafx is avaialable
        }
    }
}


abstract class OutputDevice {
    abstract fun show(script: GGPlot): Any
}


class SwingPlottingDevice : OutputDevice() {
    override fun show(script: GGPlot) {
        val imageFile = script.render(".png")

        require(imageFile.exists()) { "Visualization Failed. Could not render image." }

        //        FXPlottingDevice.showImage(imageFile)
        showImage(imageFile)
    }

    val panel by lazy {
        val plotResultPanel = PlotResultPanel()

        val frame = JDialog()
        frame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE

        frame.contentPane.add(plotResultPanel.mainPanel)
        frame.isVisible = true

        frame.setSize(600, 500)

        plotResultPanel
    }

    internal fun showImage(imageFile: File) {
        val img = ImageIO.read(imageFile); // eventually C:\\ImageTest\\pic2.jpg

        panel.imagePanel.setImage(img)
    }
}


fun main(args: Array<String>) {
    SwingPlottingDevice().showImage(File("/Users/brandl/Dropbox/sharedDB/fotos/2017-07-01 14.35.05.jpg"))
}