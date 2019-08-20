/**
 * Interface for classes to be attached to UCI to listen for inputted commands
 */
public interface UCIListener {

    /**
     * Gets called, when the "ucinewgame" command was entered
     */
    void receivedNewGame();

    /**
     * Gets called, when the "stop" command was entered
     */
    void receivedStop();

    /**
     * Gets called, when the "position" command was entered
     * @param board
     * @param move
     */
    void receivedPosition(Board board, Move move);

    /**
     * Gets called, when the "go" command was entered
     */
    void receivedGo(String options);
}
