import java.io.IOException;
import java.util.*;

/**
 * Central class of the UCI module
 * threadsafe singleton
 */
public class UCI implements Runnable {

    private static final String OPTIONS_CONF_FILE = "ucioptions.properties";

    private static UCI instance;

    private List<UCIListener> listeners;


    /**
     * private constructor
     */
    private UCI() {
        listeners = new ArrayList<>();
        attachListener(new DebugUCIListener());
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
     * A test-main
     *
     * @param args all arguments will be printed as info string
     */
    public static void main(String[] args) {
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
     */
    public ArrayList<OptionValuePair> initialize() {
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
     */
    public void awaitNextCommand() {
        String input = UCIBridge.getInstance().receiveString();
        try {


            if (input.startsWith(UCICommands.GO)) {
                for (UCIListener listener : listeners) {
                    listener.receivedGo(input);
                }
            } else if (input.equals(UCICommands.STOP)) {
                for (UCIListener listener : listeners) {
                    listener.receivedStop();
                }
            } else if (input.equals(UCICommands.UCI_NEW_GAME)) {
                for (UCIListener listener : listeners) {
                    listener.receivedNewGame();
                }
            } else if (input.startsWith(UCICommands.POSITION)) {
                for (UCIListener listener : listeners) {
                    Move move = parsePosition(input);
                    Board board = move.getBoard();
                    listener.receivedPosition(board, move);
                }
            } else {
                InfoHandler.sendUnknownCommandMessage(input);
            }
        } catch (InputMismatchException e) {
            InfoHandler.sendUnknownCommandMessage(input + " * " + e.getMessage());
        }
    }

    /**
     * Parses a position command an returns the corresponding move
     *
     * @param input the position command
     * @return the move
     */
    private Move parsePosition(String input) {
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
        return move;
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
     * Send the bestmove command to the gui
     *
     * @param move the best move
     */
    public synchronized void sendBestMove(Move move) {
        UCIBridge.getInstance().sendString(UCICommands.BEST_MOVE + " " + move);
    }

    /**
     * Waits forever for all commands from GUI
     * Informs all listeners, when there is a new command
     */
    public void awaitCommandsForever() {
        InfoHandler.sendDebugMessage("UCIThread: Sending implicit ucinewgame");
        for (UCIListener listener : listeners) {
            listener.receivedNewGame();
        }

        while (true) {
            try {
                awaitNextCommand();
            } catch (Exception e) {
                InfoHandler.sendMessage("UCIThread ERROR: " + e.getMessage());
            }
            InfoHandler.getInstance().flushInfoBuffer();
        }

    }

    /**
     * Run method for the UCI thread
     */
    public void run() {
        awaitCommandsForever();

    }
}
