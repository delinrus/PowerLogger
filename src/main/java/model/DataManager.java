package model;

import presenter.Observer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class DataManager {
    private final File recordFile = new File("records.svr");
    private PowerMeterReader powerMeterReader;
    private RecordWriter<PowerRecord> recordWriter;
    private final List<PowerRecord> allRecords = new CopyOnWriteArrayList<>();
    private Observer observer;


    private List<PowerRecord> readAllRecords() throws Exception {
        RecordReader<PowerRecord> recordReader = new RecordReader<>(recordFile);
        List<PowerRecord> powerRecords = recordReader.readAll();
        recordReader.close();
        return powerRecords;
    }

    public void start() throws Exception {
        allRecords.addAll(readAllRecords());
        System.out.println("Records count " + allRecords.size());

        recordWriter = new RecordWriter<>(recordFile);
        recordWriter.write(allRecords);
        powerMeterReader = new PowerMeterReader(power -> {
            PowerRecord record = new PowerRecord(power, LocalDateTime.now());
            allRecords.add(record);
            if (observer != null) {
                observer.updateValue(record.power);
            }
            try {
                recordWriter.write(record);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<LocalDate> getAvailableDates() {
        return allRecords.stream()
                .map(powerRecord -> powerRecord.time.toLocalDate())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<PowerRecord> getRecords(LocalDate date) {
        return allRecords.stream()
                .filter(powerRecord -> powerRecord.time.toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    public void stop() throws Exception {
        powerMeterReader.close();
        recordWriter.close();
    }

    public void subscribe(Observer observer) {
        this.observer = observer;
    }
}
