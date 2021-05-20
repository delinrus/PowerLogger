import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordReader<T> implements AutoCloseable {
    private ObjectInputStream in;
    private boolean isFileFound = false;

    public RecordReader(File file) throws IOException {
        try {
            in = new ObjectInputStream(new FileInputStream(file));
            isFileFound = true;
        } catch (FileNotFoundException ignored) {
        }
    }

    public List<T> readAll() throws IOException, ClassNotFoundException {
        if (!isFileFound) return Collections.emptyList();

        List<T> list = new ArrayList<>();
        try {
            while (true) {
                @SuppressWarnings("unchecked") T record = (T) in.readObject();
                if (record == null) break;
                list.add(record);
            }
        } catch (EOFException e) {
            // End of file is reached
        }
        return list;
    }

    @Override
    public void close() throws Exception {
        if (in != null) {
            in.close();
        }
    }
}
