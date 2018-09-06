package kravis.ggplot

import kravis.GGPlot
import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestName
import java.io.File
import java.io.StringReader
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

/**
 * @author Holger Brandl
 */
abstract class AbstractSvgPlotRegression {

    @Rule
    @JvmField
    val name = TestName()


    abstract val testDataDir: File

    protected fun assertExpected(plot: GGPlot, subtest: String? = null) {
        val render = plot.save(createTempFile(suffix = ".svg"))

        assertTrue(render.exists() && render.length() > 0)

        val svgDoc = render.readLines().joinToString("\n")
        val obtained = prettyFormat(svgDoc, 4).trim()

        val methodName = name.methodName

        if (methodName == null) return // because we're running not in test mode

        val file = File(testDataDir, methodName.replace(" ", "_") + "${subtest?.let { "." + it } ?: ""}.svg")
        if (!file.exists()) {
            file.writeText(obtained)
            fail("could not find expected result.")
        }

        // maybe https://stackoverflow.com/questions/8596161/json-string-tidy-formatter-for-java

        val expected = file.readLines().joinToString("\n").trim()

        assertEquals(expected, obtained)

        // compare actual images
        //        saveImage(File(testDataDir, name.methodName.replace(" ", "_") + ".png"))
    }

    private fun prettyFormat(input: String, indent: Int): String {
        try {
            val xmlInput = StreamSource(StringReader(input))
            val stringWriter = StringWriter()
            val xmlOutput = StreamResult(stringWriter)
            val transformerFactory = TransformerFactory.newInstance()
            transformerFactory.setAttribute("indent-number", indent)
            val transformer = transformerFactory.newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.transform(xmlInput, xmlOutput)
            return xmlOutput.writer.toString()
        } catch (e: Exception) {
            throw RuntimeException(e) // simple exception handling, please review it
        }

    }

}