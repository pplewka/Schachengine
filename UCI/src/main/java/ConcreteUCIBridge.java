import java.util.Map;
import java.util.Scanner;

public class ConcreteUCIBridge implements UCIBridge {

    private volatile static ConcreteUCIBridge instance;

    Scanner reader;

    private ConcreteUCIBridge() {
        reader = new Scanner(System.in);
    }

    public static ConcreteUCIBridge getInstance(){
        if(instance == null){
            synchronized (ConcreteUCIBridge.class){
                if(instance == null){
                    instance = new ConcreteUCIBridge();
                }
            }
        }
        return instance;
    }

    private synchronized void sendString(String string) {
        System.out.println(string);
    }

    private synchronized String receiveString() {
        return reader.nextLine();
    }

    @Override
    public void sendLongInfo(Map<String, Long> longValues) {
    }

    @Override
    public void sendDoubleInfo(Map<String, Double> doubleValues) {

    }

    @Override
    public void sendIntInfo(Map<String, Integer> intValues) {

    }

    @Override
    public void sendStringInfo(Map<String, String> stringValues) {
        StringBuilder out = new StringBuilder("info ");
        for (String key :
                stringValues.keySet()) {
            out.append(key.toLowerCase()).append(" ").append(stringValues.get(key)).append(" ");
        }
        sendString(out.toString());
    }

    @Override
    public void sendStringListInfo(Map<String, String[]> stringListValues) {

    }
}
