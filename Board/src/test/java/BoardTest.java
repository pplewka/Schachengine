import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void constructorTest(){
        Board b = new BoardImpl();
        for(int i = 26; i < 118; ) {
            assertEquals(b.getPiece(i), Piece.EMPTY);
            i++;
            if ((i + 2) % 12 == 0) {
                i += 4;
            }
        }

        for(int i = 0; i < 144; ) {
            assertEquals(b.getPiece(i), Piece.SPACE);
            i++;
            if ((i== 26)||(i== 38)||(i== 50)||(i== 62)||(i== 74)||(i== 86)||(i== 98)||(i== 110)) {
                i += 8;
            }
        }
    }

    @Test
    public void copyTest(){
        Board b1= new BoardImpl();
        b1.setField(Piece.WPAWN,65);
        assertEquals(Piece.WPAWN,b1.getPiece(65));

        //check for right copy
        Board b2= b1.copy();
        assertEquals(Piece.WPAWN,b2.getPiece(65));

        //change first board
        b1.setField(Piece.BPAWN,65);
        assertEquals(Piece.BPAWN,b1.getPiece(65));

        //check if second board has changed
        assertEquals(Piece.WPAWN,b2.getPiece(65));
    }

    @Test
    public void applyMoveTest(){
        Board b= new BoardImpl();
        b.startPos();

        b.applyMove(100,88);
        assertEquals(Piece.WPAWN,b.getPiece(88));
        assertEquals(Piece.EMPTY,b.getPiece(100));

        b.applyMove(116,91);
        assertEquals(Piece.WKNIGHT,b.getPiece(91));
        assertEquals(Piece.EMPTY,b.getPiece(116));
    }

    @Test
    public void pathHasPiece(){
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,115);
        b.setField(Piece.BPAWN,28);
        assertTrue(b.pathHasPiece(114,117));
        assertFalse(b.pathHasPiece(114,110));
        assertTrue(b.pathHasPiece(26,30));
        assertFalse(b.pathHasPiece(30,33));
    }

    @Test
    public void castlingDoneTest(){
        Board b= new BoardImpl();
        //all pieces "moved"
        assertTrue(b.castlingDone(26));
        assertTrue(b.castlingDone(33));
        assertTrue(b.castlingDone(110));
        assertTrue(b.castlingDone(117));

        //all pieces moved and pieces in between
        b.setField(Piece.WPAWN,27);
        b.setField(Piece.WPAWN,32);
        b.setField(Piece.WPAWN,116);
        b.setField(Piece.WPAWN,111);
        assertTrue(b.castlingDone(26));
        assertTrue(b.castlingDone(33));
        assertTrue(b.castlingDone(110));
        assertTrue(b.castlingDone(117));

        //all pieces not moved and pieces in between
        b.startPos();
        assertTrue(b.castlingDone(26));
        assertTrue(b.castlingDone(33));
        assertTrue(b.castlingDone(110));
        assertTrue(b.castlingDone(117));

        //all pieces not moved and no pieces in between
        b=new BoardImpl();
        b.setwRightRockMoved(false);
        b.setwLeftRockMoved(false);
        b.setwKingMoved(false);
        b.setbRightRockMoved(false);
        b.setbLeftRockMoved(false);
        b.setbKingMoved(false);
        assertFalse(b.castlingDone(26));
        assertFalse(b.castlingDone(33));
        assertFalse(b.castlingDone(110));
        assertFalse(b.castlingDone(117));

    }

    @Test
    public void fieldOnBoard(){
        Board b= new BoardImpl();
        for(int i = 0; i < 144; ) {
            assertFalse(b.fieldIsOnBoard(i));
            i++;
            if ((i== 26)||(i== 38)||(i== 50)||(i== 62)||(i== 74)||(i== 86)||(i== 98)||(i== 110)) {
                i += 8;
            }
        }

        for(int i = 26; i < 118; ) {
            assertTrue(b.fieldIsOnBoard(i));
            i++;
            if ((i + 2) % 12 == 0) {
                i += 4;
            }
        }
    }

    @Test
    public void fieldOccupiedTest(){
        Board b= new BoardImpl();
        for(int i = 0; i < 144; ) {
            assertFalse(b.fieldIsOccupied(i));
            i++;
        }

        int p=0;
        for(int i = 26; i < 118; ) {
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

            i++;
            p++;
            if(p==12){
                p=0;
            }
            if ((i + 2) % 12 == 0) {
                i += 4;
            }
        }

        for(int i = 26; i < 118; ) {
            assertTrue(b.fieldIsOccupied(i));
            i++;
            if ((i + 2) % 12 == 0) {
                i += 4;
            }
        }
    }

    @Test
    public void fieldHasOpponentTest(){
        Board testBoard = new BoardImpl();
        testBoard.setField(Piece.SPACE,66);
        assertTrue(!testBoard.fieldHasOpponent(66,true));

        testBoard.setField(Piece.EMPTY,66);
        assertTrue(!testBoard.fieldHasOpponent(66,true));

        testBoard.setField(Piece.SPACE,66);
        assertTrue(!testBoard.fieldHasOpponent(66,false));

        testBoard.setField(Piece.EMPTY,66);
        assertTrue(!testBoard.fieldHasOpponent(66,false));



        testBoard.setField(Piece.WPAWN,66);
        assertTrue(testBoard.fieldHasOpponent(66,true));

        testBoard.setField(Piece.WKING,66);
        assertTrue(testBoard.fieldHasOpponent(66,true));

        testBoard.setField(Piece.WQUEEN,66);
        assertTrue(testBoard.fieldHasOpponent(66,true));

        testBoard.setField(Piece.WKNIGHT,66);
        assertTrue(testBoard.fieldHasOpponent(66,true));

        testBoard.setField(Piece.WROOK,66);
        assertTrue(testBoard.fieldHasOpponent(66,true));

        testBoard.setField(Piece.WBISHOP,66);
        assertTrue(testBoard.fieldHasOpponent(66,true));



        testBoard.setField(Piece.BPAWN,66);
        assertTrue(testBoard.fieldHasOpponent(66,false));

        testBoard.setField(Piece.BKING,66);
        assertTrue(testBoard.fieldHasOpponent(66,false));

        testBoard.setField(Piece.BQUEEN,66);
        assertTrue(testBoard.fieldHasOpponent(66,false));

        testBoard.setField(Piece.BKNIGHT,66);
        assertTrue(testBoard.fieldHasOpponent(66,false));

        testBoard.setField(Piece.BROOK,66);
        assertTrue(testBoard.fieldHasOpponent(66,false));

        testBoard.setField(Piece.BBISHOP,66);
        assertTrue(testBoard.fieldHasOpponent(66,false));
    }


}
