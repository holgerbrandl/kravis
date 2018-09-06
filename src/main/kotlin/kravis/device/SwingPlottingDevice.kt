package kravis.device

import kravis.render.PlotFormat
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
    abstract fun getPreferredFormat(): PlotFormat

    abstract fun show(imageFile: File): Any
}


class SwingPlottingDevice : OutputDevice() {
    override fun getPreferredFormat() = PlotFormat.PNG

    override fun show(imageFile: File) {
        //        FXPlottingDevice.showImage(imageFile)
        val img = ImageIO.read(imageFile)
        panel.imagePanel.setImage(img)
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