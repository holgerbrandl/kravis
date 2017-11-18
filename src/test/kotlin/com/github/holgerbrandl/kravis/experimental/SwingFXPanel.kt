package com.github.holgerbrandl.kravis.experimental

import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.VBox


// http://www.jensd.de/wordpress/?p=1029
class SwingFXPanel : JFXPanel() {

    var testButton: Button? = null
        private set
    var testTextField: TextField? = null
        private set
    var testLabel: Label? = null
        private set
    private var pane: VBox? = null

    init {
        init()
    }

    private fun init() {
        testButton = Button("I am a JavaFX Button")
        testTextField = TextField()
        testLabel = Label("empty")
        pane = VBox()
        pane!!.alignment = Pos.CENTER
        pane!!.children.addAll(testTextField, testButton, testLabel)
        Platform.runLater({ createScene() })
    }

    private fun createScene() {
        val scene = Scene(pane!!)
        setScene(scene)
    }
}

fun main(args: Array<String>) {
    SwingFXPanel()
}