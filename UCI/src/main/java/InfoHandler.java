package main.java;

import java.util.*;

public class InfoHandler {

    private UCIBridge uciBridge;

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
    public static final String STRING = "STRING";
    public static final String REFUTATION = "REFUTATION";
    public static final String CURRLINE_CPUNR = "CURRLINE_CPUNR";
    public static final String CURRLINE_MOVES = "CURRLINE_MOVES";

    private Map<String, Object> values = Map.of();
    private HashMap<String, Boolean> changed = new HashMap<>();

    private static final Set<String> KEYS = Set.of(
            DEPTH,
            SELDEPTH,
            TIME,
            NODES,
            PV,
            MULTIPV,
            CURRMOVE,
            SCORE_CP,
            SCORE_MATE,
            SCORE_LOWERBOUND,
            SCORE_UPPERBOUND,
            CURRMOVE_NUMBER,
            HASHFULL,
            NPS,
            TBHITS,
            SBHITS,
            CPULOAD,
            STRING,
            REFUTATION,
            CURRLINE_CPUNR,
            CURRLINE_MOVES
    );

    private static final Set<String> INT_KEYS = Set.of(
            DEPTH, SELDEPTH, MULTIPV, SCORE_CP, SCORE_MATE,
            SCORE_LOWERBOUND, SCORE_UPPERBOUND, HASHFULL, CURRLINE_CPUNR
    );
    private static final Set<String> LONG_KEYS = Set.of(TIME, NODES, CURRMOVE_NUMBER, NPS, TBHITS, SBHITS);
    private static final Set<String> DOUBLE_KEYS = Set.of(CPULOAD);
    private static final Set<String> STRING_KEYS = Set.of(STRING, CURRMOVE);
    private static final Set<String> LIST_STRING_KEYS = Set.of(PV, REFUTATION, CURRLINE_MOVES);


    public InfoHandler(UCIBridge uciBridge) {
        this.uciBridge = uciBridge;
     /*   setDEPTH(0);
        setSELDEPTH(0);
        setTIME(System.currentTimeMillis());
        setNODES(0);
        setPv(new ArrayList<>());
        setMULTIPV(0);
        setCURRMOVE("");  //TODO: neutral move
        setSCORE_CP(0);
        setSCORE_MATE(0);
        setSCORE_LOWERBOUND(0);
        setSCORE_UPPERBOUND(0);
        setCURRMOVE_NUMBER(0);
        setHASHFULL(0);
        setNPS(0);
        setTBHITS(0);
        setSBHITS(0);
        setCPULOAD(ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());
        setSTRING("");  // TODO: was zur hoelle macht String??? UCI erklaert es schlecht
        setREFUTATION(new ArrayList<>());
        setCURRLINE_CPUNR(0);
        setCURRLINE_MOVES(new ArrayList<>());*/
        initializeChanged();
    }

    public void set(String key, Object value){
        if(!KEYS.contains(key)){
            throw new RuntimeException("NOT ALLOWED KEYS USED. ONLY USE KEYS FROM InfoHandler.KEYS");
        }
        values.put(key, value);
        changed.put(key, true);
    }

    private void initializeChanged() {
        for (String key : KEYS) {
            changed.put(key, false);
        }
    }

    public void commitChanges() {
        for (String key : changed.keySet()) {
            if (changed.get(key)) {
                sendInfo(key);
            }
        }
    }

    private void sendInfo(String key) {

        uciBridge.sendInfo(key);
    }

}