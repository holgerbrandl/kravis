package com.github.holgerbrandl.kravis.javafx

import javafx.application.Application
import javafx.collections.FXCollections
import javafx.scene.Scene
import javafx.scene.chart.*
import javafx.scene.layout.StackPane
import javafx.stage.Stage

/**
 * Demonstrates how to draw layers of XYCharts.
 * https://forums.oracle.com/forums/thread.jspa?threadID=2435995 "Using StackPane to layer more different type charts"
 */

// from https://gist.github.com/jewelsea/3668862  Uses JavaFX to draw layers of XYCharts.
class LayeredXyChartsSample : Application() {

    override fun start(stage: Stage) {
        stage.scene = Scene(
            layerCharts(
                createBarChart(),
                createLineChart()
            )
        )
        stage.show()
    }

    private fun createYaxis(): NumberAxis {
        val axis = NumberAxis(0.0, 21.0, 1.0)
        axis.prefWidth = 35.0
        axis.minorTickCount = 10

        axis.tickLabelFormatter = object : NumberAxis.DefaultFormatter(axis) {
            override fun toString(`object`: Number): String {
                return String.format("%7.2f", `object`.toFloat())
            }
        }

        return axis
    }

    private fun createBarChart(): BarChart<String, Number> {
        val chart = BarChart(CategoryAxis(), createYaxis())
        setDefaultChartProperties(chart)
        chart.data.addAll(
            XYChart.Series(
                FXCollections.observableArrayList<XYChart.Data<String, Number>>(
                    XYChart.Data("Jan", 2),
                    XYChart.Data("Feb", 10),
                    XYChart.Data("Mar", 8),
                    XYChart.Data("Apr", 4),
                    XYChart.Data("May", 7),
                    XYChart.Data("Jun", 5),
                    XYChart.Data("Jul", 4),
                    XYChart.Data("Aug", 8),
                    XYChart.Data("Sep", 16.5),
                    XYChart.Data("Oct", 13.9),
                    XYChart.Data("Nov", 17),
                    XYChart.Data("Dec", 10)
                )
            )
        )
        return chart
    }

    private fun createLineChart(): LineChart<String, Number> {
        val chart = LineChart(CategoryAxis(), createYaxis())
        setDefaultChartProperties(chart)
        chart.createSymbols = false
        chart.data.addAll(
            XYChart.Series(
                FXCollections.observableArrayList<XYChart.Data<String, Number>>(
                    XYChart.Data("Jan", 1.0),
                    XYChart.Data("Feb", 2),
                    XYChart.Data("Mar", 1.5),
                    XYChart.Data("Apr", 3),
                    XYChart.Data("May", 2.5),
                    XYChart.Data("Jun", 5),
                    XYChart.Data("Jul", 4),
                    XYChart.Data("Aug", 8),
                    XYChart.Data("Sep", 6.5),
                    XYChart.Data("Oct", 13),
                    XYChart.Data("Nov", 10),
                    XYChart.Data("Dec", 20)
                )
            )
        )
        return chart
    }

    private fun setDefaultChartProperties(chart: XYChart<String, Number>) {
        chart.isLegendVisible = false
        chart.animated = false
    }

    private fun layerCharts(vararg charts: XYChart<String, Number>): StackPane {
        for (i in 1 until charts.size) {
            configureOverlayChart(charts[i])
        }

        val stackpane = StackPane()
        stackpane.children.addAll(*charts)

        return stackpane
    }

    private fun configureOverlayChart(chart: XYChart<String, Number>) {
        chart.isAlternativeRowFillVisible = false
        chart.isAlternativeColumnFillVisible = false
        chart.isHorizontalGridLinesVisible = false
        chart.verticalGridLinesVisible = false
        chart.xAxis.isVisible = false
        chart.yAxis.isVisible = false

        chart.stylesheets.addAll(javaClass.getResource("/overlay-chart.css").toExternalForm())
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Application.launch(LayeredXyChartsSample::class.java, *args)
        }
    }
}