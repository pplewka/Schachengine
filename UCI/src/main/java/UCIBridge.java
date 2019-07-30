import Exceptions.EngineQuitSignal;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class UCIBridge {


    public static final String QUIT_MSG = "GUI sends \"quit\"!";
    private volatile static UCIBridge instance;

    private Scanner reader;

    private UCIBridge() {
        reader = new Scanner(System.in);
    }

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

    private synchronized void sendString(String string) {
        System.out.println(string);
    }

    private synchronized String receiveString() throws EngineQuitSignal {

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

    private String removeUnnecessaryWS(String input) {
        input = input.trim();
        input = input.replaceAll("\\s+", " ");
        return input;
    }


    public void sendLongInfo(Map<String, Long> longValues) {
    }


    public void sendDoubleInfo(Map<String, Double> doubleValues) {

    }


    public void sendIntInfo(Map<String, Integer> intValues) {

    }


    public void sendStringInfo(Map<String, String> stringValues) {
        StringBuilder out = new StringBuilder(UCICommands.INFO);
        for (String key :
                stringValues.keySet()) {
            out.append(key.toLowerCase()).append(" ").append(stringValues.get(key)).append(" ");
        }
        sendString(out.toString());
    }


    public void sendStringListInfo(Map<String, String[]> stringListValues) {

    }

    private void sendID(Properties ucioptions) {
        sendString(UCICommands.ID_NAME + ucioptions.getProperty("id.name"));
        sendString(UCICommands.ID_AUTHOR + ucioptions.getProperty("id.author"));

    }

    public void initialize(Properties ucioptions) throws EngineQuitSignal {
        String input = receiveCommand(UCICommands.UCI);
        sendID(ucioptions);
        sendAvailableOptions(ucioptions);
        sendUCIOk();
        receiveOptions(ucioptions);
        receiveCommand(UCICommands.UCI_NEW_GAME);
    }

    private String receiveCommand(String command) throws EngineQuitSignal {
        String input = receiveString();
        while (!input.equals(command)) {
            sendUnknownCommandMessage(input + " expected " + command);
            input = receiveString();
        }
        return input;
    }

    private void receiveOptions(Properties ucioptions) throws EngineQuitSignal {
        //TODO: receive options correctly
    }

    private void sendUCIOk() {
        sendString(UCICommands.UCI_OK);
    }

    private void sendReadyOk() {
        sendString(UCICommands.READY_OK);
    }

    private void sendAvailableOptions(Properties ucioptions) {

    }


    public void sendUnknownCommandMessage(String command) {
        InfoHandler.getInstance().storeInfo(InfoHandler.STRING, UCICommands.UNKNOWN_CMD + command);
        InfoHandler.getInstance().sendStoredInfos();
    }
}
