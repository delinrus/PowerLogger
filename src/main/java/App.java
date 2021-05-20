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
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.statistics.HistogramDataset;

import java.time.DayOfWeek;
import java.time.LocalDate;


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

        double[] values = {95, 49, 14, 59, 50, 66, 47, 40, 1, 67,
                12, 58, 28, 63, 14, 9, 31, 17, 94, 71,
                49, 64, 73, 97, 15, 63, 10, 12, 31, 62,
                93, 49, 74, 90, 59, 14, 15, 88, 26, 57,
                77, 44, 58, 91, 10, 67, 57, 19, 88, 84
        };


        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", values, 20);

        JFreeChart histogram = ChartFactory.createHistogram("JFreeChart Histogram",
                "y values", "x values", dataset);

        return histogram;
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
