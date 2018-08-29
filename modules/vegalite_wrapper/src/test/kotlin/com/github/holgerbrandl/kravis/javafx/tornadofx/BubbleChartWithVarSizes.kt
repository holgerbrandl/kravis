package com.github.holgerbrandl.kravis.javafx.tornadofx

import com.github.holgerbrandl.kravis.MathUtil
import javafx.application.Application
import javafx.scene.chart.NumberAxis
import tornadofx.*
import java.util.*

//https://stackoverflow.com/questions/26803380/how-to-combine-scatter-chart-with-line-chart-to-show-line-of-regression-javafx
// Solution1) tranparaent lines for second series. Requires css mess by index
// Solution2) 2 layers with opacity --> seems better byt legends overlap


/**
 * @author Holger Brandl
 */
class BubbleChartWithCategory : View("My View") {

    val norm = MathUtil.multNormDist().run { (1..100).map { sample() } }
    val norm2 = MathUtil.multNormDist(arrayOf(3.0, 2.0)).run { (1..100).map { sample() } }

    override val root = bubblechart("Machine Capacity by Product/Week", NumberAxis(), NumberAxis()) {

        series("Product X") {
            norm.map { data(it[0], it[1], Random().nextDouble()) }
        }

        series("Product X") {
            norm2.map { data(it[0], it[1], Random().nextDouble()) }
        }

        System.err.println(" adding data")

        animated = true
    }
}


class BubbleChartWithCategoryApp : App(BubbleChartWithCategory::class)

fun main(args: Array<String>) {
    Application.launch(BubbleChartWithCategoryApp::class.java, *args)
}