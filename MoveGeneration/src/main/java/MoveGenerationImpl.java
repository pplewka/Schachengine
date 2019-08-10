import java.util.*;

public class MoveGenerationImpl implements MoveGeneration{
    private static MoveGeneration moveGen;
    private static HashSet<Short> space = new HashSet<>();
    private static byte[] realTranslationTable;
    private MoveGenerationImpl(){}

    static{
        realTranslationTable = new byte[]{26, 27, 28, 29, 30, 31, 32, 33,
                                        38, 39, 40, 41, 42, 43, 44, 45,
                                        50, 51, 52, 53, 54, 55, 56, 57,
                                        62, 63, 64, 65, 66, 67, 68, 69,
                                        74, 75, 76, 77, 78, 79, 80, 81,
                                        86, 87, 88, 89, 90, 91, 92, 93,
                                        98, 99, 100, 101, 102, 103, 104, 105,
                                        110, 111, 112, 113, 114, 115, 116, 117};

        short []shorts = new short[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,
                25,  34,35,36,37,46,47,48,49,58,59,60,61,70,71,72,73,82,83,84,85,94,
                95,96,97,106,107,108,109,118,119,120,121,122,123,124,125,126,127,
                128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143};

        for(short s:shorts){
            space.add(s);
        }
    }

    public static MoveGeneration getMoveGeneration(){
        if(moveGen==null){
            moveGen= new MoveGenerationImpl();
        }
        return moveGen;
    }

    @Override
    public ArrayList<Move> generateAllMoves(Move parent) {
        ArrayList<Move> moves = new ArrayList<>();
        Board board = parent.getBoard();

        for(byte i = 0; i < 64;i++) {
            byte temp=board.getPiece(i);
            byte converted = translateTo144(i);

            //most fields are Empty
            if(temp!=Piece.EMPTY) {
                //half of not empty fields are opponents
                if (!board.fieldHasOpponent(i, !parent.blacksTurn())) {
                    switch (temp) {
                        case Piece.BPAWN:
                        case Piece.WPAWN:
                            generatePawnMoves(parent, converted, moves);
                            break;

                        case Piece.BROOK:
                        case Piece.WROOK:
                            generateRookMoves(parent, converted, moves);
                            break;

                        case Piece.BBISHOP:
                        case Piece.WBISHOP:
                            generateBishopMoves(parent, converted, moves);
                            break;

                        case Piece.BKNIGHT:
                        case Piece.WKNIGHT:
                            generateKnightMoves(parent, converted, moves);
                            break;

                        case Piece.BQUEEN:
                        case Piece.WQUEEN:
                            generateQueenMoves(parent, converted, moves);
                            break;

                        case Piece.BKING:
                        case Piece.WKING:
                            generateKingMoves(parent, converted, moves);
                            break;

                        case Piece.SPACE:
                            //should never happen
                            throw new IndexOutOfBoundsException("generateAllMoves reached space");
                    }
                }
            }
        }


        generateCastling(parent,moves);

        generateEnpassant(parent,moves);

        return moves;
    }

