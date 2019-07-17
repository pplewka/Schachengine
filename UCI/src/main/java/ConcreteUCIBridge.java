import java.util.Map;
import java.util.Scanner;

public class ConcreteUCIBridge implements UCIBridge {

    Scanner reader;

    public ConcreteUCIBridge() {
        reader = new Scanner(System.in);
    }

    private void sendString(String string) {
        System.out.println(string);
    }

    private String receiveString() {
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

    }

    @Override
    public void sendStringListInfo(Map<String, String[]> stringListValues) {

    }
}
