import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataManagerImpl implements DataManager {
    private final File recordFile = new File("records.svr");
    private PowerMeterReader powerMeterReader;
    private RecordWriter<PowerRecord> recordWriter;
    private List<PowerRecord> allRecords = new ArrayList<>();


    private List<PowerRecord> readAllRecords() throws Exception {
        RecordReader<PowerRecord> recordReader = new RecordReader<>(recordFile);
        List<PowerRecord> powerRecords = recordReader.readAll();
        recordReader.close();
        return powerRecords;
    }

    @Override
    public void start() throws Exception {
        allRecords = readAllRecords();
        System.out.println("Records count " + allRecords.size());

        recordWriter = new RecordWriter<>(recordFile);
        recordWriter.write(allRecords);

        powerMeterReader = new PowerMeterReader(power -> {
            PowerRecord record = new PowerRecord(power, LocalDateTime.now());
            System.out.println("Power = " + record.toString() + " W");
            try {
                recordWriter.write(record);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void stop() throws Exception {
        powerMeterReader.close();
        recordWriter.close();
    }
}
