package com.github.holgerbrandl.kravis.scratch

/**
 * @author Holger Brandl
 */

//class MultipleAxesLineChart @JvmOverloads constructor(private val baseChart: LineChart<*, *>, lineColor: Color, strokeWidth: Double? = null) : StackPane() {
//    private val backgroundCharts = FXCollections.observableArrayList<LineChart<*, *>>()
//    private val chartColorMap = HashMap<LineChart<*, *>, Color>()
//
//    private val yAxisWidth = 60.0
//    private val detailsWindow: AnchorPane
//
//    private val yAxisSeparation = 20.0
//    private var strokeWidth = 0.3
//
//    val legend: Node
//        get() {
//            val hBox = HBox()
//
//            val baseChartCheckBox = CheckBox(baseChart.yAxis.label)
//            baseChartCheckBox.isSelected = true
//            baseChartCheckBox.style = "-fx-text-fill: " + toRGBCode(chartColorMap!![baseChart]) + "; -fx-font-weight: bold;"
//            baseChartCheckBox.isDisable = true
//            baseChartCheckBox.styleClass.add("readonly-checkbox")
//            baseChartCheckBox.setOnAction { event -> baseChartCheckBox.isSelected = true }
//            hBox.children.add(baseChartCheckBox)
//
//            for (lineChart in backgroundCharts) {
//                val checkBox = CheckBox(lineChart.yAxis.label)
//                checkBox.style = "-fx-text-fill: " + toRGBCode(chartColorMap!![lineChart]) + "; -fx-font-weight: bold"
//                checkBox.isSelected = true
//                checkBox.setOnAction { event ->
//                    if (backgroundCharts.contains(lineChart)) {
//                        backgroundCharts.remove(lineChart)
//                    } else {
//                        backgroundCharts.add(lineChart)
//                    }
//                }
//                hBox.children.add(checkBox)
//            }
//
//            hBox.alignment = Pos.CENTER
//            hBox.spacing = 20.0
//            hBox.style = "-fx-padding: 0 10 20 10"
//
//            return hBox
//        }
//
//    init {
//        if (strokeWidth != null) {
//            this.strokeWidth = strokeWidth
//        }
//
//        chartColorMap.put(baseChart, lineColor)
//
//        styleBaseChart(baseChart)
//        styleChartLine(baseChart, lineColor)
//        setFixedAxisWidth(baseChart)
//
//        alignment = Pos.CENTER_LEFT
//
//        backgroundCharts.addListener { observable: Observable -> rebuildChart() }
//
//        detailsWindow = AnchorPane()
//        bindMouseEvents(baseChart, this.strokeWidth)
//
//        rebuildChart()
//    }
//
//    private fun bindMouseEvents(baseChart: LineChart<*, *>, strokeWidth: Double?) {
//        val detailsPopup = DetailsPopup()
//        children.add(detailsWindow)
//        detailsWindow.children.add(detailsPopup)
//        detailsWindow.prefHeightProperty().bind(heightProperty())
//        detailsWindow.prefWidthProperty().bind(widthProperty())
//        detailsWindow.isMouseTransparent = true
//
//        onMouseMoved = null
//        isMouseTransparent = false
//
//        val xAxis = baseChart.xAxis
//        val yAxis = baseChart.yAxis
//
//        val xLine = Line()
//        val yLine = Line()
//        yLine.fill = Color.GRAY
//        xLine.fill = Color.GRAY
//        yLine.strokeWidth = strokeWidth!! / 2
//        xLine.strokeWidth = strokeWidth / 2
//        xLine.isVisible = false
//        yLine.isVisible = false
//
//        val chartBackground = baseChart.lookup(".chart-plot-background")
//        for (n in chartBackground.parent.childrenUnmodifiable) {
//            if (n !== chartBackground && n !== xAxis && n !== yAxis) {
//                n.isMouseTransparent = true
//            }
//        }
//        chartBackground.cursor = Cursor.CROSSHAIR
//        chartBackground.setOnMouseEntered { event ->
//            chartBackground.onMouseMoved.handle(event)
//            detailsPopup.isVisible = true
//            xLine.isVisible = true
//            yLine.isVisible = true
//            detailsWindow.children.addAll(xLine, yLine)
//        }
//        chartBackground.setOnMouseExited { event ->
//            detailsPopup.isVisible = false
//            xLine.isVisible = false
//            yLine.isVisible = false
//            detailsWindow.children.removeAll(xLine, yLine)
//        }
//        chartBackground.setOnMouseMoved { event ->
//            val x = event.x + chartBackground.layoutX
//            val y = event.y + chartBackground.layoutY
//
//            xLine.startX = 10.0
//            xLine.endX = detailsWindow.width - 10
//            xLine.startY = y + 5
//            xLine.endY = y + 5
//
//            yLine.startX = x + 5
//            yLine.endX = x + 5
//            yLine.startY = 10.0
//            yLine.endY = detailsWindow.height - 10
//
//            detailsPopup.showChartDescrpition(event)
//
//            if (y + detailsPopup.height + 10.0 < height) {
//                AnchorPane.setTopAnchor(detailsPopup, y + 10)
//            } else {
//                AnchorPane.setTopAnchor(detailsPopup, y - 10.0 - detailsPopup.height)
//            }
//
//            if (x + detailsPopup.width + 10.0 < width) {
//                AnchorPane.setLeftAnchor(detailsPopup, x + 10)
//            } else {
//                AnchorPane.setLeftAnchor(detailsPopup, x - 10.0 - detailsPopup.width)
//            }
//        }
//    }
//
//    private fun styleBaseChart(baseChart: LineChart<*, *>) {
//        baseChart.createSymbols = false
//        baseChart.isLegendVisible = false
//        baseChart.xAxis.isAutoRanging = false
//        baseChart.xAxis.animated = false
//        baseChart.yAxis.animated = false
//    }
//
//    private fun setFixedAxisWidth(chart: LineChart<*, *>) {
//        chart.yAxis.prefWidth = yAxisWidth
//        chart.yAxis.maxWidth = yAxisWidth
//    }
//
//    private fun rebuildChart() {
//        children.clear()
//
//        children.add(resizeBaseChart(baseChart))
//        for (lineChart in backgroundCharts) {
//            children.add(resizeBackgroundChart(lineChart))
//        }
//        children.add(detailsWindow)
//    }
//
//    private fun resizeBaseChart(lineChart: LineChart<*, *>): Node {
//        val hBox = HBox(lineChart)
//        hBox.alignment = Pos.CENTER_LEFT
//        hBox.prefHeightProperty().bind(heightProperty())
//        hBox.prefWidthProperty().bind(widthProperty())
//
//        lineChart.minWidthProperty().bind(widthProperty().subtract((yAxisWidth + yAxisSeparation) * backgroundCharts.size))
//        lineChart.prefWidthProperty().bind(widthProperty().subtract((yAxisWidth + yAxisSeparation) * backgroundCharts.size))
//        lineChart.maxWidthProperty().bind(widthProperty().subtract((yAxisWidth + yAxisSeparation) * backgroundCharts.size))
//
//        return lineChart
//    }
//
//    private fun resizeBackgroundChart(lineChart: LineChart<*, *>): Node {
//        val hBox = HBox(lineChart)
//        hBox.alignment = Pos.CENTER_LEFT
//        hBox.prefHeightProperty().bind(heightProperty())
//        hBox.prefWidthProperty().bind(widthProperty())
//        hBox.isMouseTransparent = true
//
//        lineChart.minWidthProperty().bind(widthProperty().subtract((yAxisWidth + yAxisSeparation) * backgroundCharts.size))
//        lineChart.prefWidthProperty().bind(widthProperty().subtract((yAxisWidth + yAxisSeparation) * backgroundCharts.size))
//        lineChart.maxWidthProperty().bind(widthProperty().subtract((yAxisWidth + yAxisSeparation) * backgroundCharts.size))
//
//        lineChart.translateXProperty().bind(baseChart.yAxis.widthProperty())
//        lineChart.yAxis.translateX = (yAxisWidth + yAxisSeparation) * backgroundCharts.indexOf(lineChart)
//
//        return hBox
//    }
//
//    fun addSeries(series: XYChart.Series<Number, Number>, lineColor: Color) {
//        val yAxis = NumberAxis()
//        val xAxis = NumberAxis()
//
//        // style x-axis
//        xAxis.isAutoRanging = false
//        xAxis.isVisible = false
//        xAxis.opacity = 0.0 // somehow the upper setVisible does not work
//        xAxis.lowerBoundProperty().bind((baseChart.xAxis as NumberAxis).lowerBoundProperty())
//        xAxis.upperBoundProperty().bind((baseChart.xAxis as NumberAxis).upperBoundProperty())
//        xAxis.tickUnitProperty().bind((baseChart.xAxis as NumberAxis).tickUnitProperty())
//
//        // style y-axis
//        yAxis.side = Side.RIGHT
//        yAxis.label = series.name
//
//        // create chart
//        val lineChart = LineChart(xAxis, yAxis)
//        lineChart.animated = false
//        lineChart.isLegendVisible = false
//        lineChart.data.add(series)
//
//        styleBackgroundChart(lineChart, lineColor)
//        setFixedAxisWidth(lineChart)
//
//        chartColorMap.put(lineChart, lineColor)
//        backgroundCharts.add(lineChart)
//    }
//
//    private fun styleBackgroundChart(lineChart: LineChart<*, *>, lineColor: Color) {
//        styleChartLine(lineChart, lineColor)
//
//        val contentBackground = lineChart.lookup(".chart-content").lookup(".chart-plot-background")
//        contentBackground.style = "-fx-background-color: transparent;"
//
//        lineChart.isVerticalZeroLineVisible = false
//        lineChart.isHorizontalZeroLineVisible = false
//        lineChart.verticalGridLinesVisible = false
//        lineChart.isHorizontalGridLinesVisible = false
//        lineChart.createSymbols = false
//    }
//
//    private fun toRGBCode(color: Color): String {
//        return String.format("#%02X%02X%02X",
//            (color.red * 255).toInt(),
//            (color.green * 255).toInt(),
//            (color.blue * 255).toInt())
//    }
//
//    private fun styleChartLine(chart: LineChart<*, *>, lineColor: Color) {
//        chart.yAxis.lookup(".axis-label").style = "-fx-text-fill: " + toRGBCode(lineColor) + "; -fx-font-weight: bold;"
//        val seriesLine = chart.lookup(".chart-series-line")
//        seriesLine.style = "-fx-stroke: " + toRGBCode(lineColor) + "; -fx-stroke-width: " + strokeWidth + ";"
//    }
//
//    private inner class DetailsPopup () : VBox() {
//
//        init {
//            style = "-fx-border-width: 1px; -fx-padding: 5 5 5 5px; -fx-border-color: gray; -fx-background-color: whitesmoke;"
//            isVisible = false
//        }
//
//        fun showChartDescrpition(event: MouseEvent) {
//            children.clear()
//
//            val xValueLong = Math.round(baseChart.xAxis.getValueForDisplay(event.x) as Double)
//
//            val baseChartPopupRow = buildPopupRow(event, xValueLong, baseChart)
//            if (baseChartPopupRow != null) {
//                children.add(baseChartPopupRow)
//            }
//
//            for (lineChart in backgroundCharts) {
//                val popupRow = buildPopupRow(event, xValueLong, lineChart) ?: continue
//
//                children.add(popupRow)
//            }
//        }
//
//        private fun buildPopupRow(event: MouseEvent, xValueLong: Long?, lineChart: LineChart<*, *>): HBox? {
//            val seriesName = Label(lineChart.yAxis.label)
//            seriesName.textFill = chartColorMap[lineChart]
//
//            val yValueForChart = getYValueForX(lineChart, xValueLong!!.toInt()) ?: return null
//            val yValueLower = Math.round(normalizeYValue(lineChart, event.y - 10))
//            val yValueUpper = Math.round(normalizeYValue(lineChart, event.y + 10))
//            val yValueUnderMouse = Math.round(lineChart.yAxis.getValueForDisplay(event.y) as Double)
//
//            // make series name bold when mouse is near given chart's line
//            if (isMouseNearLine(yValueForChart, yValueUnderMouse, Math.abs(yValueLower.toDouble() - yValueUpper.toDouble()))) {
//                seriesName.style = "-fx-font-weight: bold"
//            }
//
//            return HBox(10, seriesName, Label("[$yValueForChart]"))
//        }
//
//        private fun normalizeYValue(lineChart: LineChart<*, *>, value: Double): Double {
//            val `val` = lineChart.yAxis.getValueForDisplay(value) as Double
//            return `val` ?: 0.0
//        }
//
//        private fun isMouseNearLine(realYValue: Number, yValueUnderMouse: Number, tolerance: Double?): Boolean {
//            return Math.abs(yValueUnderMouse.toDouble() - realYValue.toDouble()) < tolerance
//        }
//
//        fun getYValueForX(chart: LineChart<*, *>, xValue: Number): Number? {
//            val dataList = (chart.data[0] as XYChart.Series<*, *>).data as List<XYChart.Data<*, *>>
//            for (data in dataList) {
//                if (data.xValue == xValue) {
//                    return data.yValue as Number
//                }
//            }
//            return null
//        }
//    }
//}

