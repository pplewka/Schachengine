import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;

public interface Search {
    public int getDepth();
    public Move getRoot();
    public Move getBestMove();
    public void setBestMove(Move newBestMove);

    int getFullGeneratedDepth();

    public Move getPonder();
    public void setPonder(Move ponder);
    public PriorityBlockingQueue<Move> getInputLookUpTable();
    public void setInputLookUpTable(PriorityBlockingQueue<Move> newTable);
    public PriorityBlockingQueue<Move> getOutputLookUpTable();
    public void setOutputLookUpTable(PriorityBlockingQueue<Move> newTable);
    public ArrayList<Move> getInputChecklist();
    public void setInputChecklist(ArrayList<Move> newChecklist);
    public ArrayList<Move> getOutputChecklist();
    public void setOutputChecklist(ArrayList<Move> newChecklist);
    public void clear();
    public boolean setIfDeeper(int depth);
    public void addSearchedNodes(int nodeCount);

    void setRoot(Move move);
}
