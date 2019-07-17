import java.util.Map;

public interface UCIBridge {
    void sendInfo(String key);

    void sendLongInfo(Map<String, Long> longValues);

    void sendDoubleInfo(Map<String, Double> doubleValues);

    void sendIntInfo(Map<String, Integer> intValues);

    void sendStringInfo(Map<String, String> stringValues);

    void sendStringListInfo(Map<String, String[]> stringListValues);
}
