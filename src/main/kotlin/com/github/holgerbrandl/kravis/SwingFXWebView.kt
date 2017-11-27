package com.github.holgerbrandl.kravis

/**
 * @author Holger Brandl
 */

import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.embed.swing.SwingFXUtils
import javafx.scene.SnapshotParameters
import javafx.scene.web.WebView
import javafx.stage.Stage
import tornadofx.App
import tornadofx.FX
import tornadofx.View
import tornadofx.webview
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants


class MyWindowRenderer : View("My View") {
    override val root = webview {
        //        me = this
        //            https@ //stackoverflow.com/questions/20149894/how-to-load-webpage-from-a-string-of-html-code-in-javafx-webviewer
        //            engine.load(TornadoFXScreencastsURI)
        //  <base> tag is the trick to load relative resources with loadContent(String)
        engine.loadContent("<html>hello, world</html>", "text/html");
    }


    // see
    // https://stackoverflow.com/questions/38543474/way-of-setting-primarystage-or-scene-properties-in-tornadofx
    // https://stackoverflow.com/questions/38432698/webview-size-in-javafx-stage
    init {
        with(root) {
            prefHeightProperty().bind(currentStage?.heightProperty())
            prefWidthProperty().bind(currentStage?.widthProperty())
        }
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

//            prefHeightProperty().bind(stage.heightProperty());
//            browser.browser.prefWidthProperty().bind(stage.widthProperty());

            app.start(stage)
        }
    }
}


internal val fxDeviceInitializer by lazy {
    SwingUtilities.invokeAndWait { SwingApp.createAndShowGUI() } }

fun show(html: String) {
    System.setProperty("java.awt.headless", "false")

    fxDeviceInitializer



    // see https://github.com/edvin/tornadofx/issues/410
    Platform.runLater {
        val webview = FX.find(MyWindowRenderer::class.java).root
        webview.engine.loadContent(html)
    }
}


private fun WebView.saveImage(file: File) {
    val snapshot = snapshot(SnapshotParameters(), null)
    val renderedImage = SwingFXUtils.fromFXImage(snapshot, null)
    try {
        ImageIO.write(renderedImage, "png", file)
    } catch (ex: IOException) {
    }
}


fun main(args: Array<String>) {
    fxDeviceInitializer

    Thread.sleep(6000)

    Platform.runLater {
        // make sure to create the window

        // see https://github.com/edvin/tornadofx/issues/410
        val webview = FX.find(MyWindowRenderer::class.java).root
        webview.engine.loadContent("<html>new content!</html>", "text/html")
        //        webview.engine.load("http://www.java2s.com/Code/Java/JavaFX/UsingWebViewtodisplayHTML.htm")
    }

    Thread.sleep(6000)

}