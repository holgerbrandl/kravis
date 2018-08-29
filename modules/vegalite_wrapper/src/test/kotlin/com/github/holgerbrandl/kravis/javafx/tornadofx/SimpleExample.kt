package com.github.holgerbrandl.kravis.javafx.tornadofx

import javafx.application.Application
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import tornadofx.*

class HelloWorld : View() {
    override val root = hbox {
        label("Hello world")
    }
}

class HelloWorldApp : App(HelloWorld::class)


fun main(args: Array<String>) {
    Application.launch(HelloWorldApp::class.java, *args)
}