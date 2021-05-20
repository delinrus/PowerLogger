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

        recordWriter = new RecordWriter<>(recordFile);
        powerMeterReader = new PowerMeterReader(power -> {
            PowerRecord record = new PowerRecord(power, LocalDateTime.now());
            System.out.println("Power = " + record.toString() + " W");
            try {
                recordWriter.writeLine(record);
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
