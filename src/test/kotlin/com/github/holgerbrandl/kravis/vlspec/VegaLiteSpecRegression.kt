package com.github.holgerbrandl.kravis.vlspec

import com.github.holgerbrandl.kravis.spec.Aggregate
import com.github.holgerbrandl.kravis.spec.EncodingChannel.*
import com.github.holgerbrandl.kravis.spec.plotOf
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import krangl.sleepData
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import java.io.File


/**
 * @author Holger Brandl
 */

data class SleepPattern(
    val name: String,
    val genus: String,
    val vore: String?,
    val order: String,
    val conservation: String?,
    val sleep_total: Double,
    val sleep_rem: Double?,
    val sleep_cycle: Double?,
    val awake: Double,
    val brainwt: Double?,
    val bodywt: Double
)

val sleepPatterns = sleepData.rows.map { row ->
    SleepPattern(
        row["name"] as String,
        row["genus"] as String,
        row["vore"] as String?,
        row["order"] as String,
        row["conservation"] as String?,
        row["sleep_total"] as Double,
        row["sleep_rem"] as Double?,
        row["sleep_cycle"] as Double?,
        row["awake"] as Double,
        row["brainwt"] as Double?,
        row["bodywt"] as Double
    )
}


class VegaLiteSpecRegression {

    @Rule
    @JvmField
    val name = TestName()

    private val testDataDir = File("src/test/resources/vl_regression")


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
    fun `simple histogram`() {
        // should be guessed correctly as histogram
        val plotOf = plotOf(sleepPatterns) {
            encoding(x, bin = true) { sleep_total }
            encoding(y, aggregate = Aggregate.count)
        }

        assertExpected(plotOf.buildJson())
    }


    private fun assertExpected(obtained: String) {
        @Suppress("NAME_SHADOWING")
        val obtained = makePretty(obtained)

        val file = File(testDataDir, name.methodName.replace(" ", "_") + ".json")
        if (!file.exists()) {
            file.writeText(obtained)
            fail("could not find expected result.")
        }

        // maybe https://stackoverflow.com/questions/8596161/json-string-tidy-formatter-for-java

        val expected = file.readLines().joinToString("\n")

        assertEquals(expected, obtained)
    }

    fun makePretty(someJson: String): String {
        val parser = JsonParser()
        val gson = GsonBuilder().setPrettyPrinting().create()

        val el = parser.parse(someJson)
        return gson.toJson(el) // done
    }
}
