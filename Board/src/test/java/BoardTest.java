import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void constructorTest(){
        Board b = new BoardImpl();
        for(int i = 0; i < 64; i++ ) {
            assertEquals(b.getPiece(i), Piece.EMPTY);
        }
    }

    @Test
    public void copyTest(){
        Board b1= new BoardImpl();
        b1.setField(Piece.WPAWN,20);
        assertEquals(Piece.WPAWN,b1.getPiece(20));

        //check for right copy
        Board b2= b1.copy();
        assertEquals(Piece.WPAWN,b2.getPiece(20));

        //change first board
        b1.setField(Piece.BPAWN,20);
        assertEquals(Piece.BPAWN,b1.getPiece(20));

        //check if second board has changed
        assertEquals(Piece.WPAWN,b2.getPiece(20));
    }

    @Test
    public void applyMoveTest(){
        Board b= new BoardImpl();
        b.startPos();

        b.applyMove(50,34);
        assertEquals(Piece.WPAWN,b.getPiece(34));
        assertEquals(Piece.EMPTY,b.getPiece(50));

        b.applyMove(62,45);
        assertEquals(Piece.WKNIGHT,b.getPiece(45));
        assertEquals(Piece.EMPTY,b.getPiece(62));
    }

    @Test
    public void pathHasPiece(){
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,61);
        b.setField(Piece.BPAWN,2);
        assertTrue(b.pathHasPiece(60,63));
        assertFalse(b.pathHasPiece(56,60));
        assertTrue(b.pathHasPiece(0,4));
        assertFalse(b.pathHasPiece(4,7));
    }

    @Test
    public void castlingDoneTest(){
        Board b= new BoardImpl();
        //all pieces "moved"
        assertTrue(b.castlingDone(0));
        assertTrue(b.castlingDone(7));
        assertTrue(b.castlingDone(56));
        assertTrue(b.castlingDone(63));

        //all pieces moved and pieces in between
        b.setField(Piece.WPAWN,1);
        b.setField(Piece.WPAWN,6);
        b.setField(Piece.WPAWN,62);
        b.setField(Piece.WPAWN,57);
        assertTrue(b.castlingDone(0));
        assertTrue(b.castlingDone(7));
        assertTrue(b.castlingDone(56));
        assertTrue(b.castlingDone(63));

        //all pieces not moved and pieces in between
        b.startPos();
        assertTrue(b.castlingDone(0));
        assertTrue(b.castlingDone(7));
        assertTrue(b.castlingDone(56));
        assertTrue(b.castlingDone(63));

        //all pieces not moved and no pieces in between
        b=new BoardImpl();
        b.setwRightRockMoved(false);
        b.setwLeftRockMoved(false);
        b.setwKingMoved(false);
        b.setbRightRockMoved(false);
        b.setbLeftRockMoved(false);
        b.setbKingMoved(false);
        assertFalse(b.castlingDone(0));
        assertFalse(b.castlingDone(7));
        assertFalse(b.castlingDone(56));
        assertFalse(b.castlingDone(63));

    }

    @Test
    public void fieldOccupiedTest(){
        Board b= new BoardImpl();
        for(int i = 0; i < 64; i++) {
            assertFalse(b.fieldIsOccupied(i));
        }

        int p=0;
        for(int i = 0; i < 64;i++ ) {
            switch (p){
                case 0: b.setField(Piece.WPAWN,i);
                    break;
                case 1:b.setField(Piece.WKING,i);
                    break;
                case 2:b.setField(Piece.WKNIGHT,i);
                    break;
                case 3:b.setField(Piece.WQUEEN,i);
                    break;
                case 4:b.setField(Piece.WBISHOP,i);
                    break;
                case 5:b.setField(Piece.WROOK,i);
                    break;
                case 6:b.setField(Piece.BPAWN,i);
                    break;
                case 7:b.setField(Piece.BKING,i);
                    break;
                case 8:b.setField(Piece.BKNIGHT,i);
                    break;
                case 9:b.setField(Piece.BQUEEN,i);
                    break;
                case 10:b.setField(Piece.BROOK,i);
                    break;
                case 11:b.setField(Piece.BBISHOP,i);
                    break;
            }

            p++;
            if(p==12){
                p=0;
            }
        }

        for(int i = 0; i < 64; i++) {
            assertTrue(b.fieldIsOccupied(i));
        }
    }

    @Test
    public void fieldHasOpponentTest(){
        Board testBoard = new BoardImpl();
        testBoard.setField(Piece.SPACE,28);
        assertTrue(!testBoard.fieldHasOpponent(28,true));

        testBoard.setField(Piece.EMPTY,28);
        assertTrue(!testBoard.fieldHasOpponent(28,true));

        testBoard.setField(Piece.SPACE,28);
        assertTrue(!testBoard.fieldHasOpponent(28,false));

        testBoard.setField(Piece.EMPTY,28);
        assertTrue(!testBoard.fieldHasOpponent(28,false));



        testBoard.setField(Piece.WPAWN,28);
        assertTrue(testBoard.fieldHasOpponent(28,true));

        testBoard.setField(Piece.WKING,28);
        assertTrue(testBoard.fieldHasOpponent(28,true));

        testBoard.setField(Piece.WQUEEN,28);
        assertTrue(testBoard.fieldHasOpponent(28,true));

        testBoard.setField(Piece.WKNIGHT,28);
        assertTrue(testBoard.fieldHasOpponent(28,true));

        testBoard.setField(Piece.WROOK,28);
        assertTrue(testBoard.fieldHasOpponent(28,true));

        testBoard.setField(Piece.WBISHOP,28);
        assertTrue(testBoard.fieldHasOpponent(28,true));



        testBoard.setField(Piece.BPAWN,28);
        assertTrue(testBoard.fieldHasOpponent(28,false));

        testBoard.setField(Piece.BKING,28);
        assertTrue(testBoard.fieldHasOpponent(28,false));

        testBoard.setField(Piece.BQUEEN,28);
        assertTrue(testBoard.fieldHasOpponent(28,false));

        testBoard.setField(Piece.BKNIGHT,28);
        assertTrue(testBoard.fieldHasOpponent(28,false));

        testBoard.setField(Piece.BROOK,28);
        assertTrue(testBoard.fieldHasOpponent(28,false));

        testBoard.setField(Piece.BBISHOP,28);
        assertTrue(testBoard.fieldHasOpponent(28,false));
    }


}
