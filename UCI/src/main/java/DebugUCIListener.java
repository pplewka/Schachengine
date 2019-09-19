/**
 * This UCIListener will always be attached and sends debug infos if it baguette is in debug mode
 * It will send messages for every received command
 */
public class DebugUCIListener implements UCIListener {
    @Override
    public void receivedNewGame() {
        InfoHandler.sendDebugMessage("DebugListener: received ucinewgame command");
    }

    @Override
    public void receivedStop() {
        InfoHandler.sendDebugMessage("DebugListener: received stop command");
    }

    @Override
    public void receivedPosition(Board board, Move move) {
        InfoHandler.sendDebugMessage("DebugListener: received new position <\n" +
                board.toString() + "\n" + move.toString() + "\n>");
    }

    @Override
    public void receivedGo(String options) {
        InfoHandler.sendDebugMessage("DebugListener: received go command <" + options + ">");
    }
}
