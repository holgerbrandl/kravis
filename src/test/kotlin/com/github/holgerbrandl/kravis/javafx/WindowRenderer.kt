package com.github.holgerbrandl.kravis.javafx

import com.github.holgerbrandl.kravis.StaticHTMLRenderer
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.web.WebView
import javafx.stage.Stage
import tornadofx.addChildIfPossible
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask

class Window {

    //  Platform.implicitExit = false

    val jsErrors = listOf<String>().toMutableList()
    val webView = WebView()

    private val webEngine = webView.engine


    val stage = Stage().apply {
        title = "Vegas"
        width = 300.0
        height = 300.0

        addChildIfPossible(webView
            //        addChildIfPossible(webview {  }) // works
            //            content = webView
            //        }
        )

        webEngine.loadContent("<html>hello, world</html>", "text/html")

    }


    fun html(specJson: String) = StaticHTMLRenderer(specJson).pageHTML()

    fun close() = stage.close()

    fun load(specJson: String) {
        //        webEngine.loadContent(html(specJson))
        webEngine.loadContent("<html>hello, world</html>", "text/html")
    }

    init {
        // Log JS errors
        //        WebConsoleListener.setDefaultListener(
        //            object : WebConsoleListener {
        //                override fun messageAdded(webView: javafx.scene.web.WebView, message: String, lineNumber: Int, sourceId: String) {
        //                    if (message.contains("Error")) jsErrors.add(message)
        //                    println(jsErrors)
        //                }
        //            })

        Platform.runLater {
            //            stage.width=400
            //            stage.height=400
            stage.showAndWait()
        }

    }

}

val init by lazy {
    JFXPanel()
}

class WindowRenderer(val specJson: String) {
    val window by lazy { Window() }

    init {
        //https://stackoverflow.com/questions/37931670/whats-the-equivalent-of-jpanel-in-javafx
        //        init = JFXPanel()
        //        init.scene

    }


    fun show() {
        init
        onUIThread { window.load(specJson) }
    }

    fun errors() = onUIThread { window.jsErrors }

    fun close() = onUIThread { window.close() }

}

fun main(args: Array<String>) {
    val windowRenderer = WindowRenderer("[]")
    windowRenderer.show()

    Thread.sleep(10000)

    //    windowRenderer.()

}


internal fun <T> onUIThread(op: () -> T): T = if (Platform.isFxApplicationThread()) {
    op()
} else {
    val futureTask = FutureTask(Callable<T> {
        onUIThread(op)
    })
    Platform.runLater(futureTask)
    futureTask.get()
}