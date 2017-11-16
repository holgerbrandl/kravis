package com.github.holgerbrandl.kravis.javafx

/**
 * @author Holger Brandl
 */
import javafx.application.Application
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.chart.Axis
import javafx.scene.chart.BubbleChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.effect.DropShadow
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color.GREY
import javafx.scene.shape.Ellipse
import javafx.scene.shape.Shape
import javafx.stage.Stage


class CircularBubbleChart<X, Y> : BubbleChart<X, Y> {

    constructor(xAxis: Axis<X>, yAxis: Axis<Y>) : super(xAxis, yAxis) {}

    constructor(xAxis: Axis<X>, yAxis: Axis<Y>, data: ObservableList<XYChart.Series<X, Y>>) : super(xAxis, yAxis, data) {}

    override fun layoutPlotChildren() {
        super.layoutPlotChildren()
        data.stream().flatMap { series -> series.data.stream() }
            .map<Node> { it.getNode() }
            .map<StackPane> { StackPane::class.java.cast(it) }
            .map<Shape> { it.getShape() }
            .map<Ellipse> { Ellipse::class.java.cast(it) }
            .forEach { ellipse -> ellipse.radiusY = ellipse.radiusX }
    }
}

class RoundBubbleChartExample : Application() {

    override fun start(stage: Stage) {
        stage.title = "Bubble Chart Sample"
        val xAxis = NumberAxis(1.0, 53.0, 4.0) as Axis<Int>
        val yAxis = NumberAxis(0.0, 80.0, 10.0) as Axis<Int>
        xAxis.label = "Week"
        yAxis.label = "Product Budget"

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

        // https@ //stackoverflow.com/questions/38613278/how-to-change-bubble-size-in-bubblechart-javafx
        val blc = CircularBubbleChart(xAxis, yAxis)
        //        val blc = object: BubbleChart<Int, Int>(xAxis, yAxis){
        //            override fun layoutPlotChildren() {
        //                super.layoutPlotChildren()
        //                data.map { it?.node as StackPane }.map { it.shape as Ellipse }.forEach{ it.radiusX = it.radiusY}
        //            }
        //        }

        blc.title = "Budget Monitoring"


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
            Application.launch(RoundBubbleChartExample::class.java, *args)
        }
    }
}