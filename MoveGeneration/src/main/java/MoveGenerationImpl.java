import java.util.ArrayList;

public class MoveGenerationImpl {

    public static ArrayList<Move> generateAllMoves(Board board){
        for(int i=26;i<118;){
            Piece position = board.fieldIs(i);



            i++;
            if((i+2)%12==0){
                i+=4;
            }
        }

        return null;
    }

    private static ArrayList<Move> generateMovesForOnePawn(Board board, int field, boolean blacksTurn){
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
