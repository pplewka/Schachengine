
import Exceptions.MismatchedKeyTypeException;

import java.util.*;

/**
 * InfoHandler bundles all informations (see the UCI "info" command ) and sends them via UCIBridge to the GUI
 * It's a threadsafe singleton.
 * <p>
 * Usage:
 * set a bunch of infos with the corresponding InfoHandler::storeInfo()
 * commit and send send your infos with InfoHandler::flushInfoBuffer()
 * Example usage:
 * InfoHandler ih = InfoHandler.getInstance();
 * ih.storeInfo(ih.CPULOAD, 50.0);
 * ih.storeInfo(ih.DEPTH, 9);
 * ih.flushInfoBuffer();
 */
public class InfoHandler {

    public static final String DEPTH = "depth";
    public static final String SELDEPTH = "seldepth";
    public static final String TIME = "time";
    public static final String NODES = "nodes";
    public static final String PV = "pv";
    public static final String MULTIPV = "multipv";
    public static final String CURRMOVE = "currmove";
    public static final String SCORE_CP = "cp";
    public static final String SCORE_MATE = "mate";
    public static final String SCORE_LOWERBOUND = "lowerbound";
    public static final String SCORE_UPPERBOUND = "upperbound";
    public static final String CURRMOVE_NUMBER = "number";
    public static final String HASHFULL = "hashfull";
    public static final String NPS = "nps";
    public static final String TBHITS = "tbhits";
    public static final String SBHITS = "sbhits";
    public static final String CPULOAD = "cpuload";
    public static final String STRING = "string";
    public static final String REFUTATION = "refutation";
    public static final String CURRLINE_CPUNR = "cpunr";
    public static final String CURRLINE_MOVES = "moves";
    public static final Set<String> INT_KEYS = Set.of(
            DEPTH, SELDEPTH, MULTIPV, SCORE_CP, SCORE_MATE,
            SCORE_LOWERBOUND, SCORE_UPPERBOUND, HASHFULL, CURRLINE_CPUNR
    );
    public static final Set<String> LONG_KEYS = Set.of(TIME, NODES, CURRMOVE_NUMBER, NPS, TBHITS, SBHITS);
    public static final Set<String> DOUBLE_KEYS = Set.of(CPULOAD);
    public static final Set<String> STRING_KEYS = Set.of(STRING, CURRMOVE);
    public static final Set<String> LIST_STRING_KEYS = Set.of(PV, REFUTATION, CURRLINE_MOVES);
    private volatile static InfoHandler instance;

    private StringBuffer infoBuffer;


    /**
     * Constructor. Only used by getInstance
     */
    private InfoHandler() {
        infoBuffer = new StringBuffer();
        flushInfoBuffer();
    }

    /**
     * Empties the info buffer to System.out
     * Only prints, if stored string != "" && != "info"
     */
    public synchronized void flushInfoBuffer() {
        String out = infoBuffer.toString();
        if (!(out.equals(UCICommands.INFO) || out.equals(""))) {
            UCIBridge.getInstance().sendString(infoBuffer.toString().trim());
        }
        infoBuffer = new StringBuffer();
        infoBuffer.append(UCICommands.INFO);
    }

    /**
     * Returns the only instance of InfoHandler
     *
     * @return the InfoHandler instance
     */
    public static InfoHandler getInstance() {
        if (instance == null) {
            synchronized (InfoHandler.class) {
                if (instance == null) {
                    instance = new InfoHandler();
                }
            }

        }
        return instance;
    }

    /**
     * Sends a message to the GUI. Message won't be parsed by GUI (uses "info string")
     * Sends already stored infos separately before sending the message
     * Allows multiline messages.
     *
     * @param msg The message
     */
    public synchronized static void sendMessage(String msg) {
        getInstance().flushInfoBuffer();

        if (msg.contains("\n")) {
            for (String line : msg.split("\n")) {
                sendMessage(line);
            }
        } else {
            msg = msg.replace(" ", ". .");
            getInstance().storeInfo(STRING, "\"" + msg + "\"");
        }
        getInstance().flushInfoBuffer();
    }

    public synchronized static void sendDebugMessage(String msg) {
        if (UCI.getDebug()) {
            sendMessage(msg);
        }
    }

    /**
     * Stores a info
     *
     * @param key   key, from KEYS. Note use keys from the InfoHandler.[Type]_KEYS corresponding to the type of value
     * @param value the value of the info
     */
    public synchronized void storeInfo(String key, String value) {
        if (!STRING_KEYS.contains(key)) {
            throw new MismatchedKeyTypeException("NOT ALLOWED KEY USED IN storeInfo(STRING, STRING). " +
                    "ONLY USE KEYS FROM InfoHandler.STRING_KEYS");
        }
        infoBuffer.append(" ").append(key).append(" ").append(value);
    }

    /**
     * Stores a info
     *
     * @param key   key, from KEYS. Note use keys from the InfoHandler.[Type]_KEYS corresponding to the type of value
     * @param value the value of the info
     */
    public synchronized void storeInfo(String key, String[] value) {
        if (!LIST_STRING_KEYS.contains(key)) {
            throw new MismatchedKeyTypeException("NOT ALLOWED KEY USED IN storeInfo(STRING, STRING[]). " +
                    "ONLY USE KEYS FROM InfoHandler.LIST_STRING_KEYS");
        }
        throw new RuntimeException("NOT IMPLEMENTED");
        // stringListValues.put(key, value);
    }

    /**
     * Stores a info
     *
     * @param key   key, from KEYS. Note use keys from the InfoHandler.[Type]_KEYS corresponding to the type of value
     * @param value the value of the info
     */
    public synchronized void storeInfo(String key, Long value) {
        if (!LONG_KEYS.contains(key)) {
            throw new MismatchedKeyTypeException("NOT ALLOWED KEY USED IN storeInfo(STRING, LONG). " +
                    "ONLY USE KEYS FROM InfoHandler.LONG_KEYS");
        }
        infoBuffer.append(" ").append(key).append(" ").append(value);
    }

    /**
     * Stores a info
     *
     * @param key   key, from KEYS. Note use keys from the InfoHandler.[Type]_KEYS corresponding to the type of value
     * @param value the value of the info
     */
    public synchronized void storeInfo(String key, Integer value) {
        if (!INT_KEYS.contains(key)) {
            throw new MismatchedKeyTypeException("NOT ALLOWED KEY USED IN storeInfo(STRING, INTEGER). " +
                    "ONLY USE KEYS FROM InfoHandler.INT_KEYS");
        }
        infoBuffer.append(" ").append(key).append(" ").append(value);
    }

    /**
     * Stores a info
     *
     * @param key   key, from KEYS. Note use keys from the InfoHandler.[Type]_KEYS corresponding to the type of value
     * @param value the value of the info
     */
    public synchronized void storeInfo(String key, Double value) {
        if (!DOUBLE_KEYS.contains(key)) {
            throw new MismatchedKeyTypeException("NOT ALLOWED KEY USED IN storeInfo(STRING, DOUBLE). " +
                    "ONLY USE KEYS FROM InfoHandler.DOUBLE_KEYS");
        }
        infoBuffer.append(" ").append(key).append(" ").append(value.intValue());
    }
}