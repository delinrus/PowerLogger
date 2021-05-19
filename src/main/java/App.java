import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;


public class App extends Application {
    private static SerialPort serialPort;
    private static Label helloWorldLabel;

    public static void main(String[] args) {
        Application.launch();
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("PowerLogger");
        primaryStage.setWidth(300);
        primaryStage.setHeight(200);

        Image image = new Image(getClass().getResourceAsStream("/thunderbolt.png"));
        primaryStage.getIcons().add(image);

        helloWorldLabel = new Label("Hello world!");
        helloWorldLabel.setAlignment(Pos.CENTER);
        Scene primaryScene = new Scene(helloWorldLabel);
        primaryStage.setScene(primaryScene);

        primaryStage.show();
        testPort();
    }

    public static void testPort() {
        serialPort = new SerialPort("COM1");
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
            serialPort.writeString("V00\r\n");
        } catch (SerialPortException ex) {
            ex.printStackTrace();
        }
    }

    private static class PortReader implements SerialPortEventListener {
        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String data = serialPort.readString(event.getEventValue());
                    Platform.runLater(() -> helloWorldLabel.setText(data));
                    serialPort.writeString("V00\r\n");
                } catch (SerialPortException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
