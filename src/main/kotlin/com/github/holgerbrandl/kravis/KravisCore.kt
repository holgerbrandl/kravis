package com.github.holgerbrandl.kravis

import krangl.*
import krangl.ArrayUtils.handleListErasure
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.XYSeries
import org.knowm.xchart.style.Styler


/**
 * A ggplot-like grammar to build plots for object collections and data-frames.
 * @author Holger Brandl
 */


fun DataFrame.plot(): PlotBuilder = PlotBuilder(this)


fun <T> plotOf(objects: List<T>) = ObjectPlotBuilder(objects)

fun plotOf(objects: DataFrame) = plotOf(sleepData.rows.toList())


// todo use dedicated type for aestehtic dimension
class AesDim(dimensionLabel: String)

internal val X = AesDim("x")
internal val Y = AesDim("y")
internal val COLOR = AesDim("color")
internal val SIZE = AesDim("size")
internal val ALPHA = AesDim("alpha")


class ObjectPlotBuilder<T>(val objects: List<T>) {

    private val plotBuilder = com.github.holgerbrandl.kravis.PlotBuilder(emptyDataFrame())

    //    private val aesthetics = mapOf<String, Aesthetic>().toMutableMap()


    //    fun x(propExtract: T.(T) -> Any?): ObjectPlotBuilder<T> = updateAes(propExtract, "x")
    fun x(label: String = "", propExtract: T.(T) -> Any?): ObjectPlotBuilder<T> = updateAes(propExtract, X, label)

    fun y(label: String = "", propExtract: T.(T) -> Any?): ObjectPlotBuilder<T> = updateAes(propExtract, Y, label)
    fun color(label: String = "", propExtract: T.(T) -> Any?): ObjectPlotBuilder<T> = updateAes(propExtract, COLOR, label)


    private fun updateAes(propExtract: T.(T) -> Any?, aesDim: AesDim, label: String): ObjectPlotBuilder<T> {
        val values = objects.map { run { propExtract(it, it) } }
        plotBuilder.aesthetics.put(aesDim, Aesthetic(handleListErasure(label, values)))

        return this
    }


    fun addPoints(): ObjectPlotBuilder<T> {
        plotBuilder.addPoints()
        return this;
    }

    fun show() {
        plotBuilder.show()
    }

    fun title(title: String): ObjectPlotBuilder<T> {
        plotBuilder.title = title
        return this
    }
}


//data class Aesthetic(val label: String, val data: List<Any?>)
data class Aesthetic(val data: DataCol) {
    val label = data.name
}


class PlotBuilder(val df: DataFrame) {

    internal val aesthetics = mapOf<AesDim, Aesthetic>().toMutableMap()

    var title: String? = null


    //    val aes = mapOf<String, TableExpression>().toMutableMap()

    private fun updateAes(colName: String, aesthetic: AesDim, label: String): PlotBuilder {
        aesthetics.put(aesthetic, Aesthetic(df[colName]));

        return this
    }

    private fun updateAes(expr: TableExpression, aesthetic: AesDim, label: String): PlotBuilder {
        aesthetics.put(aesthetic, Aesthetic(df.addColumn(label, expr)[label])); return this
    }

    fun x(label: String, expr: TableExpression): PlotBuilder = updateAes(expr, X, label)
    fun x(colNameExpr: () -> String): PlotBuilder = updateAes({ it[colNameExpr()] }, X, colNameExpr())
    fun x(colName: String): PlotBuilder = updateAes(colName, X, colName)

    fun y(label: String, expr: TableExpression): PlotBuilder = updateAes(expr, Y, label)
    fun y(colNameExpr: () -> String): PlotBuilder = updateAes({ it[colNameExpr()] }, Y, colNameExpr())
    fun y(colName: String): PlotBuilder = updateAes(colName, Y, colName)


    fun color(label: String, expr: TableExpression): PlotBuilder = updateAes(expr, COLOR, label)
    fun color(colNameExpr: () -> String): PlotBuilder = updateAes({ it[colNameExpr()] }, COLOR, colNameExpr())
    fun color(colName: String): PlotBuilder = updateAes(colName, COLOR, colName)