    @Override
    public ArrayList<Move> generatePawnMoves(Move parent, int field, ArrayList<Move> moves) {
        //changing site to move
        boolean blacksTurn = !parent.blacksTurn();
        Board board = parent.getBoard();
        ArrayList<Move> pawnMoves = new ArrayList<>();

        int turn = blacksTurn ? 1 : -1;
        int tempmove;

        //normal forward move
        tempmove = field + (turn * 12);
        if (pawnOnLastPosition(tempmove, blacksTurn)) {
            //todo add correct letters
            //cant use addIfValid because pawns cant go on fields with opponents
            addIfValidPawn(false, board, field, tempmove, 'Q', blacksTurn, -1, pawnMoves);
            addIfValidPawn(false, board, field, tempmove, 'R', blacksTurn, -1, pawnMoves);
            addIfValidPawn(false, board, field, tempmove, 'K', blacksTurn, -1, pawnMoves);
            addIfValidPawn(false, board, field, tempmove, 'B', blacksTurn, -1, pawnMoves);
        } else {
            addIfValidPawn(false, board, field, tempmove, ' ', blacksTurn, -1, pawnMoves);
        }

        //capturing
        tempmove = field + (turn * 11);
        if (pawnOnLastPosition(tempmove, blacksTurn)) {
            //todo add correct letters
            //cant use addIfValid because pawns cant go on fields with opponents
            addIfValidPawn(true, board, field, tempmove, 'Q', blacksTurn, -1, pawnMoves);
            addIfValidPawn(true, board, field, tempmove, 'R', blacksTurn, -1, pawnMoves);
            addIfValidPawn(true, board, field, tempmove, 'K', blacksTurn, -1, pawnMoves);
            addIfValidPawn(true, board, field, tempmove, 'B', blacksTurn, -1, pawnMoves);
        } else {
            addIfValidPawn(true, board, field, tempmove, ' ', blacksTurn, -1, pawnMoves);
        }

        tempmove = field + (turn * 13);
        if (pawnOnLastPosition(tempmove, blacksTurn)) {
            //todo add correct letters
            //cant use addIfValid because pawns cant go on fields with opponents
            addIfValidPawn(true, board, field, tempmove, 'Q', blacksTurn, -1, pawnMoves);
            addIfValidPawn(true, board, field, tempmove, 'R', blacksTurn, -1, pawnMoves);
            addIfValidPawn(true, board, field, tempmove, 'K', blacksTurn, -1, pawnMoves);
            addIfValidPawn(true, board, field, tempmove, 'B', blacksTurn, -1, pawnMoves);
        } else {
            addIfValidPawn(true, board, field, tempmove, ' ', blacksTurn, -1, pawnMoves);
        }

        if (pawnOnFirstPosition(field, blacksTurn)) {
            tempmove = field + (24 * turn);

            int enpassant = tempmove + (turn * -12);

            if(!board.fieldIsOccupied(translateToReal((byte)enpassant))) {
                //cant use addIfValid because pawns cant go on fields with opponents
                addIfValidPawn(false, board, field, tempmove, ' ', blacksTurn, enpassant, pawnMoves);
            }
        }

        moves.addAll(pawnMoves);

        return pawnMoves;
    }

    private boolean pawnOnFirstPosition(int field,boolean blacksTurn){
        if(blacksTurn){
            return field>=38&&field<=45;
        }else{
            return field>=98&&field<=105;
        }
    }

    private boolean pawnOnLastPosition(int field,boolean blacksTurn){
        if(blacksTurn){
            return field>=110&&field<=117;
        }else{
            return field>=26&&field<=33;
        }
    }

    @Override
    public ArrayList<Move> generateRookMoves(Move parent, int field, ArrayList<Move> moves) {
        //changing site to move
        boolean blacksTurn=!parent.blacksTurn();
        Board board=parent.getBoard();
        ArrayList<Move> rookMoves=new ArrayList<>();

        calculatePathMoves( board, field,  blacksTurn,  -12 , rookMoves);
        calculatePathMoves( board, field,  blacksTurn,  12 , rookMoves);
        calculatePathMoves( board, field,  blacksTurn,  -1 , rookMoves);
        calculatePathMoves( board, field,  blacksTurn,  1 , rookMoves);

        //changing the bool
        if(blacksTurn){
            if(field==26) {
                if(!board.isbLeftRockMoved()){
                    rookMoves.forEach(move -> move.getBoard().setbLeftRockMoved(true));
                }
            }else if(field==33){
                if(!board.isbRightRockMoved()){
                    rookMoves.forEach(move -> move.getBoard().setbRightRockMoved(true));
                }
            }
        }else{
            if(field==110) {
                if(!board.iswLeftRockMoved()){
                    rookMoves.forEach(move -> move.getBoard().setwLeftRockMoved(true));
                }
            }else if(field==117){
                if(!board.iswRightRockMoved()){
                    rookMoves.forEach(move -> move.getBoard().setwRightRockMoved(true));
                }

            }
        }

        moves.addAll(rookMoves);

        return rookMoves;
    }

    @Override
    public ArrayList<Move> generateBishopMoves(Move parent, int field, ArrayList<Move> moves) {
        //changing site to move
        boolean blacksTurn=!parent.blacksTurn();
        Board board=parent.getBoard();
        ArrayList<Move> bishopMoves = new ArrayList<>();

        calculatePathMoves( board, field,  blacksTurn,  -13 , bishopMoves);
        calculatePathMoves( board, field,  blacksTurn,  13 , bishopMoves);
        calculatePathMoves( board, field,  blacksTurn,  -11 , bishopMoves);
        calculatePathMoves( board, field,  blacksTurn,  11 , bishopMoves);

        moves.addAll(bishopMoves);

        return bishopMoves;
    }

