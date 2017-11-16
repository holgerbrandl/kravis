package com.github.holgerbrandl.kravis.javafx

/**
 * @author Holger Brandl
 */
import javafx.application.Application
import javafx.geometry.Side
import javafx.scene.Scene
import javafx.scene.chart.*
import javafx.stage.Stage
import org.apache.commons.math3.distribution.NormalDistribution
import tornadofx.data

// from  https://docs.oracle.com/javafx/2/charts/scatter-chart.htm
class JitterScatterWithCategory : Application() {

    override fun start(stage: Stage) {
        stage.title = "Scatter Chart Sample"
        val yAxis = NumberAxis() as Axis<Double>


        //        val norm = MathUtil.multNormDist().run { (1..100).map { sample() } }
        //        val norm2 = MathUtil.multNormDist(arrayOf(3.0, 2.0)).run { (1..100).map { sample() } }

        val norm1 = NormalDistribution(3.0, 1.0)
        val norm2 = NormalDistribution(3.0, 1.0)

        val series1 = XYChart.Series<String, Double>().apply {
            name = "Product X"
            repeat(100) { data("foo", norm1.sample()) }
            repeat(100) { data("bar", norm1.sample() + 1) }

        }

        val series2 = XYChart.Series<String, Double>().apply {
            name = "Product Y"
            repeat(100) { data("foo", norm2.sample()) }
            repeat(100) { data("bar", norm1.sample() + 3) }
        }


        val xAxis = CategoryAxis()

        xAxis.label = "Age (years)"
        yAxis.label = "Returns to date"

        // to use color aesthetic we can either use rectanble approach

        // https://stackoverflow.com/questions/21826607/how-to-set-specific-color-to-javafx-chart-series-data
        //        val newData = XYChart.Data<Double, Double>(1.0,2.0)
        //        val rect1 = Rectangle(5.0, 5.0)
        //        rect1.setFill(gradientColor(random.nextDouble(), 0.0, 1.0))
        //        newData.setNode(rect1)
        //        newData.getNode().setOpacity(random.nextDouble())


        // or mess with layoutPlotChildren

        val sc = object : ScatterChart<String, Double>(xAxis, yAxis) {}
        //        val sc = object : ScatterChart<String, Double>(xAxis, yAxis) {
        //
        //            override fun layoutPlotChildren() {
        //                super.layoutPlotChildren()
        //                //                plotChildren.removeAll(shapes)
        //                //                shapes.clear()
        //                val random = Random(3)
        //
        //                //                val shapes = listOf<Shape>().toMutableList()
        //                for (d in series1.getData()) {
        //                    val x = xAxis.getDisplayPosition(d.getXValue())
        //                    val y = yAxis.getDisplayPosition(d.getYValue())
        //                    //                    shapes.add(Circle(x, y, 3.0, Color.RED))
        //                    //                    shapes.add(Text(x-30, y, "."))
        //                    //                    d.node
        //
        //
        //                    d.node.opacity = 0.3
        //                    //                    d.node.translateY = random.nextInt(10).toDouble()
        //                    d.node.layoutX = x - 25 + random.nextInt(50).toDouble()
        //                    //                    shapes.add(Circle(x- 15 +  random.nextInt(30), y, 3.0, Color.RED))
        //                }
        //                //                plotChildren.addAll(shapes)
        //                //                plotChildren.addAll(listOf(Text(50.0, 200.0, ".")))
        //                //                plotChildren.addAll(listOf(point(3.2, 3.2)))
        //
        //            }
        //        }

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
    Application.launch(JitterScatterWithCategory::class.java, *args)
}
