import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class CastlingTest {

    private void setRooksAndKings(Board b){
        b.setField(Piece.BROOK,0);
        b.setField(Piece.BROOK,7);
        b.setField(Piece.BKING,4);
        b.setField(Piece.WROOK,56);
        b.setField(Piece.WROOK,63);
        b.setField(Piece.WKING,60);
    }

    private void setNotMoved(Board b){
        b.setbRightRockMoved(false);
        b.setbLeftRockMoved(false);
        b.setbKingMoved(false);

        b.setwRightRockMoved(false);
        b.setwLeftRockMoved(false);
        b.setwKingMoved(false);
    }

    @Test
    public void freeCastlingWhite(){
        Board b= new BoardImpl();
        setRooksAndKings(b);
        setNotMoved(b);

        Move root = new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> castels= new ArrayList<>();
        mg.generateCastling(root,castels);

        Board [] expected = new BoardImpl[2];
        expected[0]= new BoardImpl();
        expected[1]= new BoardImpl();
        setRooksAndKings(expected[0]);
        setRooksAndKings(expected[1]);
        expected[0].setField(Piece.WROOK,59);
        expected[0].setField(Piece.WKING,58);
        expected[0].setField(Piece.EMPTY,56);
        expected[0].setField(Piece.EMPTY,60);

        expected[1].setField(Piece.WROOK,61);
        expected[1].setField(Piece.WKING,62);
        expected[1].setField(Piece.EMPTY,63);
        expected[1].setField(Piece.EMPTY,60);

        assertEquals(castels.size(),2);

        for(Move move:castels){
            boolean found=false;
            for(Board board:expected){
                if(Arrays.equals(board.getBoard(),move.getBoard().getBoard())){
                    found=true;
                }
            }

            if(!found){
                fail();
            }
        }
    }

    @Test
    public void freeCastlingBlack(){
        Board b= new BoardImpl();
        setRooksAndKings(b);
        setNotMoved(b);

        Move root = new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> castels= new ArrayList<>();
        mg.generateCastling(root,castels);

        Board [] expected = new BoardImpl[2];
        expected[0]= new BoardImpl();
        expected[1]= new BoardImpl();
        setRooksAndKings(expected[0]);
        setRooksAndKings(expected[1]);
        expected[0].setField(Piece.BROOK,3);
        expected[0].setField(Piece.BKING,2);
        expected[0].setField(Piece.EMPTY,0);
        expected[0].setField(Piece.EMPTY,4);

        expected[1].setField(Piece.BROOK,5);
        expected[1].setField(Piece.BKING,6);
        expected[1].setField(Piece.EMPTY,7);
        expected[1].setField(Piece.EMPTY,4);

        assertEquals(castels.size(),2);

        for(Move move:castels){
            boolean found=false;
            for(Board board:expected){
                if(Arrays.equals(board.getBoard(),move.getBoard().getBoard())){
                    found=true;
                }
            }

            if(!found){
                fail();
            }
        }
    }

    @Test
    public void alreadyDoneWhite(){
        Board b= new BoardImpl();
        setRooksAndKings(b);

        Move root = new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> castels= new ArrayList<>();
        mg.generateCastling(root,castels);

        assertEquals(castels.size(),0);
    }

    @Test
    public void alreadyDoneBlack(){
        Board b= new BoardImpl();
        setRooksAndKings(b);

        Move root = new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> castels= new ArrayList<>();
        mg.generateCastling(root,castels);

        assertEquals(castels.size(),0);
    }


    @Test
    public void pieceInBetweenWhite(){
        Board b= new BoardImpl();
        setRooksAndKings(b);
        setNotMoved(b);
        b.setField(Piece.WPAWN,57);
        b.setField(Piece.BPAWN,62);

        Move root = new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> castels= new ArrayList<>();
        mg.generateCastling(root,castels);

        assertEquals(castels.size(),0);
    }

    @Test
    public void pieceInBetweenBlack(){
        Board b= new BoardImpl();
        setRooksAndKings(b);
        setNotMoved(b);
        b.setField(Piece.BPAWN,1);
        b.setField(Piece.WPAWN,5);

        Move root = new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> castels= new ArrayList<>();
        mg.generateCastling(root,castels);

        assertEquals(castels.size(),0);
    }

    @Test
    public void attackedWhite(){
        Board b= new BoardImpl();
        setRooksAndKings(b);
        setNotMoved(b);
        b.setField(Piece.BROOK,27);
        b.setField(Piece.BROOK,29);

        Move root = new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> castels= new ArrayList<>();
        mg.generateCastling(root,castels);

        assertEquals(castels.size(),0);
    }

    @Test
    public void attackedBlack(){
        Board b= new BoardImpl();
        setRooksAndKings(b);
        setNotMoved(b);
        b.setField(Piece.WROOK,27);
        b.setField(Piece.WROOK,29);

        Move root = new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> castels= new ArrayList<>();
        mg.generateCastling(root,castels);

        assertEquals(castels.size(),0);
    }
}
