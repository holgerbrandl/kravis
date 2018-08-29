package com.github.holgerbrandl.kravis.javafx.tornadofx

import com.github.holgerbrandl.kravis.MathUtil.Companion.multNormDist
import javafx.application.Application
import javafx.scene.chart.NumberAxis
import tornadofx.*
import java.util.*


val random = Random(1)

// for clustering example see https://github.com/thomasnield/kotlin-statistics

class MassiveScatter : View("My View") {

    //    val norm = multNormDist().run{ (1..10000).map{ sample()}
    val norm = multNormDist().run { (1..10000).map { sample() } }

    val norm2 = multNormDist(arrayOf(3.0, 2.0)).run { (1..10000).map { sample() } }

    override val root = scatterchart("Machine Capacity by Product/Week", NumberAxis(), NumberAxis()) {

        series("Product X") {
            norm.map { data(it[0], it[1]) }
        }

        series("Product X") {
            norm2.map { data(it[0], it[1]) }
        }

        System.err.println(" adding data")

    }
}


class MassiveScatteStyles : Stylesheet() {
    val chartBar by cssclass()

    init {
        //        defaultColor0 and chartBar {
        //            barFill = Color.VIOLET
        //        }
    }
}


class MassiveScatterApp : App(MassiveScatter::class, MassiveScatteStyles::class)


fun main(args: Array<String>) {
    Application.launch(MassiveScatterApp::class.java, *args)
}