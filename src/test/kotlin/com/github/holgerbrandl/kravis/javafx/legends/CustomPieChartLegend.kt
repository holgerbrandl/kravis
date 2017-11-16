package com.github.holgerbrandl.kravis.javafx.legends

/**
 * @author Holger Brandl
 */
import javafx.application.Application
import javafx.collections.FXCollections
import javafx.geometry.Side
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.chart.PieChart
import javafx.scene.control.Label
import javafx.scene.effect.Glow
import javafx.scene.effect.Reflection
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Stage
import tornadofx.add

//from https://gist.github.com/jewelsea/1422628
class PieChartCustomLegend : Application() {

    override fun start(stage: Stage) {
        val scene = Scene(Group(), 500.0, 500.0)
        stage.title = "Imported Fruits"

        val pieChartData = FXCollections.observableArrayList(
            PieChart.Data("Grapefruit", 13.0),
            PieChart.Data("Oranges", 25.0),
            PieChart.Data("Plums", 10.0),
            PieChart.Data("Pears", 22.0),
            PieChart.Data("Apples", 30.0))
        val chart = PieChart(pieChartData)
        chart.title = "Imported Fruits"

        chart.legendSide = Side.RIGHT


        val newLegend = MyLegend()
        newLegend.items.addAll(listOf(MyLegend.LegendItem("foo"), MyLegend.LegendItem("foo")))


        chart.lookupAll(".chart-legend").first().add(newLegend)
        newLegend.isVertical = true
        //        chart.legendSideProperty().


        // gradient elemnt
        //        http@ //www.java2s.com/Tutorials/Java/JavaFX/0110__JavaFX_Gradient_Color.htm/**/

        (scene.root as Group).children.add(chart)
        stage.scene = scene
        stage.show()

        val items = chart.lookupAll("Label.chart-legend")
        var i = 0
        // these colors came from caspian.css .default-color0..4.chart-pie
        val colors = arrayOf(Color.web("#f9d900"), Color.web("#a9e200"), Color.web("#22bad9"), Color.web("#0181e2"), Color.web("#2f357f"))

        //        items.first().parent.addChildIfPossible(Text("HUHUHUHU").apply { font = Font.font(null, FontWeight.BOLD, 20.0) }, 0)

        for (item in items) {
            val label = item as Label
            val rectangle = Rectangle(10.0, 10.0, colors[i])
            val niceEffect = Glow()
            niceEffect.input = Reflection()
            rectangle.effect = niceEffect
            label.graphic = rectangle
            i++
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            Application.launch(PieChartCustomLegend::class.java, *args)
        }
    }
}