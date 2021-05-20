import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.DayOfWeek;
import java.time.LocalDate;


public class App extends Application {

    private final DataManager dataManager = new DataManagerImpl();

    public static void main(String[] args) {
        Application.launch();
    }

    private void initStage(Stage primaryStage) {
        primaryStage.setTitle("PowerLogger");
        primaryStage.setWidth(300);
        primaryStage.setHeight(200);

        Image image = new Image(getClass().getResourceAsStream("/thunderbolt.png"));
        primaryStage.getIcons().add(image);

        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.setShowWeekNumbers(true);
        datePicker.setDayCellFactory(getDayCellFactory());

        datePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            System.out.println(newValue);
        });

        FlowPane root = new FlowPane();
        root.getChildren().add(datePicker);
        root.setPadding(new Insets(10));

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
