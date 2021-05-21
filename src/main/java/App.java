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
import model.DataManager;
import model.DataManagerImpl;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import presenter.Presenter;
import utils.DateTimeUtils;
import view.View;
import view.ViewImpl;

import java.time.*;
import java.util.List;


public class App extends Application {

    private final DataManager dataManager = new DataManagerImpl();
    private final ViewImpl view = new ViewImpl();
    private final Presenter presenter = new Presenter(view);

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        view.setPresenter(presenter);
        view.initStage(primaryStage);
        dataManager.start();

        view.setAvailableDates(List.of(LocalDate.now().minusDays(1),
                LocalDate.now(),
                LocalDate.now().plusDays(1)));
    }

    @Override
    public void stop() throws Exception {
        dataManager.stop();
    }
}
