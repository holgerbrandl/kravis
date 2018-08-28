package com.github.holgerbrandl.kravis.ggplot

import com.github.holgerbrandl.kravis.ggplot.Aesthetic.*
import com.github.holgerbrandl.kravis.ggplot.GgplotRegressions.IrisData.SepalLength
import com.github.holgerbrandl.kravis.ggplot.GgplotRegressions.IrisData.SepalWidth
import com.github.holgerbrandl.kravis.ggplot.nshelper.*
import krangl.*
import org.junit.Test
import java.awt.Desktop
import java.io.File


@Suppress("UNUSED_EXPRESSION")
/**
 * @author Holger Brandl
 */
class GgplotRegressions : AbstractSvgPlotRegression() {

    override val testDataDir: File
        get() = File("src/test/resources/com/github/holgerbrandl/kravis/ggplot")


    @Test
    fun `boxplot with overlay`() {
        irisData.ggplot("Species" to x, "Petal.Length" to y)
            .geomBoxplot(notch = null, fill = RColor.orchid, color = RColor.create("#3366FF"))
            .geomPoint(position = PositionJitter(width = 0.1, seed = 1), alpha = 0.3)
            .title("Petal Length by Species")
            .apply { assertExpected(this) }
        //            .also { it.toString() }
    }


    val mpgData by lazy {
        DataFrame.readTSV("https://git.io/fAqWh")
    }

    @Test
    fun `custom boxplot`() {
        val data = GgplotRegressions().mpgData

        val plot = data.ggplot(Aes("class", "hwy"))
            .geomBoxplot(notch = true, fill = RColor.orchid, color = RColor.create("#3366FF"))


        //        plot.also { println(it.toString()) }
        //        plot.show()
        assertExpected(plot)
    }


    @Test
    fun `different data overlays`() {
        // we would need a summerizeEach/All here
        val irisSummary = irisData.groupBy("Species").summarize(
            "Petal.Length.Mean" to { it["Petal.Length"].mean() },
            "Sepal.Length.Mean" to { it["Sepal.Length"].mean() }
        )

        val plot = irisData.ggplot("Sepal.Length" to x, "Petal.Length" to y, "Species" to color)
            .geomPoint(alpha = 0.3)
            .geomPoint(data = irisSummary, mapping = Aes("Sepal.Length.Mean", "Petal.Length.Mean"), shape = 4, stroke = 4)

        //        plot.show()
        assertExpected(plot)
        //        Thread.sleep(10000)
    }

    //
    // Later
    //


    enum class IrisData {
        SepalLength, SepalWidth, PetalLength, PetalWidth;

        override fun toString(): String {
            return super.toString().replace("(.)([A-Z])".toRegex()) {
                it.groupValues[1] + "." + it.groupValues[2]
            }
        }
    }

    fun testFixedTheme() {
        irisData.ggplot(SepalLength to x, SepalWidth to y).themeBW().show()
    }

    @Test
    fun `theme adjustments`() {

        """
        plot <- ggplot(mpg, aes(displ, hwy)) + geom_point()

        plot + theme(
            panel.background = element_blank(),
            axis.title.y = element_blank(),
            axis.text = element_text(size=20, color="red")
        """

        val basePlot = mpgData.ggplot("displ" to x, "hwy" to y).geomPoint()

        val plot = basePlot
            .theme(panelBackground = ElementTextBlank(), axisText = ElementText("size=20, color='red'"))

        assertExpected(plot, "axis")

        //        Desktop.getDesktop().open(plot.render())


        """
        plot + theme(
        axis.line = element_line(arrow = arrow())
        )
        """

        val plot2 = basePlot.theme(axisLine = ElementLine("arrow=arrow()"))
        plot.open()
    }
}

fun GGPlot.open() = Desktop.getDesktop().open(render())

fun main(args: Array<String>) {
    GgplotRegressions().`theme adjustments`()
    //        println(GgplotRegressions.IrisData.SepalWidth)
    //    irisData.ggplot(SepalLength to x, SepalWidth to y)
    //        .geomPoint()
    //        .themeBW()
    //        .show()
}