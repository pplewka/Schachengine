import java.util.ArrayList;

public class PrefTest {

    public static int perf(int i){
        MoveGenerationImpl mg = new MoveGenerationImpl();
        ArrayList<Move> leafs1 = new ArrayList<>();
        ArrayList<Move> leafs2 = new ArrayList<>();
        Move root = new MoveImpl(0, 0, ' ', new BoardImpl(), true);
        leafs1.add(root);

        for(int j=0;j<i;j++){
            for (Move move:leafs1) {
                leafs2.addAll(mg.generateAllMoves(move,true));
            }
            leafs1.clear();

            ArrayList<Move> temp =leafs2;
            leafs2= leafs1;
            leafs1=temp;
        }

        return leafs1.size();
    }
}
