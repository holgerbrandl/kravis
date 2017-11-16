package com.github.holgerbrandl.kravis.tornadofx

import javafx.application.Application
import javafx.geometry.Side
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import org.apache.commons.math3.distribution.NormalDistribution
import tornadofx.*
import java.util.*

//https://stackoverflow.com/questions/26803380/how-to-combine-scatter-chart-with-line-chart-to-show-line-of-regression-javafx
// Solution1) tranparaent lines for second series. Requires css mess by index
// Solution2) 2 layers with opacity --> seems better byt legends overlap


/**
 * @author Holger Brandl
 */
class ScatterWithCategoryReversed : View("My View") {

    val norm1 = NormalDistribution(1.0, 1.0)
    val norm2 = NormalDistribution(3.0, 1.0)
    val norm3 = NormalDistribution(5.0, 1.0)

    override val root = scatterchart("Machine Capacity by Product/Week", NumberAxis(), CategoryAxis()) {

        series("Product X") {
            repeat(100) { data(norm1.sample(), "foo", Random().nextDouble()) }
            repeat(100) { data(norm2.sample(), "bar", Random().nextDouble()) }
        }

        series("Product T") {
            repeat(100) { data(norm2.sample(), "foo", Random().nextDouble()) }
            repeat(100) { data(norm3.sample(), "bar", Random().nextDouble()) }

        }

        legendSide = Side.RIGHT
        System.err.println(" adding data")

        //        animated = true
    }
}


class ScatterWithCategoryReversedApp : App(ScatterWithCategoryReversed::class)

fun main(args: Array<String>) {
    Application.launch(ScatterWithCategoryReversedApp::class.java, *args)
}