/**
 * This UCIListener will be attached, once the "debug on" command was entered
 * It will send messages for every received command
 */
public class DebugUCIListener implements UCIListener {
    @Override
    public void receivedNewGame() {
        InfoHandler.sendMessage("received ucinewgame command");
    }

    @Override
    public void receivedStop() {
        InfoHandler.sendMessage("received stop command");
    }

    @Override
    public void receivedPosition(Board board, Move move) {
        InfoHandler.sendMessage("received new position <\n" + board.toString() + "\n"+move.toString()+"\n>");
    }

    @Override
    public void receivedGo(String options) {
        InfoHandler.sendMessage("received go command <" + options + ">");
    }
}
