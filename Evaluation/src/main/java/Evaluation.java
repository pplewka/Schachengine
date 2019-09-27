public interface Evaluation {

    public int evaluate(Move toEvaluate);

    public int material(Board board,boolean blacksTurn);
    public int mobility(Board board,boolean blacksTurn);
    public int repetitionScore(Move toEvaluate);

    int evaluateNoRep(Move move);
}
