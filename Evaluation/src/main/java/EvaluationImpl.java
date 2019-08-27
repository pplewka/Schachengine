public class EvaluationImpl implements Evaluation {
    private static Evaluation eval;

    /*
        [0, 1] = Space and Empty   = 0
        [2, 3] = idk               = 0
        [4]    = Pawn              = 1
        [5, 7] = Knight and Bishop = 3
        [8]    = Queen             = 9
        [9]    = King              = a much higher value than the others...
     */
    private int [] pieceValues = {0, 0, 0, 0, 1, 3, 5, 3, 9, 100};


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

        parent.setMaxMinIfChanged(true,sum);
         */

    }

    /**
     * Calculate the material value of a board for a specific site.
     * @param board the board that will be valued
     * @param blacksTurn indicates which site of the board should be calculated
     * @return the combination of all piece values of a site in centipawns (100 centipawns = 1 pawn).
     */
    @Override
    public int material(Board board, boolean blacksTurn) {
        // TODO checking for certain piece combination bonus/penalty
        // TODO unit tests of course

        byte [] boardByte = board.getBoard();

        int materialValue = 0;
        for (byte b : boardByte) {
            int field = b * -1;

            if (field > 1) { // if neither empty or "SPACE"
                materialValue += pieceValues[field] * 100;
            } else if (field < -1) {
                materialValue -= pieceValues[-1*field] * 100;
            }
        }

        return materialValue;
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
