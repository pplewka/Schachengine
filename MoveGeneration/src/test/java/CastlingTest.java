import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class CastlingTest {

    private void setRooksAndKings(Board b){
        b.setField(Piece.BROOK,26);
        b.setField(Piece.BROOK,33);
        b.setField(Piece.BKING,30);
        b.setField(Piece.WROOK,110);
        b.setField(Piece.WROOK,117);
        b.setField(Piece.WKING,114);
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
        expected[0].setField(Piece.WROOK,113);
        expected[0].setField(Piece.WKING,112);
        expected[0].setField(Piece.EMPTY,110);
        expected[0].setField(Piece.EMPTY,114);

        expected[1].setField(Piece.WROOK,115);
        expected[1].setField(Piece.WKING,116);
        expected[1].setField(Piece.EMPTY,117);
        expected[1].setField(Piece.EMPTY,114);

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
        expected[0].setField(Piece.BROOK,29);
        expected[0].setField(Piece.BKING,28);
        expected[0].setField(Piece.EMPTY,26);
        expected[0].setField(Piece.EMPTY,30);

        expected[1].setField(Piece.BROOK,31);
        expected[1].setField(Piece.BKING,32);
        expected[1].setField(Piece.EMPTY,33);
        expected[1].setField(Piece.EMPTY,30);

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
        b.setField(Piece.WPAWN,111);
        b.setField(Piece.BPAWN,116);

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
        b.setField(Piece.BPAWN,27);
        b.setField(Piece.WPAWN,31);

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
        b.setField(Piece.BROOK,65);
        b.setField(Piece.BROOK,67);

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
        b.setField(Piece.WROOK,65);
        b.setField(Piece.WROOK,67);

        Move root = new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> castels= new ArrayList<>();
        mg.generateCastling(root,castels);

        assertEquals(castels.size(),0);
    }
}
