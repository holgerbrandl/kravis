package com.github.holgerbrandl.kravis.scratch

import com.github.holgerbrandl.kravis.spec.xplot.plot
import com.github.holgerbrandl.kravis.spec.xplot.xplotOf
import krangl.sleepData
import org.junit.Test
import java.time.LocalDate

fun main(args: Array<String>) {
    data class User(val name: String, val birthDay: LocalDate, val sex: String, val height: Double) {}

    val users = listOf(
        User("Max", LocalDate.parse("2007-12-03"), "M", 1.89),
        User("Jane", LocalDate.parse("1980-07-03"), "F", 1.67),
        User("Anna", LocalDate.parse("1992-07-03"), "F", 1.32)
    )

    xplotOf(users)
        .x("Year of Birth") { birthDay.year }
        .y("Height (m)") { height }
        .color { sex }
        .title("user stats")
        .addPoints()
        .show()
}

class PlotTests {

    /**
    @author Holger Brandl

    How can we unit-test a visualization library. svg-regressions?

    Even without such, we can at least give example and ensure that common workflows compile.
     */

    @Test
    fun objectPlot() {
        //fun `some desc here`() { see https://youtrack.jetbrains.com/issue/KT-21209

        data class User(val name: String, val birthDay: LocalDate, val sex: String, val height: Double) {}

        val friends = listOf(
            User("Max", LocalDate.parse("2007-12-03"), "M", 1.89),
            User("Jane", LocalDate.parse("1980-07-03"), "F", 1.67),
            User("Anna", LocalDate.parse("1992-07-03"), "F", 1.32)
        )

        xplotOf(friends)
            .x("Year of Birth") { birthDay.year } //good
            .y("Height (m)") { height } //good
            .color { sex } // for quick and directly it potentially should also work wihtout labels
            .addPoints()
            .show()


        // Later
        //    plotOf(objects)
        //        .x("birthday") { birthDay.year } //good
        //        .y{ birthDay.year } // should work as well without label?!
        //        .color { sex } // for quick and directly it potentially should also work wihtout labels
        //        .addBars()
        //        .show()

    }


    @Test
    fun `iris scatter plot`() {

        // dedicated api for tables
        sleepData
            .plot()
            .x("width plus 2") { it["Sepal.Width"] + 2 } // good: label and expression
            .x("Sepal.Length") // needs to be data-frame specific: refer to just a column
            .y { "Sepal.Length" }
            //todo continue here: which ones works best
            .color("Species")
            .addPoints()
            .show()


        // might work but we loose vectorization, butdo we really care here?
        xplotOf(sleepData)
            .x { it["Species"] }
            .y { it["Sepal.Length"] }
            .color { it["Species"] }
            .addPoints()
            .show()
    }


}

