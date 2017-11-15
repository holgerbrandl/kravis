package com.github.holgerbrandl.kravis.javafx

/**
 * @author Holger Brandl
 */
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.chart.Axis
import javafx.scene.chart.BubbleChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color.GREY
import javafx.stage.Stage


class BubbleChartSample : Application() {

    override fun start(stage: Stage) {
        stage.title = "Bubble Chart Sample"
        val xAxis = NumberAxis(1.0, 53.0, 4.0) as Axis<Int>
        val yAxis = NumberAxis(0.0, 80.0, 10.0) as Axis<Int>
        val blc = BubbleChart(xAxis, yAxis)
        xAxis.label = "Week"
        yAxis.label = "Product Budget"
        blc.title = "Budget Monitoring"

        val series1 = XYChart.Series<Int, Int>()
        series1.setName("Product 1")
        series1.getData().add(XYChart.Data(3, 35))
        series1.getData().add(XYChart.Data(12, 60))
        series1.getData().add(XYChart.Data(15, 15))
        series1.getData().add(XYChart.Data(22, 30))
        series1.getData().add(XYChart.Data(28, 20))
        series1.getData().add(XYChart.Data(35, 41))
        series1.getData().add(XYChart.Data(42, 17))
        series1.getData().add(XYChart.Data(49, 30))

        val series2 = XYChart.Series<Int, Int>()
        series2.setName("Product 2")
        series2.getData().add(XYChart.Data(8, 15))
        series2.getData().add(XYChart.Data(13, 23))
        series2.getData().add(XYChart.Data(15, 45))
        series2.getData().add(XYChart.Data(24, 30))
        series2.getData().add(XYChart.Data(38, 78))
        series2.getData().add(XYChart.Data(40, 41))
        series2.getData().add(XYChart.Data(45, 57))
        series2.getData().add(XYChart.Data(47, 23))

        val scene = Scene(blc)
        blc.data.addAll(
            series1,
            series2
        )

        blc.effect = DropShadow().apply {
            setOffsetX(200.0)
            setColor(GREY)
        }

        stage.scene = scene
        stage.show()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            Application.launch(BubbleChartSample::class.java, *args)
        }
    }
}