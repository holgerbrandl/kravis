package com.github.holgerbrandl.kravis.vlspec

import com.github.holgerbrandl.kravis.spec.Aggregate
import com.github.holgerbrandl.kravis.spec.EncodingChannel.*
import com.github.holgerbrandl.kravis.spec.plotOf
import krangl.util.sleepPatterns
import org.junit.Test
import java.io.File


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

        assertExpected(plotOf.buildJson())
    }
}
