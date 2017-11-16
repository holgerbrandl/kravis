package com.github.holgerbrandl.kravis.tornadofx

import com.github.holgerbrandl.kravis.MathUtil.Companion.multNormDist
import javafx.application.Application
import javafx.scene.chart.NumberAxis
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
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

                //                data.add(newData)

                newData
            }


        }

        https@ //stackoverflow.com/questions/21826607/how-to-set-specific-color-to-javafx-chart-series-data

        series("Product X") {
            norm2.map { data(it[0], it[1]) }
        }

        System.err.println(" adding data")

    }


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