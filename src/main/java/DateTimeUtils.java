import org.jfree.data.time.Second;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeUtils {

    public static Second convertToSecond(LocalDateTime ldt) {
        Date date  = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        return new Second(date);
    }

    public static Date convertToDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

}
