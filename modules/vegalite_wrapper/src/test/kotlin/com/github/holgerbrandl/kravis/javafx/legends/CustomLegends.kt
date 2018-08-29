package com.github.holgerbrandl.kravis.javafx.legends

/**
 * @author Holger Brandl
 */
import com.github.holgerbrandl.kravis.MathUtil
import javafx.application.Application
import javafx.geometry.Side
import javafx.scene.Scene
import javafx.scene.chart.Axis
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.XYChart
import javafx.scene.control.Label
import javafx.stage.Stage
import tornadofx.data

// from  https://docs.oracle.com/javafx/2/charts/scatter-chart.htm
class CustomLegends : Application() {

    override fun start(stage: Stage) {
        stage.title = "Scatter Chart Sample"


        val norm1 = MathUtil.multNormDist()
        val norm2 = MathUtil.multNormDist(arrayOf(3.0, 2.0))
        val norm3 = MathUtil.multNormDist(arrayOf(8.0, 2.0))

        val series1 = XYChart.Series<Double, Double>().apply {
            name = "Product X"

            repeat(100) {
                val (x, y) = norm1.sample()
                data(x, y)
            }

            repeat(100) {
                val (x, y) = norm2.sample()
                data(x, y)
            }
        }

        val series2 = XYChart.Series<Double, Double>().apply {
            name = "Product Y"

            repeat(100) {
                val (x, y) = norm1.sample()
                data(x, y)
            }

            repeat(100) {
                val (x, y) = norm3.sample()
                data(x, y)

            }
        }


        val xAxis = NumberAxis() as Axis<Double>
        val yAxis = NumberAxis() as Axis<Double>

        xAxis.label = "Age (years)"
        yAxis.label = "Returns to date"


        val sc = object : ScatterChart<Double, Double>(xAxis, yAxis) {
            override fun layoutChildren() {
                super.layoutChildren()
            }
        }

        //        run(1 to 2){ (x, y) -> x + y}
        //        listOf(1 to 2).map{ (x, y) -> x+y}
        //        (1 to 2).let { (x, y) -> x+y}
        //        (1 to 2).let { (x, y) -> x+y}
        //        with((1.to(2)) { (x, y) -> x+y}

        //        (listOf(1,2)).let { (x, y) -> x+y}
        //        somfun(listOf(1,2)) { (x, y) -> x+y}
        //
        //        val foo = with(1 to 2){ (x,y) -> ""x + y""}

        // https://stackoverflow.com/questions/34881129/javafx-scatter-chart-custom-legend
        val items = sc.lookupAll("Label.chart-legend-item")
        for (item in items) {
            val label = item as Label
            //            label.setGraphic(seriesNodes.get(it))
            // fixme does not work --> items is empty
        }

        sc.legendSide = Side.RIGHT
        sc.title = "Investment Overview"

        sc.data.addAll(series1, series2)

        //        sc.series("Product X") {
        //            norm.map { data("foo", it[1], Random().nextDouble()) }
        //            norm.map { data("bar", it[1], Random().nextDouble()+3) }
        //        }
        //
        //        sc.series("Product X") {
        //            norm.map { data("foo", it[1], Random().nextDouble()+1) }
        //            norm.map { data("bar", it[1], Random().nextDouble()+2) }
        //        }


        val scene = Scene(sc, 500.0, 400.0)
        stage.scene = scene
        stage.show()

    }

}


fun main(args: Array<String>) {
    Application.launch(CustomLegends::class.java, *args)
}
