import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class SearchThread extends Thread {
    private static boolean searching = false;
    private final Object lock;

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

            //get new Parent
            Move currentParent = null;
            try {
                currentParent = lookupTable.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //generate and evaluate children
            ArrayList<Move> currentChildren = moveGen.generateAllMoves(currentParent);
            for (Move child : currentChildren) {
                child.setMaxMin(eval.evaluate(child));
            }
            currentParent.setChildren(currentChildren.toArray(new Move[0]));


            int fullGeneratedDepth =search.getFullGeneratedDepth();

            //if reached new full generated depth change value in search, search for new bestMove and send infos
            if (search.setIfDeeper(fullGeneratedDepth)) {
                alphaBetaSearch(search.getRoot(), fullGeneratedDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);

                InfoHandler.getInstance().storeInfo(InfoHandler.DEPTH, search.getDepth());
                InfoHandler.getInstance().storeInfo(InfoHandler.NODES, (long) lookupTable.size());
                InfoHandler.getInstance().flushInfoBuffer();
                //send Infos
            }
        }
    }

    public static boolean isSearching() {
        return searching;
    }

    public static void setSearching(boolean searching) {
        SearchThread.searching = searching;
    }

    public int alphaBetaSearch(Move parent, int maxDepth, int alpha, int beta) {
        Search search = SearchImpl.getSearch();
        Move[] children = parent.getChildren();

        Move bestMove = null;
        int curValue;
        int bestValue;
        boolean hopeful = true;
        ArrayList<Move> exploredChildren = new ArrayList<>();

        if (parent.blacksTurn()) {
            bestValue = Integer.MAX_VALUE;
        } else {
            bestValue = Integer.MIN_VALUE;
        }

        if (parent.getDepth() == maxDepth) {

            return parent.getMaxMin();
        } else {
            for (int i = 0; i < children.length && hopeful; i++) {
                exploredChildren.add(children[i]);
                curValue = alphaBetaSearch(children[i], maxDepth, alpha, beta);
                //boolean add = false;

                if (parent.blacksTurn()) {
                    if (curValue < bestValue) {
                        bestValue = curValue;
                        beta = curValue;
                        bestMove = children[i];

                        //add = true;
                    }
                } else {
                    if (curValue > bestValue) {
                        bestValue = curValue;
                        alpha = curValue;
                        bestMove = children[i];

                        //add = true;
                    }
                }

                //alpha beta cutoff
                if (beta <= alpha) {
                    hopeful = false;
                }
            }

            parent.setMaxMin(bestValue);
            parent.setChildren(exploredChildren.toArray(new Move[0]));

            //set bestMove
            if (parent == search.getRoot() && bestMove != null) {
                search.setBestMove(bestMove);
            }

            if(parent.getDepth() == maxDepth -1){
                exploredChildren.forEach(child->{
                    if(child.getDepth() == maxDepth ){
                        child.addIfNotAllready(search.getLookUpTable());
                    }
                });
            }


            return bestValue;
        }
    }
}
