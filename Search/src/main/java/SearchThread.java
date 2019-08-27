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


            Move currentParent = null;
            try {
                currentParent = lookupTable.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (search.setIfDeeper(currentParent.getDepth() - 1)) {
                InfoHandler.getInstance().storeInfo(InfoHandler.DEPTH, search.getDepth());
                InfoHandler.getInstance().storeInfo(InfoHandler.NODES, (long) lookupTable.size());
                InfoHandler.getInstance().flushInfoBuffer();
                //send Infos
            }

            //generate and evaluate children
            ArrayList<Move> currentChildren = moveGen.generateAllMoves(currentParent);
            for (Move child : currentChildren) {
                child.setMaxMin(eval.evaluate(child));
            }
            currentParent.setChildren(currentChildren.toArray(new Move[0]));

            //get the eval value to root if it is "good" enough
            Move parentIterator = currentParent;

            //minmaxing of the children from the currentParent
            Move bestChild =getBestChild(parentIterator);
            int minmax = bestChild.getMaxMin();
            boolean changed = parentIterator.setMaxMinIfChanged(minmax);

            //minmaxing down to root
            while (parentIterator != search.getRoot() && changed){
                parentIterator = parentIterator.getParent();

                bestChild =getBestChild(parentIterator);
                minmax = bestChild.getMaxMin();
                changed = parentIterator.setMaxMinIfChanged(minmax);
            }

            //set bestmove if reached root and value changed
            if (parentIterator == search.getRoot() && changed) {
                search.setBestMove(bestChild);
            }

            //add new Moves to the lookUpTable
            lookupTable.addAll(currentChildren);

        }
    }

    private Move getBestChild(Move parent){
        Move bestChild = null;
        int minmax = parent.blacksTurn() ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        for (Move m : parent.getChildren()) {
            if (parent.blacksTurn()) {
                if (m.getMaxMin() < minmax) {
                    minmax = m.getMaxMin();
                    bestChild = m;
                }
            } else {
                if (m.getMaxMin() > minmax) {
                    minmax = m.getMaxMin();
                    bestChild = m;
                }
            }
        }

        if(bestChild == null){
                //parent is checkmate
                bestChild = new MoveImpl(0,0,' ',null, !parent.blacksTurn());
                if(parent.blacksTurn()){
                    bestChild.setMaxMin(Integer.MIN_VALUE);
                }else{
                    bestChild.setMaxMin(Integer.MAX_VALUE);
                }
        }

        return bestChild;
    }

    public static boolean isSearching() {
        return searching;
    }

    public static void setSearching(boolean searching) {
        SearchThread.searching = searching;
    }
}
