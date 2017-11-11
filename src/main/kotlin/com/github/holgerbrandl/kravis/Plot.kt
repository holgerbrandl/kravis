package com.github.holgerbrandl.kravis

import krangl.DataFrame
import krangl.TableExpression
import krangl.sleepData

/**
 * @author Holger Brandl
 */


fun DataFrame.plot(): PlotBuilder = PlotBuilder(this)

class PlotBuilder(val df: DataFrame) {
    val aes = mapOf<String, TableExpression>().toMutableMap()

    fun x(colName: String): PlotBuilder {
        aes.put("x", { df[colName] }); return this; }

    fun x(expr: TableExpression): PlotBuilder {
        aes.put("x", expr); return this; }

    fun y(expr: TableExpression): PlotBuilder {
        aes.put("x", expr); return this; }

    fun color(expr: TableExpression): PlotBuilder {
        aes.put("x", expr); return this; }

    fun label(expr: TableExpression): PlotBuilder {
        aes.put("x", expr); return this; }

    fun size(expr: TableExpression): PlotBuilder {
        aes.put("x", expr); return this; }

    fun shape(expr: TableExpression): PlotBuilder {
        aes.put("x", expr); return this; }

    fun xIntercept(expr: TableExpression): PlotBuilder {
        aes.put("x", expr); return this; }

    fun xYntercept(expr: TableExpression): PlotBuilder {
        aes.put("x", expr); return this; }

    fun addPoints(): PlotBuilder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun show() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}


class Plot() {

}

// todo do we really need *Is{

fun main(args: Array<String>) {

    sleepData
        .plot()
        .x { it["Sepal.Width"] + 2 }
        .y { "Sepal.Length" }
        .color { "Species" }
        .addPoints()
        .show()
}