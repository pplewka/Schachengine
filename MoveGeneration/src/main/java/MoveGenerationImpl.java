import java.util.ArrayList;

public class MoveGenerationImpl implements MoveGeneration{

    @Override
    public ArrayList<Move> generateAllMoves(Move parent,boolean checkAttacks) {
        ArrayList<Move> moves = new ArrayList<>();
        Board board = parent.getBoard();

        for(int i = 26; i < 118; ) {
            Piece temp=board.getPiece(i);

            //most fields are Empty
            if(temp!=Piece.EMPTY) {
                //half of not empty fields are opponents
                if (!board.fieldHasOpponent(i, !parent.blacksTurn())) {
                    switch (temp) {
                        case BPAWN:
                        case WPAWN:
                            generatePawnMoves(parent, i, moves,checkAttacks);
                            break;

                        case BROOK:
                        case WROOK:
                            generateRookMoves(parent, i, moves,checkAttacks);
                            break;

                        case BBISHOP:
                        case WBISHOP:
                            generateBishopMoves(parent, i, moves,checkAttacks);
                            break;

                        case BKNIGHT:
                        case WKNIGHT:
                            generateKnightMoves(parent, i, moves,checkAttacks);
                            break;

                        case BQUEEN:
                        case WQUEEN:
                            generateQueenMoves(parent, i, moves,checkAttacks);
                            break;

                        case BKING:
                        case WKING:
                            generateKingMoves(parent, i, moves,checkAttacks);
                            break;

                        case SPACE:
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

        generateCastling(parent,moves, checkAttacks);
        generateEnpassant(parent,moves, checkAttacks);

        return moves;
    }

    @Override
    public ArrayList<Move> generatePawnMoves(Move parent, int field, ArrayList<Move> moves, boolean checkAttacks) {
        //changing site to move
        boolean blacksTurn = !parent.blacksTurn();
        Board board = parent.getBoard();

        int turn = blacksTurn ? 1 : -1;
        int tempmove;

        //normal forward move
        tempmove = field + (turn * 12);
        if (pawnOnLastPosition(tempmove, blacksTurn)) {
            //todo add correct letters
            //cant use addIfValid because pawns cant go on fields with opponents
            addIfValidPawn(false, board, field, tempmove, 'Q', blacksTurn, -1, moves, checkAttacks);
            addIfValidPawn(false, board, field, tempmove, 'R', blacksTurn, -1, moves, checkAttacks);
            addIfValidPawn(false, board, field, tempmove, 'K', blacksTurn, -1, moves, checkAttacks);
            addIfValidPawn(false, board, field, tempmove, 'B', blacksTurn, -1, moves, checkAttacks);
        } else {
            addIfValidPawn(false, board, field, tempmove, ' ', blacksTurn, -1, moves, checkAttacks);
        }

        //capturing
        tempmove = field + (turn * 11);
        if (pawnOnLastPosition(tempmove, blacksTurn)) {
            //todo add correct letters
            //cant use addIfValid because pawns cant go on fields with opponents
            addIfValidPawn(true, board, field, tempmove, 'Q', blacksTurn, -1, moves, checkAttacks);
            addIfValidPawn(true, board, field, tempmove, 'R', blacksTurn, -1, moves, checkAttacks);
            addIfValidPawn(true, board, field, tempmove, 'K', blacksTurn, -1, moves, checkAttacks);
            addIfValidPawn(true, board, field, tempmove, 'B', blacksTurn, -1, moves, checkAttacks);
        } else {
            addIfValidPawn(true, board, field, tempmove, ' ', blacksTurn, -1, moves, checkAttacks);
        }

        tempmove = field + (turn * 13);
        if (pawnOnLastPosition(tempmove, blacksTurn)) {
            //todo add correct letters
            //cant use addIfValid because pawns cant go on fields with opponents
            addIfValidPawn(true, board, field, tempmove, 'Q', blacksTurn, -1, moves, checkAttacks);
            addIfValidPawn(true, board, field, tempmove, 'R', blacksTurn, -1, moves, checkAttacks);
            addIfValidPawn(true, board, field, tempmove, 'K', blacksTurn, -1, moves, checkAttacks);
            addIfValidPawn(true, board, field, tempmove, 'B', blacksTurn, -1, moves, checkAttacks);
        } else {
            addIfValidPawn(true, board, field, tempmove, ' ', blacksTurn, -1, moves, checkAttacks);
        }


        if (pawnOnFirstPosition(field, blacksTurn)) {
            tempmove = field + (24 * turn);

            int enpassant = tempmove + (turn * -12);

            //cant use addIfValid because pawns cant go on fields with opponents
            addIfValidPawn(false, board, field, tempmove, ' ', blacksTurn, enpassant, moves, checkAttacks);
        }

        //todo switch piece when at the end of board

        return moves;
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
    public ArrayList<Move> generateRookMoves(Move parent, int field, ArrayList<Move> moves,boolean checkAttacks) {
        //changing site to move
        boolean blacksTurn=!parent.blacksTurn();
        Board board=parent.getBoard();

        calculatePathMoves( board, field,  blacksTurn,  -12 , moves,checkAttacks);
        calculatePathMoves( board, field,  blacksTurn,  12 , moves,checkAttacks);
        calculatePathMoves( board, field,  blacksTurn,  -1 , moves,checkAttacks);
        calculatePathMoves( board, field,  blacksTurn,  1 , moves,checkAttacks);
        //todo change bool when moved


        return moves;
    }

    @Override
    public ArrayList<Move> generateBishopMoves(Move parent, int field, ArrayList<Move> moves,boolean checkAttacks) {
        //changing site to move
        boolean blacksTurn=!parent.blacksTurn();
        Board board=parent.getBoard();

        calculatePathMoves( board, field,  blacksTurn,  -13 , moves,checkAttacks);
        calculatePathMoves( board, field,  blacksTurn,  13 , moves,checkAttacks);
        calculatePathMoves( board, field,  blacksTurn,  -11 , moves,checkAttacks);
        calculatePathMoves( board, field,  blacksTurn,  11 , moves,checkAttacks);

        return moves;
    }

    @Override
    public ArrayList<Move> generateKnightMoves(Move parent, int field, ArrayList<Move> moves,boolean checkAttacks) {
        //changing site to move
        boolean blacksTurn=!parent.blacksTurn();

        int [] knightMoves= new int[8];
        knightMoves[0]=field+10;
        knightMoves[1]=field-10;
        knightMoves[2]=field+14;
        knightMoves[3]=field-14;
        knightMoves[4]=field+25;
        knightMoves[5]=field-25;
        knightMoves[6]=field+23;
        knightMoves[7]=field-23;

        for(int temp:knightMoves){
            addIfValid(parent.getBoard(),field,temp,blacksTurn,moves, checkAttacks);
        }

        return moves;
    }

    @Override
    public ArrayList<Move> generateQueenMoves(Move parent, int field, ArrayList<Move> moves,boolean checkAttacks) {
        //not changing site to move because it is changed in the methods below

        generateRookMoves(parent,field,moves,checkAttacks);
        generateBishopMoves(parent,field,moves,checkAttacks);

        return moves;
    }

    @Override
    public ArrayList<Move> generateKingMoves(Move parent, int field, ArrayList<Move> moves,boolean checkAttacks) {
        //changing site to move
        boolean blacksTurn=!parent.blacksTurn();

        int [] kingMoves= new int[8];
        kingMoves[0]=field+12;
        kingMoves[1]=field-12;
        kingMoves[2]=field+1;
        kingMoves[3]=field-1;
        kingMoves[4]=field+13;
        kingMoves[5]=field-13;
        kingMoves[6]=field+11;
        kingMoves[7]=field-11;

        for(int temp:kingMoves){
            addIfValid(parent.getBoard(),field,temp,blacksTurn,moves, checkAttacks);
        }

        //todo change bool when moved

        return moves;
    }

    @Override
    public ArrayList<Move> generateEnpassant(Move parent,ArrayList<Move> moves,boolean checkAttacks) {
        //changing site to move
        boolean blacksTurn=!parent.blacksTurn();
        int enpassant=parent.getEnpassant();
        Board board = parent.getBoard();

        if (enpassant>-1) {
            int possiblePawn1;
            int possiblePawn2;
            Piece tempPawn;

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
                addIfValid(parent.getBoard(),possiblePawn1,enpassant,blacksTurn,moves,checkAttacks);
            }

            if(board.getPiece(possiblePawn2)==tempPawn){
                addIfValid(parent.getBoard(),possiblePawn2,enpassant,blacksTurn,moves,checkAttacks);
            }
        }

        //todo change bool when moved

        return moves;
    }

    @Override
    public ArrayList<Move> generateCastling(Move parent, ArrayList<Move> moves,boolean checkAttacks) {
        //changing site to move
        boolean blacksTurn=!parent.blacksTurn();
        Board board=parent.getBoard();

        if(blacksTurn){
            if(board.castlingPossible(26)&&castlingAttacked(26,board)){
                Board temp= board.copy();
                temp.applyMove(30,28);
                temp.applyMove(26,29);
                //todo add right notation
                moves.add(new MoveImpl(0,0,'0',temp,true));
            }

            if(board.castlingPossible(33)&&castlingAttacked(33,board)){
                Board temp= board.copy();
                temp.applyMove(30,32);
                temp.applyMove(33,31);
                //todo add right notation
                moves.add(new MoveImpl(0,0,'0',temp,true));
            }
        }else{
            if(board.castlingPossible(110)&&castlingAttacked(110,board)){
                Board temp= board.copy();
                temp.applyMove(114,112);
                temp.applyMove(110,113);
                //todo add right notation
                moves.add(new MoveImpl(0,0,'0',temp,false));
            }

            if(board.castlingPossible(117)&&castlingAttacked(117,board)){
                Board temp= board.copy();
                temp.applyMove(114,116);
                temp.applyMove(117,115);
                //todo add right notation
                moves.add(new MoveImpl(0,0,'0',temp,false));
            }
        }

        return moves;
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
     * @return parameter moves with added moves
     */
    private ArrayList<Move> calculatePathMoves(Board board,int field, boolean blacksTurn, int direction ,ArrayList<Move> moves,boolean checkAttacks){
        int temp = field;
        boolean pathBlocked=false;

        while (!pathBlocked) {
            temp = temp + direction;

                                //statement
            pathBlocked = !(  addIfValid(board,field,temp,blacksTurn,moves, checkAttacks));
        }

        return moves;
    }

    @Override
    public boolean kingInCheck(Board board, boolean blacksTurn){
        Piece tempKing;
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
        //set a crash test dummy on field
        Piece tempPawn;
        if(blacksTurn){
            tempPawn=Piece.BPAWN;
        }else{
            tempPawn=Piece.WPAWN;
        }
        Board tempBoard=board.copy();
        tempBoard.setField(tempPawn,field);

        //generate opponents moves (!blacksTurn)
        //overhead: generate moves for trash                                                  !changed here
        ArrayList<Move> tempMoves= generateAllMoves(new MoveImpl(0,0,'0',tempBoard,blacksTurn),false);

        //check if the dummy was hurt in next move
        for (Move tempMove : tempMoves) {
            if (tempMove.getBoard().getPiece(field)!=tempPawn) {
                return true;
            }
        }

        return false;
    }

    private boolean addIfValidPawn(boolean capture,Board board,int from,int to,char c,boolean blacksTurn,int enpassant,ArrayList<Move> moves,boolean checkAttacks){
        if (validPawnMove(capture,board,from,to,blacksTurn,checkAttacks)) {
            moves.add(makeMove(from, to, c, board,blacksTurn,enpassant));
            return true;
        }
        return false;
    }

    private boolean validPawnMove(boolean capture,Board board,int from,int to,boolean blacksTurn,boolean checkAttacks){

        boolean onBoard_AND_kingNotInCheck = onBoard_AND_kingNotInCheck(board,from,to,blacksTurn,checkAttacks);
        boolean fieldValid;

        if(capture){
            fieldValid = board.fieldHasOpponent(to, blacksTurn);
        }else{
            fieldValid = !board.fieldHasOpponent(to, blacksTurn);
        }

        return onBoard_AND_kingNotInCheck && fieldValid;
    }

    private boolean addIfValid(Board board,int from,int to,boolean blacksTurn,ArrayList<Move> moves,boolean checkAttacks){
        if (validMove(board,from,to,blacksTurn, checkAttacks)) {

            //todo correct values for char and enpassant
            moves.add(makeMove(from, to, ' ', board,blacksTurn,-1));
            return true;
        }

        return false;
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
    private boolean validMove(Board board,int from,int to,boolean blacksTurn,boolean checkAttacks){

        boolean onBoard_AND_kingNotInCheck = onBoard_AND_kingNotInCheck(board,from,to,blacksTurn,checkAttacks);
        boolean opponent_OR_free=(!board.fieldIsOccupied(to)||board.fieldHasOpponent(to,blacksTurn));

        return onBoard_AND_kingNotInCheck && (opponent_OR_free);
    }

    private boolean onBoard_AND_kingNotInCheck(Board board,int from,int to,boolean blacksTurn,boolean checkAttacks){
        Board tempboard= board.copy();
        tempboard.applyMove(from,to);

        if(checkAttacks){
            return board.fieldIsOnBoard(to)&&!kingInCheck(tempboard,blacksTurn);
        }else{
            return board.fieldIsOnBoard(to);
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
        temp.applyMove(from,to);
        return new MoveImpl(from,to,c,temp,blacksTurn,enpassant);
    }
}