    @Override
    public ArrayList<Move> generateKnightMoves(Move parent, int field, ArrayList<Move> moves) {
        //changing site to move
        boolean blacksTurn=!parent.blacksTurn();
        ArrayList<Move> knightMoves = new ArrayList<>();

        int [] nextMoves= new int[8];
        nextMoves[0]=field+10;
        nextMoves[1]=field-10;
        nextMoves[2]=field+14;
        nextMoves[3]=field-14;
        nextMoves[4]=field+25;
        nextMoves[5]=field-25;
        nextMoves[6]=field+23;
        nextMoves[7]=field-23;

        for(int nextField:nextMoves){
            addIfValid(parent.getBoard(),field,nextField,blacksTurn,knightMoves);
        }

        moves.addAll(knightMoves);

        return knightMoves;
    }

    @Override
    public ArrayList<Move> generateQueenMoves(Move parent, int field, ArrayList<Move> moves) {
        Board board = parent.getBoard();
        boolean blacksTurn = !parent.blacksTurn();
        ArrayList<Move> queenMoves = new ArrayList<>();

        //bishop moves
        calculatePathMoves( board, field,  blacksTurn,  -13 , queenMoves);
        calculatePathMoves( board, field,  blacksTurn,  13 , queenMoves);
        calculatePathMoves( board, field,  blacksTurn,  -11 , queenMoves);
        calculatePathMoves( board, field,  blacksTurn,  11 , queenMoves);

        //rook moves
        calculatePathMoves( board, field,  blacksTurn,  -12 , queenMoves);
        calculatePathMoves( board, field,  blacksTurn,  12 , queenMoves);
        calculatePathMoves( board, field,  blacksTurn,  -1 , queenMoves);
        calculatePathMoves( board, field,  blacksTurn,  1 , queenMoves);

        moves.addAll(queenMoves);

        return queenMoves;
    }

    @Override
    public ArrayList<Move> generateKingMoves(Move parent, int field, ArrayList<Move> moves) {
        //changing site to move
        boolean blacksTurn=!parent.blacksTurn();
        ArrayList<Move> kingMoves= new ArrayList<>();
        Board parentBoard= parent.getBoard();

        int [] nextMoves= new int[8];
        nextMoves[0]=field+12;
        nextMoves[1]=field-12;
        nextMoves[2]=field+1;
        nextMoves[3]=field-1;
        nextMoves[4]=field+13;
        nextMoves[5]=field-13;
        nextMoves[6]=field+11;
        nextMoves[7]=field-11;

        for(int nextField:nextMoves){
            addIfValid(parentBoard,field,nextField,blacksTurn,kingMoves);
        }

        //changing the bool
        if(kingMoves.size()>0) {
            if (!parentBoard.isbKingMoved()) {
                kingMoves.forEach(move -> move.getBoard().setbKingMoved(true));
            } else if (!parentBoard.iswKingMoved()) {
                kingMoves.forEach(move -> move.getBoard().setwKingMoved(true));
            }
        }

        moves.addAll(kingMoves);

        return kingMoves;
    }

    @Override
    public ArrayList<Move> generateEnpassant(Move parent,ArrayList<Move> moves) {
        //changing site to move
        boolean blacksTurn=!parent.blacksTurn();
        int enpassant=parent.getEnpassant();
        Board board = parent.getBoard();
        ArrayList<Move> enpassantMoves = new ArrayList<>();

        if (enpassant>-1) {
            int possiblePawn1;
            int possiblePawn2;
            byte tempPawn;

            if(blacksTurn){
                tempPawn=Piece.BPAWN;
                possiblePawn1=enpassant-13;
                possiblePawn2=enpassant-11;
            }else{
                tempPawn=Piece.WPAWN;
                possiblePawn1=enpassant+13;
                possiblePawn2=enpassant+11;
            }

            //check if there are pawns on this fields
            if(!isSpace(possiblePawn1)) {
                if (board.getPiece(translateToReal((byte) possiblePawn1)) == tempPawn) {
                    addIfValid(parent.getBoard(), possiblePawn1, enpassant, blacksTurn, enpassantMoves);
                }
            }

            if(!isSpace(possiblePawn2)) {
                if (board.getPiece(translateToReal((byte) possiblePawn2)) == tempPawn) {
                    addIfValid(parent.getBoard(), possiblePawn2, enpassant, blacksTurn, enpassantMoves);
                }
            }
        }

        //removing pawn
        int toRemove=blacksTurn? enpassant-12:enpassant+12;
        for(Move enpm: enpassantMoves){
            enpm.getBoard().setField(Piece.EMPTY,translateToReal((byte)toRemove));
        }
        moves.addAll(enpassantMoves);

        return enpassantMoves;
    }

