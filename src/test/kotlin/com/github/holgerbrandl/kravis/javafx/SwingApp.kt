package com.github.holgerbrandl.kravis.javafx

import com.github.holgerbrandl.kravis.ggplot.device.PlottingPanel
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.stage.Stage
import tornadofx.App
import tornadofx.InternalWindow
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants


class HelloWorldApp : App(PlottingPanel::class, InternalWindow.Styles::class)

/**
 * @author Holger Brandl
 */
object SwingApp {
    private fun createAndShowGUI() {
        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        val wrapper = JFXPanel()
        frame.contentPane.add(wrapper)
        frame.pack()
        frame.isVisible = true

        // Init TornadoFX Application
        Platform.runLater({
            val stage = Stage()
            val app = HelloWorldApp()
            app.start(stage)
        })
    }

    @JvmStatic
    fun main(args: Array<String>) {
        SwingUtilities.invokeLater { createAndShowGUI() }
    }
}