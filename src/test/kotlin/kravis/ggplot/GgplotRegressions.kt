package kravis.ggplot

import krangl.*
import krangl.SumFuns.mean
import krangl.SumFuns.sd
import kravis.*
import kravis.Aesthetic.*
import kravis.OrderUtils.reorder
import kravis.ggplot
import kravis.ggplot.GgplotRegressions.IrisData.SepalLength
import kravis.ggplot.GgplotRegressions.IrisData.SepalWidth
import kravis.nshelper.ggplot
import kravis.render.saveTempFile
import org.junit.Test
import java.awt.Desktop
import java.io.File
import kotlin.math.roundToInt


@Suppress("UNUSED_EXPRESSION")
/**
 * @author Holger Brandl
 */
class GgplotRegressions : AbstractSvgPlotRegression() {

    override val testDataDir: File
        get() = File("src/test/resources/kravis")

    @Test
    fun `empty plot without axes`() {
        irisData.ggplot().apply { assertExpected(this) }
    }


    @Test
    fun `empty plot with axes`() {
        irisData.ggplot(x = "Species", y = "Petal.Length").apply { assertExpected(this) }
    }

    @Test
    fun `support custom r preamble for rendering`() {
        val preabmle = """
        devtools::source_url("https://git.io/fAiQN")
        """

        irisData.ggplot(x = "Species", y = "Sepal.Length", fill = "Species")
            .addPreamble(preabmle)
            .addCustom("""geom_flat_violin(scale = "count", trim = FALSE)""")
            // todo
            //            .stat_summary(fun.data = mean_sdl, fun.args = list(mult = 1), geom = "pointrange", position = position_nudge(0.05)) +
            .geomDotplot(binaxis = "y", dotsize = 0.5, stackdir = "down", binwidth = 0.1, position = PositionNudge(-0.025))
            //            .geomPoint()
            .theme(legendPosition = "none")
            .labs(x = "Species", y = "Sepal length (cm)")

            //            .show()
            .apply { assertExpected(this) }
    }

    @Test
    fun `boxplot with overlay`() {
        irisData.ggplot("Species" to x, "Petal.Length" to y)
            .geomBoxplot(fill = RColor.orchid, color = RColor.create("#3366FF"))
            .geomPoint(position = PositionJitter(width = 0.1, seed = 1), alpha = 0.3)
            .title("Petal Length by Species")
            //            .apply { open() }
            .apply { assertExpected(this) }
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
        //                plot.show()
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

    @Test
    fun testFixedTheme() {
        val plot = irisData.ggplot(SepalLength to x, SepalWidth to y).themeBW() //.show()
        assertExpected(plot)
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
        //        plot2.open()
        //        plot.open()
    }

    @Test
    fun `create factor ordered barchart with errorbars`() {
        val plot = irisData.groupBy("Species")
            .summarizeAt({ listOf("Sepal.Length") }, mean, sd)
            .addColumn("ymax") { it["Sepal.Length.mean"] + it["Sepal.Length.sd"] }
            .addColumn("ymin") { it["Sepal.Length.mean"] - it["Sepal.Length.sd"] }
            .ggplot(x = reorder("Species", "Sepal.Length.mean", ascending = false), y = "Sepal.Length.mean", fill = "Species")
            .geomBar(stat = BarStat.identity)
            .geomErrorBar(Aes(ymin = "ymin", ymax = "ymax"), width = .3)
            .xLabel("Species")
            .yLabel("Sepal.Length")

        //        plot.show()
        assertExpected(plot)
    }


    @Test
    fun `convert continues variable to discrete`() {
        val plot = irisData.addColumn("Approx.SL") { it["Sepal.Length"].map<Double> { it.roundToInt() } }
            .ggplot(x = "Approx.SL".asDiscreteVariable, y = "Sepal.Width", color = "Species", size = "Petal.Width")
            .geomPoint(alpha = .4)

        //        plot.show()
        assertExpected(plot)
    }

}


class ScaleRegressions {


    @Test
    fun `it should deparse collections and allow for custom options`() {
        val basePlot = sleepPatterns.ggplot(
            x = { brainwt },
            y = { bodywt },
            alpha = { sleep_total }
        )

        // add layers
        basePlot.geomPoint()
            .scaleXLog10()
            .scaleYLog10("labels" to "comma")
        //            .show()
    }
}


fun GGPlot.open() = Desktop.getDesktop().open(saveTempFile())

