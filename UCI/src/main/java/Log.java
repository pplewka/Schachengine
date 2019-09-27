import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Log {
    private volatile static Log uniqueInstance;
    private boolean writeLog;
    private String logFilePath;
    private PrintWriter writer;
    private boolean ignoreNextWrites;

    public Log(String filename, boolean writeLog) {
        this.writeLog = writeLog;
        ignoreNextWrites = false;
        if (!writeLog) {
            logFilePath = null;
            writer = null;
        } else {
            logFilePath = filename;
            try {
                writer = new PrintWriter(logFilePath, StandardCharsets.UTF_8);
            } catch (IOException e) {
                InfoHandler.sendMessage("Unable to open file: " + logFilePath);
                writer = null;
            }
        }
    }

    public static void setInstance(Log instance){
        uniqueInstance = instance;
    }

    public static Log getInstance() {
        return uniqueInstance;
    }

    public synchronized void writeToLog(List<OptionValuePair> options) {
        for (OptionValuePair pair : options) {
            writeToLog("Option: " + pair.option + " value " + pair.value);
        }
    }

    public synchronized void writeToLog(String text) {
        if (writer == null) {
            return;
        }
        if (ignoreNextWrites) {
            return;
        }
        writer.println(text);
        writer.flush();
    }

    public synchronized void close() {
        if(writer == null){
            return;
        }
        writer.close();
    }

    public synchronized boolean isIgnoreNextWrites() {
        return ignoreNextWrites;
    }

    public synchronized void setIgnoreNextWrites(boolean ignoreNextWrites) {
        this.ignoreNextWrites = ignoreNextWrites;
    }
}
