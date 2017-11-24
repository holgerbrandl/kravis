package com.github.holgerbrandl.kravis.spec.example

import com.github.holgerbrandl.kravis.spec.EncodingChannel.*
import com.github.holgerbrandl.kravis.spec.Mark.guess
import com.github.holgerbrandl.kravis.spec.plotOf
import java.time.LocalDate

/**
 * @author Holger Brandl
 */


fun main(args: Array<String>) {

    data class User(val name: String, val birthDay: LocalDate, val sex: String, val height: Double) {}

    val friends = listOf(
        User("Max", LocalDate.parse("2007-12-03"), "M", 1.89),
        User("Jane", LocalDate.parse("1980-07-03"), "F", 1.67),
        User("Anna", LocalDate.parse("1992-07-03"), "F", 1.32)
    )


    val plot = plotOf(friends) {
        //        mark(bar)
        mark = guess // optional

        title = "foo" // optional

        encoding(x) {
            height
        }

        encoding(y) {
            birthDay.year
        }

        encoding(color) {
            sex
        }

        // optional
        axis {
            color = "red"
            maxExtent = 3.2
        }
    }


    //    val moshi = Moshi.Builder()
    //        // Add any other JsonAdapter factories.
    //        .add(KotlinJsonAdapterFactory())
    //        .build()

    //
    plot.render()

    // build same plot but with more traditional api


    //    plotOf(friends)
    //        .setX("year of birth"){ birthday }
    //        .setY(){}
    //        ...


}
