public class EvaluationImpl implements Evaluation {
    private static Evaluation eval;

    /*
        [0, 1] = Space and Empty   = 0
        [2, 3] = idk               = 0
        [4]    = Pawn              = 1
        [5, 7] = Knight and Bishop = 3
        [8]    = Queen             = 9
        King has a theoretical value of infinite and does not need to be in the calculation for the material value
     */
    private int [] pieceValues = {0, 0, 0, 0, 1, 3, 5, 3, 9, 0};


    private EvaluationImpl(){
    }

    public static Evaluation getEvaluation(){
        if(eval==null){
            eval= new EvaluationImpl();
        }
        return eval;
    }

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

    /**
     * Calculate the material value of a board for a specific site.
     * @param board the board that will be valued
     * @param blacksTurn indicates which site of the board should be calculated
     * @return the value of a site in centipawns (100 centipawns = 1 pawn). The value returned is the average of the
     *         point values from all pieces of a site.
     */
    @Override
    public int material(Board board, boolean blacksTurn) {

        int turnModifier = blacksTurn ? -1 : 1;

        byte [] boardByte = board.getBoard();

        int materialValue = 0;
        int pieceCnt      = 0;

        for(int i = 0; i < boardByte.length; i++) {
            int field = boardByte[i]*turnModifier;

            if(field > 1) { // if neither empty or "SPACE"
                materialValue += pieceValues[field] * 100 ;
                pieceCnt++;
            }

        }

        return (materialValue/pieceCnt);
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
