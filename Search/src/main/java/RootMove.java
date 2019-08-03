public class RootMove extends MoveImpl {
    public RootMove(int from, int to, char c, Board board, boolean blacksTurn) {
        super(from, to, c, board, blacksTurn);
    }

    @Override
    public synchronized void setMaxMinIfBiggerSmaller(int newValue) {
        //todo
        /*
        if(changed){
            Search.getSearch().setBestMove(new BestMove);
        }
         */
    }
}