    @Override
    public ArrayList<Move> generateCastling(Move parent, ArrayList<Move> moves) {
        //changing site to move
        boolean blacksTurn=!parent.blacksTurn();
        Board board=parent.getBoard();
        ArrayList<Move> castlingMoves = new ArrayList<>();

        if(blacksTurn){
            if((!board.castlingDone(0))&&(!castlingAttacked(26,board))){
                Board temp= board.copy();
                temp.applyMove(4,2);
                temp.applyMove(0,3);
                temp.setbKingMoved(true);
                temp.setbLeftRockMoved(true);

                //todo add right notation
                castlingMoves.add(new MoveImpl(0,0,'0',temp,true));
            }

            if((!board.castlingDone(7))&&(!castlingAttacked(33,board))){
                Board temp= board.copy();
                temp.applyMove(4,6);
                temp.applyMove(7,5);
                temp.setbKingMoved(true);
                temp.setbRightRockMoved(true);

                //todo add right notation
                castlingMoves.add(new MoveImpl(0,0,'0',temp,true));
            }
        }else{
            if((!board.castlingDone(56))&&(!castlingAttacked(110,board))){
                Board temp= board.copy();
                temp.applyMove(60,58);
                temp.applyMove(56,59);
                temp.setwKingMoved(true);
                temp.setwLeftRockMoved(true);

                //todo add right notation
                castlingMoves.add(new MoveImpl(0,0,'0',temp,false));
            }

            if((!board.castlingDone(63))&&(!castlingAttacked(117,board))){
                Board temp= board.copy();
                temp.applyMove(60,62);
                temp.applyMove(63,61);
                temp.setwKingMoved(true);
                temp.setwRightRockMoved(true);

                //todo add right notation
                castlingMoves.add(new MoveImpl(0,0,'0',temp,false));
            }
        }

        moves.addAll(castlingMoves);

        return castlingMoves;
    }

    private boolean castlingAttacked(int rookPosition,Board board){
        switch(rookPosition){
            case 26:
                return fieldUnderAttack(board,28,true)
                        ||fieldUnderAttack(board,29,true)
                        ||fieldUnderAttack(board,30,true);

            case 110:
                return fieldUnderAttack(board,112,false)
                        ||fieldUnderAttack(board,113,false)
                        ||fieldUnderAttack(board,114,false);

            case 33:
                return fieldUnderAttack(board,32,true)
                        ||fieldUnderAttack(board,31,true)
                        ||fieldUnderAttack(board,30,true);

            case 117:
                return fieldUnderAttack(board,114,false)
                        ||fieldUnderAttack(board,115,false)
                        ||fieldUnderAttack(board,116,false);

            default:
                throw new IllegalArgumentException("castlingAttacked: wrong position");
        }
    }

    /**
     * method to calculate path moves for rook and
     * !!does not check for wrong directions!!
     *
     * @param board board to calculate on
     * @param field field of the rook to calculate
     * @param blacksTurn site to check for obstacles or opponents
     * @param direction rook: -12 is up. +12 is down. +1 is right. -1 is left
     *                  bishop: -11 is up/right. -13 is up/left. +11 is down/left. +13 is down/right
     * @param moves ArrayList to append moves
     * @return all moves generated in this method
     */
    private ArrayList<Move> calculatePathMoves(Board board,int field, boolean blacksTurn, int direction ,ArrayList<Move> moves){
        int temp = field;
        boolean pathBlocked=false;
        ArrayList<Move> pathmoves= new ArrayList<>();
        ArrayList<Integer> possiblePositions= new ArrayList<>();

        while (!pathBlocked) {
            temp = temp + direction;


            if (isSpace(temp)) {
                pathBlocked = true;

            } else {
                byte real=translateToReal((byte)temp);
                if (board.fieldIsOccupied(real)) {
                    if (board.fieldHasOpponent(real, blacksTurn)) {
                        pathBlocked = true;
                        possiblePositions.add(temp);
                    } else {
                        pathBlocked = true;
                    }

                } else {
                    possiblePositions.add(temp);
                }
            }
        }


        for(int i:possiblePositions){
            Move tempMove=addIfValid(board,field,i,blacksTurn,moves);
            if(tempMove!=null){
                pathmoves.add(tempMove);
            }
        }

        return pathmoves;
    }

