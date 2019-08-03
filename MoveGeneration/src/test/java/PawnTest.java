import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class PawnTest {

    @Test
    public void justForwardMoveWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,88);

        Board expected= new BoardImpl();
        expected.setField(Piece.WPAWN,76);

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
        b.setField(Piece.BPAWN,88);

        Board expected= new BoardImpl();
        expected.setField(Piece.BPAWN,100);

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
        b.setField(Piece.WPAWN,100);

        Move root = new MoveImpl(0,0,' ',b,true);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,100,moves);

        boolean jumped=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(76)==Piece.WPAWN){
                jumped=true;
            }
        }

        assertEquals(moves.size(),2);
        assertTrue(jumped);
    }

    @Test
    public void firstMoveBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,40);

        Move root = new MoveImpl(0,0,' ',b,false);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,40,moves);

        boolean jumped=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(64)==Piece.WPAWN){
                jumped=true;
            }
        }

        assertEquals(moves.size(),2);
        assertTrue(jumped);
    }

    @Test
    public void dontCaptureOwnPiecesTestWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,100);
        b.setField(Piece.WKNIGHT,76);
        b.setField(Piece.WKNIGHT,88);
        b.setField(Piece.WKNIGHT,87);
        b.setField(Piece.WKNIGHT,89);

        Move root = new MoveImpl(0,0,' ',b,true);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,100,moves);

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(76),Piece.WKNIGHT);
        }

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(88),Piece.WKNIGHT);
        }

        boolean captured1=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(87)==Piece.WPAWN){
                captured1=true;
            }
        }
        assertFalse(captured1);

        boolean captured2=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(89)==Piece.WPAWN){
                captured2=true;
            }
        }

        assertFalse(captured2);
    }

    @Test
    public void dontCaptureOwnPiecesTestBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.BPAWN,40);
        b.setField(Piece.BKNIGHT,52);
        b.setField(Piece.BKNIGHT,64);
        b.setField(Piece.BKNIGHT,51);
        b.setField(Piece.BKNIGHT,53);

        Move root = new MoveImpl(0,0,' ',b,false);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,40,moves);

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(52),Piece.BKNIGHT);
        }

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(64),Piece.BKNIGHT);
        }

        boolean captured1=false;
        for (Move m:moves) {

            if(m.getBoard().getPiece(51)==Piece.BPAWN){
                captured1=true;
            }
        }
        assertFalse(captured1);

        boolean captured2=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(53)==Piece.BPAWN){
                captured2=true;
            }
        }

        assertFalse(captured2);
    }

    @Test
    public void captureOpponentPiecesTestWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,100);
        b.setField(Piece.BKNIGHT,76);
        b.setField(Piece.BKNIGHT,88);
        b.setField(Piece.BKNIGHT,87);
        b.setField(Piece.BKNIGHT,89);

        Move root = new MoveImpl(0,0,' ',b,true);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,100,moves);

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(76),Piece.BKNIGHT);
        }

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(88),Piece.BKNIGHT);
        }

        boolean captured1=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(87)==Piece.WPAWN){
                captured1=true;
            }
        }
        assertTrue(captured1);

        boolean captured2=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(89)==Piece.WPAWN){
                captured2=true;
            }
        }

        assertTrue(captured2);
    }

    @Test
    public void captureOpponentPiecesTestBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.BPAWN,40);
        b.setField(Piece.WKNIGHT,52);
        b.setField(Piece.WKNIGHT,64);
        b.setField(Piece.WKNIGHT,51);
        b.setField(Piece.WKNIGHT,53);

        Move root = new MoveImpl(0,0,' ',b,false);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,40,moves);

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(52),Piece.WKNIGHT);
        }

        for (Move m:moves) {
            assertEquals(m.getBoard().getPiece(64),Piece.WKNIGHT);
        }

        boolean captured1=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(51)==Piece.BPAWN){
                captured1=true;
            }
        }
        assertTrue(captured1);

        boolean captured2=false;
        for (Move m:moves) {
            if(m.getBoard().getPiece(53)==Piece.BPAWN){
                captured2=true;
            }
        }

        assertTrue(captured2);
    }

    @Test
    public void kingInCheckTestWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,90);
        b.setField(Piece.BPAWN,77);
        b.setField(Piece.BPAWN,79);
        b.setField(Piece.BROOK,78);
        b.setField(Piece.WKING,114);

        Move root = new MoveImpl(0,0,' ',b,true);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,90,moves);

        assertEquals(0,moves.size());
    }

    @Test
    public void kingInCheckTestBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.BPAWN,54);
        b.setField(Piece.WPAWN,67);
        b.setField(Piece.WPAWN,65);
        b.setField(Piece.WROOK,66);
        b.setField(Piece.BKING,30);

        Move root = new MoveImpl(0,0,' ',b,false);
        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,54,moves);

        assertEquals(0,moves.size());
    }

    @Test
    public void promotionTestWhite(){
        Board b= new BoardImpl();
        b.setField(Piece.WPAWN,40);

        Move root = new MoveImpl(0,0,' ',b,true);

        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,40,moves);

        assertEquals(4,moves.size());

        boolean rightPromotion = true;
        for(Move m:moves){
            byte p = m.getBoard().getPiece(28);

            if(!(p==Piece.WBISHOP||p==Piece.WQUEEN||p==Piece.WKNIGHT||p==Piece.WROOK)){
                rightPromotion=false;
            }
        }

        assertTrue(rightPromotion);
    }

    @Test
    public void promotionTestBlack(){
        Board b= new BoardImpl();
        b.setField(Piece.BPAWN,99);

        Move root = new MoveImpl(0,0,' ',b,false);

        MoveGeneration moveGen = MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> moves = new ArrayList<>();
        moveGen.generatePawnMoves(root,99,moves);

        assertEquals(4,moves.size());

        boolean rightPromotion = true;
        for(Move m:moves){
            byte p = m.getBoard().getPiece(111);

            if(!(p==Piece.BBISHOP||p==Piece.BQUEEN||p==Piece.BKNIGHT||p==Piece.BROOK)){
                rightPromotion=false;
            }
        }

        assertTrue(rightPromotion);
    }
}
