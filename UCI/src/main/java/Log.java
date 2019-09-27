import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Log writes Strings to a log file
 * <p>
 * One Log instance should be set with setInstance to be available to every other Class with getInstance.
 * This way all Strings will be written correctly to the same log file.
 */
public class Log {
    private volatile static Log uniqueInstance;
    private boolean writeLog;
    private String logFilePath;
    private PrintWriter writer;
    private boolean ignoreNextWrites;

    /**
     * Constructor
     * Should only be called once, unless you now exactly what you're doing
     * If an IOException occurs, this constructor returns a fail-safe instance.
     * This fail-safe instance won't do anything.
     *
     * @param filename the file name of the log file
     * @param writeLog true if logs should be written. If this is false, every method call will be ignored.
     */
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

    /**
     * Returns the unique instance
     *
     * @return the unique instance
     */
    public static Log getInstance() {
        return uniqueInstance;
    }

    /**
     * Sets the instance as the unique instance that will be returned by getInstance
     *
     * @param instance
     */
    public static void setInstance(Log instance) {
        uniqueInstance = instance;
    }

    /**
     * Writes all option with their current value to the log file
     * Does nothing, if the instance is the fail-safe instance from the constructor, or ignoreNextWrites is set to true
     *
     * @param options A List with all options and values
     */
    public synchronized void writeToLog(List<OptionValuePair> options) {
        for (OptionValuePair pair : options) {
            writeToLog("Option: " + pair.option + " value " + pair.value);
        }
    }

    /**
     * Writes a String to the log file
     * Does nothing, if the instance is the fail-safe instance from the constructor, or ignoreNextWrites is set to true
     *
     * @param text the String to write to the log file
     */
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

    /**
     * Closes the Log instance. No methods are allowed to be called after this.
     */
    public synchronized void close() {
        if (writer == null) {
            return;
        }
        writer.close();
    }

    /**
     * Check if the next writes should be ignored
     *
     * @return ignoreNextWrites
     */
    public synchronized boolean isIgnoreNextWrites() {
        return ignoreNextWrites;
    }

    /**
     * Sets ignoreNextWrites
     *
     * @param ignoreNextWrites if true all writes will be ignored,
     *                         until ignoreNextWrites is set to false with this method
     */
    public synchronized void setIgnoreNextWrites(boolean ignoreNextWrites) {
        this.ignoreNextWrites = ignoreNextWrites;
    }
}
