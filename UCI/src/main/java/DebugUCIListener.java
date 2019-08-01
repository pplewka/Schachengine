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
    public void receivedPosition(String position) {
        InfoHandler.sendMessage("received new position <" + position + ">");
    }

    @Override
    public void receivedGo() {
        InfoHandler.sendMessage("received go command");
    }
}
