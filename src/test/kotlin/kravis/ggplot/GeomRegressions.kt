package kravis.ggplot

import krangl.*
import krangl.SumFuns.mean
import krangl.SumFuns.sd
import kravis.*
import kravis.Aesthetic.x
import kravis.Aesthetic.y
import kravis.OrderUtils.reorder
import kravis.demo.IrisData
import kravis.demo.irisScatter
import kravis.demo.lakeHuron
import kravis.nshelper.plot
import org.junit.Ignore
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
    fun `iris-scatter with limits`() {
//        irisData.plot(x = "Species", y = "Sepal.Length", fill = "Species").geomPoint()
        irisScatter
            .xlim(5.0 to 6.0)
            .apply { assertExpected(this) }
    }

    @Test
    fun `boxplot with overlay`() {
        irisData.plot("Species" to x, "Petal.Length" to y)
            .geomBoxplot(fill = RColor.orchid, color = RColor.create("#3366FF"))
            .geomPoint(position = PositionJitter(width = 0.1, seed = 1), alpha = 0.3)
            .title("Petal Length by Species")
            //            .apply { open() }
            .apply { assertExpected(this) }
    }

    @Test
    fun `simple heatmap`() {

        val plot = faithfuld.plot(Aes("eruptions", "waiting", fill = "density"))
            .geomTile()
            .scaleXDiscrete(expand = listOf(0.0, 0.0))
            .scaleYDiscrete(expand = listOf(0.0, 0.0))

        assertExpected(plot)
    }


    @Test
    fun `create factor ordered barchart with error bars`() {
        val plot = irisData.groupBy("Species")
            .summarizeAt({ listOf("Sepal.Length") }, mean, sd)
            .addColumn("ymax") { it["Sepal.Length.mean"] + it["Sepal.Length.sd"] }
            .addColumn("ymin") { it["Sepal.Length.mean"] - it["Sepal.Length.sd"] }
            .plot(
                x = reorder("Species", "Sepal.Length.mean", ascending = false),
                y = "Sepal.Length.mean",
                fill = "Species"
            )
            .geomBar(stat = Stat.identity)
            .geomErrorBar(Aes(ymin = "ymin", ymax = "ymax"), width = .3)
            .xLabel("Species")
            .yLabel("Sepal.Length")

        //        plot.show()
        assertExpected(plot)
    }

    @Test
    fun `create a bar chart with a weight attribute`() {
        val plot = irisData.plot(x = "Species", weight = "Sepal.Length").geomBar()

//                plot.show()
        assertExpected(plot)
    }

    @Test
    fun `grouped line plot`() {
        // create random time series

        val flightsSummary = flightsData
            .groupBy("carrier", "day")
            .summarize("mean_dep_delay") { it["dep_delay"].mean(removeNA = true) }

        flightsSummary.head().print()

        val basePlot = flightsSummary.plot(x = "day", y = "mean_dep_delay", color = "carrier")


        //        plot.show()
        assertExpected(basePlot.geomLine(), "line")
        assertExpected(basePlot.geomStep(color = RColor.darkgoldenrod), "step")
    }


    @Test
    fun `text labels in plot`() {
        // todo use custom trafo here to convert to metric units on the fly
        val plot = mtcars
            .plot(x = "wt", y = "mpg", label = "model", color = "cyl").geomText(hjust = .0, nudgeX = 0.05)
            .scaleXContinuous(expand = listOf(.3, .1))
            .scaleYContinuous(limits = Limits(5.0, 30.0))

//        println(plot.spec)
//        plot.show()

        assertExpected(plot)
    }

    @Test
    fun `segments plot`() {
        val segments = dataFrameOf("x", "y", "x_end", "y_end")(
            2.6, 21, 3.57, 15
        )

        val plot = mtcars
            .plot()
            .geomPoint(Aes(x = "wt", y = "mpg", color = "cyl"))
            .geomSegment(
                mapping = Aes(x = "x", y = "y", xend = "x_end", yend = "y_end"),
                data = segments
            )

        println(plot.spec)
//        plot.show()
//        Thread.sleep(10000)

        assertExpected(plot)
    }

    @Test
    fun `ribbon plot`() {
        val plot = lakeHuron
            .addColumn("lPLus1") { it["level"] + 1 }
            .addColumn("lMin1") { it["level"] - 1 }
            .plot(x = "year")
            .geomRibbon(Aes(ymin = "lMin1", ymax = "lPLus1"), fill = RColor.gray70)
            .geomLine(Aes(y = "level"))


        assertExpected(plot)
    }


    @Test
    fun `area plot`() {
        val plot = irisData
            .addColumn("row") { rowNumber }
            .plot("row" to x, IrisData.PetalLength to y, IrisData.Species to Aesthetic.fill).geomArea()


        assertExpected(plot)
    }


    @Test
    fun `linear smooth`() {
        val plot = irisData
            .plot(IrisData.SepalLength to x, IrisData.PetalLength to y, IrisData.Species to Aesthetic.color)
            .geomPoint()
            .geomSmooth(method = "lm", se = false)

        print(plot.spec)

        assertExpected(plot)
    }


    @Test
    fun `loess smooth`() {
        val plot = irisData
            .plot(IrisData.SepalLength to x, IrisData.PetalLength to y, IrisData.Species to Aesthetic.color)
            .geomPoint()
            .geomSmooth()

        print(plot.spec)

//        plot.show()
//        sleep(100000)

        assertExpected(plot)
    }


    //    @Test
    //todo
    fun `enforce mandatory aestetics`() {
        // create random time series
        shouldThrow<MissingAestheticsMapping> {
            faithfuld.plot(x = "eruptions").geomTile() // .show()
        }
    }

    @Ignore
    @Test
    //https://github.com/holgerbrandl/kravis/issues/17
    fun `github ticket 14 regression`() {
        val xs = 0..500 step 9
        val ys = (xs.map { listOf(it, 1.0 * it, "y") }
                + xs.map { listOf(it, 2.0 * it, "dy/dx") }
                + xs.map { listOf(it, 3.0 * it, "d²y/x²") }
                + xs.map { listOf(it, 4.0 * it, "d³y/dx³") }
                + xs.map { listOf(it, 5.0 * it, "d⁴y/dx⁴") }
                + xs.map { listOf(it, 6.0 * it, "d⁵y/dx⁵") }
                ).flatten()

        val plot = dataFrameOf("x", "y", "Function")(ys)
            .plot(x = "x", y = "y", color = "Function")
            .geomLine(size = 1.0)
            .title("Derivatives of y=$y")
//                .save(File("src/main/resources/plot.png"))

        assertExpected(plot)
    }

}
