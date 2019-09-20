import java.util.concurrent.BlockingQueue;

public class MoveImpl implements Move {
    public static final Move DUMMIEMOVE = new MoveImpl(0, 0, ' ', null, false);
    private static final String BLACK_CASTLING_LONG = "e8c8";
    private static final String BLACK_CASTLING_SHORT = "e8g8";
    private static final String WHITE_CASTLING_LONG = "e1c1";
    private static final String WHITE_CASTLING_SHORT = "e1g1";

    private short bitwiseMove;

    private Board board;
    private boolean blacksTurn;

    private int enpassant;

    private byte depth;
    private int eval;
    private int maxMin;

    private Move[] children;
    private Move parent;
    private boolean added;

    @Override
    public boolean isAdded() {
        return added;
    }

    @Override
    public void setAdded(boolean added) {
        this.added = added;
    }

    @Override
    public boolean hasChildren(){
        if(children == null){
            return false;
        }else{
            for(Move child: children){
                if(child != null){
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public synchronized void addIfNotAllready(BlockingQueue<Move> lookupTable){
        if(!added){
            lookupTable.add(this);
            added = true;
        }
    }

    public MoveImpl(String fenString) {
        fenString = fenString.trim();
        String[] splittedFen = fenString.split(" ");
        Board fenBoard = new BoardImpl(splittedFen[0]);

        //site to move
        switch (splittedFen[1]) {
            case "w":
                this.blacksTurn = true;
                this.maxMin = Integer.MAX_VALUE;

                break;
            case "b":
                this.blacksTurn = false;
                this.maxMin = Integer.MIN_VALUE;

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

            if (splittedFen[3].charAt(0) != '-') {
                char row = splittedFen[3].charAt(1);
                if (row != '3' && row != '6') {
                    throw new MoveException("malformed fen String");
                }

                setEnpassant(Translators.translateAlgTo64(splittedFen[3]));
            } else {
                enpassant = -1;
            }
        }
        board = fenBoard;

        //todo set halfMove time and full move time
        /*
        if(splittedFen.length>4){
            setHalfmoveTime(Integer.parseInt(splittedFen[4]));
        }

        if(splittedFen.length>5){
            setFullmoveTime(Integer.parseInt(splittedFen[4]));
        }
        */

        this.depth = 0;
        added = false;
    }

    public MoveImpl(int from, int to, char c, Board board, boolean blacksTurn) {
        this(from, to, c, board, blacksTurn, -1, 0);
    }

    public MoveImpl(int from, int to, char c, Board board, boolean blacksTurn, int enpassant) {
        this(from, to, c, board, blacksTurn, enpassant, 0);
    }

    public MoveImpl(int from, int to, char c, Board board, boolean blacksTurn, int enpassant, int eval) {
        String binaryFrom = Integer.toBinaryString(63 & from);
        binaryFrom = String.format("%6s", binaryFrom);
        binaryFrom = binaryFrom.replaceAll(" ", "0");
        String binaryTo = Integer.toBinaryString(63 & to);
        binaryTo = String.format("%6s", binaryTo);
        binaryTo = binaryTo.replaceAll(" ", "0");
        String binaryChar;
        switch (c) {
            case ' ':
                binaryChar = "0000";
                break;
            case 'Q':
                binaryChar = "1000";
                break;
            case 'R':
                binaryChar = "0100";
                break;
            case 'K':
                binaryChar = "0010";
                break;
            case 'B':
                binaryChar = "0001";
                break;
            case '0':
                binaryChar = "1111";
                break;
            default:
                throw new MoveException("Constructor: wrong char in Move Constructor");
        }

        bitwiseMove = (short) Integer.parseInt(binaryFrom + binaryTo + binaryChar, 2);

        this.board = board;
        this.blacksTurn = blacksTurn;
        if (blacksTurn) {
            this.maxMin = Integer.MAX_VALUE;
        } else {
            this.maxMin = Integer.MIN_VALUE;
        }

        this.enpassant = enpassant;

        this.eval = eval;

        this.children = null;
        this.parent = null;
        this.depth = 0;
        this.added = false;

    }

    @Override
    public void moves(String moves) {
        moves = moves.trim();
        String[] splittedMoves = moves.split(" ");

        for (int i = 0; i < splittedMoves.length; i++) {
            blacksTurn = !blacksTurn;

            byte king = blacksTurn ? Piece.BKING : Piece.WKING;
            int kingFirstPosition = blacksTurn ? 4 : 60;
            byte pawn = blacksTurn ? Piece.BPAWN : Piece.WPAWN;
            byte rook = blacksTurn ? Piece.BROOK : Piece.WROOK;


            String move = splittedMoves[i];

            String from = move.substring(0, 2);
            String to = move.substring(2, 4);
            int from64 = Translators.translateAlgTo64(from);
            int to64 = Translators.translateAlgTo64(to);
            boolean setEnpassant = false;

            //promotion
            if (move.length() > 4) {
                char promotion = move.charAt(4);

                switch (promotion) {
                    case 'n':
                        board.setField(blacksTurn ? Piece.BKNIGHT : Piece.WKNIGHT, to64);
                        board.setField(Piece.EMPTY, from64);

                        break;
                    case 'q':
                        board.setField(blacksTurn ? Piece.BQUEEN : Piece.WQUEEN, to64);
                        board.setField(Piece.EMPTY, from64);

                        break;
                    case 'r':
                        board.setField(blacksTurn ? Piece.BROOK : Piece.WROOK, to64);
                        board.setField(Piece.EMPTY, from64);

                        break;
                    case 'b':
                        board.setField(blacksTurn ? Piece.BBISHOP : Piece.WBISHOP, to64);
                        board.setField(Piece.EMPTY, from64);

                        break;
                    default:
                        throw new MoveException("malformed move: promotion char");
                }
            } else {
                //king Moves
                if (board.getPiece(from64) == king) {
                    //first king move
                    if (from64 == kingFirstPosition) {
                        //castling
                        switch (to64) {
                            case 2:
                                board.setField(Piece.EMPTY, 4);
                                board.setField(Piece.EMPTY, 0);
                                board.setField(Piece.BKING, 2);
                                board.setField(Piece.BROOK, 3);

                                board.setbKingMoved(true);
                                board.setbLeftRockMoved(true);

                                break;
                            case 6:
                                board.setField(Piece.EMPTY, 4);
                                board.setField(Piece.EMPTY, 7);
                                board.setField(Piece.BKING, 6);
                                board.setField(Piece.BROOK, 5);

                                board.setbKingMoved(true);
                                board.setbRightRockMoved(true);

                                break;
                            case 58:
                                board.setField(Piece.EMPTY, 60);
                                board.setField(Piece.EMPTY, 56);
                                board.setField(Piece.WKING, 58);
                                board.setField(Piece.WROOK, 59);

                                board.setwKingMoved(true);
                                board.setwLeftRockMoved(true);

                                break;
                            case 62:
                                board.setField(Piece.EMPTY, 60);
                                board.setField(Piece.EMPTY, 63);
                                board.setField(Piece.WKING, 62);
                                board.setField(Piece.WROOK, 61);

                                board.setwKingMoved(true);
                                board.setwRightRockMoved(true);

                                break;
                            //no castling
                            default:
                                board.applyMove(from64, to64);

                                if (blacksTurn) {
                                    board.setbKingMoved(true);
                                } else {
                                    board.setwKingMoved(true);
                                }
                        }
                    } else {
                        board.applyMove(from64, to64);
                    }
                } else {
                    if (board.getPiece(from64) == rook) {
                        board.applyMove(from64, to64);

                        //first rook move
                        switch (from64) {
                            case 0:
                                board.setbLeftRockMoved(true);
                                break;
                            case 7:
                                board.setbRightRockMoved(true);
                                break;
                            case 56:
                                board.setwLeftRockMoved(true);
                                break;
                            case 63:
                                board.setwRightRockMoved(true);
                                break;
                            default:
                                //not first move
                        }
                    } else {
                        if (board.getPiece(from64) == pawn) {
                            board.applyMove(from64, to64);

                            if (blacksTurn) {
                                //enpassant
                                if (from.charAt(1) == '5') {
                                    if (to64 == from64 + 9 || to64 == from64 + 7) {
                                        if (board.getPiece(to64) == Piece.EMPTY) {
                                            board.setField(Piece.EMPTY, to64 - 8);
                                        }
                                    }
                                } else {
                                    //first pawn move, long move
                                    if (from.charAt(1) == '2') {
                                        if (to64 == from64 + 16) {
                                            enpassant = to64 - 8;
                                            setEnpassant = true;
                                        }
                                    }
                                }
                            } else {
                                //enpassant
                                if (from.charAt(1) == '4') {
                                    if (to64 == from64 - 9 || to64 == from64 - 7) {
                                        if (board.getPiece(to64) == Piece.EMPTY) {
                                            board.setField(Piece.EMPTY, to64 + 8);
                                        }
                                    }
                                } else {
                                    //first pawn move, long move
                                    if (from.charAt(1) == '7') {
                                        if (to64 == from64 - 16) {
                                            enpassant = to64 + 8;
                                            setEnpassant = true;
                                        }
                                    }
                                }
                            }
                        } else {
                            board.applyMove(from64, to64);
                        }
                    }
                }
            }

            if (!setEnpassant) {
                enpassant = -1;
            }
        }
    }

    @Override
    public Move[] getChildren() {
        return children;
    }

    @Override
    public void setChildren(Move[] children) {
        this.children = children;
    }

    @Override
    public int getFrom() {
        String shortBinary = Integer.toBinaryString(0xFFFF & bitwiseMove);
        shortBinary = String.format("%16s", shortBinary);
        shortBinary = shortBinary.replaceAll(" ", "0");
        String binaryFrom = shortBinary.substring(0, 6);
        return Integer.parseInt(binaryFrom, 2);
    }

    @Override
    public int getTo() {
        String shortBinary = Integer.toBinaryString(0xFFFF & bitwiseMove);
        shortBinary = String.format("%16s", shortBinary);
        shortBinary = shortBinary.replaceAll(" ", "0");
        String binaryTo = shortBinary.substring(6, 12);
        return Integer.parseInt(binaryTo, 2);
    }

    @Override
    public char getChar() {
        String shortBinary = Integer.toBinaryString(0xFFFF & bitwiseMove);
        shortBinary = String.format("%16s", shortBinary);
        shortBinary = shortBinary.replaceAll(" ", "0");
        String binaryChar = shortBinary.substring(12, 16);
        switch (binaryChar) {
            case "0000":
                return ' ';
            case "1000":
                return 'Q';
            case "0100":
                return 'R';
            case "0010":
                return 'K';
            case "0001":
                return 'B';
            case "1111":
                return '0';
            default:
                throw new MoveException("getChar: wrong char encoding");
        }
    }

    @Override
    public void resetEnpassant() {
        enpassant = -1;
    }

    @Override
    public int getEnpassant() {
        return enpassant;
    }

    @Override
    public void setEnpassant(int field) {
        this.enpassant = field;
    }

    @Override
    public void setBlacksTurn(boolean blacksTurn) {
        this.blacksTurn = blacksTurn;
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
        this.parent = parent;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public int getEval() {
        return eval;
    }

    @Override
    public void setEval(int eval) {
        this.eval = eval;
    }

    @Override
    public byte getDepth() {
        return depth;
    }

    @Override
    public void setDepth(byte newDepth) {
        depth = newDepth;
    }

    public int getMaxMin() {
        return maxMin;
    }

    public void setMaxMin(int maxMin) {
        this.maxMin = maxMin;
    }

    @Override
    public synchronized boolean setMaxMinIfChanged(int newValue) {
        if (newValue == maxMin) {
            return false;
        } else {
            maxMin = newValue;
            return true;
        }
    }

    @Override
    public String toString() {
        int from = getFrom();
        int to = getTo();
        char c = getChar();
        String fromS = Translators.translate64ToAlg(from);
        String toS = Translators.translate64ToAlg(to);
        StringBuilder output = new StringBuilder();
        boolean blacksturn = blacksTurn();

        if (c != ' ') {
            if (c == '0') {
                if (blacksturn) {
                    output.append(BLACK_CASTLING_LONG);
                }else{
                    output.append(WHITE_CASTLING_LONG);
                }
            } else {
                output.append(fromS).append(toS).append(c);
            }
        } else {
            if (from == 0 && to == 0) {
                if(blacksturn){
                    output.append(BLACK_CASTLING_SHORT);
                }else{
                    output.append(WHITE_CASTLING_SHORT);
                }
            } else {
                output.append(fromS).append(toS);
            }
        }

        return output.toString();
    }
}
