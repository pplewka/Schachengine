package Exceptions;

/**
 * Will be thrown if the GUI sends the quit command
 */
public class EngineQuitSignal extends Exception {
    public EngineQuitSignal(String msg) {
        super(msg);
    }
}
