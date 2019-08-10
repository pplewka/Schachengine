import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class PawnTest {

    @Test
    public void justForwardMoveWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,42);

        Board expected= new BoardImpl();
        expected.setField(Piece.WPAWN,34);

        Move root = new MoveImpl(0,0,' ',b,true);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,88,moves);

        assertEquals(1,moves.size());
        assertArrayEquals(expected.getBoard(),moves.get(0).getBoard().getBoard());
    }

    @Test
    public void justForwardMoveBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.BPAWN,42);

        Board expected= new BoardImpl();
        expected.setField(Piece.BPAWN,50);

        Move root = new MoveImpl(0,0,' ',b,false);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,88,moves);

        assertEquals(1,moves.size());
        assertArrayEquals(expected.getBoard(),moves.get(0).getBoard().getBoard());
    }

    @Test
    public void firstMoveWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,50);

        Move root = new MoveImpl(0,0,' ',b,true);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,100,moves);

        boolean jumped=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(34)==Piece.WPAWN){
                jumped=true;
            }
        }

        assertEquals(moves.size(),2);
        assertTrue(jumped);
    }

    @Test
    public void firstMoveBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,10);

        Move root = new MoveImpl(0,0,' ',b,false);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,40,moves);

        boolean jumped=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(26)==Piece.WPAWN){
                jumped=true;
            }
        }

        assertEquals(moves.size(),2);
        assertTrue(jumped);
    }

    @Test
    public void dontCaptureOwnPiecesTestWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,50);
        b.setField(Piece.WKNIGHT,34);
        b.setField(Piece.WKNIGHT,42);
        b.setField(Piece.WKNIGHT,41);
        b.setField(Piece.WKNIGHT,43);

        Move root = new MoveImpl(0,0,' ',b,true);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,100,moves);

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(34),Piece.WKNIGHT);
        }

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(42),Piece.WKNIGHT);
        }

        boolean captured1=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(41)==Piece.WPAWN){
                captured1=true;
            }
        }
        assertFalse(captured1);

        boolean captured2=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(43)==Piece.WPAWN){
                captured2=true;
            }
        }

        assertFalse(captured2);
    }

    @Test
    public void dontCaptureOwnPiecesTestBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.BPAWN,10);
        b.setField(Piece.BKNIGHT,18);
        b.setField(Piece.BKNIGHT,26);
        b.setField(Piece.BKNIGHT,17);
        b.setField(Piece.BKNIGHT,19);

        Move root = new MoveImpl(0,0,' ',b,false);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,40,moves);

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(18),Piece.BKNIGHT);
        }

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(26),Piece.BKNIGHT);
        }

        boolean captured1=false;
        for (Move m:moves) {

            if(m.getBoard().getPiece(17)==Piece.BPAWN){
                captured1=true;
            }
        }
        assertFalse(captured1);

        boolean captured2=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(19)==Piece.BPAWN){
                captured2=true;
            }
        }

        assertFalse(captured2);
    }

    @Test
    public void captureOpponentPiecesTestWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,50);
        b.setField(Piece.BKNIGHT,34);
        b.setField(Piece.BKNIGHT,42);
        b.setField(Piece.BKNIGHT,41);
        b.setField(Piece.BKNIGHT,43);

        Move root = new MoveImpl(0,0,' ',b,true);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,100,moves);

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(34),Piece.BKNIGHT);
        }

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(42),Piece.BKNIGHT);
        }

        boolean captured1=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(41)==Piece.WPAWN){
                captured1=true;
            }
        }
        assertTrue(captured1);

        boolean captured2=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(43)==Piece.WPAWN){
                captured2=true;
            }
        }

        assertTrue(captured2);
    }

    @Test
    public void captureOpponentPiecesTestBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.BPAWN,10);
        b.setField(Piece.WKNIGHT,18);
        b.setField(Piece.WKNIGHT,26);
        b.setField(Piece.WKNIGHT,17);
        b.setField(Piece.WKNIGHT,19);

        Move root = new MoveImpl(0,0,' ',b,false);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,40,moves);

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(18),Piece.WKNIGHT);
        }

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(26),Piece.WKNIGHT);
        }

        boolean captured1=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(17)==Piece.BPAWN){
                captured1=true;
            }
        }
        assertTrue(captured1);

        boolean captured2=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(19)==Piece.BPAWN){
                captured2=true;
            }
        }

        assertTrue(captured2);
    }

    @Test
    public void kingInCheckTestWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,44);
        b.setField(Piece.BPAWN,35);
        b.setField(Piece.BPAWN,37);
        b.setField(Piece.BROOK,36);
        b.setField(Piece.WKING,60);

        Move root = new MoveImpl(0,0,' ',b,true);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,90,moves);

        assertEquals(0,moves.size());
    }

    @Test
    public void kingInCheckTestBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.BPAWN,20);
        b.setField(Piece.WPAWN,29);
        b.setField(Piece.WPAWN,27);
        b.setField(Piece.WROOK,28);
        b.setField(Piece.BKING,4);

        Move root = new MoveImpl(0,0,' ',b,false);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,54,moves);

        assertEquals(0,moves.size());
    }

    @Test
    public void promotionTestWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,10);

        Move root = new MoveImpl(0,0,' ',b,true);

        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,40,moves);

        assertEquals(4,moves.size());

        boolean rightPromotion = true;
        for(Move m:moves){
            Board mboard=m.getBoard();
            byte p = mboard.getPiece(2);

            if(!(p==Piece.WBISHOP||p==Piece.WQUEEN||p==Piece.WKNIGHT||p==Piece.WROOK)){
                rightPromotion=false;
            }
        }

        assertTrue(rightPromotion);
    }

    @Test
    public void promotionTestBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.BPAWN,49);

        Move root = new MoveImpl(0,0,' ',b,false);

        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,99,moves);

        assertEquals(4,moves.size());

        boolean rightPromotion = true;
        for(Move m:moves){
            byte p = m.getBoard().getPiece(57);

            if(!(p==Piece.BBISHOP||p==Piece.BQUEEN||p==Piece.BKNIGHT||p==Piece.BROOK)){
                rightPromotion=false;
            }
        }

        assertTrue(rightPromotion);
    }
}
