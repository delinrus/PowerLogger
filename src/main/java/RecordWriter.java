import java.io.*;
import java.util.List;

public class RecordWriter<T> implements AutoCloseable {
    private final ObjectOutputStream out;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public RecordWriter(File file) throws IOException {
        file.createNewFile(); // if file already exists will do nothing
        out = new ObjectOutputStream(new FileOutputStream(file));
    }

    public void write(List<T> records) throws IOException {
        for (T record : records) {
            out.writeObject(record);
        }
    }

    public void write(T record) throws IOException {
        out.writeObject(record);
        out.flush();
    }

    @Override
    public void close() throws Exception {
        if (out != null) {
            out.close();
        }
    }
}
