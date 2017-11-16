package com.github.holgerbrandl.kravis.javafx.multaxes;

/**
 * @author Holger Brandl
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.function.Function;

public class MultipleAxesLineChartMain extends Application {

    public static final int X_DATA_COUNT = 3600;


    @Override
    public void start(Stage primaryStage) throws Exception {
        NumberAxis xAxis = new NumberAxis(0, X_DATA_COUNT, 200);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Series 1");

        LineChart baseChart = new LineChart(xAxis, yAxis);
        baseChart.getData().add(prepareSeries("Series 1", (x) -> (double) x));

        MultipleAxesLineChart chart = new MultipleAxesLineChart(baseChart, Color.RED);
        chart.addSeries(prepareSeries("Series 2", (x) -> (double) x * x), Color.BLUE);
        chart.addSeries(prepareSeries("Series 3", (x) -> (double) -x * x), Color.GREEN);
        chart.addSeries(prepareSeries("Series 4", (x) -> ((double) (x - 250)) * x), Color.DARKCYAN);
        chart.addSeries(prepareSeries("Series 5", (x) -> ((double) (x + 100) * (x - 200))), Color.BROWN);

        primaryStage.setTitle("MultipleAxesLineChart");

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(chart);
        borderPane.setBottom(chart.getLegend());

        Scene scene = new Scene(borderPane, 1024, 600);
        scene.getStylesheets().add(getClass().getResource("/MultipleAxesLineChart.style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private XYChart.Series<Number, Number> prepareSeries(String name, Function<Integer, Double> function) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);
        for (int i = 0; i < X_DATA_COUNT; i++) {
            series.getData().add(new XYChart.Data<>(i, function.apply(i)));
        }
        return series;
    }


    public static void main(String[] args) {
        launch(args);
    }
}