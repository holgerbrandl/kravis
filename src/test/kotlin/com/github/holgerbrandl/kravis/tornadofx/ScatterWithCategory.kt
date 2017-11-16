package com.github.holgerbrandl.kravis.tornadofx

import com.github.holgerbrandl.kravis.MathUtil
import javafx.application.Application
import javafx.geometry.Side
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import tornadofx.*
import java.util.*

//https://stackoverflow.com/questions/26803380/how-to-combine-scatter-chart-with-line-chart-to-show-line-of-regression-javafx
// Solution1) tranparaent lines for second series. Requires css mess by index
// Solution2) 2 layers with opacity --> seems better byt legends overlap


/**
 * @author Holger Brandl
 */
class ScatterWithCategory : View("My View") {

    val norm = MathUtil.multNormDist().run { (1..100).map { sample() } }
    val norm2 = MathUtil.multNormDist(arrayOf(3.0, 2.0)).run { (1..100).map { sample() } }

    override val root = scatterchart("Machine Capacity by Product/Week", CategoryAxis(), NumberAxis()) {

        series("Product X") {
            norm.map { data("foo", it[1], Random().nextDouble()) }
        }

        series("Product X") {
            norm2.map { data("bar", it[1], Random().nextDouble()) }
        }

        legendSide = Side.RIGHT
        System.err.println(" adding data")

        //        animated = true
    }
}


class ScatterWithCategoryApp : App(ScatterWithCategory::class)

fun main(args: Array<String>) {
    Application.launch(ScatterWithCategoryApp::class.java, *args)
}