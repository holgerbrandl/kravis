package com.github.holgerbrandl.kravis.tornadofx

import com.github.holgerbrandl.kravis.MathUtil.Companion.multNormDist
import com.github.holgerbrandl.kravis.javafx.legends.MyLegend
import javafx.application.Application
import javafx.geometry.Side
import javafx.scene.chart.NumberAxis
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import tornadofx.*


// for clustering example see https://github.com/thomasnield/kotlin-statistics

class ColorGradient : View("My View") {

    //    val norm = multNormDist().run{ (1..10000).map{ sample()}
    val norm = multNormDist().run { (1..1000).map { sample() } }

    val norm2 = multNormDist(arrayOf(3.0, 2.0)).run { (1..1000).map { sample() } }

    override val root = scatterchart("Machine Capacity by Product/Week", NumberAxis(), NumberAxis()) {

        series("Product X") {
            norm.map {

                val newData = data(it[0], it[1])

                // https://stackoverflow.com/questions/21826607/how-to-set-specific-color-to-javafx-chart-series-data
                val rect1 = Rectangle(5.0, 5.0)

                rect1.setFill(gradientColor(random.nextDouble(), 0.0, 1.0))
                //                rect1.setFill(if (random.nextDouble() > 0.3) Color.RED else Color.BLUE)
                newData.setNode(rect1)

                newData.getNode().setOpacity(random.nextDouble())
                newData
            }
        }

        //        https@ //stackoverflow.com/questions/21826607/how-to-set-specific-color-to-javafx-chart-series-data
        series("Product X") {
            norm2.map { data(it[0], it[1]) }
        }

        // gradient legend
        legendSide = Side.RIGHT


        // todo build actual cool legend with http://www.java2s.com/Tutorials/Java/JavaFX/0110__JavaFX_Gradient_Color.htm

        val newLegend = MyLegend()
        newLegend.isVertical = true

        newLegend.items.addAll(listOf(MyLegend.LegendItem("grad"), MyLegend.LegendItem("ient"), MyLegend.LegendItem("legend")))


        //        val legend = lookupAll(".chart-legend").first()

        // https://stackoverflow.com/questions/47339421/how-to-add-secondary-legend-to-javafx-chart/47358154#47358154
        with(lookupAll(".chart-legend").first()) {

            getChildList()?.clear()
            vbox {
                vbox {
                    label("Line 1")
                    label("Line 2")
                    label("Line 3")
                }
                vbox {}
                vbox {
                    paddingTop = 30
                    label("Line 1")
                    label("Line 2")
                    label("Line 3")
                }
                vbox {
                    Text(50.0, 30.0, "foo")
                }
            }

        }

        // cascades
        //        lookupAll(".chart-legend").first().add(newLegend)

        // upper left corner artifact
        //        legend.getParent().addChildIfPossible(newLegend)

    }


    // see https://stackoverflow.com/questions/47339421/how-to-add-secondary-legend-to-javafx-chart

}

//https://stackoverflow.com/questions/27532/generating-gradients-programmatically
// todo use sigmoid transition here
// todo we may want to rather built some lookup table
// what about 3way gradients (with white in the middele (https://stackoverflow.com/questions/13771575/java-3-color-gradient)?
fun gradientColor(x: Double, minX: Double, maxX: Double,

                  from: Color = Color.RED, to: Color = Color.GREEN): Color {
    val range = maxX - minX
    val p = (x - minX) / range

    return Color(
        from.red * p + to.red * (1 - p),
        from.green * p + to.green * (1 - p),
        from.blue * p + to.blue * (1 - p),
        1.0
    )
}


class ColorGradientStyles : Stylesheet() {
    val chartBar by cssclass()

    init {
        //        defaultColor0 and chartBar {
        //            barFill = Color.VIOLET
        //        }
    }
}


class ColorGradientApp : App(ColorGradient::class, ColorGradientStyles::class)


fun main(args: Array<String>) {
    Application.launch(ColorGradientApp::class.java, *args)
}