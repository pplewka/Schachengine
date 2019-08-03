import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class EnpassantTest {
    @Test
    public void regularEnpassantBlack(){
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();
        Board b= new BoardImpl();
        b.setField(Piece.BPAWN,75);
        b.setField(Piece.BPAWN,77);
        b.setField(Piece.WPAWN,100);

        Move root = new MoveImpl(0,0,'0',b,true);

        ArrayList<Move> movelist = new ArrayList<>();
        mg.generatePawnMoves(root,100,movelist);

        Move rightOne=null;
        for(Move move:movelist){
            if(move.getTo()==76){
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
            assertEquals(move.getBoard().getPiece(88),Piece.BPAWN);
            assertEquals(move.getBoard().getPiece(76),Piece.EMPTY);
        }
    }

    @Test
    public void regularEnpassantWhite(){
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,63);
        b.setField(Piece.WPAWN,65);
        b.setField(Piece.BPAWN,40);

        Move root = new MoveImpl(0,0,'0',b,false);

        ArrayList<Move> movelist = new ArrayList<>();
        mg.generatePawnMoves(root,40,movelist);

        Move rightOne=null;
        for(Move move:movelist){
            if(move.getTo()==64){
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
            assertEquals(move.getBoard().getPiece(52),Piece.WPAWN);
            assertEquals(move.getBoard().getPiece(64),Piece.EMPTY);
        }
    }

    @Test
    public void emptyEnpassantBlack(){
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,100);

        Move root = new MoveImpl(0,0,'0',b,true);

        ArrayList<Move> movelist = new ArrayList<>();
        mg.generatePawnMoves(root,100,movelist);

        Move rightOne=null;
        for(Move move:movelist){
            if(move.getTo()==76){
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
        b.setField(Piece.BPAWN,40);

        Move root = new MoveImpl(0,0,'0',b,false);

        ArrayList<Move> movelist = new ArrayList<>();
        mg.generatePawnMoves(root,40,movelist);

        Move rightOne=null;
        for(Move move:movelist){
            if(move.getTo()==64){
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
