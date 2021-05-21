package view;

import javafx.stage.Stage;
import model.PowerRecord;

import java.time.LocalDate;
import java.util.List;

public interface View {
    void initStage(Stage primaryStage);
    void setAvailableDates(List<LocalDate> dates);
    void scaleChart(LocalDate date);
    void setChartData(List<PowerRecord> records);
    void setCurrentPower(double power);
}
