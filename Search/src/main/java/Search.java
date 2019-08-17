import java.util.Queue;

public interface Search {
    public int getDepth();
    public Move getRoot();
    public Move getBestMove();
    public void setBestMove(Move newBestMove);
    public Move getPonderMove();
    public Queue<Move> getLookUpTable();
}
