package com.github.holgerbrandl.kravis.javafx.tornadofx

import javafx.application.Application
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import tornadofx.*

/**
 * @author Holger Brandl
 */


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

class LineChartTestApp : App(LineChartTest::class)


fun main(args: Array<String>) {
    Application.launch(LineChartTestApp::class.java, *args)
}