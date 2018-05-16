package com.github.holgerbrandl.kravis.vlspec

import com.github.holgerbrandl.kravis.histogram
import com.github.holgerbrandl.kravis.spec.*
import com.github.holgerbrandl.kravis.spec.EncodingChannel.*
import krangl.DataFrame
import krangl.fromJson
import krangl.sleepPatterns
import org.junit.Test
import java.io.File
import java.net.URL


class VegaLiteSpecRegression : AbstractPlotRegression() {

    override val testDataDir: File
        get() = File("src/test/resources/vl_regression")


    @Test
    fun `simple scatter`() {
        //    fun simple_scatter() {

        val plotOf = plotOf(sleepPatterns) {
            encoding(x) { sleep_total }
            encoding(y) { sleep_rem }
            encoding(color) { vore }
        }

        assertExpected(plotOf.buildJson())
    }

    @Test
    fun `double aggregation`() {
        // similar to https://altair-viz.github.io/documentation/encoding.html

        val plotOf = plotOf(sleepPatterns) {
            encoding(x) { sleep_total }
            encoding(y) { sleep_rem }
            encoding(color) { bodywt }
            encoding(size) { bodywt }
        }

        assertExpected(plotOf.buildJson())
    }


//    @Test
    fun `gradient scatter with dynamic size`() {
        //    fun simple_scatter() {

        val plotOf = plotOf(sleepPatterns) {
            encoding(x) { sleep_total }
            encoding(y) { sleep_rem }
            encoding(color) { bodywt }
            encoding(size) { bodywt }
        }

        assertExpected(plotOf.buildJson())
    }

    @Test
    fun `simple histogram`() {
        // should be guessed correctly as histogram
        val plotOf = plotOf(sleepPatterns) {
            encoding(x, bin = true) { sleep_total }
            encoding(y, aggregate = Aggregate.count)
        }


        sleepPatterns.histogram(ylabel = "region size")
        assertExpected(plotOf.buildJson())
    }

    @Test
    fun `cars heatmap`() {
        // https://vega.github.io/vega-lite/examples/rect_heatmap.html

        val cars = DataFrame.fromJson(URL("https://vega.github.io/vega-datasets/data/cars.json"))

        val heatmap = plotOf(cars) {

            mark = Mark(MarkType.rect)

            encoding(y, "Origin", scale = Scale(ScaleType.Ordinal))
            //            encoding(x, "Cylinders", scale = Scale(ScaleType.Ordinal))
            encoding(x, "Cylinders")
            encoding(color, "Horsepower", aggregate = Aggregate.mean)
        }

        //        println(heatmap.buildJson())
        heatmap.render()
        assertExpected(heatmap.buildJson())
    }
}

fun main(args: Array<String>) {
    VegaLiteSpecRegression().`cars heatmap`()
}

