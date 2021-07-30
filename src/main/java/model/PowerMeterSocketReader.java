package model;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PowerMeterSocketReader implements AutoCloseable {
    private Socket clientSocket;
    private final Consumer<Double> callback;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
    private ScheduledFuture<?> scheduledFuture;
    private PrintWriter out;
    private BufferedReader in;
    private InputStream inputStream;

    public PowerMeterSocketReader(Consumer<Double> callback) throws IOException {
        this.callback = callback;
        startConnection("127.0.0.1", 50002);
        startCommunication();
    }

    @Override
    public void close() throws Exception {
        executorService.shutdownNow();
        in.close();
        out.close();
        clientSocket.close();
    }

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        inputStream = clientSocket.getInputStream();
        in = new BufferedReader(new InputStreamReader(inputStream));
    }

    private void sendQuery() {
        out.println("V02\r\n");
    }

    private void startCommunication() {
        executorService.scheduleAtFixedRate(this::sendQuery, 0, 5, TimeUnit.SECONDS);
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    String data = in.readLine();
                    callback.accept(Double.valueOf(data));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
