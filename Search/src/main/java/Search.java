public interface Search {
    public int getDepth();
    public Move getRoot();
    public Move getBestMove();
    public Move getPonderMove();
}
