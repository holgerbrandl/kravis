package kravis.ggplot

import krangl.*
import krangl.SumFuns.mean
import krangl.SumFuns.sd
import kravis.*
import kravis.Aesthetic.*
import kravis.OrderUtils.reorder
import kravis.ggplot
import org.junit.Test
import java.io.File


@Suppress("UNUSED_EXPRESSION")
/**
 * @author Holger Brandl
 */
class GeomRegressions : AbstractSvgPlotRegression() {

    override val testDataDir: File
        get() = File("src/test/resources/kravis")


    @Test
    fun `boxplot with overlay`() {
        irisData.ggplot("Species" to x, "Petal.Length" to y)
            .geomBoxplot(fill = RColor.orchid, color = RColor.create("#3366FF"))
            .geomPoint(position = PositionJitter(width = 0.1, seed = 1), alpha = 0.3)
            .title("Petal Length by Species")
            //            .apply { open() }
            .apply { assertExpected(this) }
    }

    @Test
    fun `simple heatmap`() {

        val plot = faithfuld.ggplot(Aes("eruptions", "waiting", fill = "density"))
            .geomTile()
            .scaleXDiscrete(expand = listOf(0.0, 0.0))
            .scaleYDiscrete(expand = listOf(0.0, 0.0))

        assertExpected(plot)
    }


    @Test
    fun `create factor ordered barchart with errorbars`() {
        val plot = irisData.groupBy("Species")
            .summarizeAt({ listOf("Sepal.Length") }, mean, sd)
            .addColumn("ymax") { it["Sepal.Length.mean"] + it["Sepal.Length.sd"] }
            .addColumn("ymin") { it["Sepal.Length.mean"] - it["Sepal.Length.sd"] }
            .ggplot(x = reorder("Species", "Sepal.Length.mean", ascending = false), y = "Sepal.Length.mean", fill = "Species")
            .geomBar(stat = Stat.identity)
            .geomErrorBar(Aes(ymin = "ymin", ymax = "ymax"), width = .3)
            .xLabel("Species")
            .yLabel("Sepal.Length")

        //        plot.show()
        assertExpected(plot)
    }

    @Test
    fun `grouped line plot`() {
        // create random time series

        val flightsSummary = flightsData
            .groupBy("carrier", "day")
            .summarize("mean_dep_delay") { it["dep_delay"].mean(removeNA = true) }

        flightsSummary.head().print()

        val basePlot = flightsSummary.ggplot(x = "day", y = "mean_dep_delay", color = "carrier")


        //        plot.show()
        assertExpected(basePlot.geomLine(), "line")
        assertExpected(basePlot.geomStep(color = RColor.darkgoldenrod), "step")
    }

}