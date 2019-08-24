import Exceptions.TimeManagementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;


public class TimeManBlitzChessBasedTest {

    private static final long FIVE_MINUTES = 300000;
    private TimeManBlitzChessBased sut = (TimeManBlitzChessBased) TimeManBlitzChessBased.getInstance();

    @BeforeEach
    public void resetSUT() {
        if(sut.isInit()) {
            sut.reset();
        }
    }


    @Test
    public void testInitNotAllowedTotalTimeValues() {
        // nice addition with junit 5...
        assertThrows(TimeManagementException.class,
                () -> sut.init(-1, 0, 0),
                "no TimeManagementException thrown, this shouldn't happen");

        assertThrows(TimeManagementException.class,
                () -> sut.init(0, 0, 0),
                "no TimeManagementException thrown, this shouldn't happen");
    }

    @Test
    public void testInitNotAllowedIncValues() {
        assertThrows(TimeManagementException.class,
                () -> sut.init(1, -1, 0),
                "no TimeManagementException thrown, this shouldn't happen");
    }

    @Test
    public void testInitNotAllowedMovesCntTimeValues() {
        assertThrows(TimeManagementException.class,
                () -> sut.init(0, 0, -1),
                "no TimeManagementException thrown, this shouldn't happen");
    }

    @Test
    public void testInitNotAllowedMoveTime() {
        assertThrows(TimeManagementException.class,
                () -> sut.init(-1),
                    "no TimeManagementException thrown, this shouldn't happen");
//        assertThrows(TimeManagementException.class,
//                () -> sut.init(0),
//                "no TimeManagementException thrown, this shouldn't happen");
    }

    @Test
    public void initTest() {
        sut.init(FIVE_MINUTES, 0, 0);
        assertAll("Not all vars are set correctly",
                () -> assertEquals((FIVE_MINUTES / 80), sut.getTimeFrame()),
                () -> assertTrue(sut.getStartTime() != -1),
                () -> assertTrue(sut.getTimeFrame() != -1));
    }

    @Test
    public void initTestFail() {
        sut.init(10, 0, 0);
        assertThrows(TimeManagementException.class,
                () -> sut.init(100,10, 0));
        sut.reset();
    }

    @Test
    public void resetTest() {
        sut.init(10000, 0, 0);
        sut.reset();

        assertAll("Incorrect Values set while doing the reset",
                () -> assertEquals(sut.getStartTime(), -1),
                () -> assertEquals(sut.getTimeFrame(), -1));
    }

    @Test
    public void resetFailAlreadyResetTest() {
        sut.init(100000, 0, 0);
        sut.reset();
        assertThrows(TimeManagementException.class, sut::reset);
    }

    @Test
    public void testIsEnoughTimeNeverInit() {
        assertTrue(sut.isEnoughTime());
    }

    @Test
    public void testIsEnoughTime() throws InterruptedException {
        sut.init(FIVE_MINUTES, 0, 0); //Time runs from now on

        // 5 min = 300'000 / 40 = 3750,
        // 3750 * 0.75 = 2812.5
        for(int i=0;i<2;i++) {
            Thread.sleep(1400);
        }
        Thread.sleep(13);
        assertFalse(sut.isEnoughTime());

        sut.reset();
    }

    @Test
    public void testInitMoveTime() {

        sut.init(10000);

        assertEquals(10000, sut.getTimeFrame());

        sut.reset();
    }

    @Test
    public void testMoveTimeIsEnoughTime() throws InterruptedException {

        sut.init(2000);
        Thread.sleep(1000);
        assertTrue(sut.isEnoughTime());
        Thread.sleep(1000);
        assertFalse(sut.isEnoughTime());

        sut.reset();
    }

}
