import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class BishopTest {

    @Test
    public void emptyMovementWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WBISHOP,65);

        int[] expected={26,39,52,78,91,104,117,32,43,54,76,87,98};

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
        b.setField(Piece.BBISHOP,65);

        int[] expected={26,39,52,78,91,104,117,32,43,54,76,87,98};

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
        b.setField(Piece.WBISHOP,65);

        b.setField(Piece.WPAWN,39);
        b.setField(Piece.WPAWN,43);
        b.setField(Piece.WPAWN,91);
        b.setField(Piece.WPAWN,87);

        int[] expected={52,78,54,76};

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
        b.setField(Piece.BBISHOP,65);

        b.setField(Piece.BPAWN,39);
        b.setField(Piece.BPAWN,43);
        b.setField(Piece.BPAWN,91);
        b.setField(Piece.BPAWN,87);

        int[] expected={52,78,54,76};

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
        b.setField(Piece.WBISHOP,65);

        b.setField(Piece.BPAWN,39);
        b.setField(Piece.BPAWN,43);
        b.setField(Piece.BPAWN,91);
        b.setField(Piece.BPAWN,87);

        int[] expected={52,78,54,76,39,43,91,87};

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
        b.setField(Piece.BBISHOP,65);

        b.setField(Piece.WPAWN,39);
        b.setField(Piece.WPAWN,43);
        b.setField(Piece.WPAWN,91);
        b.setField(Piece.WPAWN,87);

        int[] expected={52,78,54,76,39,43,91,87};

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
