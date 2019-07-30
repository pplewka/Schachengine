import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTest {

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