//class MultipleAxesLineChartMain : Application() {
//
//    @Throws(Exception::class)
//    override fun start(primaryStage: Stage) {
//        val xAxis = NumberAxis(0.0, X_DATA_COUNT.toDouble(), 200.0)
//        val yAxis = NumberAxis()
//        yAxis.label = "Series 1"
//
//        val baseChart = LineChart(xAxis, yAxis)
//        baseChart.data.add(prepareSeries("Series 1", { x -> x }))
//
//        val chart = MultipleAxesLineChart(baseChart, Color.RED)
//        chart.addSeries(prepareSeries("Series 2", { x -> x as Double * x }), Color.BLUE)
//        chart.addSeries(prepareSeries("Series 3", { x -> -x as Double * x }), Color.GREEN)
//        chart.addSeries(prepareSeries("Series 4", { x -> (x - 250).toDouble() * x }), Color.DARKCYAN)
//        chart.addSeries(prepareSeries("Series 5", { x -> (x + 100).toDouble() * (x - 200) }), Color.BROWN)
//
//        primaryStage.setTitle("MultipleAxesLineChart")
//
//        val borderPane = BorderPane()
//        borderPane.setCenter(chart)
//        borderPane.setBottom(chart.legend)
//
//        val scene = Scene(borderPane, 1024, 600)
//        scene.getStylesheets().add(this::class.java.getResource("style.css").toExternalForm())
//
//        primaryStage.setScene(scene)
//        primaryStage.show()
//    }
//
//    private fun prepareSeries(name: String, function: Function<Int, Double>): XYChart.Series<Number, Number> {
//        val series = XYChart.Series<Number, Number>()
//        series.name = name
//        for (i in 0 until X_DATA_COUNT) {
//            series.data.add(XYChart.Data<Number, Number>(i, function.apply(i)))
//        }
//        return series
//    }
//
//    companion object {
//
//        val X_DATA_COUNT = 3600
//
//        @JvmStatic
//        fun main(args: Array<String>) {
//            launch(args)
//        }
//    }
//}