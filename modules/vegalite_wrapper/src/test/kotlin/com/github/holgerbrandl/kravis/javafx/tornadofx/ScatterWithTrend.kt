package com.github.holgerbrandl.kravis.javafx.tornadofx

import com.github.holgerbrandl.kravis.MathUtil
import javafx.application.Application
import javafx.scene.chart.NumberAxis
import tornadofx.*

//https://stackoverflow.com/questions/26803380/how-to-combine-scatter-chart-with-line-chart-to-show-line-of-regression-javafx
// Solution1) tranparaent lines for second series. Requires css mess by index
// Solution2) 2 layers with opacity --> seems better byt legends overlap


/**
 * @author Holger Brandl
 */
class ScatterWithTrend : View("My View") {

    val norm = MathUtil.multNormDist().run { (1..1000).map { sample() } }
    val norm2 = MathUtil.multNormDist(arrayOf(3.0, 2.0)).run { (1..1000).map { sample() } }

    override val root = scatterchart("Machine Capacity by Product/Week", NumberAxis(), NumberAxis()) {

        series("Product X") {
            norm.map { data(it[0], it[1]) }
        }

        series("Product X") {
            norm2.map { data(it[0], it[1]) }
        }

        System.err.println(" adding data")

        animated = true
    }
}


class ScatterWithTrendApp : App(ScatterWithTrend::class)

fun main(args: Array<String>) {
    Application.launch(ScatterWithTrendApp::class.java, *args)
}