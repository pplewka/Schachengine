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

    public MoveImpl(String fenString) {
        fenString = fenString.trim();
        String[] splittedFen = fenString.split(" ");
        Board fenBoard = new BoardImpl(splittedFen[0]);

        //site to move
        switch (splittedFen[1]) {
            case "w":
                this.blacksTurn = true;

                break;
            case "b":
                this.blacksTurn = false;

                break;
            default:
                throw new MoveException("malformed Fen String");
        }

        //castling rights
        if (splittedFen.length > 2) {
            for (int i = 0; i < splittedFen[2].length(); i++) {
                char castlingIndicator = splittedFen[2].charAt(i);
                switch (castlingIndicator) {
                    case 'K':
                        fenBoard.setwRightRockMoved(false);
                        fenBoard.setwKingMoved(false);

                        break;
                    case 'k':
                        fenBoard.setbRightRockMoved(false);
                        fenBoard.setbKingMoved(false);

                        break;
                    case 'Q':
                        fenBoard.setwLeftRockMoved(false);
                        fenBoard.setwKingMoved(false);

                        break;
                    case 'q':
                        fenBoard.setbLeftRockMoved(false);
                        fenBoard.setbKingMoved(false);

                        break;
                    case '-':
                        break;
                    default:
                        throw new MoveException("malformed Fen String");
                }
            }
        }

        //enpassant field
        if (splittedFen.length > 3) {
            char column = splittedFen[3].charAt(0);
            byte multiplier;
            byte addition;

            if (column != '-') {
                switch (column) {
                    case 'a':
                        addition = 0;
                        break;
                    case 'b':
                        addition = 1;
                        break;
                    case 'c':
                        addition = 2;
                        break;
                    case 'd':
                        addition = 3;
                        break;
                    case 'e':
                        addition = 4;
                        break;
                    case 'f':
                        addition = 5;
                        break;
                    case 'g':
                        addition = 6;
                        break;
                    case 'h':
                        addition = 7;
                        break;
                    default:
                        throw new MoveException("malformed Fen String");
                }

                char row = splittedFen[3].charAt(1);
                switch (row) {
                    case '3':
                        multiplier = 2;
                        break;
                    case '6':
                        multiplier = 5;
                        break;
                    default:
                        throw new MoveException("malformed Fen String");
                }

                this.setEnpassant(multiplier * 8 + addition);
            }
        }

        //todo set halfMove time and full move time
        /*
        if(splittedFen.length>4){
            setHalfmoveTime(Integer.parseInt(splittedFen[4]));
        }

        if(splittedFen.length>5){
            setFullmoveTime(Integer.parseInt(splittedFen[4]));
        }
        */
    }

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
