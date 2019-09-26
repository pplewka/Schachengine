import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;

public interface Move {
    public void setChildren(Move[] children);
    public Move [] getChildren();

    public int getFrom();
    public int getTo();
    public char getChar();

    public void setEnpassant(int field);
    public void resetEnpassant();
    public int getEnpassant();

    public boolean isKilled();
    public void setKilled(boolean killed);
    public void kill();

    public void setBlacksTurn(boolean blacksTurn);
    public boolean blacksTurn();

    public Move getParent();
    public void setParent(Move parent);

    public void setBoard(Board board);
    public Board getBoard();

    public void setEval(int eval);
    public int getEval();

    public byte getDepth();
    public void setDepth(byte newDepth);
    public void setMaxMin(int newValue);

    boolean isAdded();

    void setAdded(boolean added);

    boolean hasChildren();

    void addIfAlright(PriorityBlockingQueue<Move> tableToAdd, ArrayList<Move> listToAdd);

    /**
     * method to make moves given by the uci "position moves" command
     * @param moves String of moves in algebraic notation separated with space
     */
    public void moves(String moves);

    /**
     *!Has to be synchronised!
     */
    public boolean setMaxMinIfChanged(int newValue);
    public int getMaxMin();
}