    @Override
    public boolean kingInCheck(Board board, boolean blacksTurn){
        byte tempKing;
        if(blacksTurn){
            tempKing=Piece.BKING;
        }else{
            tempKing=Piece.WKING;
        }

        for(int i = 0; i < 64; i++) {
            if(board.getPiece(i)==tempKing){
                return fieldUnderAttack(board,translateTo144((byte)i),blacksTurn);
            }
        }

        //todo
        //no king==not in check??
        return false;
    }

    @Override
    public boolean fieldUnderAttack(Board board,int field, boolean blacksTurn) {
        byte pawn = blacksTurn? Piece.WPAWN : Piece.BPAWN ;
        byte king = blacksTurn? Piece.WKING : Piece.BKING ;
        byte queen = blacksTurn? Piece.WQUEEN : Piece.BQUEEN ;
        byte knight = blacksTurn? Piece.WKNIGHT : Piece.BKNIGHT ;
        byte bishop = blacksTurn? Piece.WBISHOP : Piece.BBISHOP ;
        byte rook = blacksTurn? Piece.WROOK : Piece.BROOK ;

        int [] pawns={blacksTurn? field+11 : field-11   ,  blacksTurn? field+13 : field-13};
        for(int j:pawns){
            if(!isSpace(j)) {
                int i = translateToReal((byte) j);
                if (board.fieldHasOpponent(i, blacksTurn)) {
                    byte temp = board.getPiece(i);
                    if (temp == pawn) {
                        return true;
                    }
                }
            }
        }

        int [] kings= {field+12,field-12,field+1,field-1,field+13,field-13,field+11,field-11};
        for(int j:kings){
            if(!isSpace(j)) {
                int i = translateToReal((byte) j);
                if (board.fieldHasOpponent(i, blacksTurn)) {
                    byte temp = board.getPiece(i);
                    if (temp == king) {
                        return true;
                    }
                }
            }
        }

        int [] knights= {field+10,field-10,field+14,field-14,field+25,field-25,field+23,field-23};
        for(int j:knights){
            if(!isSpace(j)) {
                int i = translateToReal((byte) j);
                if (board.fieldHasOpponent(i, blacksTurn)) {
                    byte temp = board.getPiece(i);
                    if (temp == knight) {
                        return true;
                    }
                }
            }
        }

        int [] rookDirections={12,-12,1,-1};

        for(int direction:rookDirections) {
            int temp1 = field;
            boolean pathBlocked = false;
            while (!pathBlocked) {
                temp1 = temp1 + direction;
                if(!isSpace(temp1)) {
                    byte temp = translateToReal((byte) temp1);

                    if (board.fieldIsOccupied(temp)) {
                        if (board.fieldHasOpponent(temp, blacksTurn)) {
                            byte tempPiece = board.getPiece(temp);
                            if (tempPiece == rook || tempPiece == queen) {
                                return true;
                            } else {
                                pathBlocked = true;
                            }
                        } else {
                            pathBlocked = true;
                        }
                    }
                }else{
                    pathBlocked=true;
                }
            }
        }

        int [] bishopDirections={13,-13,11,-11};
        for(int direction:bishopDirections) {
            int temp1 = field;
            boolean pathBlocked = false;
            while (!pathBlocked) {
                temp1 = temp1 + direction;

                if(!isSpace(temp1)) {
                    byte temp = translateToReal((byte) temp1);

                    if (board.fieldIsOccupied(temp)) {
                        if (board.fieldHasOpponent(temp, blacksTurn)) {
                            byte tempPiece = board.getPiece(temp);
                            if (tempPiece == bishop || tempPiece == queen) {
                                return true;
                            } else {
                                pathBlocked = true;
                            }
                        } else {
                            pathBlocked = true;
                        }
                    }
                }else{
                    pathBlocked=true;
                }
            }
        }

        return false;
    }


