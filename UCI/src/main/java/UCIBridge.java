import Exceptions.EngineQuitSignal;

import java.util.Map;
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
     * Sends a String to the GUI
     * Use carefully, because the GUI expects input according to UCI specifications
     *
     * @param string The String
     */
    private synchronized void sendString(String string) {
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
    public synchronized String receiveString() throws EngineQuitSignal {

        String input = reader.nextLine();
        input = removeUnnecessaryWS(input);
        if (input.equals(UCICommands.IS_READY)) {
            sendReadyOk();
            return receiveString();
        }
        if (input.equals(UCICommands.QUIT)) {
            throw new EngineQuitSignal(QUIT_MSG);
        }
        if (input.equals(UCICommands.DEBUG_OFF)) {
            UCI.setDebug(false);
            return receiveString();
        }
        if (input.equals(UCICommands.DEBUG_ON)) {
            UCI.setDebug(true);
            return receiveString();
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


    public synchronized void sendLongInfo(Map<String, Long> longValues) {
    }


    public synchronized void sendDoubleInfo(Map<String, Double> doubleValues) {

    }


    public synchronized void sendIntInfo(Map<String, Integer> intValues) {

    }


    public synchronized void sendStringInfo(Map<String, String> stringValues) {
        StringBuilder out = new StringBuilder(UCICommands.INFO + " ");
        for (String key :
                stringValues.keySet()) {
            out.append(key.toLowerCase()).append(" ").append(stringValues.get(key)).append(" ");
        }
        String complete = removeUnnecessaryWS(out.toString());
        if (!complete.equals(UCICommands.INFO)) {
            sendString(out.toString());
        }
    }


    public synchronized void sendStringListInfo(Map<String, String[]> stringListValues) {

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
     * @param ucioptions options for uci TODO: necessary? can setoptions and option be ignored?
     * @throws EngineQuitSignal if the engine received the quit input from the GUI
     */
    public synchronized void initialize(Properties ucioptions) throws EngineQuitSignal {
        String input = receiveCommand(UCICommands.UCI);
        sendID(ucioptions);
        sendAvailableOptions(ucioptions);
        sendUCIOk();
        receiveOptions(ucioptions);
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
     * Should handle the option commands at initialization
     * Currently empty, because we don't support options at this moment
     *
     * @param ucioptions options for uci
     * @throws EngineQuitSignal if the engine received the quit input from the GUI
     */
    private void receiveOptions(Properties ucioptions) throws EngineQuitSignal {
        //TODO: receive options correctly
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

    }

    /**
     * Sends a message to the GUI, because a entered command was unknown
     *
     * @param command the unknown command
     */
    public synchronized void sendUnknownCommandMessage(String command) {
        InfoHandler.sendMessage(UCICommands.UNKNOWN_CMD + " " + command);
    }

    /**
     * Deletes the unique UCIBridge instance
     * Used in test to reload the reader attribute
     */
    public static synchronized void deleteInstance(){
        instance = null;
    }
}
