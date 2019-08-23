import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class SearchThread extends Thread {
    private static boolean searching = false;
    private Object lock;

    public SearchThread(Object lock){
        this.lock= lock;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        Search search = SearchImpl.getSearch();
        BlockingQueue<Move> lookupTable = search.getLookUpTable();
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        Evaluation eval = EvaluationImpl.getEvaluation();

        while (!isInterrupted()) {

            while (!searching) {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


            Move currentParent = null;
            try {
                currentParent = lookupTable.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            search.setIfDeeper(currentParent.getDepth() - 1);
            ArrayList<Move> currentChildren = moveGen.generateAllMoves(currentParent);
            for (Move child : currentChildren) {
                child.setEval(eval.material(child.getBoard(), child.blacksTurn()));

                //get the eval value to root if it is "good" enough
                Move previous = null;
                Move toRootIterator = child;
                boolean changed = true;
                while (toRootIterator != search.getRoot() && changed) {
                    previous = toRootIterator;
                    toRootIterator = toRootIterator.getParent();

                    changed = toRootIterator.setMaxMinIfBiggerSmaller(child.getEval());
                }

                if (toRootIterator == search.getRoot() && changed) {
                    search.setBestMove(previous);
                }

                //for alpha beta pruning just add a child to the
                // lookupTable if its eval value is better than alpha/beta
            }

            //add new Moves to the lookUpTable
            lookupTable.addAll(currentChildren);

        }
    }

    public static boolean isSearching() {
        return searching;
    }

    public static void setSearching(boolean searching) {
        SearchThread.searching = searching;
    }
}
