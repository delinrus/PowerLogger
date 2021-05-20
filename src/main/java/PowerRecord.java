import java.io.Serializable;
import java.time.LocalDateTime;

public record PowerRecord(double power, LocalDateTime time) implements Serializable {
}
