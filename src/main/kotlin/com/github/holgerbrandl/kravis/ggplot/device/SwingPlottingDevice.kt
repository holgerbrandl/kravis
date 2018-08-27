package com.github.holgerbrandl.kravis.ggplot.device

import java.io.File
import javax.imageio.ImageIO
import javax.swing.JDialog
import javax.swing.WindowConstants

object SwingPlottingDevice {
    val panel by lazy {
        val plotResultPanel = PlotResultPanel()

        val frame = JDialog()
        frame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE

        frame.contentPane.add(plotResultPanel.mainPanel)
        frame.isVisible = true

        frame.setSize(600, 500)

        plotResultPanel
    }

    fun showImage(imageFile: File) {
        val img = ImageIO.read(imageFile); // eventually C:\\ImageTest\\pic2.jpg

        panel.imagePanel.setImage(img)
    }
}


fun main(args: Array<String>) {
    SwingPlottingDevice.showImage(File("/Users/brandl/Dropbox/sharedDB/fotos/2017-07-01 14.35.05.jpg"))
}