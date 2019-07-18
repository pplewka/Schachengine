import java.util.ArrayList;

public class MoveGenerationImpl implements MoveGeneration{

    public ArrayList<Move> generateAllMoves(Board board,boolean blacksTurn){
        for(int i=26;i<118;){



            i++;
            if((i+2)%12==0){
                i+=4;
            }
        }

        return null;
    }

    @Override
    public ArrayList<Move> generateAllPawnMoves(Board board, boolean blacksTurn) {
        return null;
    }

    @Override
    public ArrayList<Move> generateAllRookMoves(Board board, boolean blacksTurn) {
        return null;
    }

    @Override
    public ArrayList<Move> generateAllBishopMoves(Board board, boolean blacksTurn) {
        return null;
    }

    @Override
    public ArrayList<Move> generateAllKnightMoves(Board board, boolean blacksTurn) {
        return null;
    }

    @Override
    public ArrayList<Move> generateAllQueenMoves(Board board, boolean blacksTurn) {
        return null;
    }

    @Override
    public ArrayList<Move> generateRookMoves(Board board, int field, boolean blacksTurn) {
        return null;
    }

    @Override
    public ArrayList<Move> generateBishopMoves(Board board, int field, boolean blacksTurn) {
        return null;
    }

    @Override
    public ArrayList<Move> generateKnightMoves(Board board, int field, boolean blacksTurn) {
        return null;
    }

    @Override
    public ArrayList<Move> generateQueenMoves(Board board, int field, boolean blacksTurn) {
        return null;
    }

    @Override
    public ArrayList<Move> generateKingMoves(Board board, boolean blacksTurn) {
        return null;
    }

    public ArrayList<Move> generatePawnMoves(Board board, int field, boolean blacksTurn){
        int move1=Integer.MIN_VALUE;
        int move2=Integer.MIN_VALUE;
        int validMoves=0;

        //check which side is moving
        if(blacksTurn){
            //check if pawn has moved already
            if(field>=38&&field<=45){
                move2= field + 24;
            }

            move1= field + 12;


            //todo check for valid position

        }else{
            //check if pawn has moved already
            if(field>=98&&field<=105){
                move2= field - 24;
            }

            move1= field - 12;

            //todo check for valid position
        }

        //todo capture diagonal
        return null;
    }
}
