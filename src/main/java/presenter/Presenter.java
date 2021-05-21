package presenter;

import model.DataManager;
import view.View;

import java.time.LocalDate;

public class Presenter implements Observer {
    private View view;
    private DataManager dataManager;
    private LocalDate date = LocalDate.now();

    public Presenter(View view, DataManager dataManager) {
        this.view = view;
        this.dataManager = dataManager;
        dataManager.subscribe(this);
    }

    public void onRefreshBtnClick() {
        refresh();
    }

    public void onDatePicked(LocalDate localDate) {
        view.setChartData(dataManager.getRecords(localDate));
        this.date = localDate;
    }

    public void start() throws Exception {
        dataManager.start();
        refresh();
    }

    public void stop() throws Exception {
        dataManager.stop();
    }

    private void refresh() {
        view.setAvailableDates(dataManager.getAvailableDates());
        view.setChartData(dataManager.getRecords(date));
        view.scaleChart(date);
    }

    @Override
    public void updateValue(double value) {
        view.setCurrentPower(value);
    }
}
