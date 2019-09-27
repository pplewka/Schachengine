import java.util.Comparator;

public class EvaluationBasedSorting implements Comparator<Move> {

    @Override
    public int compare(Move move, Move t1) {
        return EvaluationImpl.getEvaluation().evaluateNoRep(move) - EvaluationImpl.getEvaluation().evaluateNoRep(t1);
    }
}
