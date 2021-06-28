import javafx.application.Application;
import javafx.stage.Stage;
import model.DataManager;
import presenter.Presenter;
import view.ViewImpl;


public class App extends Application { 
    private final DataManager dataManager = new DataManager();
    private final ViewImpl view = new ViewImpl();
    private final Presenter presenter = new Presenter(view, dataManager);

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        view.setPresenter(presenter);
        view.initStage(primaryStage);
        presenter.start();
    }

    @Override
    public void stop() throws Exception {
        presenter.stop();
    }
}
