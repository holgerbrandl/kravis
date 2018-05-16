package com.github.holgerbrandl.kravis.vlspec

import com.github.holgerbrandl.kravis.spec.example.barChart
import com.github.holgerbrandl.kravis.spec.example.binnedScatterplot
import com.github.holgerbrandl.kravis.spec.example.colorShapeScatterplot
import com.github.holgerbrandl.kravis.spec.example.gapminderScatter
import org.junit.Test
import java.io.File

/**
 * @author Holger Brandl
 */

class VLGalleryRegression : AbstractPlotRegression() {

    override val testDataDir: File
        get() = File("src/test/resources/vl_gallery")


    @Test
    fun `simple barchart`() {
        assertExpected(barChart())
    }

    @Test
    fun `color and shape scatter`() {
        assertExpected(colorShapeScatterplot())
    }


    @Test
    fun `binned scatter`() {
        assertExpected(binnedScatterplot())
    }

    @Test
    fun `gapminder scatter`() {
        assertExpected(gapminderScatter())
    }
}
