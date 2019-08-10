import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class EnpassantTest {
    @Test
    public void regularEnpassantBlack(){
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();
        Board b= new BoardImpl();
        b.setField(Piece.BPAWN,33);
        b.setField(Piece.BPAWN,35);
        b.setField(Piece.WPAWN,50);

        Move root = new MoveImpl(0,0,'0',b,true);

        ArrayList<Move> movelist = new ArrayList<>();
        mg.generatePawnMoves(root,100,movelist);

        Move rightOne=null;
        for(Move move:movelist){
            if(move.getTo()==34){
                rightOne=move;
            }
        }
        if(rightOne==null){
            fail();
        }

        movelist = new ArrayList<>();
        mg.generateEnpassant(rightOne,movelist);

        assertEquals(movelist.size(),2);

        for(Move move: movelist){
            assertEquals(move.getBoard().getPiece(42),Piece.BPAWN);
            assertEquals(move.getBoard().getPiece(34),Piece.EMPTY);
        }
    }

    @Test
    public void regularEnpassantWhite(){
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,25);
        b.setField(Piece.WPAWN,27);
        b.setField(Piece.BPAWN,10);

        Move root = new MoveImpl(0,0,'0',b,false);

        ArrayList<Move> movelist = new ArrayList<>();
        mg.generatePawnMoves(root,40,movelist);

        Move rightOne=null;
        for(Move move:movelist){
            if(move.getTo()==26){
                rightOne=move;
            }
        }
        if(rightOne==null){
            fail();
        }

        movelist = new ArrayList<>();
        mg.generateEnpassant(rightOne,movelist);

        assertEquals(movelist.size(),2);

        for(Move move: movelist){
            assertEquals(move.getBoard().getPiece(18),Piece.WPAWN);
            assertEquals(move.getBoard().getPiece(26),Piece.EMPTY);
        }
    }

    @Test
    public void emptyEnpassantBlack(){
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,50);

        Move root = new MoveImpl(0,0,'0',b,true);

        ArrayList<Move> movelist = new ArrayList<>();
        mg.generatePawnMoves(root,100,movelist);

        Move rightOne=null;
        for(Move move:movelist){
            if(move.getTo()==34){
                rightOne=move;
            }
        }
        if(rightOne==null){
            fail();
        }

        movelist = new ArrayList<>();
        mg.generateEnpassant(rightOne,movelist);

        assertEquals(movelist.size(),0);
    }

    @Test
    public void emptyEnpassantWhite(){
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();
        Board b= new BoardImpl();
        b.setField(Piece.BPAWN,10);

        Move root = new MoveImpl(0,0,'0',b,false);

        ArrayList<Move> movelist = new ArrayList<>();
        mg.generatePawnMoves(root,40,movelist);

        Move rightOne=null;
        for(Move move:movelist){
            if(move.getTo()==26){
                rightOne=move;
            }
        }
        if(rightOne==null){
            fail();
        }

        movelist = new ArrayList<>();
        mg.generateEnpassant(rightOne,movelist);

        assertEquals(movelist.size(),0);
    }
}
