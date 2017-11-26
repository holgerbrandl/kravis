package com.github.holgerbrandl.kravis

import javafx.application.Platform
import javafx.beans.value.ObservableValue
import tornadofx.FX
import java.util.*

/**
 * @author Holger Brandl
 */


object SizeAdjustProxy {

    private var curJson: String? = null


    fun showPlot(vlJsonSpec: String) {
        curJson = vlJsonSpec

        // todo adjust size
        embed(vlJsonSpec)
    }

    private fun embed(vlJsonSpec: String) {
        show(StaticHTMLRenderer(vlJsonSpec).pageHTML())
    }


    class RerenderTask : TimerTask() {
        override fun run() {
            renderSizeAdjusted()
        }
    }

    private val timer = Timer(); //Instantiate again, as we Cancel the Timer
    private var rerenderTask: RerenderTask? = null


    init {
        fxDeviceInitializer

        Platform.runLater {
            // make sure to create the window

            // see https://github.com/edvin/tornadofx/issues/410
            val webview = FX.find(MyWindowRenderer::class.java)


            // https://blog.idrsolutions.com/2012/11/adding-a-window-resize-listener-to-javafx-scene/
            val function: (ObservableValue<out Number>, Number, Number) -> Unit = { _, _, _ ->

                if (rerenderTask != null) rerenderTask!!.cancel()

                rerenderTask = RerenderTask()

                timer.schedule(rerenderTask, 100L)
            }

            webview.root.widthProperty().addListener(function)
            webview.root.heightProperty().addListener(function)
        }

    }

    private fun renderSizeAdjusted() {
        Platform.runLater {
            // make sure to create the window

            // see https://github.com/edvin/tornadofx/issues/410
            val webview = FX.find(MyWindowRenderer::class.java)


            if (curJson != null) {

                //todo what if the spec contains width or height already?
                //todo why those weired offsets?

                val sizeAdjustedJson = """
                            {
                              "width": ${webview.root.width - 200},
                              "height": ${webview.root.height - 100},
                            """ + curJson!!.removePrefix("{")

                println("replotting....")
                show(StaticHTMLRenderer(sizeAdjustedJson).pageHTML())
            }
        }

    }

}