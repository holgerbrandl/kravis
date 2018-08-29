package com.github.holgerbrandl.kravis.javafx

/**
 * @author Holger Brandl
 */
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.chart.Axis
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.XYChart
import javafx.scene.layout.StackPane
import javafx.stage.Stage


// from  https://docs.oracle.com/javafx/2/charts/scatter-chart.htm
// from  https://stackoverflow.com/questions/38613278/how-to-change-bubble-size-in-bubblechart-javafx
class ScatterChartWithSizeAes : Application() {

    override fun start(stage: Stage) {
        stage.title = "Scatter Chart Sample"
        val xAxis = NumberAxis(0.0, 10.0, 1.0) as Axis<Double>
        val yAxis = NumberAxis(-100.0, 500.0, 100.0) as Axis<Double>


        val series1 = XYChart.Series<Double, Double>()
        series1.setName("Equities")
        series1.getData().add(XYChart.Data(4.2, 193.2))
        series1.getData().add(XYChart.Data(2.8, 33.6))
        series1.getData().add(XYChart.Data(6.2, 24.8))
        series1.getData().add(XYChart.Data(1.0, 14.0))
        series1.getData().add(XYChart.Data(1.2, 26.4))
        series1.getData().add(XYChart.Data(4.4, 114.4))
        series1.getData().add(XYChart.Data(8.5, 323.0))
        series1.getData().add(XYChart.Data(6.9, 289.8))
        series1.getData().add(XYChart.Data(9.9, 287.1))
        series1.getData().add(XYChart.Data(0.9, -9.0))
        series1.getData().add(XYChart.Data(3.2, 150.8))
        series1.getData().add(XYChart.Data(4.8, 20.8))
        series1.getData().add(XYChart.Data(7.3, -42.3))
        series1.getData().add(XYChart.Data(1.8, 81.4))
        series1.getData().add(XYChart.Data(7.3, 110.3))
        series1.getData().add(XYChart.Data(2.7, 41.2))

        val series2 = XYChart.Series<Double, Double>()
        series2.setName("Mutual funds")
        series2.getData().add(XYChart.Data(5.2, 229.2))
        series2.getData().add(XYChart.Data(2.4, 37.6))
        series2.getData().add(XYChart.Data(3.2, 49.8))
        series2.getData().add(XYChart.Data(1.8, 134.0))
        series2.getData().add(XYChart.Data(3.2, 236.2))
        series2.getData().add(XYChart.Data(7.4, 114.1))
        series2.getData().add(XYChart.Data(3.5, 323.0))
        series2.getData().add(XYChart.Data(9.3, 29.9))
        series2.getData().add(XYChart.Data(8.1, 287.4))


        // https://stackoverflow.com/questions/39658056/how-do-i-change-the-size-of-a-chart-symbol-in-a-javafx-scatter-chart
        val sc = ScatterChart<Double, Double>(xAxis, yAxis)


        //        val sc = object : ScatterChart<Double, Double>(xAxis, yAxis) {
        //
        //            private val shapes = ArrayList<Shape>()
        //
        //
        //            override fun layoutPlotChildren() {
        //                super.layoutPlotChildren()
        //                plotChildren.removeAll(shapes)
        //                shapes.clear()
        //                for (d in series1.getData()) {
        //                    val x = xAxis.getDisplayPosition(d.getXValue())
        //                    val y = yAxis.getDisplayPosition(d.getYValue())
        //                    //                    shapes.add(Circle(x, y, 3.0, Color.RED))
        //                    shapes.add(Text(x, y, "foo"))
        //                }
        //                //                plotChildren.addAll(shapes)
        //                plotChildren.addAll(listOf(Text(50.0, 200.0, "foo")))
        //                plotChildren.addAll(shapes)
        //
        //            }
        //
        //        }


        xAxis.label = "Age (years)"
        yAxis.label = "Returns to date"

        sc.title = "Investment Overview"

        sc.data.addAll(series1, series2)


        // https://stackoverflow.com/questions/39658056/how-do-i-change-the-size-of-a-chart-symbol-in-a-javafx-scatter-chart
        for (series in sc.getData()) {
            //for all series, take date, each data has Node (symbol) for representing point
            for (data in series.getData()) {
                // this node is StackPane
                val stackPane = data.getNode() as StackPane
                stackPane.prefWidth = 5.0
                stackPane.prefHeight = 5.0
            }
        }


        // https://stackoverflow.com/questions/38871202/how-to-add-shapes-on-javafx-linechart
        //        sc.
        val scene = Scene(sc, 500.0, 400.0)
        stage.scene = scene
        stage.show()

    }

}


fun main(args: Array<String>) {
    Application.launch(ScatterChartWithSizeAes::class.java, *args)
}
