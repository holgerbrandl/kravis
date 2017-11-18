package com.github.holgerbrandl.kravis.experimental

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.web.WebView
import tornadofx.App
import tornadofx.View
import tornadofx.webview


var me: WebView? = null

/**
 * @author Holger Brandl
 */
class MyWindowRenderer : View("My View") {
    override val root = webview {
        prefWidth = 470.0

        me = this
        //            https@ //stackoverflow.com/questions/20149894/how-to-load-webpage-from-a-string-of-html-code-in-javafx-webviewer
        //            engine.load(TornadoFXScreencastsURI)
        //  <base> tag is the trick to load relative resources with loadContent(String)
        engine.loadContent("<html>hello, world</html>", "text/html");
    }
}

class Renderer : App(MyWindowRenderer::class)


fun main(args: Array<String>) {

    //    Platform.runLater {
    //        val stage = Stage()
    //        stage.title = "New Window"
    //        stage.scene = Scene(MyWindowRenderer().root, 600.0, 400.0)
    //        stage.show()
    //    }


    https@ //stackoverflow.com/questions/21083945/how-to-avoid-not-on-fx-application-thread-currentthread-javafx-application-th
    Platform.runLater {
        Thread.sleep(5000)
        me?.engine?.loadContent("<html>hello, world again</html>", "text/html")
    }

    Application.launch(Renderer::class.java, *args)


}