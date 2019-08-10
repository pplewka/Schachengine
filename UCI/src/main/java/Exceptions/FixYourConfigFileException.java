package Exceptions;

/**
 * Exception for when a config file is malformed. Should not be thrown, once the program is finished
 */
public class FixYourConfigFileException extends RuntimeException {
    public FixYourConfigFileException(String msg) {
        super(msg);
    }
}
