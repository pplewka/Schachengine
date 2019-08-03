public interface Evaluation {

    public void evaluate(Move parent);

    public int material(Board board,boolean blacksTurn);
    public int mobility(Board board,boolean blacksTurn);

}
