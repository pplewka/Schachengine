import java.util.ArrayList;

public class MoveGenerationImpl implements MoveGeneration{
    private static MoveGeneration moveGen;
    private MoveGenerationImpl(){}

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

        for(int i = 26; i < 118; ) {
            byte temp=board.getPiece(i);

            //most fields are Empty
            if(temp!=Piece.EMPTY) {
                //half of not empty fields are opponents
                if (!board.fieldHasOpponent(i, !parent.blacksTurn())) {
                    switch (temp) {
                        case Piece.BPAWN:
                        case Piece.WPAWN:
                            generatePawnMoves(parent, i, moves);
                            break;

                        case Piece.BROOK:
                        case Piece.WROOK:
                            generateRookMoves(parent, i, moves);
                            break;

                        case Piece.BBISHOP:
                        case Piece.WBISHOP:
                            generateBishopMoves(parent, i, moves);
                            break;

                        case Piece.BKNIGHT:
                        case Piece.WKNIGHT:
                            generateKnightMoves(parent, i, moves);
                            break;

                        case Piece.BQUEEN:
                        case Piece.WQUEEN:
                            generateQueenMoves(parent, i, moves);
                            break;

                        case Piece.BKING:
                        case Piece.WKING:
                            generateKingMoves(parent, i, moves);
                            break;

                        case Piece.SPACE:
                            //should never happen
                            throw new IndexOutOfBoundsException("generateAllMoves reached space");
                    }
                }
            }

            i++;
            if ((i + 2) % 12 == 0) {
                i += 4;
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

            if(!board.fieldIsOccupied(enpassant)) {
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
                possiblePawn1=enpassant+13;
                possiblePawn2=enpassant+11;
            }else{
                tempPawn=Piece.WPAWN;
                possiblePawn1=enpassant-13;
                possiblePawn2=enpassant-11;
            }

            //check if there are pawns on this fields
            if(board.getPiece(possiblePawn1)==tempPawn){
                addIfValid(parent.getBoard(),possiblePawn1,enpassant,blacksTurn,enpassantMoves);
            }

            if(board.getPiece(possiblePawn2)==tempPawn){
                addIfValid(parent.getBoard(),possiblePawn2,enpassant,blacksTurn,enpassantMoves);
            }
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
            if((!board.castlingDone(26))&&(!castlingAttacked(26,board))){
                Board temp= board.copy();
                temp.applyMove(30,28);
                temp.applyMove(26,29);
                temp.setbKingMoved(true);
                temp.setbLeftRockMoved(true);

                //todo add right notation
                castlingMoves.add(new MoveImpl(0,0,'0',temp,true));
            }

            if((!board.castlingDone(33))&&(!castlingAttacked(33,board))){
                Board temp= board.copy();
                temp.applyMove(30,32);
                temp.applyMove(33,31);
                temp.setbKingMoved(true);
                temp.setbRightRockMoved(true);

                //todo add right notation
                castlingMoves.add(new MoveImpl(0,0,'0',temp,true));
            }
        }else{
            if((!board.castlingDone(110))&&(!castlingAttacked(110,board))){
                Board temp= board.copy();
                temp.applyMove(114,112);
                temp.applyMove(110,113);
                temp.setwKingMoved(true);
                temp.setwLeftRockMoved(true);

                //todo add right notation
                castlingMoves.add(new MoveImpl(0,0,'0',temp,false));
            }

            if((!board.castlingDone(117))&&(!castlingAttacked(117,board))){
                Board temp= board.copy();
                temp.applyMove(114,116);
                temp.applyMove(117,115);
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

        while (!pathBlocked) {
            temp = temp + direction;

            Move tempMove=addIfValid(board,field,temp,blacksTurn,moves);

            if(tempMove!=null){
                if(board.fieldHasOpponent(temp,blacksTurn)){
                    pathBlocked=true;
                }
                pathmoves.add(tempMove);
            }else{
                pathBlocked =true;
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

        for(int i = 26; i < 118; ) {
            if(board.getPiece(i)==tempKing){
                return fieldUnderAttack(board,i,blacksTurn);
            }

            i++;
            if ((i + 2) % 12 == 0) {
                i += 4;
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
        for(int i:pawns){
            if(board.fieldHasOpponent(i,blacksTurn)){
                byte temp= board.getPiece(i);
                if(temp==pawn){
                    return true;
                }
            }
        }

        int [] kings= {field+12,field-12,field+1,field-1,field+13,field-13,field+11,field-11};
        for(int i:kings){
            if(board.fieldHasOpponent(i,blacksTurn)){
                byte temp= board.getPiece(i);
                if(temp==king){
                    return true;
                }
            }
        }

        int [] knights= {field+10,field-10,field+14,field-14,field+25,field-25,field+23,field-23};
        for(int i:knights){
            if(board.fieldHasOpponent(i,blacksTurn)){
                byte temp= board.getPiece(i);
                if(temp==knight){
                    return true;
                }
            }
        }

        int [] rookDirections={12,-12,1,-1};

        for(int direction:rookDirections) {
            int temp = field;
            boolean pathBlocked = false;
            while (!pathBlocked) {
                temp = temp + direction;

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
                }else if(board.getPiece(temp)==Piece.SPACE){
                    pathBlocked=true;
                }
            }
        }

        int [] bishopDirections={13,-13,11,-11};
        for(int direction:bishopDirections) {
            int temp = field;
            boolean pathBlocked = false;
            while (!pathBlocked) {
                temp = temp + direction;

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
                }else if(board.getPiece(temp)==Piece.SPACE){
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

                switch (c){
                    //todo add Right notation
                    case 'Q':
                        tempBoard.setField(queen,to);
                        break;
                    case 'K':
                        tempBoard.setField(knight,to);
                        break;
                    case 'R':
                        tempBoard.setField(rook,to);
                        break;
                    case 'B':
                        tempBoard.setField(bishop,to);
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

        boolean onBoard_AND_kingNotInCheck = onBoard_AND_kingNotInCheck(board,from,to,blacksTurn);
        boolean fieldValid;

        if(capture){
            fieldValid = board.fieldHasOpponent(to, blacksTurn);
        }else{
            fieldValid = !board.fieldIsOccupied(to);
        }

        return onBoard_AND_kingNotInCheck && fieldValid;
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

        boolean onBoard_AND_kingNotInCheck = onBoard_AND_kingNotInCheck(board,from,to,blacksTurn);
        boolean opponent_OR_free=(!board.fieldIsOccupied(to)||board.fieldHasOpponent(to,blacksTurn));

        return onBoard_AND_kingNotInCheck && (opponent_OR_free);
    }

    private boolean onBoard_AND_kingNotInCheck(Board board,int from,int to,boolean blacksTurn){
        Board tempboard= board.copy();
        tempboard.applyMove(from,to);

            return board.fieldIsOnBoard(to)&&!kingInCheck(tempboard,blacksTurn);
    }

    /**
     *Method to create a new Move
     * will copy the board and apply the from, to values to it before handing it over
     *
     * @param board board before the move
     */
    private Move makeMove(int from, int to,char c,Board board,boolean blacksTurn,int enpassant){
        Board temp = board.copy();
        temp.applyMove(from,to);
        return new MoveImpl(from,to,c,temp,blacksTurn,enpassant);
    }
}
