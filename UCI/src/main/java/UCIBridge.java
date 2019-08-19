import Exceptions.EngineQuitSignal;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

/**
 * The UCIBridge class is a threadsafe singleton responsible for the direct communication with the GUI
 * Every communication !MUST! go through this class!!!!
 */
public class UCIBridge {


    public static final String QUIT_MSG = "GUI sends \"quit\"!";
    private volatile static UCIBridge instance;

    private Scanner reader;

    /**
     * Private Constructor
     */
    private UCIBridge() {
        reader = new Scanner(System.in);
    }

    /**
     * Returns the unique instance of UCIBridge
     *
     * @return
     */
    public static UCIBridge getInstance() {
        if (instance == null) {
            synchronized (UCIBridge.class) {
                if (instance == null) {
                    instance = new UCIBridge();
                }
            }
        }
        return instance;
    }

    /**
     * Deletes the unique UCIBridge instance
     * Used in test to reload the reader attribute
     */
    public static synchronized void deleteInstance() {
        instance = null;
    }

    /**
     * Sends a String to the GUI
     * Use carefully, because the GUI expects input according to UCI specifications
     *
     * @param string The String
     */
    public synchronized void sendString(String string) {
        System.out.println(string);
    }

    /**
     * Waits for the next String
     * The following commands will be executed directly and receiveString will wait for the next String:
     * isready will be answered directly with readyok
     * debug on/off will use UCI.setDebug()
     * Quit will throw a EngineQuitSignal
     *
     * @return the String (trimmed and multiple whitespaces replaced with a single space)
     * @throws EngineQuitSignal if the "quit" command was received
     */
    public String receiveString() throws EngineQuitSignal {
        return receiveString(true);
    }

    /**
     * Waits for the next String
     * The following commands will be executed directly and receiveString will wait for the next String:
     * isready will be answered directly with readyok
     * debug on/off will use UCI.setDebug()
     * Quit will throw a EngineQuitSignal
     *
     * @param handleIsReadyCommand handles the isready command internally. if set to false, it won't and isready is a
     *                             valid return value
     * @return the String (trimmed and multiple whitespaces replaced with a single space)
     * @throws EngineQuitSignal if the "quit" command was received
     */
    public String receiveString(boolean handleIsReadyCommand) throws EngineQuitSignal {

        String input = reader.nextLine();
        input = removeUnnecessaryWS(input);
        if (input.equals(UCICommands.IS_READY) && handleIsReadyCommand) {
            sendReadyOk();
            return receiveString(true);
        }
        if (input.equals(UCICommands.QUIT)) {
            throw new EngineQuitSignal(QUIT_MSG);
        }
        if (input.equals(UCICommands.DEBUG_OFF)) {
            UCI.setDebug(false);
            return receiveString(handleIsReadyCommand);
        }
        if (input.equals(UCICommands.DEBUG_ON)) {
            UCI.setDebug(true);
            return receiveString(handleIsReadyCommand);
        }
        return input;
    }

    /**
     * Replaces multiple whitespaces with a single space and trims the String
     *
     * @param input The String
     * @return The shorted String
     */
    private String removeUnnecessaryWS(String input) {
        input = input.trim();
        input = input.replaceAll("\\s+", " ");
        return input;
    }

    /**
     * Sends the id's. Used at engine initialization
     *
     * @param ucioptions
     */
    private synchronized void sendID(Properties ucioptions) {
        sendString(UCICommands.ID_NAME + " " + ucioptions.getProperty("id.name"));
        sendString(UCICommands.ID_AUTHOR + " " + ucioptions.getProperty("id.author"));
    }

    /**
     * Initializes the engine. Engine and GUI will be before the first "ucinewgame" after this
     *
     * @param ucioptions options for uci
     * @return a ArrayList of the set options.
     * If option is not set (but supports default values) a Pair with default value is added
     * @throws EngineQuitSignal if the engine received the quit input from the GUI
     */
    public synchronized ArrayList<OptionValuePair> initialize(Properties ucioptions) throws EngineQuitSignal {
        receiveCommand(UCICommands.UCI);
        sendID(ucioptions);
        sendAvailableOptions(ucioptions);
        sendUCIOk();
        var result = receiveOptions(ucioptions);
        InfoHandler.sendDebugMessage("Received all options");
        sendReadyOk();
        for (OptionValuePair optionValuePair : result) {
            InfoHandler.sendDebugMessage(optionValuePair.option + " value " + optionValuePair.value);
        }
        return result;
    }

    /**
     * Waits until the given command is received
     * Use carefully:
     * <p>
     * all other commands (except the commands automatically handled
     * by receiveString()) will be ignored until this specific command is entered
     * The given command will be compared with the start of the output of receiveString()
     * receiveCommand("position") could return "position startpos"
     *
     * @param command The command to wait for
     * @return The command entered, matching the parameter command
     * @throws EngineQuitSignal if the engine received the quit input from the GUI
     */
    private String receiveCommand(String command) throws EngineQuitSignal {
        String input = receiveString();
        while (!input.startsWith(command)) {
            sendUnknownCommandMessage(input + " expected " + command);
            input = receiveString();
        }
        return input;
    }

    /**
     * Handles all incoming options
     *
     * @param ucioptions options for uci
     * @return a List of the options with values. if options are unset by the GUI, then they will be set with default
     * @throws EngineQuitSignal if the engine received the quit input from the GUI
     */
    private ArrayList<OptionValuePair> receiveOptions(Properties ucioptions) throws EngineQuitSignal {
        return UCIOptionHandler.receiveOptions(ucioptions);
    }

    /**
     * Sends the uciok command
     */
    private void sendUCIOk() {
        sendString(UCICommands.UCI_OK);
    }

    /**
     * Sends the readyok command
     */
    private void sendReadyOk() {
        sendString(UCICommands.READY_OK);
    }

    /**
     * Should send all supported setoption commands
     * Currently empty, because we don't support options at this moment
     *
     * @param ucioptions options for uci
     */
    private void sendAvailableOptions(Properties ucioptions) {
        UCIOptionHandler.sendAvailableOptions(ucioptions);

    }

    /**
     * Sends a message to the GUI, because a entered command was unknown
     *
     * @param command the unknown command
     */
    public synchronized void sendUnknownCommandMessage(String command) {
        InfoHandler.sendMessage(UCICommands.UNKNOWN_CMD + " " + command);
    }
}
