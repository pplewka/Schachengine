import Exceptions.EngineQuitSignal;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class UCI {

    private static final String OPTIONS_CONF_FILE = "ucioptions.properties";

    private static UCI instance;

    private static boolean debug = false;

    private UCIBridge uciBridge;

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

    public static void setDebug(boolean debugmode) {
        debug = debugmode;
    }

    public static boolean getDebug() {
        return debug;
    }

    public void initialize() throws EngineQuitSignal {
        Properties options = new Properties();

        try {
            options.load(Objects.requireNonNull(UCI.class.getClassLoader().getResourceAsStream(OPTIONS_CONF_FILE)));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("fuck" + OPTIONS_CONF_FILE);
        }
        uciBridge.initialize(options);
    }

    private UCI() {
        uciBridge = UCIBridge.getInstance();
    }

    public static void main(String[] bla) throws EngineQuitSignal {
        UCI uci = UCI.getInstance();
        uci.initialize();
        InfoHandler infoHandler = InfoHandler.getInstance();
        infoHandler.storeInfo("string", "hello");
        infoHandler.sendStoredInfos();
    }
}
