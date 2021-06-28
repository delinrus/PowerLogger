package view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
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
    private Button refreshBtn;
    private Label currentPowerLbl;

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
        datePicker.valueProperty().addListener((ov, oldValue, newValue) -> presenter.onDatePicked(newValue));

        refreshBtn = new Button("Refresh");
        root.getChildren().add(refreshBtn);
        StackPane.setAlignment(refreshBtn, Pos.TOP_RIGHT);
        refreshBtn.setOnAction((e) -> presenter.onRefreshBtnClick());

        currentPowerLbl = new Label("0 W");
        root.getChildren().add(currentPowerLbl);
        StackPane.setAlignment(currentPowerLbl, Pos.TOP_CENTER);

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
        rangeAxis.setAutoRange(true);
        DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
        domainAxis.setAutoRange(false);
        domainAxis.setRange(
                DateTimeUtils.convertToDate(LocalDateTime.of(date, LocalTime.of(0, 0, 0))),
                DateTimeUtils.convertToDate(LocalDateTime.of(date.plusDays(1), LocalTime.of(0, 0, 0))));
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
        chart = ChartFactory.createTimeSeriesChart("Power diagram",
                null, "P, W", dataset);
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
 
        Second lastTime = new Second();
        // Adding to series only unique times
        for(PowerRecord pr: records) {
            Second newTime = DateTimeUtils.convertToSecond(pr.time);
            if (!lastTime.equals(newTime)) {
                series.add(newTime, pr.power);
            }
            lastTime = newTime;
        }

        dataset.addSeries(series);
    }

    @Override
    public void setCurrentPower(double power) {
        Platform.runLater(() -> currentPowerLbl.setText(Double.toString(power) + " W"));
    }
}
