public interface Evaluation {

    public int evaluate(Board board,boolean blacksTurn);

    public int material(Board board,boolean blacksTurn);
    public int mobility(Board board,boolean blacksTurn);

}
