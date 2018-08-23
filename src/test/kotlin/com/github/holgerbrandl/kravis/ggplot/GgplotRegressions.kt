package com.github.holgerbrandl.kravis.ggplot

import com.github.holgerbrandl.kravis.ggplot.Aesthetic.x
import com.github.holgerbrandl.kravis.ggplot.Aesthetic.y
import krangl.irisData
import org.junit.Test
import java.io.File

/**
 * @author Holger Brandl
 */
class GgplotRegressions : AbstractSvgPlotRegression() {

    override val testDataDir: File
        get() = File("src/test/resources/vl_gallery")


    @Test
    fun `boxplot with overlay`() {
        irisData.ggplot("Species" to x, "Petal.Length" to y)
            .geomBoxplot()
            .geomPoint(position = PositionJitter(width = 0.1), alpha = 0.3)
            .title("Petal Length by Species")
            .also { it.toString() }

        Thread.sleep(100000)
    }
}