    fun size(label: String, expr: TableExpression): PlotBuilder = updateAes(expr, SIZE, label)
    fun size(colNameExpr: () -> String): PlotBuilder = updateAes({ it[colNameExpr()] }, SIZE, colNameExpr())
    fun size(colName: String): PlotBuilder = updateAes(colName, SIZE, colName)

    fun alpha(label: String, expr: TableExpression): PlotBuilder = updateAes(expr, ALPHA, label)
    fun alpha(colNameExpr: () -> String): PlotBuilder = updateAes({ it[colNameExpr()] }, ALPHA, colNameExpr())
    fun alpha(colName: String): PlotBuilder = updateAes(colName, ALPHA, colName)


    fun title(title: String): PlotBuilder {
        this.title = title; return this
    }

    val layers = listOf<GeomLayer>().toMutableList()


    fun addPoints(): PlotBuilder {
        layers.add(PointLayer())
        return this;
    }

    fun show() {

        var plotDataDf = aesthetics.map { it.value.data }.asDataFrame()

        if (layers.any { it is PointLayer }) {
            val chart = XYChartBuilder()
                .width(600)
                .height(500)
                .apply { if (this@PlotBuilder.title != null) title = this@PlotBuilder.title }
                //                .title(title)
                .theme(Styler.ChartTheme.GGPlot2)
                .xAxisTitle("X").yAxisTitle("Y").build()

            // Customize Chart
            chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter)
            //        chart.getStyler().setChartTitleVisible(false)
            chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE)
            //        chart.getStyler().setMarkerSize(16)


            // Series
            val x = aesthetics[X]!!
            val y = aesthetics[Y]!!
            val color = aesthetics[COLOR]

            with(chart) {
                //                x.label
                xAxisTitle = x.label
                yAxisTitle = y.label
            }


            //            chart.addSeries("Gaussian Blob 1", x, y)


            if (color != null) {
                plotDataDf = plotDataDf.groupBy(color.label).filter { it[x.label].isNotNA() AND it[y.label].isNotNA() }
                // todo map missing color labels to NA
            }

            // fixme there should be a more elegant way to detect grouping
            val grouper = if (plotDataDf === plotDataDf.groups().first()) dataFrameOf("foo")("bar") else plotDataDf.groupedBy()

            grouper.rows.zip(plotDataDf.groups()).toMap().map { (grouping, data) ->
                val groupLabel = grouping.map { it.value.toString() }.joinToString(",")

                val xData = data[x.label].asDoubles().filterNotNull().run { DoubleArray(size, { this[it] }) }
                val yData = data[y.label].asDoubles().filterNotNull().run { DoubleArray(size, { this[it] }) }

                // note there does not seem to be a way to mix ints with doubles in xCharts
                chart.addSeries(groupLabel, xData, yData)
            }

            // series.setMarker(SeriesMarkers.DIAMOND)

            SwingWrapper(chart).displayChart()
        }
    }

    fun addBars(): PlotBuilder {
        layers.add(GeomLayer())
        return this
    }

    class BarLayer(val stat: String = "count", val position: String = "stack") : GeomLayer() {

    }
}

open class GeomLayer {

}

class PointLayer() : GeomLayer() {

}


/**Test if for first non-null elemeent in list if it has specific type bu peeking into it from the top. */
inline fun <reified T> isOfType(items: List<Any?>): Boolean {
    val it = items.iterator()

    while (it.hasNext()) {
        if (it.next() is T) return true
    }

    return false
}


private fun TableExpression.eval(df: DataFrame): List<Any?> {
    return ((this(ExpressionContext(df), ExpressionContext(df)) as DataCol).values() as Array<Any?>).toList()
}


class XYPlot() {

    val xAxis = Axis()
}

interface TickRenderer {

}

class DefaultRenderer() : TickRenderer {

}

class Axis(val tickRenderer: TickRenderer = DefaultRenderer()) {

}

// todo do we really need *Is{

fun main(args: Array<String>) {

    irisData
        .plot()
        .x("label+2") { it["Sepal.Width"] + 2 } // or even do on the fly data transformation using krangl's table expressions
        .y { "Sepal.Length" }
        .color { "Species" }
        .title("Iris Flowers")
        .addPoints()
        .show()
}

