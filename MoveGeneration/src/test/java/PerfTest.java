import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class PerfTest {

    private static long perf(int i) {
            MoveGenerationImpl mg = new MoveGenerationImpl();
            ArrayList<ArrayList<Move>> leafs1 = new ArrayList<>();
            ArrayList<ArrayList<Move>> leafs2 = new ArrayList<>();
            Board b = new BoardImpl();
            b.startPos();
            Move root = new MoveImpl(0, 0, ' ', b, true);
            ArrayList<Move> rootList =new ArrayList<Move>();
            rootList.add(root);
            leafs1.add(rootList);

            for (int j = 0; j < i; j++) {
                for (ArrayList<Move> innerList1 : leafs1) {
                    for(Move mv:innerList1){
                        ArrayList innerList2 = mg.generateAllMoves(mv);
                        leafs2.add(innerList2);
                    }
                }

                leafs1.clear();

                ArrayList<ArrayList<Move>> temp = leafs2;
                leafs2 = leafs1;
                leafs1 = temp;
            }

            long output=0;
            for(ArrayList<Move> li:leafs1){
                output+=li.size();
            }

            return output;

    }

    @Test //@Disabled
    public void perfTest() {
        assertEquals(20, perf(1));
        assertEquals(400, perf(2));
        assertEquals(8902, perf(3));
        assertEquals(197281, perf(4));
        //assertEquals(4865609, perf(5));
        //assertEquals(119060324, perf(6)); //outOfMemory
        //assertEquals(3195901860, perf(7));
        //assertEquals(84998978956, perf(8));
        //assertEquals(2439530234167, perf(9));

        //No detailed data
        //assertEquals(69352859712417, perf(10));
        //assertEquals(2097651003696806, perf(11));
        //assertEquals(62854969236701747, perf(12));
        //assertEquals(1981066775000396239, perf(13));
    }
}
