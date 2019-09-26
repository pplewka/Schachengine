import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class SearchImpl implements Search {
    private Move root;
    private int depth;
    private int searchedNodes;
    private Move bestMove;
    private Move ponder;
    private PriorityBlockingQueue<Move> inputLookUpTable;
    private ArrayList<Move> inputChecklist;
    private PriorityBlockingQueue<Move> outputLookUpTable;
    private ArrayList<Move> outputChecklist;

    private static Search sr;

    private SearchImpl() {
        this.depth = 0;
        this.searchedNodes = 0;
        this.inputLookUpTable = new PriorityBlockingQueue<Move>(400,Comparator.comparingInt(Move::getDepth));
        this.inputChecklist = new ArrayList<>(400);
        this.outputLookUpTable = new PriorityBlockingQueue<Move>(400,Comparator.comparingInt(Move::getDepth));
        this.outputChecklist = new ArrayList<>(400);
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
    public void clear() {
        outputLookUpTable.clear();
        inputChecklist.clear();
        root = null;
        depth = 0;
        searchedNodes = 0;
        bestMove = null;
        ponder = null;
        inputLookUpTable.clear();
        outputChecklist.clear();
    }

    @Override
    public synchronized boolean setIfDeeper(int depth){
        if(depth>this.depth){
            this.depth= depth;
            return true;
        }
        return false;
    }

    @Override
    public int getFullGeneratedDepth(){
        return getFullGeneratedDepth(root);
    }

    private int getFullGeneratedDepth(Move parent){
        if(parent.getChildren() == null){
            return 0;
        }else {
            int minDepth = Integer.MAX_VALUE;

            for(Move child : parent.getChildren()){
                int curDepth =getFullGeneratedDepth(child);
                if(curDepth < minDepth){
                    minDepth = curDepth;
                }
            }

            return minDepth +1;
        }
    }

    @Override
    public synchronized void addSearchedNodes(int newSearchedNodes){
        this.searchedNodes += newSearchedNodes;
    }

    public Move getPonder() {
        return ponder;
    }

    public void setPonder(Move ponder) {
        this.ponder = ponder;
    }

    @Override
    public PriorityBlockingQueue<Move> getInputLookUpTable() {
        return inputLookUpTable;
    }

    @Override
    public void setInputLookUpTable(PriorityBlockingQueue<Move> inputLookUpTable) {
        this.inputLookUpTable = inputLookUpTable;
    }

    @Override
    public ArrayList<Move> getInputChecklist() {
        return inputChecklist;
    }

    @Override
    public void setInputChecklist(ArrayList<Move> inputChecklist) {
        this.inputChecklist = inputChecklist;
    }

    @Override
    public PriorityBlockingQueue<Move> getOutputLookUpTable() {
        return outputLookUpTable;
    }

    @Override
    public void setOutputLookUpTable(PriorityBlockingQueue<Move> outputLookUpTable) {
        this.outputLookUpTable = outputLookUpTable;
    }

    @Override
    public ArrayList<Move> getOutputChecklist() {
        return outputChecklist;
    }

    @Override
    public void setOutputChecklist(ArrayList<Move> outputChecklist) {
        this.outputChecklist = outputChecklist;
    }
}
