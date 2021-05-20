import java.io.*;

public class RecordWriter<T> implements AutoCloseable {
    private final ObjectOutputStream out;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public RecordWriter(File file) throws IOException {
        file.createNewFile(); // if file already exists will do nothing
        out = new ObjectOutputStream(new FileOutputStream(file, true));
    }

    public void writeLine(T record) throws IOException {
        out.writeObject(record);
    }

    @Override
    public void close() throws Exception {
        if (out != null) {
            out.close();
        }
    }
}
