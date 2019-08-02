import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class KnightTest {
    @Test
    public void emptyMovementWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WKNIGHT,78);
        Move root= new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg = new MoveGenerationImpl();
        ArrayList<Move> list = new ArrayList<>();
        mg.generateKnightMoves(root,78,list);
        int [] expected = new int[]{55,68,92,103,101,88,64,53};
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
        b.setField(Piece.BKNIGHT,78);
        Move root= new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg = new MoveGenerationImpl();
        ArrayList<Move> list = new ArrayList<>();
        mg.generateKnightMoves(root,78,list);
        int [] expected = new int[]{55,68,92,103,101,88,64,53};
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
        b.setField(Piece.WKNIGHT,78);
        int [] blocking = new int[]{55,68,92,103,101,88,64,53};
        for(int i:blocking){
            b.setField(Piece.WPAWN,i);
        }
        Move root= new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg = new MoveGenerationImpl();
        ArrayList<Move> list = new ArrayList<>();
        mg.generateKnightMoves(root,78,list);

        assertEquals(list.size(),0);
    }

    @Test
    public void ownPiecesBlockingBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.BKNIGHT,78);
        int [] blocking = new int[]{55,68,92,103,101,88,64,53};
        for(int i:blocking){
            b.setField(Piece.BPAWN,i);
        }
        Move root= new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg = new MoveGenerationImpl();
        ArrayList<Move> list = new ArrayList<>();
        mg.generateKnightMoves(root,78,list);

        assertEquals(list.size(),0);
    }

    @Test
    public void opponentBlockingWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WKNIGHT,78);
        int [] blocking = new int[]{55,68,92,103,101,88,64,53};
        for(int i:blocking){
            b.setField(Piece.BPAWN,i);
        }
        Move root= new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg = new MoveGenerationImpl();
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
        b.setField(Piece.BKNIGHT,78);
        int [] blocking = new int[]{55,68,92,103,101,88,64,53};
        for(int i:blocking){
            b.setField(Piece.WPAWN,i);
        }
        Move root= new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg = new MoveGenerationImpl();
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
        b.setField(Piece.WKNIGHT,110);
        int [] expected = new int[]{100,87};
        Move root= new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg = new MoveGenerationImpl();
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
        b.setField(Piece.WKNIGHT,26);
        expected = new int[]{40,51};
        root= new MoveImpl(0,0,'0',b,true);
        mg = new MoveGenerationImpl();
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
        b.setField(Piece.WKNIGHT,33);
        expected = new int[]{43,56};
        root= new MoveImpl(0,0,'0',b,true);
        mg = new MoveGenerationImpl();
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
        b.setField(Piece.WKNIGHT,117);
        expected = new int[]{92,103};
        root= new MoveImpl(0,0,'0',b,true);
        mg = new MoveGenerationImpl();
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
        b.setField(Piece.BKNIGHT,110);
        int [] expected = new int[]{100,87};
        Move root= new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg = new MoveGenerationImpl();
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
        b.setField(Piece.BKNIGHT,26);
        expected = new int[]{40,51};
        root= new MoveImpl(0,0,'0',b,false);
        mg = new MoveGenerationImpl();
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
        b.setField(Piece.BKNIGHT,33);
        expected = new int[]{43,56};
        root= new MoveImpl(0,0,'0',b,false);
        mg = new MoveGenerationImpl();
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
        b.setField(Piece.BKNIGHT,117);
        expected = new int[]{92,103};
        root= new MoveImpl(0,0,'0',b,false);
        mg = new MoveGenerationImpl();
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
