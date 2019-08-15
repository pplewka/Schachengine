import java.util.Queue;

public class SearchThread extends Thread {

    @Override
    public void run(){
        Queue<Move> lookupTable = SearchImpl.getSearch().getLookUpTable();
        //MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();

        while(!this.isInterrupted()){
            Move currentParent = lookupTable.poll();
           // ArrayList<Move> currentChildren =
        }
    }
}
