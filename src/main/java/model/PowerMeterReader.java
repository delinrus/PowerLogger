package model;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PowerMeterReader implements AutoCloseable {
    private SerialPort serialPort;
    private final Consumer<Double> callback;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public PowerMeterReader(Consumer<Double> callback) {
        this.callback = callback;
        openPort();
        startCommunication();
    }

    @Override
    public void close() throws Exception {
        executorService.shutdownNow();
        serialPort.closePort();
    }

    private void openPort() {
        serialPort = new SerialPort("COM1");
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
        } catch (SerialPortException ex) {
            ex.printStackTrace();
        }
    }

    private void sendQuery() {
        try {
            serialPort.writeString("V02\r\n");  // Sending command to read Watt https://www.gwinstek.com/en-GB/products/downloadSeriesDownNew/11635/987
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    private void startCommunication() {
        executorService.scheduleAtFixedRate(this::sendQuery, 0, 1, TimeUnit.SECONDS);
    }

    private class PortReader implements SerialPortEventListener {
        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String data = serialPort.readString(event.getEventValue());
                    callback.accept(Double.valueOf(data));
                } catch (SerialPortException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
}
