import java.util.ArrayList;

public interface Move {
    public void setChildren(ArrayList<Move> children);
    public void addChildren(ArrayList<Move> children);
    public ArrayList<Move> getChildren();

    public void setBoard(Board board);
    public Board getBoard();

    public void setEval(int eval);
    public int getEval();
}
