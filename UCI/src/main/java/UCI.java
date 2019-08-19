import Exceptions.EngineQuitSignal;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

/**
 * Central class of the UCI module
 * threadsafe singleton
 */
public class UCI implements Runnable {

    private static final String OPTIONS_CONF_FILE = "ucioptions.properties";

    private static UCI instance;

    private static boolean debug = false;

    private UCIBridge uciBridge;

    private List<UCIListener> listeners;


    /**
     * private constructor
     */
    private UCI() {
        listeners = new ArrayList<>();

    }

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
     * Gets debug mode. Engine should determine what it wants to send based on the debugmode
     *
     * @return the debug mode
     */
    public static synchronized boolean getDebug() {
        return debug;
    }

    /**
     * Sets debug mode. Engine should determine what it wants to send based on the debug_mode
     * Attaches (or removes) the DebugUCIListener depending on debug_mode
     *
     * @param debug_mode the debug mode
     */
    public static synchronized void setDebug(boolean debug_mode) {
        if (debug == debug_mode) {
            return;
        }
        debug = debug_mode;
        if (debug) {
            getInstance().attachListener(new DebugUCIListener());
        } else {
            List<UCIListener> listeners = getInstance().listeners;
            List<UCIListener> new_listeners = new ArrayList<>();
            for (UCIListener lis : listeners) {
                if (!(lis instanceof DebugUCIListener)) {
                    new_listeners.add(lis);
                }
            }
            getInstance().listeners = new_listeners;
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
        var options = uci.initialize();
        for (OptionValuePair optionValuePair : options) {
            InfoHandler.sendDebugMessage(optionValuePair.option + " value " + optionValuePair.value);
        }
        InfoHandler.sendMessage("Hello\nWorld!");
        InfoHandler.getInstance().storeInfo("nodes", 5L);
        InfoHandler.getInstance().storeInfo(InfoHandler.CPULOAD, 10.0);
        InfoHandler.getInstance().flushInfoBuffer();
        for (String argument : args) {
            InfoHandler.sendDebugMessage(argument);
        }
        InfoHandler.getInstance().flushInfoBuffer();
        uci.awaitCommandsForever();
    }

    /**
     * Initialize the UCI engine and GUI. See UCIBridge.initialize for more information
     *
     * @return
     * @throws EngineQuitSignal if the engine received the quit command from GUI
     */
    public ArrayList<OptionValuePair> initialize() throws EngineQuitSignal {
        Properties options = new Properties();

        try {
            options.load(Objects.requireNonNull(UCI.class.getClassLoader().getResourceAsStream(OPTIONS_CONF_FILE)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return UCIBridge.getInstance().initialize(options);
    }

    /**
     * Waits for the next command from GUI and informs all attached listeners
     *
     * @throws EngineQuitSignal if the GUI sends the quit command
     */
    public void awaitNextCommand() throws EngineQuitSignal {
        String input = UCIBridge.getInstance().receiveString();
        if (input.startsWith(UCICommands.GO)) {
            for (UCIListener listener : listeners) {
                listener.receivedGo(input);
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
                Board board = parsePosition(input);
                listener.receivedPosition(board);
            }
        } else {
            UCIBridge.getInstance().sendUnknownCommandMessage(input);
        }
    }

    private Board parsePosition(String input) {
        String original_input = input;
        if (input.contains("startpos")) {
            input = input.replaceFirst("startpos", "fen " + Board.START_FEN);
        }
        input = input.replaceFirst("position fen ", "");
        String[] fenandmoves = input.split("moves");
        Move move = new MoveImpl(fenandmoves[0]);
        if (fenandmoves.length == 2) {
            move.moves(fenandmoves[1]);
        } else if (fenandmoves.length > 2) {
            throw new InputMismatchException("input: " + original_input + "has too many \"moves\"");
        }
        return move.getBoard();
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
    public void awaitCommandsForever() throws EngineQuitSignal {
        InfoHandler.sendDebugMessage("Sending implicit ucinewgame");
        for (UCIListener listener : listeners) {
            listener.receivedNewGame();
        }

        while (true) {
            awaitNextCommand();
            InfoHandler.getInstance().flushInfoBuffer();
        }

    }

    public void run() {
        try {
            awaitCommandsForever();
        } catch (EngineQuitSignal engineQuitSignal) {
            System.exit(0);
        }
    }
}
