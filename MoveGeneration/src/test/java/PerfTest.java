import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class PerfTest {

    private static int perf(int i) {
        MoveGenerationImpl mg = new MoveGenerationImpl();
        ArrayList<Move> leafs1 = new ArrayList<>();
        ArrayList<Move> leafs2 = new ArrayList<>();
        Board b = new BoardImpl();
        b.startPos();
        Move root = new MoveImpl(0, 0, ' ', b, true);
        leafs1.add(root);

        for(int j=0;j<i;j++){
            for (Move move:leafs1) {
                ArrayList<Move> gen = mg.generateAllMoves(move,true);
                leafs2.addAll(gen);
            }

            leafs1.clear();

            ArrayList<Move> temp =leafs2;
            leafs2= leafs1;
            leafs1=temp;
        }

        return leafs1.size();
    }

    @Test //@Disabled
    public void perfTest() {
        assertEquals(20, perf(1));
        assertEquals(400, perf(2));
        assertEquals(8902, perf(3));
        //assertEquals(197281, perf(4));
        //assertEquals(4865609, perf(5));
        //assertEquals(119060324, perf(6));
    }
}
