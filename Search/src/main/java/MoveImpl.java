public class MoveImpl implements Move {
    private int from;
    private int to;
    private char aChar;

    private Board board;
    private boolean blacksTurn;

    private int enpassant;

    private int eval;
    private int maxMin;

    private Move [] children;
    private Move parent;

    public MoveImpl(int from, int to,char c,Board board,boolean blacksTurn){
        this(from,to,c,board,blacksTurn,-1,0);
    }

    public MoveImpl(int from, int to,char c,Board board,boolean blacksTurn,int enpassant){
        this(from,to,c,board,blacksTurn,enpassant,0);
    }

    public MoveImpl(int from, int to,char c,Board board,boolean blacksTurn,int enpassant,int eval){
        this.from=from;
        this.to=to;
        this.aChar=c;
        this.board=board;
        this.blacksTurn=blacksTurn;

        this.enpassant=enpassant;

        this.eval=eval;
        this.maxMin=0;

        this.children=null;
        this.parent=null;
    }




    @Override
    public void setChildren(Move [] children) {
        this.children=children;
    }

    @Override
    public Move [] getChildren() {
        return children;
    }

    @Override
    public int getFrom() {
        return from;
    }

    @Override
    public int getTo() {
        return to;
    }

    @Override
    public char getChar() {
        return aChar;
    }

    @Override
    public void setEnpassant(int field) {
        this.enpassant=enpassant;
    }

    @Override
    public void resetEnpassant() {
        enpassant=-1;
    }

    @Override
    public int getEnpassant() {
        return enpassant;
    }

    @Override
    public void setBlacksTurn(boolean blacksTurn) {
        this.blacksTurn=blacksTurn;
    }

    @Override
    public boolean blacksTurn() {
        return blacksTurn;
    }

    @Override
    public Move getParent() {
        return parent;
    }

    @Override
    public void setParent(Move parent) {
        this.parent=parent;
    }

    @Override
    public void setBoard(Board board) {
        this.board=board;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public void setEval(int eval) {
        this.eval=eval;
    }

    @Override
    public int getEval() {
        return eval;
    }

    @Override
    public synchronized void setMaxMinIfBiggerSmaller(int newValue) {
        //todo
        /*
        if(changed){
        parent.setMaxIfBiggerSmaller(newValue);
         */
    }
}