    private Move addIfValidPawn(boolean capture,Board board,int from,int to,char c,boolean blacksTurn,int enpassant,ArrayList<Move> moves){
        if (validPawnMove(capture,board,from,to,blacksTurn)) {
            Move temp=makeMove(from, to, c, board,blacksTurn,enpassant);
            Board tempBoard = temp.getBoard();
            if(c!=' '){
                byte queen = blacksTurn? Piece.BQUEEN:Piece.WQUEEN;
                byte knight = blacksTurn? Piece.BKNIGHT:Piece.WKNIGHT;
                byte rook = blacksTurn? Piece.BROOK:Piece.WROOK;
                byte bishop = blacksTurn? Piece.BBISHOP:Piece.WBISHOP;
                int rto = translateToReal((byte)to);

                switch (c){
                    //todo add Right notation
                    case 'Q':
                        tempBoard.setField(queen,rto);
                        break;
                    case 'K':
                        tempBoard.setField(knight,rto);
                        break;
                    case 'R':
                        tempBoard.setField(rook,rto);
                        break;
                    case 'B':
                        tempBoard.setField(bishop,rto);
                        break;
                    default:
                        throw new IllegalArgumentException("invalid byte in Move");
                }
            }

            moves.add(temp);
            return temp;
        }
        return null;
    }

    private boolean validPawnMove(boolean capture,Board board,int from,int to,boolean blacksTurn){

        if(onBoard_AND_kingNotInCheck(board,from,to,blacksTurn)){
            boolean fieldValid;

            if(capture){
                fieldValid = board.fieldHasOpponent(translateToReal((byte)to), blacksTurn);
            }else{
                fieldValid = !board.fieldIsOccupied(translateToReal((byte)to));
            }

            return fieldValid;
        }else{
            return false;
        }
    }

    /**
     * @param board board to apply the move to
     * @param from from field
     * @param to to field
     * @param blacksTurn site to move
     * @param moves the collection to add the move
     * @return if the move is valid, the equivalent Move object. else null
     */
    private Move addIfValid(Board board,int from,int to,boolean blacksTurn,ArrayList<Move> moves){
        if (validMove(board,from,to,blacksTurn)) {

            //todo correct values for byte and enpassant
            Move temp=makeMove(from, to, ' ', board,blacksTurn,-1);
            moves.add(temp);
            return temp;
        }

        return null;
    }

    /**
     * checks for
     * -on Board
     * -king in check
     * -field free or opponent
     *
     * does not check if individual pieces move right
     * or if from has an valid piece
     */
    private boolean validMove(Board board,int from,int to,boolean blacksTurn){

        if(onBoard_AND_kingNotInCheck(board,from,to,blacksTurn)){
            boolean opponent_OR_free=(!board.fieldIsOccupied(translateToReal((byte)to))
                    ||board.fieldHasOpponent(translateToReal((byte)to),blacksTurn));

            return opponent_OR_free;
        }else{
            return false;
        }
    }

    private boolean onBoard_AND_kingNotInCheck(Board board,int from,int to,boolean blacksTurn){
        if(!isSpace(to)){
            Board tempboard= board.copy();
            tempboard.applyMove(translateToReal((byte)from),translateToReal((byte)to));
            return !kingInCheck(tempboard,blacksTurn);
        }else{
            return false;
        }
    }

    /**
     *Method to create a new Move
     * will copy the board and apply the from, to values to it before handing it over
     *
     * @param board board before the move
     */
    private Move makeMove(int from, int to,char c,Board board,boolean blacksTurn,int enpassant){
        Board temp = board.copy();
        int rFrom=translateToReal((byte)from);
        int rTo=translateToReal((byte)to);
        temp.applyMove(rFrom,rTo);
        return new MoveImpl(rFrom,rTo,c,temp,blacksTurn,enpassant);
    }

    private boolean isSpace(int field){
        return space.contains((short) field);
    }

    private byte translateToReal(byte calculated){
        int index = Arrays.binarySearch(realTranslationTable,calculated);
        if(index<0){
            throw new RuntimeException("wrong parameter: translateToReal");
        }else{
            return (byte)index;
        }
    }

    private byte translateTo144(byte field){
        try{
            return realTranslationTable[field];
        }catch (IndexOutOfBoundsException   e){
            throw new IndexOutOfBoundsException("wrong parameter: translateTo144");
        }
    }
}
