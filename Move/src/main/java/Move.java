public interface Move {
    public void setChildren(Move[] children);
    public Move [] getChildren();

    public int getFrom();
    public int getTo();
    public char getChar();

    public void setEnpassant(int field);
    public void resetEnpassant();
    public int getEnpassant();

    public void setBlacksTurn(boolean blacksTurn);
    public boolean blacksTurn();

    public Move getParent();
    public void setParent(Move parent);

    public void setBoard(Board board);
    public Board getBoard();

    public void setEval(int eval);
    public int getEval();

    public byte getDepth();
    public void setDepth(byte newDepth);

    /**
     *!Has to be synchronised!
     */
    public boolean setMaxMinIfBiggerSmaller(int newValue);
}
