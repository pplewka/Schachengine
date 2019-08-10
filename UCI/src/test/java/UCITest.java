import Exceptions.EngineQuitSignal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Disabled("Disabled until interface is considered stable") // set the tests public again when re-enabling
public class UCITest {

    private static final InputStream systemin = System.in;
    private static final PrintStream systemout = System.out;

    private static final String ENGINE_STARTUP_INPUT = "uci\nisready\nquit\n";
    private static final String ENGINE_STARTUP_OUTPUT = "id name HTW-Schachengine\n" +
            "id author Alessio Ragusa Matthias Sennikow Patrick Plewka\n" +
            "uciok\ninfo string \"Hello\"\ninfo string \"World!\"\ninfo nodes 5 cpuload 10\nreadyok\n";

    private static final String ENGINE_ONE_TURN_DEBUG_INPUT = "uci\nisready\ndebug on\nucinewgame\ngo\nstop\ngo\nquit\n";
    private static final String ENGINE_ONE_TURN_DEBUG_OUTPUT = "id name HTW-Schachengine\n" +
            "id author Alessio Ragusa Matthias Sennikow Patrick Plewka\n" +
            "uciok\n" +
            "info string \"Hello\"\n" +
            "info string \"World!\"\n" +
            "info nodes 5 cpuload 10\n" +
            "readyok\n" +
            "info string \"received ucinewgame command\"\n" +
            "info string \"received go command\"\n" +
            "info string \"received stop command\"\n" +
            "info string \"received go command\"\n";

    @AfterEach
    void teardown() {
        System.setOut(systemout);
        System.setIn(systemin);
        UCIBridge.deleteInstance();
    }

    @Test
    private void testEngineStartup() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printOutStream = new PrintStream(byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ENGINE_STARTUP_INPUT.getBytes());
        System.setOut(printOutStream);
        System.setIn(byteArrayInputStream);
        try {
            UCI.main(new String[]{});
            fail(); // engine should properly terminate with EngineQuitSignal
        } catch (EngineQuitSignal e) {

        }
        System.out.flush();
        assertEquals(ENGINE_STARTUP_OUTPUT, byteArrayOutputStream.toString());

    }

    @Test
    private void testEngineOneEmptyTurnWithDebug() {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printOutStream = new PrintStream(byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ENGINE_ONE_TURN_DEBUG_INPUT.getBytes());
        System.setOut(printOutStream);
        System.setIn(byteArrayInputStream);

        try {
            UCIBridge.deleteInstance();
            UCI.main(new String[]{});
            fail(); // engine should properly terminate with EngineQuitSignal
        } catch (EngineQuitSignal e) {

        }
        System.out.flush();
        assertEquals(ENGINE_ONE_TURN_DEBUG_OUTPUT, byteArrayOutputStream.toString());
    }
}
