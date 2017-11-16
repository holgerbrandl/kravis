package com.github.holgerbrandl.kravis.tornadofx

import com.github.holgerbrandl.kravis.MathUtil.Companion.multNormDist
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.chart.NumberAxis
import tornadofx.*


// for clustering example see https://github.com/thomasnield/kotlin-statistics

class ScatterTheme : View("My View") {

    //    val norm = multNormDist().run{ (1..10000).map{ sample()}
    val norm = multNormDist().run { (1..100).map { sample() } }

    val norm2 = multNormDist(arrayOf(3.0, 2.0)).run { (1..100).map { sample() } }

    override val root = scatterchart(
        "Machine Capacity by Product/Week",
        NumberAxis().apply { label = "Sepal Length"; padding = Insets(100.0, 10.0, 10.0, 10.0) },
        NumberAxis().apply { label = "Sepal Width" }
    ) {


        series("Product X") {
            norm.map { data(it[0], it[1]) }
        }

        series("Product X") {
            norm2.map { data(it[0], it[1]) }
        }

        System.err.println(" adding data")

        stylesheets.add("/ggplot.css")
    }


}


class ScatterThemeStyles : Stylesheet() {
    val chartBar by cssclass()

    init {

        // https://docs.oracle.com/javafx/2/charts/css-styles.htm
        //        defaultColor0 and chartBar {
        //            barFill = Color.VIOLET
        //        }
    }
}


class ScatterThemeApp : App(ScatterTheme::class, ScatterThemeStyles::class)


fun main(args: Array<String>) {
    Application.launch(ScatterThemeApp::class.java, *args)
}