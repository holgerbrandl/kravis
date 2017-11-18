package com.github.holgerbrandl.kravis

/**
 * @author Holger Brandl
 */

import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.stage.Stage
import tornadofx.App
import tornadofx.FX
import tornadofx.View
import tornadofx.webview
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants


open class MyWindowRenderer : View("My View") {
    override val root = webview {
        //        me = this
        //            https@ //stackoverflow.com/questions/20149894/how-to-load-webpage-from-a-string-of-html-code-in-javafx-webviewer
        //            engine.load(TornadoFXScreencastsURI)
        //  <base> tag is the trick to load relative resources with loadContent(String)
        engine.loadContent("<html>hello, world</html>", "text/html");
    }

    //    fun foo() ="hello"
}

object Renderer : App(MyWindowRenderer::class)

// see https://github.com/edvin/tornadofx/wiki/Integrate-with-existing-Applications#bootstrapping-tornadofx-from-swing
object SwingApp {

    private val app1 = App(MyWindowRenderer::class)


    fun createAndShowGUI() {
        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        val wrapper = JFXPanel()
        frame.contentPane.add(wrapper)
        frame.pack()
        frame.isVisible = true

        // Init TornadoFX Application
        Platform.runLater {
            val stage = Stage()
            val app = app1
            app.start(stage)
        }
    }
}


fun main(args: Array<String>) {
    SwingUtilities.invokeLater { SwingApp.createAndShowGUI() }
    Thread.sleep(6000)

    Platform.runLater {
        val webview = FX.find(MyWindowRenderer::class.java).webview()
        webview.engine.loadContent("<html>new content!</html>", "text/html")
    }
}