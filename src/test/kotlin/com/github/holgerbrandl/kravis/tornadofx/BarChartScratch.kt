package com.github.holgerbrandl.kravis.tornadofx

import javafx.application.Application
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.paint.Color
import tornadofx.*

class BarChartTestApp : App(BarChartTest::class, BarChartStyles::class)
class LineChartTestApp : App(LineChartTest::class, BarChartStyles::class)


fun main(args: Array<String>) {
    Application.launch(LineChartTestApp::class.java, *args)
}

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

class LineChartTest : View() {
    override val root = linechart("Unit Sales Q2 2016", CategoryAxis(), NumberAxis()) {
        series("Product X") {
            data("MAR", 10245)
            data("APR", 23963)
            data("MAY", 15038)
        }
        series("Product Y") {
            data("MAR", 28443)
            data("APR", 22845)
            data("MAY", 19045)
        }
    }
}
