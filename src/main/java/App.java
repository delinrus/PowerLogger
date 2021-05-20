import javafx.application.Application;
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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.time.*;


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

        StackPane root = new StackPane();
        root.setPadding(new Insets(10, 10, 10, 10));

        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.setShowWeekNumbers(true);
        datePicker.setDayCellFactory(getDayCellFactory());
        root.getChildren().add(datePicker);
        StackPane.setAlignment(datePicker, Pos.TOP_LEFT);
        datePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            System.out.println(newValue);
        });

        Button scaleChartBtn = new Button("Scale");
        root.getChildren().add(scaleChartBtn);
        StackPane.setAlignment(scaleChartBtn, Pos.TOP_RIGHT);
        scaleChartBtn.setOnAction((e)-> System.out.println("Btn click"));

        ChartViewer viewer = new ChartViewer(createChart());
        root.getChildren().add(viewer);
        StackPane.setMargin(viewer, new Insets(40, 0, 0, 0));

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
        series.add(DateTimeUtils.convertToSecond(LocalDateTime.of(LocalDate.now(), LocalTime.of(7, 2, 10))), 10);
        series.add(DateTimeUtils.convertToSecond(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 2, 10))), 20);
        series.add(DateTimeUtils.convertToSecond(LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 2, 10))), 30);
        series.add(DateTimeUtils.convertToSecond(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 2, 10))), 20);
        series.add(DateTimeUtils.convertToSecond(LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 2, 10))), 25);

        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createTimeSeriesChart("JFreeChart Histogram",
                "y values", "x values", dataset);

        XYPlot plot = chart.getXYPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
        rangeAxis.setAutoRange(false);
        rangeAxis.setRange(0, 100);

        domainAxis.setAutoRange(false);
        domainAxis.setRange(
                DateTimeUtils.convertToDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0))),
                DateTimeUtils.convertToDate(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0, 0, 0))));


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
