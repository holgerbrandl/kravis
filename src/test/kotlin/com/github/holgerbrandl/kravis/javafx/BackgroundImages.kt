package com.github.holgerbrandl.kravis.javafx

/**
 * @author Holger Brandl
 */

// https://stackoverflow.com/questions/32639882/conditionally-color-background-javafx-linechart
import javafx.application.Application
import javafx.collections.FXCollections
import javafx.scene.Scene
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Polygon
import javafx.stage.Stage


class LineChartJavaFXTest : Application() {

    override fun start(stage: Stage) {
        stage.title = "Line Chart Sample"
        //defining the axes
        val xAxis = NumberAxis() as Axis<Int>
        val yAxis = NumberAxis() as Axis<Int>
        xAxis.label = "Seconds"
        yAxis.label = "Volume"


        //defining a series
        val series = XYChart.Series<Int, Int>()
        //populating the series with data

        series.getData().add(XYChart.Data(1, 0))
        series.getData().add(XYChart.Data(2, 1))
        series.getData().add(XYChart.Data(3, 2))
        series.getData().add(XYChart.Data(4, 2))
        series.getData().add(XYChart.Data(5, 1))
        series.getData().add(XYChart.Data(6, 2))
        series.getData().add(XYChart.Data(7, 3))
        series.getData().add(XYChart.Data(8, 3))
        series.getData().add(XYChart.Data(9, 4))
        series.getData().add(XYChart.Data(10, 3))
        series.getData().add(XYChart.Data(11, 2))
        series.getData().add(XYChart.Data(12, 1))


        //creating the chart
        val lineChart = object : LineChart<Int, Int>(xAxis, yAxis,
            FXCollections.observableArrayList<XYChart.Series<Int, Int>>(series)
        ) {

            override fun layoutPlotChildren() {
                super.layoutPlotChildren()
                val xySeries = series
                val listOfData = xySeries.data

                for (i in 0 until listOfData.size - 1) {
                    // Check for Y value >=3
                    if (listOfData[i].yValue.toDouble() >= 3 && listOfData[i + 1].yValue.toDouble() >= 3) {
                        val x1 = getXAxis().getDisplayPosition(listOfData[i].getXValue())
                        val y1 = getYAxis().getDisplayPosition(0)
                        val x2 = getXAxis().getDisplayPosition(listOfData[i + 1].getXValue())
                        val y2 = getYAxis().getDisplayPosition(0)
                        val polygon = Polygon()
                        val linearGrad = LinearGradient(0.0, 0.0, 0.0, 1.0,
                            true, // proportional
                            CycleMethod.NO_CYCLE, // cycle colors
                            Stop(0.1, Color.rgb(255, 0, 0, .3)))

                        polygon.points.addAll(*arrayOf(x1, y1, x1, getYAxis().getDisplayPosition(listOfData[i].getYValue()), x2, getYAxis().getDisplayPosition(listOfData[i + 1].getYValue()), x2, y2))
                        plotChildren.add(polygon)
                        polygon.toFront()
                        polygon.fill = linearGrad
                    }
                }
            }
        }

        lineChart.title = "Test Chart"

        val scene = Scene(lineChart, 800.0, 600.0)
        scene.stylesheets.add("/LineChart.css")
        lineChart.applyCss()

        stage.scene = scene
        stage.show()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            Application.launch(LineChartJavaFXTest::class.java, *args)
        }
    }
}