import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class SearchImpl implements Search {
    private Move root;
    private int depth;
    private Move bestMove;
    private Move ponder;
    private PriorityBlockingQueue<Move> lookUpTable;

    private static Search sr;

    private SearchImpl() {
        this.depth = 0;
        this.lookUpTable = new PriorityBlockingQueue<Move>(400, new Comparator<Move>() {
            @Override
            public int compare(Move o1, Move o2) {
                return o1.getDepth() - o2.getDepth();
            }
        });
    }

    public static Search getSearch() {
        if (sr == null) {
            sr = new SearchImpl();
        }
        return sr;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public Move getRoot() {
        return root;
    }

    @Override
    public void setRoot(Move move){
        root = move;
    }

    @Override
    public Move getBestMove() {
        return bestMove;
    }

    @Override
    public void setBestMove(Move newBestMove) {
        bestMove = newBestMove;
    }

    @Override
    public BlockingQueue<Move> getLookUpTable() {
        return lookUpTable;
    }

    @Override
    public void clear() {
        root = null;
        depth = 0;
        bestMove = null;
        ponder = null;
        lookUpTable.clear();
    }

    @Override
    public synchronized boolean setIfDeeper(int depth){
        if(depth>this.depth){
            this.depth= depth;
            return true;
        }
        return false;
    }

    public Move getPonder() {
        return ponder;
    }

    public void setPonder(Move ponder) {
        this.ponder = ponder;
    }
}
