package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.PowerRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import presenter.Presenter;
import utils.DateTimeUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ViewImpl implements View {
    private Presenter presenter;
    private DatePicker datePicker;
    private JFreeChart chart;

    @Override
    public void initStage(Stage primaryStage) {
        primaryStage.setTitle("PowerLogger");
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);

        Image image = new Image(getClass().getResourceAsStream("/thunderbolt.png"));
        primaryStage.getIcons().add(image);

        StackPane root = new StackPane();
        root.setPadding(new Insets(10, 10, 10, 10));

        datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.setShowWeekNumbers(true);
        datePicker.setDayCellFactory(getDayCellFactory(List.of(LocalDate.now())));
        root.getChildren().add(datePicker);
        StackPane.setAlignment(datePicker, Pos.TOP_LEFT);
        datePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            System.out.println(newValue);
        });

        Button scaleChartBtn = new Button("Scale");
        root.getChildren().add(scaleChartBtn);
        StackPane.setAlignment(scaleChartBtn, Pos.TOP_RIGHT);
        scaleChartBtn.setOnAction((e) -> presenter.onScaleBtnClick());

        ChartViewer viewer = new ChartViewer(createChart());
        scaleChart(LocalDate.now());
        root.getChildren().add(viewer);
        StackPane.setMargin(viewer, new Insets(40, 0, 0, 0));

        Scene primaryScene = new Scene(root);
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    @Override
    public void setAvailableDates(List<LocalDate> dates) {
        datePicker.setDayCellFactory(getDayCellFactory(dates));
    }

    @Override
    public void scaleChart(LocalDate date) {
        XYPlot plot = chart.getXYPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
        rangeAxis.setAutoRange(false);
        rangeAxis.setRange(0, 100);
        domainAxis.setAutoRange(false);
        domainAxis.setRange(
                DateTimeUtils.convertToDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0))),
                DateTimeUtils.convertToDate(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0, 0, 0))));
    }

    private Callback<DatePicker, DateCell> getDayCellFactory(List<LocalDate> dates) {

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!dates.contains(item)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        return dayCellFactory;
    }

    public JFreeChart createChart() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries("Time");
        series.add(DateTimeUtils.convertToSecond(LocalDateTime.of(LocalDate.now(), LocalTime.of(7, 2, 10))), 10);
        series.add(DateTimeUtils.convertToSecond(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 2, 10))), 20);
        series.add(DateTimeUtils.convertToSecond(LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 2, 10))), 30);
        series.add(DateTimeUtils.convertToSecond(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 2, 10))), 20);
        series.add(DateTimeUtils.convertToSecond(LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 2, 10))), 25);

        dataset.addSeries(series);
        chart = ChartFactory.createTimeSeriesChart("JFreeChart Histogram",
                "y values", "x values", dataset);
        return chart;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setChartData(List<PowerRecord> records) {
        XYPlot plot = chart.getXYPlot();
        TimeSeriesCollection dataset = (TimeSeriesCollection) plot.getDataset();
        dataset.removeAllSeries();
        TimeSeries series = new TimeSeries("Time");
        records.forEach((pr)-> series.add(DateTimeUtils.convertToSecond(pr.time), pr.power));
        dataset.addSeries(series);
    }
}
