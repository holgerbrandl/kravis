package kravis.ggplot

import io.kotest.assertions.fail
import krangl.DataFrame
import krangl.readTSV
import kravis.GGPlot
import kravis.SessionPrefs
import kravis.render.LocalR
import kravis.render.saveTempFile
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestName
import java.awt.Desktop
import java.io.File
import java.io.StringReader
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import kotlin.io.path.createTempFile
import kotlin.io.path.exists
import kotlin.io.path.readText

/**
 * @author Holger Brandl
 */
abstract class AbstractSvgPlotRegression {

    @Rule
    @JvmField
    val name = TestName()


    abstract val testDataDir: File

    protected fun assertExpected(plot: GGPlot, subtest: String? = null) {
        val plotFile = plot.save(createTempFile(suffix = ".svg"))

        assertTrue(plotFile.exists() && plotFile.toFile().length() > 0)

        val svgDoc = plotFile.readText().run { prettyFormat(this, 4) }.trim()
        //        val obtained = prettyFormat(svgDoc, 4).trim()

        val methodName = name.methodName

        if (methodName == null) return // because we're running not in test mode

        val file = File(testDataDir, methodName.replace(" ", "_") + "${subtest?.let { "." + it } ?: ""}.svg")
        if (!file.exists()) {
            file.writeText(svgDoc)
            fail("could not find expected result.")
        }

        // maybe https://stackoverflow.com/questions/8596161/json-string-tidy-formatter-for-java

        val expected = file.readText().trim() //.run { prettyFormat(this, 4) }

        // note assertEquals would be cleaner but since its printing the complete diff, it's polluting the travis logs
        //        assertEquals(expected, svgDoc)
        val failMsg = "svg mismatch got:\n${svgDoc.lines().take(30).joinToString("\n")}"
        assertTrue(failMsg, expected.equals(svgDoc))

        // compare actual images
        //        saveImage(File(testDataDir, name.methodName.replace(" ", "_") + ".png"))
    }

    private fun prettyFormat(input: String, indent: Int): String {
        try {
            val xmlInput = StreamSource(StringReader(input))
            val stringWriter = StringWriter()
            val xmlOutput = StreamResult(stringWriter)
            val transformerFactory = TransformerFactory.newInstance()
            val transformer = transformerFactory.newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", indent.toString());
            transformer.transform(xmlInput, xmlOutput)
            return xmlOutput.writer.toString()
        } catch (e: Exception) {
            throw RuntimeException(e) // simple exception handling, please review it
        }

    }


    @Before
    fun setup() {
//        SessionPrefs.RENDER_BACKEND = RserveEngine()
//        SessionPrefs.RENDER_BACKEND = Docker("holgerbrandl/kravis_core:3.5.1")
        SessionPrefs.RENDER_BACKEND = LocalR()
    }
}

fun GGPlot.open() = Desktop.getDesktop().open(saveTempFile().toFile())


val mpgData by lazy {
    DataFrame.readTSV("https://git.io/fAqWh")
}


val faithfuld by lazy {
    DataFrame.readTSV("src/test/resources/kravis/data/faithfuld.txt")
}

/**
 * A data frame with 32 observations on 11 (numeric) variables.
 *
 * * `mpg` - Miles/(US) gallon
 * * `cyl` - Number of cylinders
 * * `disp` - Displacement (cu.in.)
 * * `hp` - Gross horsepower
 * * `drat` - Rear axle ratio
 * * `wt` - Weight (1000 lbs)
 * * `qsec` - 1/4 mile time
 * * `vs` - Engine (0 = V-shaped, 1 = straight)
 * * `am` - Transmission (0 = automatic, 1 = manual)
 * * `gear` - Number of forward gears
 * * `carb` - Number of carburetors
 *
 *
 * Source
 * Henderson and Velleman (1981), Building multiple regression models interactively. Biometrics, 37, 391–411.
 */
val mtcars by lazy {
    DataFrame.readTSV("src/test/resources/kravis/data/mtcars.txt")
}



internal inline fun <reified T> shouldThrow(thunk: () -> Any): T {
    val e = try {
        thunk()
        null
    } catch (e: Exception) {
        e
    }

    if (e == null)
        fail("Expected exception ${T::class.qualifiedName} but no exception was thrown")
    else if (e.javaClass.name != T::class.qualifiedName) {
        e.printStackTrace()
        fail("Expected exception ${T::class.qualifiedName} but ${e.javaClass.name} was thrown")
    } else
        return e as T
}
