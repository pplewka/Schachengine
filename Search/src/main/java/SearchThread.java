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

            //if reached new depth change it and send infos
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
        }
    }
/*
    private Move getBestChild(Move parent) {
        Move bestChild = null;
        int minmax = parent.blacksTurn() ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        Move[] children = parent.getChildren();
        if (children != null) {
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
        }else{
            return parent;
        }

        if (bestChild == null) {
            //parent is checkmate
            bestChild = new MoveImpl(0, 0, ' ', null, !parent.blacksTurn());
            if (parent.blacksTurn()) {
                bestChild.setMaxMin(Integer.MIN_VALUE);
            } else {
                bestChild.setMaxMin(Integer.MAX_VALUE);
            }
        }

        return bestChild;
    }

    private Move getWorstChild(Move parent){
        Move bestChild = null;
        int minmax = !parent.blacksTurn() ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        for (Move m : parent.getChildren()) {
            if (!parent.blacksTurn()) {
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
            if(!parent.blacksTurn()){
                bestChild.setMaxMin(Integer.MIN_VALUE);
            }else{
                bestChild.setMaxMin(Integer.MAX_VALUE);
            }
        }

        return bestChild;
    }
    */

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

        if (parent.blacksTurn()) {
            bestValue = Integer.MAX_VALUE;
        } else {
            bestValue = Integer.MIN_VALUE;
        }

        if (parent.getDepth() == maxDepth && children == null) {
            parent.addIfNotAllready(search.getLookUpTable());

            return parent.getMaxMin();
        } else if(!parent.hasChildren()){

            //at this point worst value
            return bestValue;
        }else {
            for (int i = 0; i < children.length && hopeful; i++) {
                if (children[i] != null) {
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

                /*/add to table if node is leaf and not cut away
                if(add && children[i].getChildren() == null){
                        search.getLookUpTable().add(children[i]);
                }*/
                }

                //alpha beta cutoff
                if (beta <= alpha) {
                    hopeful = false;
                    for (; i < children.length; i++) {
                        children[i] = null;
                    }
                }
            }

            parent.setMaxMin(bestValue);

            //set bestMove
            if (parent == search.getRoot() && bestMove != null) {
                search.setBestMove(bestMove);
            }

            return bestValue;
        }
    }
}
