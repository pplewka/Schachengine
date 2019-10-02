import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SearchThread extends Thread {
    private  static  final int WAIT_NS = 5; //Wait Time Before Taking Again (in nanoseconds)
    private static boolean searching = false;
    private final Object lock;

    public SearchThread(Object lock){
        this.lock= lock;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        Search search = SearchImpl.getSearch();
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        Evaluation eval = EvaluationImpl.getEvaluation();

        try {
            while (!isInterrupted()) {
                while (!searching) {
                    synchronized (lock) {
                        lock.wait();
                    }
                }


                //get new Parent. Wait and try again if empty
                Move currentParent = null;
                do {
                    currentParent = search.getOutputLookUpTable().take();
                } while (currentParent == null);

                //generate and evaluate children
                ArrayList<Move> currentChildren = moveGen.generateAllMoves(currentParent);
                for (Move child : currentChildren) {
                    child.setMaxMin(eval.evaluate(child));
                }
                currentParent.setChildren(currentChildren.toArray(new Move[0]));
                //if children were set clear entry in checklist
                search.getOutputLookUpTable().addAll(currentChildren);

                //new full generated depth is reached, when outputChecklist is empty
                //if reached new full generated depth change value in search, search for new bestMove and send infos
                int fullGeneratedDepth = currentParent.getDepth() +1;
                if (search.setIfDeeper(fullGeneratedDepth)) {
                    Thread.currentThread().setPriority(MAX_PRIORITY);
                    fullGeneratedDepth = search.getFullGeneratedDepth();
                    //search and add new nodes in inputQueue
                    alphaBetaSearch(search.getRoot(), fullGeneratedDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);

                    //swapTablesAndLists(search);
                    Thread.currentThread().setPriority(NORM_PRIORITY);

                    InfoHandler.getInstance().storeInfo(InfoHandler.DEPTH, search.getDepth());
                    InfoHandler.getInstance().storeInfo(InfoHandler.NODES, (long) 0);
                    InfoHandler.getInstance().flushInfoBuffer();
                    //send Infos
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        }else {
            try {
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

                //todo
                //parent.setMaxMin(bestValue);
                parent.setChildren(exploredChildren.toArray(new Move[0]));

                //set bestMove
                if (parent == search.getRoot() && bestMove != null) {
                    search.setBestMove(bestMove);
                }
            }catch(NullPointerException e){
                e.printStackTrace();
            }


            return bestValue;
        }
    }

    private void swapTablesAndLists(Search search){
        PriorityBlockingQueue<Move> tempTable = search.getInputLookUpTable();
        ArrayList<Move> tempList = search.getInputChecklist();
        search.setInputLookUpTable( search.getOutputLookUpTable());
        search.setInputChecklist( search.getOutputChecklist());
        search.setOutputLookUpTable( tempTable);
        search.setOutputChecklist( tempList);
    }
}
