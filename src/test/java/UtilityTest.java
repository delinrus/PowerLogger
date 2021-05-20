import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilityTest {

    @Test
    void test1() {
        int secondOfDay = LocalTime.of(1, 0, 0).toSecondOfDay();
        assertEquals(60 * 60, secondOfDay);
    }
}
