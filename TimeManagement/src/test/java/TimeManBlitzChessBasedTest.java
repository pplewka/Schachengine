import Exceptions.TimeManagementException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TimeManBlitzChessBasedTest {

    private static final long FIVE_MINUTES = 300000;

    @Test
    public void testConstructObjectWithnotAllowedValues() {
        // nice addition with junit 5...
        assertThrows(TimeManagementException.class,
                () -> new TimeManBlitzChessBased(-1),
                "no TimeManagementException thrown, this shouldn't happen");

        assertThrows(TimeManagementException.class,
                () -> new TimeManBlitzChessBased(0),
                "no TimeManagementException thrown, this shouldn't happen");

    }

    @Test
    public void initTest() {
        TimeManBlitzChessBased sut = new TimeManBlitzChessBased(FIVE_MINUTES);
        sut.init();
        assertAll("Not all vars are set correctly",
                () -> assertEquals((FIVE_MINUTES / 80), sut.getTimeFrame()),
                () -> assertEquals(sut.getMovesCnt(), 0),
                () -> assertTrue(sut.getStartTime() != -1),
                () -> assertTrue(sut.getTimeFrame() != -1));
    }

    @Test
    public void initTestFail() {
        TimeManBlitzChessBased sut = new TimeManBlitzChessBased(10);
        sut.init();
        assertThrows(TimeManagementException.class,
                () -> sut.init());
    }

    @Test
    public void resetTest() {
        TimeManBlitzChessBased sut = new TimeManBlitzChessBased(10000);

        sut.init();
        sut.reset();

        assertAll("Incorrect Values set while doing the reset",
                () -> assertEquals(sut.getStartTime(), -1),
                () -> assertEquals(sut.getTimeFrame(), -1));
    }

    @Test
    public void resetFailAlreadyResetTest() {
        TimeManBlitzChessBased sut = new TimeManBlitzChessBased(100000);
        sut.init();
        sut.reset();
        assertThrows(TimeManagementException.class, () -> sut.reset());
        assertEquals(1, sut.getMovesCnt());
    }

    @Test
    public void resetFailNeverInitTest() {
        TimeManBlitzChessBased sut = new TimeManBlitzChessBased(10);
        assertThrows(TimeManagementException.class, () -> sut.reset());
        assertEquals(0, sut.getMovesCnt());
    }

    @Test
    public void testIsEnoughTimeNeverInit() {
        TimeManagement sut = new TimeManBlitzChessBased(1);
        assertTrue(sut.isEnoughTime());
    }

    @Test
    // Well, this test will be problematic
    public void testIsEnoughTime() throws InterruptedException {
        TimeManagement sut = new TimeManBlitzChessBased(FIVE_MINUTES);
        sut.init(); //Time runs from now on

        // 5 min = 300'000 / 40 = 3750,
        // 3750 * 0.75 = 2812.5
        for(int i=0;i<2;i++) {
            Thread.sleep(1400);
        }
        Thread.sleep(13);
        assertFalse(sut.isEnoughTime());
    }

}
