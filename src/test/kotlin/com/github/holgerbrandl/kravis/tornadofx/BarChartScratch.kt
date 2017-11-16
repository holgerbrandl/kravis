package com.github.holgerbrandl.kravis.tornadofx

import javafx.application.Application
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.paint.Color
import tornadofx.*


/**
 * @author Holger Brandl
 */
class BarChartTest : View() {
    override val root = barchart("Stock Monitoring, 2010", CategoryAxis(), NumberAxis()) {
        series("Portfolio 1") {
            data("Jan", 23)
            data("Feb", 14)
            data("Mar", 15)
        }
        series("Portfolio 2") {
            data("Jan", 11)
            data("Feb", 19)
            data("Mar", 27)
        }
    }
}


class BarChartStyles : Stylesheet() {
    val chartBar by cssclass()

    init {
        defaultColor0 and chartBar {
            barFill = Color.VIOLET
        }
    }
}


class BarChartTestApp : App(BarChartTest::class, BarChartStyles::class)


fun main(args: Array<String>) {
    Application.launch(LineChartTestApp::class.java, *args)
}
