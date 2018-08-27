package com.github.holgerbrandl.kravis.ggplot

import com.github.holgerbrandl.kravis.ggplot.Aesthetic.*
import krangl.*
import org.junit.Test
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
        """
           require(ggplot2)
           iris %>% ggplot(aes(x=Species)) +
               geom_boxplot()+
           geom_point(position=position_jitter(width=0.1, seed=1), alpha=.3) +
           ggtitle("Petal Length by Species")
        """

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
        """
            require(ggplot2)
            ggplot(mpg, aes(class, hwy)) +
                geom_boxplot(notch = TRUE, fill = "orchid3", colour = "#3366FF")
        """.trimIndent()

        GgplotRegressions().mpgData.ggplot(Aes("class", "hwy"))
            .geomBoxplot(notch = true, fill = RColor.orchid, color = RColor.create("#3366FF"))
            .also { println(it.toString()) }
        //            .apply { show() }
    }


    @Test
    fun `test different data overlays`() {
        // we would need a summerizeEach/All here
        val irisSummary = irisData.groupBy("Species").summarize(
            "Petal.Length.Mean" to { it["Petal.Length"].mean() },
            "Sepal.Length.Mean" to { it["Sepal.Length"].mean() }
        )

        irisData.ggplot("Species" to x, "Petal.Length" to y, "Species" to color)
            .geomPoint(alpha = 0.3)
            .geomPoint(data = irisSummary, shape = 4, size = 4)
            .show()

        Thread.sleep(10000)
    }

    //
    // Later
    //


    fun testThemes() {
        """
            require(ggplot2)

            ggplot(data.frame(x = c(0, 1)), aes(x = x)) +
                    stat_function(fun = dnorm, args = list(0.2, 0.1), aes(colour = "Group 1"), size = 1.5) +
                    stat_function(fun = dnorm, args = list(0.7, 0.05), aes(colour = "Group 2"), size = 1.5) +
                    scale_x_continuous(name = "Probability", breaks = seq(0, 1, 0.2), limits=c(0, 1)) +
                    scale_y_continuous(name = "Frequency") +
                    ggtitle("Normal function curves of probabilities") +
                    scale_colour_brewer(palette="Set1") +
                    labs(colour = "Groups") +
                    theme(axis.line = element_line(size=1, colour = "black"),
                          panel.grid.major = element_blank(),
                          panel.grid.minor = element_blank(),
                          panel.border = element_blank(),
                          panel.background = element_blank(),
                          axis.text.x=element_text(colour="black", size = 12),
                          axis.text.y=element_text(colour="black", size = 12))
        """.trimIndent()

        //        dataFrameOf("x"){ 0.0, 1 }


    }
}

fun main(args: Array<String>) {
    GgplotRegressions().mpgData.ggplot(Aes("class", "hwy"))
        .geomBoxplot(notch = true, fill = RColor.orchid, color = RColor.create("#3366FF"))
        .show()
}