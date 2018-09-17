package kravis.ggplot

import krangl.irisData
import krangl.sleepPatterns
import kravis.*
import kravis.nshelper.ggplot
import org.junit.Test
import java.io.File

class ScaleRegressions : AbstractSvgPlotRegression() {

    override val testDataDir: File
        get() = File("src/test/resources/kravis")


    @Test
    fun `allow to change coordinate system`() {
        val plot = irisData.ggplot(IrisData.SepalLength to Aesthetic.x, IrisData.SepalWidth to Aesthetic.y)
            .geomPoint()

        assertExpected(plot.coordFlip(), "flip")
        assertExpected(plot.coordFixed(0.5), "fixed")
        assertExpected(plot.coordCartesian(xlim = 5.5 to 7.1), "cartesian")
    }

}