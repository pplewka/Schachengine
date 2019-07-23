import java.util.ArrayList;

public class PrefTest {

    public static void main(String[] args) {
        Board board = new BoardImpl();
        Move root = new MoveImpl(0, 0, ' ', board, false);
        ArrayList<Move> leafs1 = new ArrayList<>();
        ArrayList<Move> leafs2 = new ArrayList<>();
        ArrayList<Move> leafs3 = new ArrayList<>();
        ArrayList<Move> leafs4 = new ArrayList<>();
        ArrayList<Move> leafs5 = new ArrayList<>();
        ArrayList<Move> leafs6 = new ArrayList<>();
        ArrayList<Move> leafs7 = new ArrayList<>();
        ArrayList<Move> leafs8 = new ArrayList<>();

        MoveGenerationImpl mg = new MoveGenerationImpl();

        leafs1 =mg.generateAllMoves(root,true);
        System.out.println("1: "+ leafs1.size());

        for (Move mv:leafs1) {
            leafs2.addAll(mg.generateAllMoves(mv,true));
        }

        System.out.println("2: "+leafs2.size());
        leafs1.clear();

        for (Move mv:leafs2) {
            leafs3.addAll(mg.generateAllMoves(mv,true));
        }

        System.out.println("3: "+leafs3.size());
        leafs2.clear();



    }
}
