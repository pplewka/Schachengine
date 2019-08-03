import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EnpassantTest {
    @Test
    public void regularEnpassant(){
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();
        Board b= new BoardImpl();
        b.setField(Piece.BPAWN,75);
        b.setField(Piece.WPAWN,100);

        Move root = new MoveImpl(0,0,'0',b,true);
    }
}
