import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class RookTest {

    @Test
    public void emptyMovementWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WROOK,65);

        int[] expected={29,41,53,77,89,101,113,62,63,64,66,67,68,69};

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
        b.setField(Piece.BROOK,65);

        int[] expected={29,41,53,77,89,101,113,62,63,64,66,67,68,69};

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
        b.setField(Piece.WROOK,65);
        b.setField(Piece.WPAWN,63);
        b.setField(Piece.WPAWN,41);
        b.setField(Piece.WPAWN,68);
        b.setField(Piece.WPAWN,101);

        int[] expected={53,77,89,64,66,67};

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
        b.setField(Piece.BROOK,65);
        b.setField(Piece.BPAWN,63);
        b.setField(Piece.BPAWN,41);
        b.setField(Piece.BPAWN,68);
        b.setField(Piece.BPAWN,101);

        int[] expected={53,77,89,64,66,67};

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
        b.setField(Piece.WROOK,65);
        b.setField(Piece.BPAWN,63);
        b.setField(Piece.BPAWN,41);
        b.setField(Piece.BPAWN,68);
        b.setField(Piece.BPAWN,101);

        int[] expected={53,77,89,64,66,67,63,41,68,101};

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
        b.setField(Piece.BROOK,65);
        b.setField(Piece.WPAWN,63);
        b.setField(Piece.WPAWN,41);
        b.setField(Piece.WPAWN,68);
        b.setField(Piece.WPAWN,101);

        int[] expected={53,77,89,64,66,67,63,41,68,101};

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
}
