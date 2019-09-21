import java.util.concurrent.BlockingQueue;

public interface Search {
    public int getDepth();
    public Move getRoot();
    public Move getBestMove();
    public void setBestMove(Move newBestMove);

    int getFullGeneratedDepth();

    public Move getPonder();
    public void setPonder(Move ponder);
    public BlockingQueue<Move> getLookUpTable();
    public void clear();
    public boolean setIfDeeper(int depth);

    void setRoot(Move move);
}
