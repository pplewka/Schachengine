import Exceptions.EngineQuitSignal;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    private List<UCIListener> listeners;

    /**
     * Returns the unique UCI instance
     *
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
     * Sets debug mode. Engine should determine what it wants to send based on the debug_mode
     * Attaches (or removes) the DebugUCIListener depending on debug_mode
     * @param debug_mode the debug mode
     */
    public static void setDebug(boolean debug_mode) {
        if(debug == debug_mode){
            return;
        }
        debug = debug_mode;
        if (debug) {
            getInstance().attachListener(new DebugUCIListener());
        } else {
            List<UCIListener> listeners = getInstance().listeners;
            List<UCIListener> new_listeners = new ArrayList<>();
            for (UCIListener lis : listeners) {
                if(!(lis instanceof DebugUCIListener)){
                    new_listeners.add(lis);
                }
            }
            getInstance().listeners = new_listeners;
        }
    }

    /**
     * Gets debug mode. Engine should determine what it wants to send based on the debugmode
     *
     * @return the debug mode
     */
    public static boolean getDebug() {
        return debug;
    }

    /**
     * Initialize the UCI engine and GUI. See UCIBridge.initialize for more information
     *
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
        listeners = new ArrayList<>();
    }

    /**
     * Waits for the next command from GUI and informs all attached listeners
     *
     * @throws EngineQuitSignal if the GUI sends the quit command
     */
    public synchronized void awaitNextCommand() throws EngineQuitSignal {
        String input = uciBridge.receiveString();
        if (input.startsWith(UCICommands.GO)) {
            for (UCIListener listener : listeners) {
                listener.receivedGo();
            }
        } else if (input.startsWith(UCICommands.STOP)) {
            for (UCIListener listener : listeners) {
                listener.receivedStop();
            }
        } else if (input.startsWith(UCICommands.UCI_NEW_GAME)) {
            for (UCIListener listener : listeners) {
                listener.receivedNewGame();
            }
        } else if (input.startsWith(UCICommands.POSITION)) {
            for (UCIListener listener : listeners) {
                listener.receivedPosition(input);
            }
        }
    }

    /**
     * Attaches a UCIListener to UCI.
     * This Listener will be informed of all inputs
     *
     * @param listener the listener
     */
    public synchronized void attachListener(UCIListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes an attached UCIListener from UCI
     *
     * @param listener the listener
     */
    public synchronized void removeListener(UCIListener listener) {
        listeners.remove(listener);
    }

    /**
     * Waits forever for all commands from GUI
     * Informs all listeners, when there is a new command
     *
     * @throws EngineQuitSignal if quit command was send from GUI
     */
    public synchronized void awaitCommandsForever() throws EngineQuitSignal {

        while (true) {
            awaitNextCommand();
        }

    }

    /**
     * A test-main
     *
     * @param args all arguments will be printed as info string
     * @throws EngineQuitSignal if engine receives the quit command
     */
    public static void main(String[] args) throws EngineQuitSignal {
        UCI uci = UCI.getInstance();
        uci.initialize();
        InfoHandler.sendMessage("Hello\nWorld!");
        for (String argument : args) {
            InfoHandler.sendMessage(argument);
        }
        uci.awaitCommandsForever();
    }
}
