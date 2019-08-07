import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PerfTest {
    private static String [] translationMatrix= new String [144];
    static {
        //above space
        for(int i=0;i<24;i++){
            translationMatrix[i]="Space";
        }

        //left space
        for(int i=24;i<109;i+=12){
            translationMatrix[i]="Space";
        }
        for(int i=25;i<110;i+=12){
            translationMatrix[i]="Space";
        }

        //right space
        for(int i=34;i<119;i+=12){
            translationMatrix[i]="Space";
        }
        for(int i=35;i<120;i+=12){
            translationMatrix[i]="Space";
        }

        //below space
        for(int i=120;i<144;i++){
            translationMatrix[i]="Space";
        }

        int j=0;
        for(int i=26;i<34;i++){
            char pre =(char)(97+j);
            translationMatrix[i]=pre+"8";
                    j++;
        }

        j=0;
        for(int i=38;i<46;i++){
            char pre =(char)(97+j);
            translationMatrix[i]=pre+"7";
            j++;
        }

        j=0;
        for(int i=50;i<58;i++){
            char pre =(char)(97+j);
            translationMatrix[i]=pre+"6";
            j++;
        }

        j=0;
        for(int i=62;i<70;i++){
            char pre =(char)(97+j);
            translationMatrix[i]=pre+"5";
            j++;
        }

        j=0;
        for(int i=74;i<82;i++){
            char pre =(char)(97+j);
            translationMatrix[i]=pre+"4";
            j++;
        }

        j=0;
        for(int i=86;i<94;i++){
            char pre =(char)(97+j);
            translationMatrix[i]=pre+"3";
            j++;
        }

        j=0;
        for(int i=98;i<106;i++){
            char pre =(char)(97+j);
            translationMatrix[i]=pre+"2";
            j++;
        }

        j=0;
        for(int i=110;i<118;i++){
            char pre =(char)(97+j);
            translationMatrix[i]=pre+"1";
            j++;
        }
    }

    private static long recursivePerf(int i,Move parent){
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();
        ArrayList<Move> children = mg.generateAllMoves(parent);

        if(i==1){
            return children.size();
        }else{
            long output=0;

            for(Move tempMove:children){
                output+=recursivePerf(i-1,tempMove);
            }

            return output;
        }
    }

    private static long perf(int i, Board b) {
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();
        Move root = new MoveImpl(0, 0, ' ', b, true);

        return recursivePerf(i,root);
    }

    private static long debugPerf(int i, Board b) {
        MoveGeneration mg = MoveGenerationImpl.getMoveGeneration();
        Move root = new MoveImpl(0, 0, ' ', b, true);

        ArrayList<Move> rootMoves= mg.generateAllMoves(root);

        long output=0;
        HashMap<String, Long> countingMap = new HashMap<>();
        for(Move tempMove:rootMoves) {
            long movesPerRootMove = recursivePerf(i-1,tempMove);
            output+=movesPerRootMove;

            String mstring=translationMatrix[tempMove.getFrom()]+translationMatrix[tempMove.getTo()];
            if(tempMove.getChar()!=' '){
                mstring+=tempMove.getChar();
            }
            countingMap.put(mstring,movesPerRootMove);

        }

        countingMap.forEach((String s,Long lon)->{
            System.out.println(s+": "+ lon);
        });


        return output;

    }

    @Test @Disabled
    public void debugPerf() {
        Board b = new BoardImpl();
        b.startPos();

        assertEquals(20, debugPerf(1,b));
        assertEquals(400, debugPerf(2,b));
        assertEquals(8902, debugPerf(3,b));
        assertEquals(197281, debugPerf(4,b));

        //assertEquals(4865609, debugPerf(5,b));  //fail
        //assertEquals(119060324, debugPerf(6,b)); //outOfMemory
        //assertEquals(3195901860, debugPerf(7,b));
        //assertEquals(84998978956, debugPerf(8,b));
        //assertEquals(2439530234167, debugPerf(9,b));

        //No detailed data
        //assertEquals(69352859712417, debugPerf(10,b));
        //assertEquals(2097651003696806, debugPerf(11,b));
        //assertEquals(62854969236701747, debugPerf(12,b));
        //assertEquals(1981066775000396239, debugPerf(13,b));
    }

    @Test //@Disabled
    public void perfTestInitialPosition() {
        Board b = new BoardImpl();
        b.startPos();
        //assertEquals(20, perf(1,b));
        //assertEquals(400, perf(2,b));
        //assertEquals(8902, perf(3,b));
        //assertEquals(197281, perf(4,b));
        //assertEquals(4865609, perf(5,b));
        assertEquals(119060324, perf(6,b));
        //assertEquals(3195901860L, perf(7,b)); //number cruncher
        //assertEquals(84998978956L, perf(8,b));
        //assertEquals(2439530234167L, perf(9,b));

        //No detailed data
        //assertEquals(69352859712417L, perf(10,b));
        //assertEquals(2097651003696806L, perf(11,b));
        //assertEquals(62854969236701747L, perf(12,b));
        //assertEquals(1981066775000396239L, perf(13,b));
    }

    @Test //@Disabled
    public void perfTestKiwipetePosition() {
        Board b = KiwipeteBoard();
        assertEquals(48, perf(1,b));
        //assertEquals(2039, perf(2,b));//fail
        //assertEquals(97862, perf(3,b));
        //assertEquals(4085603, perf(4,b));
        //assertEquals(193690690, perf(5,b));
        //assertEquals(8031647685, perf(6,b));
    }

    @Test //@Disabled
    public void perfTestPosition3() {
        Board b = Position3();
        assertEquals(14, perf(1,b));
        //assertEquals(191, perf(2,b));//fail
        //assertEquals(2812, perf(3,b));
        //assertEquals(43238, perf(4,b));
        //assertEquals(674624, perf(5,b));
        //assertEquals(11030083, perf(6,b));
        //assertEquals(178633661, perf(7,b));
        //assertEquals(3009794393, perf(8,b));
    }

    @Test //@Disabled
    public void perfTestPosition4() {
        Board b = Position4();
        assertEquals(6, perf(1,b));
        //assertEquals(264, perf(2,b));//fail
        //assertEquals(9467, perf(3,b));
        //assertEquals(422333, perf(4,b));
        //assertEquals(15833292, perf(5,b));
        //assertEquals(706045033, perf(6,b));
    }

    @Test //@Disabled
    public void perfTestPosition5() {
        Board b = Position5();
        //assertEquals(44, perf(1,b));
        assertEquals(1486, perf(2,b));
        //assertEquals(62379, perf(3,b));//fail
        //assertEquals(2103487, perf(4,b));
        //assertEquals(89941194, perf(5,b));
    }

    @Test //@Disabled
    public void perfTestPosition6() {
        Board b = Position6();
        //assertEquals(46, perf(1,b));
        //assertEquals(2079, perf(2,b));
        //assertEquals(89890, perf(3,b));
        assertEquals(3894594, perf(4,b));
        //assertEquals(164075551, perf(5,b));
        //assertEquals(6923051137, perf(6,b));
        //assertEquals(287188994746, perf(7,b));
        //assertEquals(11923589843526, perf(8,b));
        //assertEquals(490154852788714, perf(9,b));
    }

    private Board KiwipeteBoard(){
        Board b= new BoardImpl();
        b.setwRightRockMoved(false);
        b.setwLeftRockMoved(false);
        b.setbRightRockMoved(false);
        b.setbLeftRockMoved(false);
        b.setbKingMoved(false);
        b.setwKingMoved(false);

        b.setField(Piece.BROOK,26);
        b.setField(Piece.BKING,30);
        b.setField(Piece.BROOK,33);

        b.setField(Piece.BPAWN,38);
        b.setField(Piece.BPAWN,40);
        b.setField(Piece.BPAWN,41);
        b.setField(Piece.BQUEEN,42);
        b.setField(Piece.BPAWN,43);
        b.setField(Piece.BBISHOP,44);

        b.setField(Piece.BBISHOP,50);
        b.setField(Piece.BKNIGHT,51);
        b.setField(Piece.BPAWN,54);
        b.setField(Piece.BKNIGHT,55);
        b.setField(Piece.BPAWN,56);

        b.setField(Piece.WPAWN,65);
        b.setField(Piece.WKNIGHT,66);

        b.setField(Piece.BPAWN,75);
        b.setField(Piece.WPAWN,78);

        b.setField(Piece.WKNIGHT,88);
        b.setField(Piece.WQUEEN,91);
        b.setField(Piece.BPAWN,93);

        b.setField(Piece.WPAWN,98);
        b.setField(Piece.WPAWN,99);
        b.setField(Piece.WPAWN,100);
        b.setField(Piece.WBISHOP,101);
        b.setField(Piece.WBISHOP,102);
        b.setField(Piece.WPAWN,103);
        b.setField(Piece.WPAWN,104);
        b.setField(Piece.WPAWN,105);

        b.setField(Piece.WROOK,110);
        b.setField(Piece.WKING,114);
        b.setField(Piece.WROOK,117);

        return b;
    }

    private Board Position3(){
        Board b = new BoardImpl();

        b.setField(Piece.BPAWN,40);
        b.setField(Piece.BPAWN,53);
        b.setField(Piece.WKING,62);
        b.setField(Piece.WPAWN,63);
        b.setField(Piece.BROOK,69);
        b.setField(Piece.WROOK,75);
        b.setField(Piece.BPAWN,79);
        b.setField(Piece.BKING,81);
        b.setField(Piece.WPAWN, 102);
        b.setField(Piece.WPAWN,104);

        return b;
    }

    private Board Position4(){
        Board b = new BoardImpl();
        b.setbKingMoved(false);
        b.setbRightRockMoved(false);
        b.setbLeftRockMoved(false);

        b.setField(Piece.BROOK,26);
        b.setField(Piece.BKING,30);
        b.setField(Piece.BROOK,33);

        b.setField(Piece.WPAWN,38);
        b.setField(Piece.BPAWN,39);
        b.setField(Piece.BPAWN,40);
        b.setField(Piece.BPAWN,41);
        b.setField(Piece.BPAWN,43);
        b.setField(Piece.BPAWN,44);
        b.setField(Piece.BPAWN,45);

        b.setField(Piece.BBISHOP,51);
        b.setField(Piece.BKNIGHT,55);
        b.setField(Piece.BBISHOP,56);
        b.setField(Piece.WKNIGHT,57);

        b.setField(Piece.BKNIGHT,62);
        b.setField(Piece.WPAWN,63);

        b.setField(Piece.WBISHOP,74);
        b.setField(Piece.WBISHOP,75);
        b.setField(Piece.WPAWN,76);
        b.setField(Piece.WPAWN,78);

        b.setField(Piece.BQUEEN,86);
        b.setField(Piece.WKNIGHT,91);

        b.setField(Piece.WPAWN,98);
        b.setField(Piece.BPAWN,99);
        b.setField(Piece.WPAWN,101);
        b.setField(Piece.WPAWN,104);
        b.setField(Piece.WPAWN,105);

        b.setField(Piece.WROOK,110);
        b.setField(Piece.WQUEEN,113);
        b.setField(Piece.WROOK,115);
        b.setField(Piece.WKING,116);

        return b;
    }

    private Board Position5(){
        Board b = new BoardImpl();
        b.setwKingMoved(false);
        b.setwRightRockMoved(false);
        b.setwLeftRockMoved(false);

        b.setField(Piece.BROOK,26);
        b.setField(Piece.BKNIGHT,27);
        b.setField(Piece.BBISHOP,28);
        b.setField(Piece.BQUEEN,29);
        b.setField(Piece.BKING,31);
        b.setField(Piece.BROOK,33);

        b.setField(Piece.BPAWN,38);
        b.setField(Piece.BPAWN,39);
        b.setField(Piece.WPAWN,41);
        b.setField(Piece.BBISHOP,42);
        b.setField(Piece.BPAWN,43);
        b.setField(Piece.BPAWN,44);
        b.setField(Piece.BPAWN,45);

        b.setField(Piece.BPAWN,52);

        b.setField(Piece.WBISHOP,76);

        b.setField(Piece.WPAWN,98);
        b.setField(Piece.WPAWN,99);
        b.setField(Piece.WPAWN,100);
        b.setField(Piece.WKNIGHT,102);
        b.setField(Piece.BKNIGHT,103);
        b.setField(Piece.WPAWN,104);
        b.setField(Piece.WPAWN,105);

        b.setField(Piece.WROOK,110);
        b.setField(Piece.WKNIGHT,111);
        b.setField(Piece.WBISHOP,112);
        b.setField(Piece.WQUEEN,113);
        b.setField(Piece.WROOK,117);
        b.setField(Piece.WKING,114);

        return b;
    }

    private Board Position6(){
        Board b = new BoardImpl();

        b.setField(Piece.BROOK,26);
        b.setField(Piece.BROOK,31);
        b.setField(Piece.BKING,32);

        b.setField(Piece.BPAWN,39);
        b.setField(Piece.BPAWN,40);
        b.setField(Piece.BQUEEN,42);
        b.setField(Piece.BPAWN,43);
        b.setField(Piece.BPAWN,44);
        b.setField(Piece.BPAWN,45);

        b.setField(Piece.BPAWN,50);
        b.setField(Piece.BKNIGHT,52);
        b.setField(Piece.BPAWN,53);
        b.setField(Piece.BKNIGHT,55);

        b.setField(Piece.BBISHOP,64);
        b.setField(Piece.BPAWN,66);
        b.setField(Piece.WBISHOP,68);

        b.setField(Piece.WBISHOP,76);
        b.setField(Piece.WPAWN,78);
        b.setField(Piece.BBISHOP,80);

        b.setField(Piece.WPAWN,86);
        b.setField(Piece.WKNIGHT,88);
        b.setField(Piece.WPAWN,89);
        b.setField(Piece.WKNIGHT,91);

        b.setField(Piece.WPAWN,99);
        b.setField(Piece.WPAWN,100);
        b.setField(Piece.WQUEEN,102);
        b.setField(Piece.WPAWN,103);
        b.setField(Piece.WPAWN,104);
        b.setField(Piece.WPAWN,105);

        b.setField(Piece.WROOK,110);
        b.setField(Piece.WROOK,115);
        b.setField(Piece.WKING,116);

        return b;
    }


    private static void printBoard(Board b){
        for(int i=0;i<144;i++){
            if(i%12==0){
                System.out.print("\n");
            }

            switch (b.getPiece(i)){
                case Piece.SPACE:
                    System.out.print("S\t");
                    break;
                case Piece.EMPTY:
                    System.out.print("\t");
                    break;


                case Piece.WPAWN:
                    System.out.print("P\t");
                    break;
                case Piece.BPAWN:
                    System.out.print("p\t");
                    break;


                case Piece.WKING:
                    System.out.print("K\t");
                    break;
                case Piece.BKING:
                    System.out.print("k\t");
                    break;


                case Piece.WKNIGHT:
                    System.out.print("N\t");
                    break;
                case Piece.BKNIGHT:
                    System.out.print("n\t");
                    break;


                case Piece.WQUEEN:
                    System.out.print("Q\t");
                    break;
                case Piece.BQUEEN:
                    System.out.print("q\t");
                    break;


                case Piece.WROOK:
                    System.out.print("R\t");
                    break;
                case Piece.BROOK:
                    System.out.print("r\t");
                    break;


                case Piece.WBISHOP:
                    System.out.print("B\t");
                    break;
                case Piece.BBISHOP:
                    System.out.print("b\t");
                    break;
                default:
                    System.out.print("fuck!\t");
                    break;

            }
        }

        System.out.println();
    }
}
