import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class SearchImpl implements Search {
    private Move root;
    private int depth;
    private Move bestMove;
    private Move ponder;
    private PriorityBlockingQueue<Move> lookUpTable;

    private static Search sr;
    private SearchImpl(){}

    public static Search getSearch(){
        if(sr==null){
            sr= new SearchImpl();
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
    public Move getPonderMove() {
        return ponder;
    }

    @Override
    public Queue<Move> getLookUpTable(){
        return lookUpTable;
    }
}
