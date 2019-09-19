/**
 * Basically a global variable to store the debug mode of the engine
 */

public abstract class Debug {
    private static boolean debug = false;

    /**
     * Gets debug mode. Engine should determine what it wants to send based on the debug mode
     *
     * @return the debug mode
     */
    public static synchronized boolean getDebug() {
        return debug;
    }

    /**
     * Sets debug mode. Engine should determine what it wants to send based on the debug mode
     *
     * @param debug_mode the debug mode
     */
    public static synchronized void setDebug(boolean debug_mode) {
        if (debug == debug_mode) {
            return;
        }
        debug = debug_mode;
    }
}
