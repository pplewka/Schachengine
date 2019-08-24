import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
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

            if (search.setIfDeeper(currentParent.getDepth() - 1)) {
                InfoHandler.getInstance().storeInfo(InfoHandler.DEPTH, search.getDepth());
                //cpu_load goes up to X00% with X = number of processors. EG. Quad Core -> 400%
                double cpu_load = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
                double num_proc = Runtime.getRuntime().availableProcessors();
                InfoHandler.getInstance().storeInfo(InfoHandler.CPULOAD, cpu_load / num_proc);
                InfoHandler.getInstance().storeInfo(InfoHandler.NODES,(long) lookupTable.size());
                InfoHandler.getInstance().flushInfoBuffer();
                //send Infos
            }
            ArrayList<Move> currentChildren = moveGen.generateAllMoves(currentParent);
            for (Move child : currentChildren) {
                //if there is no real bestMove set any real move
                if (search.getBestMove() == MoveImpl.DUMMIEMOVE) {
                    search.setBestMove(child);
                }

                child.setEval(eval.material(child.getBoard(), child.blacksTurn()));

                //get the eval value to root if it is "good" enough
                Move preprevious = null;
                Move previous = null;
                Move toRootIterator = child;
                boolean changed = true;
                while (toRootIterator != search.getRoot() && changed) {
                    preprevious = previous;
                    previous = toRootIterator;
                    toRootIterator = toRootIterator.getParent();

                    changed = toRootIterator.setMaxMinIfBiggerSmaller(child.getEval());
                }

                if (toRootIterator == search.getRoot() && changed) {
                    search.setBestMove(previous);
                    search.setPonder(preprevious);
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
