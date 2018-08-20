package com.github.holgerbrandl.kravis.spec.example

import com.github.holgerbrandl.kravis.spec.*
import com.github.holgerbrandl.kravis.spec.EncodingChannel.*
import com.github.holgerbrandl.kravis.spec.MarkType.*
import krangl.*
import java.awt.Color
import java.net.URL

/**
 * @author Holger Brandl
 */

/** https://vega.github.io/vega-lite/examples/bar.html */
fun barChart(): VLBuilder<DataFrameRow> {
    val df = DataFrame.fromJsonString("""
    [
      {"a": "A","b": 28}, {"a": "B","b": 55}, {"a": "C","b": 43},
      {"a": "D","b": 91}, {"a": "E","b": 81}, {"a": "F","b": 53},
      {"a": "G","b": 19}, {"a": "H","b": 87}, {"a": "I","b": 52}
    ]
    """.trimIndent())

    return plotOf(df) {
        //        mark = Mark(bar)
        mark(bar)

        encoding(x, "a")
        encoding(y, "b")
    }
}


fun colorShapeScatterplot(): VLBuilder<SleepPattern> {
    return plotOf(sleepPatterns) {
        mark = Mark(point, filled = true)
        encoding(x) { sleep_total }
        encoding(y) { sleep_rem }
        encoding(color) { it.order }
        encoding(shape) { vore }
    }
}

// see https://altair-viz.github.io/gallery/bubble_health_income.html
fun gapminderScatterVerbose(): VLBuilder<DataFrameRow> {
    val gapminder = DataFrame.readCSV("https://vega.github.io/vega-lite/data/gapminder-health-income.csv")

    val plot = plotOf(gapminder.rows) {
        encoding(x, scale = Scale(ScaleType.Log)) {
            it["income"]
        }

        encoding(size) {
            it["population"]
        }

        encoding(y, scale = Scale(zero = true)) {
            it["health"]
        }
    }


    return plot
}

/** see https://altair-viz.github.io/gallery/bubble_health_income.html */
fun gapminderScatter(): VLBuilder<DataFrameRow> {
    val gapminder = DataFrame.readCSV("https://vega.github.io/vega-lite/data/gapminder-health-income.csv")

    val plot = plotOf(gapminder) {
        mark = Mark(circle, filled = true)

        encoding(x, "income", scale = Scale(ScaleType.Log))
        encoding(size) { "population" }

        // we can also mix with extractor
        //        encoding(size) { it["population"] }
        //        encoding(y, "health")

        // or spec out encoding details
        encoding(y, label = "health index", scale = Scale(zero = true)) {
            it["health"]
        }

        encoding(color, value = Color.RED) {}
    }

    return plot

}

/** from https://vega.github.io/vega-lite/examples/circle_binned.html */
fun binnedScatterplot(): VLBuilder<DataFrameRow> {
    val movies = DataFrame.fromJson(URL("https://raw.githubusercontent.com/vega/vega/master/docs/data/movies.json"))
    //        .take(100)

    val plot = plotOf(movies) {
        mark = Mark(circle)

        encoding(x, "IMDB_Rating", binParams = BinParams(10))
        encoding(y, "Rotten_Tomatoes_Rating", bin = true)
        encoding(size, aggregate = Aggregate.count)
    }

    return plot
}


fun main(args: Array<String>) {
    colorShapeScatterplot().apply {

        println(buildJson())
        render()
        //        StaticHTMLRenderer(buildJson()).openInChrome()
    }
}
