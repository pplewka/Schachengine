import Exceptions.EngineQuitSignal;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Central class of the UCI module
 * threadsafe singleton
 */
public class UCI {

    private static final String OPTIONS_CONF_FILE = "ucioptions.properties";

    private static UCI instance;

    private static boolean debug = false;

    private UCIBridge uciBridge;

    /**
     * Returns the unique UCI instance
     * @return the instance
     */
    public static UCI getInstance() {
        if (instance == null) {
            synchronized (UCI.class) {
                if (instance == null) {
                    instance = new UCI();
                }
            }
        }
        return instance;
    }

    /**
     * Sets debug mode. Engine should determine what it wants to send based on the debugmode
     * @param debugmode the debug mode
     */
    public static void setDebug(boolean debugmode) {
        debug = debugmode;
    }

    /**
     * Gets debug mode. Engine should determine what it wants to send based on the debugmode
     * @return the debug mode
     */
    public static boolean getDebug() {
        return debug;
    }

    /**
     * Initialize the UCI engine and GUI. See UCIBridge.initialize for more information
     * @throws EngineQuitSignal if the engine received the quit command from GUI
     */
    public void initialize() throws EngineQuitSignal {
        Properties options = new Properties();

        try {
            options.load(Objects.requireNonNull(UCI.class.getClassLoader().getResourceAsStream(OPTIONS_CONF_FILE)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        uciBridge.initialize(options);
    }

    /**
     * private constructor
     */
    private UCI() {
        uciBridge = UCIBridge.getInstance();
    }

    /**
     * A test-main
     * @param bla will be ignored
     * @throws EngineQuitSignal if engine receives the quit command
     */
    public static void main(String[] bla) throws EngineQuitSignal {
        UCI uci = UCI.getInstance();
        uci.initialize();
        InfoHandler.sendMessage("Hello");
    }
}
