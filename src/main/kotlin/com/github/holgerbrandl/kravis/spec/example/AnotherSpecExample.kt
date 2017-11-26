package com.github.holgerbrandl.kravis.spec.example

import com.github.holgerbrandl.kravis.show
import com.github.holgerbrandl.kravis.spec.EncodingChannel.*
import com.github.holgerbrandl.kravis.spec.Mark.bar
import com.github.holgerbrandl.kravis.spec.plotOf

/**
 * @author Holger Brandl
 */


fun main(args: Array<String>) {

    data class Figur(val name: String, val abwehr: Int, val angriff: Int, val gruppe: String) {}

    val star_wars = listOf(
        Figur("darth vader", 90, 94, "Imperium"),
        Figur("luke skuwalker", 85, 90, "Rebellen"),
        Figur("r2d2", 20, 20, "Rebellen"),
        Figur("moster yoda", 100, 98, "Rebellen")
    )


    val plot = plotOf(star_wars) {
        //        mark(bar)
        mark = bar // optional

        title = "foo" // optional

        encoding(x) {
            angriff
        }

        encoding(y) {
            abwehr
        }

        encoding(color) {
            gruppe
        }

        // optional
        axis {
            color = "red"
            maxExtent = 3.2
        }
    }


    val msg = star_wars.map { it.name }
    show(msg.joinToString(","))

    plot.render()
    println("jetxt ein bildchen!")

    // build same plot but with more traditional api


    //    plotOf(friends)
    //        .setX("year of birth"){ birthday }
    //        .setY(){}
    //        ...


}
