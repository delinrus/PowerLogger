package view;

import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public interface View {
    void initStage(Stage primaryStage);
    void setAvailableDates(List<LocalDate> dates);
}
