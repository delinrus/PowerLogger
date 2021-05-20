import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class PowerRecord implements Serializable {
    public double power;
    public LocalDateTime time;

    public PowerRecord(double power, LocalDateTime time) {
        this.power = power;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PowerRecord record = (PowerRecord) o;
        return Double.compare(record.power, power) == 0 &&
                Objects.equals(time, record.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(power, time);
    }

    @Override
    public String toString() {
        return "PowerRecord{" +
                "power=" + power +
                ", time=" + time +
                '}';
    }

    public double getPower() {
        return power;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
