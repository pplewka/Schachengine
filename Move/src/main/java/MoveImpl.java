public class MoveImpl implements Move {
    private short bitwiseMove;

    private Board board;
    private boolean blacksTurn;

    private int enpassant;

    private byte depth;
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
        String binaryFrom= Integer.toBinaryString(63 & from);
        binaryFrom=String.format("%6s",binaryFrom);
        binaryFrom = binaryFrom.replaceAll(" ","0");
        String binaryTo = Integer.toBinaryString(63 & to);
        binaryTo=String.format("%6s",binaryTo);
        binaryTo = binaryTo.replaceAll(" ","0");
        String binaryChar;
        switch (c){
            case ' ':
                binaryChar="0000";
                break;
            case 'Q':
                binaryChar="1000";
                break;
            case 'R':
                binaryChar="0100";
                break;
            case 'K':
                binaryChar="0010";
                break;
            case 'B':
                binaryChar="0001";
                break;
            case '0':
                binaryChar="1111";
                break;
            default:
                throw new MoveException("Constructor: wrong char in Move Constructor");
        }

        bitwiseMove=(short)Integer.parseInt(binaryFrom+binaryTo+binaryChar,2);

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
        String shortBinary=Integer.toBinaryString(0xFFFF & bitwiseMove);
        shortBinary= String.format("%16s",shortBinary);
        shortBinary = shortBinary.replaceAll(" ","0");
        String binaryFrom= shortBinary.substring(0,6);
        return Integer.parseInt(binaryFrom,2);
    }

    @Override
    public int getTo() {
        String shortBinary=Integer.toBinaryString(0xFFFF & bitwiseMove);
        shortBinary= String.format("%16s",shortBinary);
        shortBinary = shortBinary.replaceAll(" ","0");
        String binaryTo= shortBinary.substring(6,12);
        return Integer.parseInt(binaryTo,2);
    }

    @Override
    public char getChar() {
        String shortBinary=Integer.toBinaryString(0xFFFF & bitwiseMove);
        shortBinary= String.format("%16s",shortBinary);
        shortBinary = shortBinary.replaceAll(" ","0");
        String binaryChar= shortBinary.substring(12,16);
        switch (binaryChar){
            case "0000":
                return ' ';
            case "1000":
                return'Q';
            case "0100":
                return'R';
            case "0010":
                return'K';
            case "0001":
                return'B';
            case "1111":
                return '0';
            default:
                throw new MoveException("getChar: wrong char encoding");
        }
    }

    @Override
    public void setEnpassant(int field) {
        this.enpassant=field;
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
    public byte getDepth() {
        return depth;
    }

    @Override
    public void setDepth(byte newDepth) {
        depth= newDepth;
    }

    @Override
    public synchronized boolean setMaxMinIfBiggerSmaller(int newValue) {
        if(blacksTurn){
            if(newValue > maxMin){
                maxMin = newValue;
                return true;
            }
        }else{
            if(newValue < maxMin){
                maxMin = newValue;
                return true;
            }
        }

        return false;
    }
}
