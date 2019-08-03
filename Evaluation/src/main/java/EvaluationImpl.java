public class EvaluationImpl implements Evaluation {
    @Override
    public void evaluate(Move parent) {
        /*
        pseudocode:
        int sum;
        for(Move m:parent.getChildren){
            if(parent.blacksTurn()){
                sum-=material(move);
                sum-=mobility(move);
            }else{
                sum+=material(move);
                sum+=mobility(move);
            }
        }

        parent.setMaxMinIfBiggerSmaller(true,sum);
         */

    }

    @Override
    public int material(Board board, boolean blacksTurn) {
        /*
        go trough board and sum each value of a piece type
         */
        return 0;
    }

    @Override
    public int mobility(Board board, boolean blacksTurn) {
        /*
        ignore at first

        later maybe counts attacked pieces or similar
         */
        return 0;
    }
}
