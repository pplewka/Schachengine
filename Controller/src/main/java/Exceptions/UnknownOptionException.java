package Exceptions;

/**
 * Will be thrown if the GUI sends the quit command
 */
public class UnknownOptionException extends RuntimeException {
    public UnknownOptionException(String msg) {
        super(msg);
    }
}
