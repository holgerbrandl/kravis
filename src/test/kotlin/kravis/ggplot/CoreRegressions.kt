package kravis.ggplot

import krangl.*
import kravis.*
import kravis.Aesthetic.*
import kravis.demo.IrisData.SepalLength
import kravis.demo.IrisData.SepalWidth
import kravis.plot
import kravis.nshelper.plot
import org.junit.Test
import java.io.File
import kotlin.math.roundToInt


@Suppress("UNUSED_EXPRESSION")
/**
 * @author Holger Brandl
 */
class CoreRegressions : AbstractSvgPlotRegression() {


    override val testDataDir: File
        get() = File("src/test/resources/kravis")

    @Test
    fun `empty plot without axes`() {
        irisData.plot().apply { assertExpected(this) }
    }


    @Test
    fun `empty plot with axes`() {
        irisData.plot(x = "Species", y = "Petal.Length").apply { assertExpected(this) }
    }

    @Test
    fun `it should deparse collections and allow for axis options`() {
        val basePlot = sleepPatterns.plot(
            x = { brainwt },
            y = { bodywt },
            alpha = { sleep_total }
        )

        // add layers
        basePlot.geomPoint()
            .scaleXLog10()
            .scaleYLog10("labels" to "comma".asRExpression)
            .apply { assertExpected(this) }
        //            .show()
    }

    @Test
    fun `support custom r preamble for rendering`() {
        val preabmle = """
        pacman::p_load(devtools)
        devtools::source_url("https://git.io/fAiQN")
        """

        irisData.plot(x = "Species", y = "Sepal.Length", fill = "Species")
            .addPreamble(preabmle)
            .addCustom("""geom_flat_violin(scale = "count", trim = FALSE)""")
            // todo
            //            .stat_summary(fun.data = mean_sdl, fun.args = list(mult = 1), geom = "pointrange", position = position_nudge(0.05)) +
            .geomDotplot(binaxis = "y", dotsize = 0.5, stackdir = "down", binWidth = 0.1, position = PositionNudge(-0.025))
            //            .geomPoint()
            .theme(legendPosition = "none")
            .labs(x = "Species", y = "Sepal length (cm)")

            //            .show()
            .apply { assertExpected(this) }
    }


    @Test
    fun `different data overlays`() {
        // we would need a summerizeEach/All here
        val irisSummary = irisData.groupBy("Species").summarize(
            "Petal.Length.Mean" to { it["Petal.Length"].mean() },
            "Sepal.Length.Mean" to { it["Sepal.Length"].mean() }
        )

        val plot = irisData.plot("Sepal.Length" to x, "Petal.Length" to y, "Species" to color)
            .geomPoint(alpha = 0.3)
            .geomPoint(data = irisSummary, mapping = Aes("Sepal.Length.Mean", "Petal.Length.Mean"), shape = 4, stroke = 4)

        //        plot.show()
        assertExpected(plot)
        //        Thread.sleep(10000)
    }

    //
    // Later
    //


    @Test
    fun testFixedTheme() {
        val plot = irisData.plot(SepalLength to x, SepalWidth to y).themeBW() //.show()
        assertExpected(plot)
    }

    @Test
    fun `theme adjustments`() {

        """
        plot <- plot(mpg, aes(displ, hwy)) + geom_point()

        plot + theme(
            panel.background = element_blank(),
            axis.title.y = element_blank(),
            axis.text = element_text(size=20, color="red")
        """

        val basePlot = mpgData.plot("displ" to x, "hwy" to y).geomPoint()

        val plot = basePlot
//            .theme(panelBackground = ElementTextBlank(), axisText = ElementText("size=20, color='red'"))
            .theme(panelBackground = ElementTextBlank(), axisText = ElementText(size = 20, color = RColor.red))

        assertExpected(plot, "axis")

        //        Desktop.getDesktop().open(plot.render())


        """
        plot + theme(
        axis.line = element_line(arrow = arrow())
        )
        """

        @Suppress("UNUSED_VARIABLE")
        val plot2 = basePlot.theme(axisLine = ElementLine("arrow=arrow()"))
        //        plot2.open()
        //        plot.open()
    }


    @Test
    fun `convert continues variable to discrete`() {
        val plot = irisData.addColumn("Approx.SL") { it["Sepal.Length"].map<Double> { it.roundToInt() } }
            .plot(x = "Approx.SL".asDiscreteVariable, y = "Sepal.Width", color = "Species", size = "Petal.Width")
            .geomPoint(alpha = .4)

        //        plot.show()
        assertExpected(plot)
    }


    @Test
    fun `manipulate legends`() {
        val mssleep = sleepData.addColumn("rem_proportion") { it["sleep_rem"] / it["sleep_total"] }
        // Analyze correlation

        val plot = mssleep.plot(x = "sleep_total", y = "rem_proportion", color = "vore", size = "brainwt")
            .geomPoint(alpha = 0.7)
            .guides(size = LegendType.none)

//        plot.show()
        assertExpected(plot)
    }

    @Test
    fun `facet by wrap`() {
        val plot = sleepData.plot(x = "sleep_total", y = "brainwt", color = "vore")
            .geomPoint(alpha = 0.7)
            .guides(size = LegendType.none)
            .facetWrap("vore")

        assertExpected(plot)

    }

    @Test
    fun `facet by grid`() {
        val plot = sleepData.plot(x = "sleep_total", y = "brainwt")
            .geomPoint(alpha = 0.7)
            .guides(size = LegendType.none)
            .facetGrid(rows = "vore", cols = "conservation")

        assertExpected(plot)
    }
}



