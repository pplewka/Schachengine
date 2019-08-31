import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// TODO redo most tests (... maybe not)

public class MaterialTest {
//    Evaluation sut = EvaluationImpl.getEvaluation();
//
//    @Test
//    public void testEmptyBoard() {
//        Board ex1 = new BoardImpl();
//
//        assertAll(
//                () -> assertEquals(0, sut.material(ex1, false)),
//                () -> assertEquals( 0, sut.material(ex1, true))
//        );
//    }
//
//    @Test
//    public void testStartPosValues() {
//
//        Board ex2 = new BoardImpl();
//        ex2.startPos();
//
//        int expected = 0;
//
//        assertAll(
//                () -> assertEquals(expected, sut.material(ex2, false)),
//                () -> assertEquals(expected, sut.material(ex2, true))
//        );
//    }
//
//    @Test
//    public void testOnePawnOneKnightOne() {
//        Board ex3 = new BoardImpl("4k3/p7/8/8/8/8/P7/4K3");
//
//        assertAll(
//                () -> assertEquals(0, sut.material(ex3, false)),
//                () -> assertEquals(0, sut.material(ex3, true))
//        );
//    }
//
//    @Test
//    public void testOnlyKnightsLeft() {
//        Board ex4 = new BoardImpl("8/8/8/2k5/4K3/8/8/8");
//        assertEquals(0, sut.material(ex4, false));
//        assertEquals(0, sut.material(ex4, true));
//    }
//
//    @Test
//    public void testImbalanceOnePawnVSTwoKnights() {
//        Board ex5 = new BoardImpl("pk6/8/8/8/8/8/3K4/1NN5");
//        int white = (100 + 1) * 100;
//        int black = (100 + 3 + 3) * 100;
//        int expectedForWhite = white - black; // = -500
//        int expectedForBlack = (black - white) * -1; // = 500 * -1
//
//        assertEquals(expectedForWhite, sut.material(ex5, false));
//        assertEquals(expectedForBlack, sut.material(ex5, true));
//    }
}
