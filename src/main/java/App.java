import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;


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

        Label helloWorldLabel = new Label("Hello world!");
        helloWorldLabel.setAlignment(Pos.CENTER);
        Scene primaryScene = new Scene(helloWorldLabel);
        primaryStage.setScene(primaryScene);

        primaryStage.show();
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
