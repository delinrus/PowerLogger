package presenter;

import model.PowerRecord;
import utils.DateTimeUtils;
import view.View;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Presenter {
    private View view;
    //private Model model;

    public Presenter(View view) {
        this.view = view;
    }

    public void onScaleBtnClick() {
        view.scaleChart(LocalDate.now());
        List<PowerRecord> list = new ArrayList<>();
        list.add(new PowerRecord(10, LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 2, 10))));
        list.add(new PowerRecord(59, LocalDateTime.of(LocalDate.now(), LocalTime.of(2, 2, 10))));
        list.add(new PowerRecord(80, LocalDateTime.of(LocalDate.now(), LocalTime.of(3, 2, 10))));
        list.add(new PowerRecord(75, LocalDateTime.of(LocalDate.now(), LocalTime.of(4, 2, 10))));
        list.add(new PowerRecord(60, LocalDateTime.of(LocalDate.now(), LocalTime.of(5, 2, 10))));
        view.setChartData(list);
    }

    public void onDatePicked(LocalDate localDate) {

    }


}
