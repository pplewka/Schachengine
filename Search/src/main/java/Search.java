import java.util.concurrent.BlockingQueue;

public interface Search {
    public int getDepth();
    public Move getRoot();
    public Move getBestMove();
    public void setBestMove(Move newBestMove);
    public Move getPonderMove();
    public BlockingQueue<Move> getLookUpTable();
    public void clear();
    public void setIfDeeper(int depth);

    void setRoot(Move move);
}
