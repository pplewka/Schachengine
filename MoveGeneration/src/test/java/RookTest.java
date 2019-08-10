import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class RookTest {

    @Test
    public void emptyMovementWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WROOK,27);

        int[] expected={3,11,19,35,43,51,59,24,25,26,28,29,30,31};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,true);

        mg.generateRookMoves(parent,65,moves);

        assertEquals(expected.length,moves.size());

        for(int i:expected){
            boolean contained= false;

            for(Move m:moves){
                if(m.getTo()==i){
                    if(!contained){
                        contained=true;
                    }else{
                        fail();
                    }
                }
            }

            assertTrue(contained);

        }

    }

    @Test
    public void emptyMovementBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.BROOK,27);

        int[] expected={3,11,19,35,43,51,59,24,25,26,28,29,30,31};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,false);

        mg.generateRookMoves(parent,65,moves);

        assertEquals(expected.length,moves.size());

        for(int i:expected){
            boolean contained= false;

            for(Move m:moves){
                if(m.getTo()==i){
                    if(!contained){
                        contained=true;
                    }else{
                        fail();
                    }
                }
            }

            assertTrue(contained);

        }

    }

    @Test
    public void ownPiecesBlockingWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WROOK,27);
        b.setField(Piece.WPAWN,25);
        b.setField(Piece.WPAWN,11);
        b.setField(Piece.WPAWN,30);
        b.setField(Piece.WPAWN,51);

        int[] expected={19,35,43,26,28,29};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,true);

        mg.generateRookMoves(parent,65,moves);

        assertEquals(expected.length,moves.size());

        for(int i:expected){
            boolean contained= false;

            for(Move m:moves){
                if(m.getTo()==i){
                    if(!contained){
                        contained=true;
                    }else{
                        fail();
                    }
                }
            }
            assertTrue(contained);
        }

    }

    @Test
    public void ownPiecesBlockingBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.BROOK,27);
        b.setField(Piece.BPAWN,25);
        b.setField(Piece.BPAWN,11);
        b.setField(Piece.BPAWN,30);
        b.setField(Piece.BPAWN,51);

        int[] expected={19,35,43,26,28,29};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,false);

        mg.generateRookMoves(parent,65,moves);

        assertEquals(expected.length,moves.size());

        for(int i:expected){
            boolean contained= false;

            for(Move m:moves){
                if(m.getTo()==i){
                    if(!contained){
                        contained=true;
                    }else{
                        fail();
                    }
                }
            }
            assertTrue(contained);
        }

    }

    @Test
    public void opponentBlockingWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WROOK,27);
        b.setField(Piece.BPAWN,25);
        b.setField(Piece.BPAWN,11);
        b.setField(Piece.BPAWN,30);
        b.setField(Piece.BPAWN,51);

        int[] expected={19,35,43,26,28,29,25,11,30,51};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,true);

        mg.generateRookMoves(parent,65,moves);

        assertEquals(expected.length,moves.size());

        for(int i:expected){
            boolean contained= false;

            for(Move m:moves){
                if(m.getTo()==i){
                    if(!contained){
                        contained=true;
                    }else{
                        fail();
                    }
                }
            }
            assertTrue(contained);
        }

    }

    @Test
    public void opponentBlockingBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.BROOK,27);
        b.setField(Piece.WPAWN,25);
        b.setField(Piece.WPAWN,11);
        b.setField(Piece.WPAWN,30);
        b.setField(Piece.WPAWN,51);

        int[] expected={19,35,43,26,28,29,25,11,30,51};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,false);

        mg.generateRookMoves(parent,65,moves);

        assertEquals(expected.length,moves.size());

        for(int i:expected){
            boolean contained= false;

            for(Move m:moves){
                if(m.getTo()==i){
                    if(!contained){
                        contained=true;
                    }else{
                        fail();
                    }
                }
            }
            assertTrue(contained);
        }

    }

    @Test
    public void changingBool(){
        Board b= new BoardImpl();
        b.setField(Piece.WROOK,56);
        b.setField(Piece.WROOK,63);
        b.setField(Piece.BROOK,0);
        b.setField(Piece.BROOK,7);
        b.setbLeftRockMoved(false);
        b.setbRightRockMoved(false);
        b.setwLeftRockMoved(false);
        b.setwRightRockMoved(false);

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,true);

        mg.generateRookMoves(parent,110,moves);
        for(Move m:moves){
            if(!m.getBoard().iswLeftRockMoved()){
                fail();
            }
        }

        moves= new ArrayList<>();
        parent = new MoveImpl(0,0,'0',b,true);

        mg.generateRookMoves(parent,117,moves);
        for(Move m:moves){
            if(!m.getBoard().iswRightRockMoved()){
                fail();
            }
        }

        moves= new ArrayList<>();
        parent = new MoveImpl(0,0,'0',b,false);

        mg.generateRookMoves(parent,26,moves);
        for(Move m:moves){
            if(!m.getBoard().isbLeftRockMoved()){
                fail();
            }
        }

        moves= new ArrayList<>();
        parent = new MoveImpl(0,0,'0',b,false);

        mg.generateRookMoves(parent,33,moves);
        for(Move m:moves){
            if(!m.getBoard().isbRightRockMoved()){
                fail();
            }
        }
    }
}
