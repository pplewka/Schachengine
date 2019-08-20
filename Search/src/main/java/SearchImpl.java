import java.util.Comparator;
import java.util.Queue;
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
    public Move getBestMove() {
        return bestMove;
    }

    @Override
    public void setBestMove(Move newBestMove) {
        bestMove = newBestMove;
    }

    @Override
    public Move getPonderMove() {
        return ponder;
    }

    @Override
    public Queue<Move> getLookUpTable() {
        return lookUpTable;
    }

    @Override
    public void clear() {
        root = null;
        depth = 0;
        bestMove = null;
        ponder = null;
        lookUpTable.clear();
        lookUpTable.add(new MoveImpl());
    }
}
