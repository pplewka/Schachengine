import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class KnightTest {
    @Test
    public void emptyMovementWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WKNIGHT,36);
        Move root= new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg =MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> list = new ArrayList<>();
        mg.generateKnightMoves(root,78,list);
        int [] expected = new int[]{21,30,46,53,51,42,26,19};
        assertEquals(list.size(),expected.length);

        for(int i:expected){
            boolean contained= false;

            for(Move m:list){
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
        b.setField(Piece.BKNIGHT,36);
        Move root= new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg =MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> list = new ArrayList<>();
        mg.generateKnightMoves(root,78,list);
        int [] expected = new int[]{21,30,46,53,51,42,26,19};
        assertEquals(list.size(),expected.length);

        for(int i:expected){
            boolean contained= false;

            for(Move m:list){
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
        b.setField(Piece.WKNIGHT,36);
        int [] blocking = new int[]{21,30,46,53,51,42,26,19};
        for(int i:blocking){
            b.setField(Piece.WPAWN,i);
        }
        Move root= new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg =MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> list = new ArrayList<>();
        mg.generateKnightMoves(root,78,list);

        assertEquals(list.size(),0);
    }

    @Test
    public void ownPiecesBlockingBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.BKNIGHT,36);
        int [] blocking = new int[]{21,30,46,53,51,42,26,19};
        for(int i:blocking){
            b.setField(Piece.BPAWN,i);
        }
        Move root= new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg =MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> list = new ArrayList<>();
        mg.generateKnightMoves(root,78,list);

        assertEquals(list.size(),0);
    }

    @Test
    public void opponentBlockingWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WKNIGHT,36);
        int [] blocking = new int[]{21,30,46,53,51,42,26,19};
        for(int i:blocking){
            b.setField(Piece.BPAWN,i);
        }
        Move root= new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg =MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> list = new ArrayList<>();
        mg.generateKnightMoves(root,78,list);

        assertEquals(list.size(),blocking.length);

        for(int i:blocking){
            boolean contained= false;

            for(Move m:list){
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
        b.setField(Piece.BKNIGHT,36);
        int [] blocking = new int[]{21,30,46,53,51,42,26,19};
        for(int i:blocking){
            b.setField(Piece.WPAWN,i);
        }
        Move root= new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg =MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> list = new ArrayList<>();
        mg.generateKnightMoves(root,78,list);

        assertEquals(list.size(),blocking.length);

        for(int i:blocking){
            boolean contained= false;

            for(Move m:list){
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
    public void inCornerWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WKNIGHT,56);
        int [] expected = new int[]{50,41};
        Move root= new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg =MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> list = new ArrayList<>();
        mg.generateKnightMoves(root,110,list);

        assertEquals(list.size(),expected.length);

        for(int i:expected){
            boolean contained= false;

            for(Move m:list){
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

        b= new BoardImpl();
        b.setField(Piece.WKNIGHT,0);
        expected = new int[]{17,10};
        root= new MoveImpl(0,0,'0',b,true);
        list = new ArrayList<>();
        mg.generateKnightMoves(root,26,list);

        assertEquals(list.size(),expected.length);

        for(int i:expected){
            boolean contained= false;

            for(Move m:list){
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

        b= new BoardImpl();
        b.setField(Piece.WKNIGHT,7);
        expected = new int[]{13,22};
        root= new MoveImpl(0,0,'0',b,true);
        list = new ArrayList<>();
        mg.generateKnightMoves(root,33,list);

        assertEquals(list.size(),expected.length);

        for(int i:expected){
            boolean contained= false;

            for(Move m:list){
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

        b= new BoardImpl();
        b.setField(Piece.WKNIGHT,63);
        expected = new int[]{46,53};
        root= new MoveImpl(0,0,'0',b,true);
        list = new ArrayList<>();
        mg.generateKnightMoves(root,117,list);

        assertEquals(list.size(),expected.length);

        for(int i:expected){
            boolean contained= false;

            for(Move m:list){
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
    public void inCornerBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.BKNIGHT,56);
        int [] expected = new int[]{50,41};
        Move root= new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg =MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> list = new ArrayList<>();
        mg.generateKnightMoves(root,110,list);

        assertEquals(list.size(),expected.length);

        for(int i:expected){
            boolean contained= false;

            for(Move m:list){
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

        b= new BoardImpl();
        b.setField(Piece.BKNIGHT,0);
        expected = new int[]{10,17};
        root= new MoveImpl(0,0,'0',b,false);
        list = new ArrayList<>();
        mg.generateKnightMoves(root,26,list);

        assertEquals(list.size(),expected.length);

        for(int i:expected){
            boolean contained= false;

            for(Move m:list){
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

        b= new BoardImpl();
        b.setField(Piece.BKNIGHT,7);
        expected = new int[]{13,22};
        root= new MoveImpl(0,0,'0',b,false);
        list = new ArrayList<>();
        mg.generateKnightMoves(root,33,list);

        assertEquals(list.size(),expected.length);

        for(int i:expected){
            boolean contained= false;

            for(Move m:list){
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

        b= new BoardImpl();
        b.setField(Piece.BKNIGHT,63);
        expected = new int[]{46,53};
        root= new MoveImpl(0,0,'0',b,false);
        list = new ArrayList<>();
        mg.generateKnightMoves(root,117,list);

        assertEquals(list.size(),expected.length);

        for(int i:expected){
            boolean contained= false;

            for(Move m:list){
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
