package com.github.holgerbrandl.kravis.ggplot

import com.github.holgerbrandl.kravis.spec.VLBuilder
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.rules.TestName
import java.io.File

/**
 * @author Holger Brandl
 */
abstract class AbstractSvgPlotRegression {

    @Rule
    @JvmField
    val name = TestName()


    abstract val testDataDir: File

    fun assertExpected(obtained: VLBuilder<*>) {
        assertExpected(obtained.buildJson())
    }

    protected fun assertExpected(obtained: String) {
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

        // compare actual images
        //        saveImage(File(testDataDir, name.methodName.replace(" ", "_") + ".png"))
    }

    fun makePretty(someJson: String): String {
        val parser = JsonParser()
        val gson = GsonBuilder().setPrettyPrinting().create()

        val el = parser.parse(someJson)
        return gson.toJson(el) // done
    }

}