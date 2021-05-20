import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;


public class App extends Application {

    private final DataManager dataManager = new DataManagerImpl();

    public static void main(String[] args) {
        Application.launch();
    }

    private void initStage(Stage primaryStage) {
        primaryStage.setTitle("PowerLogger");
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);

        Image image = new Image(getClass().getResourceAsStream("/thunderbolt.png"));
        primaryStage.getIcons().add(image);

        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.setShowWeekNumbers(true);
        datePicker.setDayCellFactory(getDayCellFactory());

        datePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            System.out.println(newValue);
        });

        StackPane root = new StackPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.getChildren().add(datePicker);

        ChartViewer viewer = new ChartViewer(createChart());
        root.getChildren().add(viewer);
        StackPane.setMargin(viewer, new Insets(40, 0, 0, 0));
        StackPane.setAlignment(datePicker, Pos.TOP_LEFT);


        Scene primaryScene = new Scene(root);
        primaryStage.setScene(primaryScene);

        primaryStage.show();
    }

    private Callback<DatePicker, DateCell> getDayCellFactory() {

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        //Disable Monday, Tuesday, Wednesday
                        if (item.getDayOfWeek() == DayOfWeek.MONDAY
                                || item.getDayOfWeek() == DayOfWeek.TUESDAY
                                || item.getDayOfWeek() == DayOfWeek.WEDNESDAY) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        return dayCellFactory;
    }

    public static JFreeChart createChart() {


        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries("Time");
        series.add(new Second(new Date(LocalTime.of(1, 2, 10).toSecondOfDay() * 1000)), 10);
        series.add(new Second(new Date(LocalTime.of(1, 4, 10).toSecondOfDay() * 1000)), 2);
        series.add(new Second(new Date(LocalTime.of(1, 8, 10).toSecondOfDay() * 1000)), 59);
        series.add(new Second(new Date(LocalTime.of(1, 13, 10).toSecondOfDay() * 1000)), 70);
        series.add(new Second(new Date(LocalTime.of(1, 45, 10).toSecondOfDay() * 1000)), 75);

        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createTimeSeriesChart("JFreeChart Histogram",
                "y values", "x values", dataset);

        XYPlot plot = chart.getXYPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
        rangeAxis.setAutoRange(false);
        rangeAxis.setRange(0, 200);

        domainAxis.setAutoRange(false);
        domainAxis.setRange(new Date(LocalTime.of(0, 0, 0).toSecondOfDay() * 1000),
                new Date(LocalTime.of(12, 0, 0).toSecondOfDay() * 1000));


        return chart;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initStage(primaryStage);
        dataManager.start();
    }

    @Override
    public void stop() throws Exception {
        dataManager.stop();
    }
}
