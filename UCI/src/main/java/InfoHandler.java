
import Exceptions.MismatchedKeyTypeException;

import java.util.*;

/**
 * InfoHandler bundles all informations (see the UCI "info" command ) and sends them via UCIBridge to the GUI
 * It's a threadsafe singleton.
 * <p>
 * Usage:
 * set a bunch of infos with the corresponding InfoHandler::storeInfo()
 * commit and send send your infos with InfoHandler::sendStoredInfos()
 * Example usage:
 * InfoHandler ih = InfoHandler.getInstance();
 * ih.storeInfo(ih.CPULOAD, 50.0);
 * ih.storeInfo(ih.DEPTH, 9);
 * ih.sendStoredInfos();
 */
public class InfoHandler {

    public static final String DEPTH = "DEPTH";
    public static final String SELDEPTH = "SELDEPTH";
    public static final String TIME = "TIME";
    public static final String NODES = "NODES";
    public static final String PV = "PV";
    public static final String MULTIPV = "MULTIPV";
    public static final String CURRMOVE = "CURRMOVE";
    public static final String SCORE_CP = "SCORE_CP";
    public static final String SCORE_MATE = "SCORE_MATE";
    public static final String SCORE_LOWERBOUND = "SCORE_LOWERBOUND";
    public static final String SCORE_UPPERBOUND = "SCORE_UPPERBOUND";
    public static final String CURRMOVE_NUMBER = "CURRMOVE_NUMBER";
    public static final String HASHFULL = "HASHFULL";
    public static final String NPS = "NPS";
    public static final String TBHITS = "TBHITS";
    public static final String SBHITS = "SBHITS";
    public static final String CPULOAD = "CPULOAD";
    public static final String STRING = "string";
    public static final String REFUTATION = "REFUTATION";
    public static final String CURRLINE_CPUNR = "CURRLINE_CPUNR";
    public static final String CURRLINE_MOVES = "CURRLINE_MOVES";
    public static final Set<String> INT_KEYS = Set.of(
            DEPTH, SELDEPTH, MULTIPV, SCORE_CP, SCORE_MATE,
            SCORE_LOWERBOUND, SCORE_UPPERBOUND, HASHFULL, CURRLINE_CPUNR
    );
    public static final Set<String> LONG_KEYS = Set.of(TIME, NODES, CURRMOVE_NUMBER, NPS, TBHITS, SBHITS);
    public static final Set<String> DOUBLE_KEYS = Set.of(CPULOAD);
    public static final Set<String> STRING_KEYS = Set.of(STRING, CURRMOVE);
    public static final Set<String> LIST_STRING_KEYS = Set.of(PV, REFUTATION, CURRLINE_MOVES);
    private volatile static InfoHandler instance;
    private UCIBridge uciBridge;
    private Map<String, String> stringValues;
    private Map<String, Long> longValues;
    private Map<String, Double> doubleValues;
    private Map<String, Integer> intValues;
    private Map<String, String[]> stringListValues;


    /**
     * Constructor. Only used by getInstance
     */
    private InfoHandler() {
        initializeValueMaps();
        uciBridge = UCIBridge.getInstance();
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
     * @param msg The message
     */
    public synchronized static void sendMessage(String msg){
        getInstance().sendStoredInfos();
        getInstance().storeInfo(STRING, msg);
        getInstance().sendStoredInfos();
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
        stringValues.put(key, value);
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
        stringListValues.put(key, value);
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
        longValues.put(key, value);
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
        intValues.put(key, value);
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
        doubleValues.put(key, value);
    }

    /**
     * Sends all stored infos via UCI-Bridge to the GUI.
     * Reset the instance automatically
     */
    public synchronized void sendStoredInfos() {
        uciBridge.sendStringListInfo(stringListValues);
        uciBridge.sendStringInfo(stringValues);
        uciBridge.sendIntInfo(intValues);
        uciBridge.sendDoubleInfo(doubleValues);
        uciBridge.sendLongInfo(longValues);
        initializeValueMaps();
    }

    /**
     * Initializes and resets the value-maps responsible for storing the infos
     */
    private synchronized void initializeValueMaps() {
        stringValues = new HashMap<>();
        longValues = new HashMap<>();
        doubleValues = new HashMap<>();
        intValues = new HashMap<>();
        stringListValues = new HashMap<>();
    }


}