import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class BishopTest {

    @Test
    public void emptyMovementWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WBISHOP,27);

        int[] expected={0,9,18,36,45,54,63,6,13,20,34,41,48};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,true);

        mg.generateBishopMoves(parent,65,moves);

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
        b.setField(Piece.BBISHOP,27);

        int[] expected={0,9,18,36,45,54,63,6,13,20,34,41,48};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,false);

        mg.generateBishopMoves(parent,65,moves);

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
        b.setField(Piece.WBISHOP,27);

        b.setField(Piece.WPAWN,9);
        b.setField(Piece.WPAWN,13);
        b.setField(Piece.WPAWN,45);
        b.setField(Piece.WPAWN,41);

        int[] expected={18,36,20,34};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,true);

        mg.generateBishopMoves(parent,65,moves);

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
        b.setField(Piece.BBISHOP,27);

        b.setField(Piece.BPAWN,9);
        b.setField(Piece.BPAWN,13);
        b.setField(Piece.BPAWN,45);
        b.setField(Piece.BPAWN,41);

        int[] expected={18,36,20,34};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,false);

        mg.generateBishopMoves(parent,65,moves);

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
        b.setField(Piece.WBISHOP,27);

        b.setField(Piece.BPAWN,9);
        b.setField(Piece.BPAWN,13);
        b.setField(Piece.BPAWN,45);
        b.setField(Piece.BPAWN,41);

        int[] expected={18,36,20,34,9,13,45,41};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,true);

        mg.generateBishopMoves(parent,65,moves);

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
        b.setField(Piece.BBISHOP,27);

        b.setField(Piece.WPAWN,9);
        b.setField(Piece.WPAWN,13);
        b.setField(Piece.WPAWN,45);
        b.setField(Piece.WPAWN,41);

        int[] expected={18,36,20,34,9,13,45,41};

        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves= new ArrayList<>();
        Move parent = new MoveImpl(0,0,'0',b,false);

        mg.generateBishopMoves(parent,65,moves);

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
