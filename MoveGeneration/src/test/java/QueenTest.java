import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class QueenTest {
    @Test
    public void emptyMovementWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WQUEEN,27);

        int[] expected={0,9,18,36,45,54,63,6,13,20,34,41,48,3,11,19,35,43,51,59,24,25,26,28,29,30,31};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,true);

        mg.generateQueenMoves(parent,65,moves);

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
        b.setField(Piece.BQUEEN,27);

        int[] expected={0,9,18,36,45,54,63,6,13,20,34,41,48,3,11,19,35,43,51,59,24,25,26,28,29,30,31};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,false);

        mg.generateQueenMoves(parent,65,moves);

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
        b.setField(Piece.WQUEEN,27);

        b.setField(Piece.WPAWN,9);
        b.setField(Piece.WPAWN,13);
        b.setField(Piece.WPAWN,45);
        b.setField(Piece.WPAWN,41);
        b.setField(Piece.WPAWN,25);
        b.setField(Piece.WPAWN,11);
        b.setField(Piece.WPAWN,30);
        b.setField(Piece.WPAWN,51);

        int[] expected={18,36,20,34,19,35,43,26,28,29};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,true);

        mg.generateQueenMoves(parent,65,moves);

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
        b.setField(Piece.BQUEEN,27);

        b.setField(Piece.BPAWN,9);
        b.setField(Piece.BPAWN,13);
        b.setField(Piece.BPAWN,45);
        b.setField(Piece.BPAWN,41);
        b.setField(Piece.BPAWN,25);
        b.setField(Piece.BPAWN,11);
        b.setField(Piece.BPAWN,30);
        b.setField(Piece.BPAWN,51);

        int[] expected={18,36,20,34,19,35,43,26,28,29};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,false);

        mg.generateQueenMoves(parent,65,moves);

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
        b.setField(Piece.WQUEEN,27);

        b.setField(Piece.BPAWN,9);
        b.setField(Piece.BPAWN,13);
        b.setField(Piece.BPAWN,45);
        b.setField(Piece.BPAWN,41);
        b.setField(Piece.BPAWN,25);
        b.setField(Piece.BPAWN,11);
        b.setField(Piece.BPAWN,30);
        b.setField(Piece.BPAWN,51);

        int[] expected={18,36,20,34,9,13,45,41,19,35,43,26,28,29,25,11,30,51};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,true);

        mg.generateQueenMoves(parent,65,moves);

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
        b.setField(Piece.BQUEEN,27);

        b.setField(Piece.WPAWN,9);
        b.setField(Piece.WPAWN,13);
        b.setField(Piece.WPAWN,45);
        b.setField(Piece.WPAWN,41);
        b.setField(Piece.WPAWN,25);
        b.setField(Piece.WPAWN,11);
        b.setField(Piece.WPAWN,30);
        b.setField(Piece.WPAWN,51);

        int[] expected={18,36,20,34,9,13,45,41,19,35,43,26,28,29,25,11,30,51};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,false);

        mg.generateQueenMoves(parent,65,moves);

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
}
