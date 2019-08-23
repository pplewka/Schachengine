import java.util.*;

public class MoveGenerationImpl implements MoveGeneration {
    private static MoveGeneration moveGen;
    private static HashSet<Short> space = new HashSet<>();

    private MoveGenerationImpl() {
    }

    static {
        short[] shorts = new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
                25, 34, 35, 36, 37, 46, 47, 48, 49, 58, 59, 60, 61, 70, 71, 72, 73, 82, 83, 84, 85, 94,
                95, 96, 97, 106, 107, 108, 109, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127,
                128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143};
        for (short s : shorts) {
            space.add(s);
        }
    }

    public static MoveGeneration getMoveGeneration() {
        if (moveGen == null) {
            moveGen = new MoveGenerationImpl();
        }
        return moveGen;
    }

    /**
     * @throws MoveGenerationException if a field on board is Piece.SPACE
     */
    @Override
    public ArrayList<Move> generateAllMoves(Move parent) {
        ArrayList<Move> moves = new ArrayList<>();
        Board board = parent.getBoard();

        for (byte i = 0; i < 64; i++) {
            byte temp = board.getPiece(i);
            byte converted =(byte) Translators.translate64To144(i);

            //most fields are Empty
            if (temp != Piece.EMPTY) {
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
                            throw new MoveGenerationException("generateAllMoves: reached space");
                    }
                }
            }
        }


        generateCastling(parent, moves);

        generateEnpassant(parent, moves);

        for(Move m : moves){
            m.setParent(parent);
            m.setDepth((byte)(parent.getDepth()+1));
        }

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

        for (int i : new int[]{11, 13}) {
            tempmove = field + (turn * i);
            if (pawnOnLastPosition(tempmove, blacksTurn)) {
                //todo add correct letters
                //cant use addIfValid because pawns cant go on fields with opponents
                for (char c : new char[]{'Q', 'R', 'K', 'B'}) {
                    addIfValidPawn(true, board, field, tempmove, c, blacksTurn, -1, pawnMoves);
                }
            } else {
                addIfValidPawn(true, board, field, tempmove, ' ', blacksTurn, -1, pawnMoves);
            }
        }

        tempmove = field + (turn * 12);
        if (pawnOnLastPosition(tempmove, blacksTurn)) {
            //todo add correct letters
            //cant use addIfValid because pawns cant go on fields with opponents
            for (char c : new char[]{'Q', 'R', 'K', 'B'}) {
                addIfValidPawn(false, board, field, tempmove, c, blacksTurn, -1, pawnMoves);
            }
        } else {
            addIfValidPawn(false, board, field, tempmove, ' ', blacksTurn, -1, pawnMoves);
        }

        if (pawnOnFirstPosition(field, blacksTurn)) {
            tempmove = field + (24 * turn);

            int enpassant = tempmove + (turn * -12);

            if (!board.fieldIsOccupied(Translators.translate144to64((byte) enpassant))) {
                //cant use addIfValid because pawns cant go on fields with opponents
                addIfValidPawn(false, board, field, tempmove, ' ', blacksTurn, enpassant, pawnMoves);
            }
        }

        moves.addAll(pawnMoves);

        return pawnMoves;
    }

    @Override
    public ArrayList<Move> generateKnightMoves(Move parent, int field, ArrayList<Move> moves) {
        //changing site to move
        boolean blacksTurn = !parent.blacksTurn();
        ArrayList<Move> knightMoves = new ArrayList<>();

        int[] nextMoves = new int[]{10, -10, 14, -14, 25, -25, 23, -23};
        for (int i = 0; i < nextMoves.length; i++) {
            nextMoves[i] += field;
            addIfValid(parent.getBoard(), field, nextMoves[i], blacksTurn, knightMoves);
        }

        moves.addAll(knightMoves);

        return knightMoves;
    }

    @Override
    public ArrayList<Move> generateKingMoves(Move parent, int field, ArrayList<Move> moves) {
        //changing site to move
        boolean blacksTurn = !parent.blacksTurn();
        ArrayList<Move> kingMoves = new ArrayList<>();
        Board parentBoard = parent.getBoard();

        int[] nextMoves = new int[]{12, -12, 1, -1, 13, -13, 11, -11};
        for (int i = 0; i < nextMoves.length; i++) {
            nextMoves[i] += field;
            addIfValid(parentBoard, field, nextMoves[i], blacksTurn, kingMoves);
        }

        //changing the bool
        if (kingMoves.size() > 0) {
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
    public ArrayList<Move> generateRookMoves(Move parent, int field, ArrayList<Move> moves) {
        //changing site to move
        boolean blacksTurn = !parent.blacksTurn();
        Board board = parent.getBoard();
        ArrayList<Move> rookMoves = new ArrayList<>();

        for (int direction : new int[]{-12, 12, -1, 1}) {
            calculatePathMoves(board, field, blacksTurn, direction, rookMoves);
        }

        //changing the bool
        if (blacksTurn) {
            if (field == 26) {
                if (!board.isbLeftRockMoved()) {
                    rookMoves.forEach(move -> move.getBoard().setbLeftRockMoved(true));
                }
            } else if (field == 33) {
                if (!board.isbRightRockMoved()) {
                    rookMoves.forEach(move -> move.getBoard().setbRightRockMoved(true));
                }
            }
        } else {
            if (field == 110) {
                if (!board.iswLeftRockMoved()) {
                    rookMoves.forEach(move -> move.getBoard().setwLeftRockMoved(true));
                }
            } else if (field == 117) {
                if (!board.iswRightRockMoved()) {
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
        boolean blacksTurn = !parent.blacksTurn();
        Board board = parent.getBoard();
        ArrayList<Move> bishopMoves = new ArrayList<>();
        for (int direction : new int[]{-13, 13, -11, 11}) {
            calculatePathMoves(board, field, blacksTurn, direction, bishopMoves);
        }

        moves.addAll(bishopMoves);

        return bishopMoves;
    }

    @Override
    public ArrayList<Move> generateQueenMoves(Move parent, int field, ArrayList<Move> moves) {
        Board board = parent.getBoard();
        boolean blacksTurn = !parent.blacksTurn();
        ArrayList<Move> queenMoves = new ArrayList<>();

        for (int direction : new int[]{-13, 13, -11, 11, -12, 12, -1, 1}) {
            calculatePathMoves(board, field, blacksTurn, direction, queenMoves);
        }
        moves.addAll(queenMoves);

        return queenMoves;
    }

    /**
     * method to calculate path moves for rook, bishop and queen
     * !!does not check for wrong directions!!
     *
     * @param board      board to calculate on
     * @param field      must be in 144 format. field of the rook to calculate
     * @param blacksTurn site to check for obstacles or opponents
     * @param direction  rook: -12 is up. +12 is down. +1 is right. -1 is left
     *                   bishop: -11 is up/right. -13 is up/left. +11 is down/left. +13 is down/right
     * @param moves      ArrayList to append moves to
     * @return moves generated in this method
     */
    private ArrayList<Move> calculatePathMoves(Board board, int field, boolean blacksTurn, int direction, ArrayList<Move> moves) {
        int temp = field;
        boolean pathBlocked = false;
        ArrayList<Move> pathmoves = new ArrayList<>();
        ArrayList<Integer> possiblePositions = new ArrayList<>();

        while (!pathBlocked) {
            temp += direction;

            if (isSpace(temp)) {
                pathBlocked = true;

            } else {
                byte real =(byte) Translators.translate144to64(temp);
                if (board.fieldIsOccupied(real)) {
                    if (board.fieldHasOpponent(real, blacksTurn)) {
                        possiblePositions.add(temp);
                    }
                    pathBlocked = true;
                } else {
                    possiblePositions.add(temp);
                }
            }
        }

        for (int i : possiblePositions) {
            addIfValid(board, field, i, blacksTurn, pathmoves);
        }

        moves.addAll(pathmoves);

        return pathmoves;
    }

    @Override
    public ArrayList<Move> generateEnpassant(Move parent, ArrayList<Move> moves) {
        //changing site to move
        boolean blacksTurn = !parent.blacksTurn();
        int enpassant = parent.getEnpassant();
        Board board = parent.getBoard();
        ArrayList<Move> enpassantMoves = new ArrayList<>();
        int turn = blacksTurn ? -1 : 1;

        if (enpassant > -1) {

            byte tempPawn = blacksTurn ? Piece.BPAWN : Piece.WPAWN;

            //check if there are pawns on this fields
            for (int possiblePawn : new int[]{enpassant + (turn * 13), enpassant + (turn * 11)}) {
                if (!isSpace(possiblePawn)) {
                    if (board.getPiece(Translators.translate144to64((byte) possiblePawn)) == tempPawn) {
                        addIfValid(parent.getBoard(), possiblePawn, enpassant, blacksTurn, enpassantMoves);
                    }
                }
            }
        }

        //removing pawn
        int toRemove = enpassant + (turn * 12);
        for (Move enpm : enpassantMoves) {
            enpm.getBoard().setField(Piece.EMPTY, Translators.translate144to64((byte) toRemove));
        }
        moves.addAll(enpassantMoves);

        return enpassantMoves;
    }

    @Override
    public ArrayList<Move> generateCastling(Move parent, ArrayList<Move> moves) {
        //changing site to move
        boolean blacksTurn = !parent.blacksTurn();
        Board board = parent.getBoard();
        ArrayList<Move> castlingMoves = new ArrayList<>();

        if (blacksTurn) {
            if ((!board.castlingDone(0)) && (!castlingAttacked(26, board))) {
                Board temp = board.copy();
                temp.applyMove(4, 2);
                temp.applyMove(0, 3);
                temp.setbKingMoved(true);
                temp.setbLeftRockMoved(true);

                castlingMoves.add(new MoveImpl(0, 0, '0', temp, true));
            }

            if ((!board.castlingDone(7)) && (!castlingAttacked(33, board))) {
                Board temp = board.copy();
                temp.applyMove(4, 6);
                temp.applyMove(7, 5);
                temp.setbKingMoved(true);
                temp.setbRightRockMoved(true);

                castlingMoves.add(new MoveImpl(0, 0, ' ', temp, true));
            }
        } else {
            if ((!board.castlingDone(56)) && (!castlingAttacked(110, board))) {
                Board temp = board.copy();
                temp.applyMove(60, 58);
                temp.applyMove(56, 59);
                temp.setwKingMoved(true);
                temp.setwLeftRockMoved(true);

                castlingMoves.add(new MoveImpl(0, 0, '0', temp, false));
            }

            if ((!board.castlingDone(63)) && (!castlingAttacked(117, board))) {
                Board temp = board.copy();
                temp.applyMove(60, 62);
                temp.applyMove(63, 61);
                temp.setwKingMoved(true);
                temp.setwRightRockMoved(true);

                castlingMoves.add(new MoveImpl(0, 0, ' ', temp, false));
            }
        }

        moves.addAll(castlingMoves);

        return castlingMoves;
    }

    /**
     * Method to check if fields of a castling are attacked
     *
     * @param rookPosition must be in 144 format. position of a Rook to castle with. must be 26, 110, 33 or 117
     * @param board        to check on
     * @return true if the specified castling is attacked
     *
     * @throws MoveGenerationException if rookPosition is invalid
     */
    private boolean castlingAttacked(int rookPosition, Board board) {
        switch (rookPosition) {
            case 26:
                return fieldUnderAttack(board, 28, true)
                        || fieldUnderAttack(board, 29, true)
                        || fieldUnderAttack(board, 30, true);

            case 110:
                return fieldUnderAttack(board, 112, false)
                        || fieldUnderAttack(board, 113, false)
                        || fieldUnderAttack(board, 114, false);

            case 33:
                return fieldUnderAttack(board, 32, true)
                        || fieldUnderAttack(board, 31, true)
                        || fieldUnderAttack(board, 30, true);

            case 117:
                return fieldUnderAttack(board, 114, false)
                        || fieldUnderAttack(board, 115, false)
                        || fieldUnderAttack(board, 116, false);

            default:
                throw new MoveGenerationException("castlingAttacked: wrong position");
        }
    }

    @Override
    public boolean kingInCheck(Board board, boolean blacksTurn) {
        byte tempKing = blacksTurn ? Piece.BKING : Piece.WKING;

        for (int i = 0; i < 64; i++) {
            if (board.getPiece(i) == tempKing) {
                return fieldUnderAttack(board, Translators.translate64To144((byte) i), blacksTurn);
            }
        }

        //todo
        //no king==not in check??
        return false;
    }

    @Override
    public boolean fieldUnderAttack(Board board, int field, boolean blacksTurn) {
        byte pawn = blacksTurn ? Piece.WPAWN : Piece.BPAWN;
        byte king = blacksTurn ? Piece.WKING : Piece.BKING;
        byte queen = blacksTurn ? Piece.WQUEEN : Piece.BQUEEN;
        byte knight = blacksTurn ? Piece.WKNIGHT : Piece.BKNIGHT;
        byte bishop = blacksTurn ? Piece.WBISHOP : Piece.BBISHOP;
        byte rook = blacksTurn ? Piece.WROOK : Piece.BROOK;

        int[][] directionsPawnsKingsKnights = new int[][]{
                {blacksTurn ? field + 11 : field - 11, blacksTurn ? field + 13 : field - 13},
                {field + 12, field - 12, field + 1, field - 1, field + 13, field - 13, field + 11, field - 11},
                {field + 10, field - 10, field + 14, field - 14, field + 25, field - 25, field + 23, field - 23}};

        int[] pieces = new int[]{pawn, king, knight};

        for (int n = 0; n < directionsPawnsKingsKnights.length; n++) {
            int[] pieceMoves = directionsPawnsKingsKnights[n];
            int piece = pieces[n];

            for (int j : pieceMoves) {
                if (!isSpace(j)) {
                    int i = Translators.translate144to64((byte) j);
                    if (board.fieldHasOpponent(i, blacksTurn)) {
                        if (board.getPiece(i) == piece) {
                            return true;
                        }
                    }
                }
            }
        }

        int[][] directionsRookBishop = new int[][]{{12, -12, 1, -1}, {13, -13, 11, -11}};
        pieces = new int[]{rook, bishop};

        for (int i = 0; i < directionsRookBishop.length; i++) {
            int[] Directions = directionsRookBishop[i];
            for (int direction : Directions) {
                int temp1 = field;
                boolean pathBlocked = false;
                while (!pathBlocked) {
                    temp1 = temp1 + direction;
                    if (!isSpace(temp1)) {
                        byte temp =(byte) Translators.translate144to64(temp1);

                        if (board.fieldIsOccupied(temp)) {
                            if (board.fieldHasOpponent(temp, blacksTurn)) {
                                byte tempPiece = board.getPiece(temp);
                                if (tempPiece == pieces[i] || tempPiece == queen) {
                                    return true;
                                } else {
                                    pathBlocked = true;
                                }
                            } else {
                                pathBlocked = true;
                            }
                        }
                    } else {
                        pathBlocked = true;
                    }
                }
            }
        }

        return false;
    }


    //Utils

    /**
     * @param capture    whether the pawn on from is captureing
     * @param board      board to check on if the move is valid
     * @param from       must be in 144 format. position where the pawn is
     * @param to         must be in 144 format. position where the pawn will be
     * @param c          char to specify to which piece the pawn will promote or '<space>' if dont
     * @param blacksTurn
     * @param enpassant  enpassant field. if not present -1, must be in 144 format
     * @param moves      ArrayList to add the move on if is valid
     * @return a valid Move or null
     *
     * @throws MoveGenerationException if c is invalid
     */
    private Move addIfValidPawn(boolean capture, Board board, int from, int to, char c, boolean blacksTurn, int enpassant, ArrayList<Move> moves) {
        if (validPawnMove(capture, board, from, to, blacksTurn)) {
            Move temp = makeMove(from, to, c, board, blacksTurn, enpassant);
            Board tempBoard = temp.getBoard();
            if (c != ' ') {

                byte queen = blacksTurn ? Piece.BQUEEN : Piece.WQUEEN;
                byte knight = blacksTurn ? Piece.BKNIGHT : Piece.WKNIGHT;
                byte rook = blacksTurn ? Piece.BROOK : Piece.WROOK;
                byte bishop = blacksTurn ? Piece.BBISHOP : Piece.WBISHOP;
                int rto = Translators.translate144to64((byte) to);
                Map<Character, Byte> switchMap = Map.of('Q', queen, 'K', knight, 'R', rook, 'B', bishop);
                if (!switchMap.containsKey(c)) {
                    throw new MoveGenerationException("addIfValidPawn: invalid char in Move");
                }
                tempBoard.setField(switchMap.get(c), rto);
            }

            moves.add(temp);
            return temp;
        }
        return null;
    }

    /**
     * checks if -to is on Board -king not in check -field is free or has a opponent
     * does not check if individual pieces move right or if from has an valid piece
     *
     * @param capture whether the pawn on from is captureing
     * @param board   board to check on if the move is valid
     * @param from    must be in 144 format. position where the pawn is
     * @param to      must be in 144 format. position where the pawn will be
     * @return true if valid, else false
     */
    private boolean validPawnMove(boolean capture, Board board, int from, int to, boolean blacksTurn) {

        if (onBoard_AND_kingNotInCheck(board, from, to, blacksTurn)) {
            boolean fieldValid;

            if (capture) {
                fieldValid = board.fieldHasOpponent(Translators.translate144to64((byte) to), blacksTurn);
            } else {
                fieldValid = !board.fieldIsOccupied(Translators.translate144to64((byte) to));
            }

            return fieldValid;
        } else {
            return false;
        }
    }

    /**
     * @param board      board to check on
     * @param from       must be in 144 format. field where the piece is
     * @param to         must be in 144 format. field where the piece will be
     * @param blacksTurn site to move
     * @param moves      the collection to add the move
     * @return a valid Move or null
     */
    private Move addIfValid(Board board, int from, int to, boolean blacksTurn, ArrayList<Move> moves) {
        if (validMove(board, from, to, blacksTurn)) {

            //todo correct values for byte and enpassant
            Move temp = makeMove(from, to, ' ', board, blacksTurn, -1);
            moves.add(temp);
            return temp;
        }

        return null;
    }

    /**
     * checks if -to is on Board -king not in check -field is free or has a opponent
     * does not check if individual pieces move right or if from has an valid piece
     *
     * @param board board to check on if the move is valid
     * @param from  must be in 144 format. position where the piece is
     * @param to    must be in 144 format. position where the piece will be
     * @return true if valid, else false
     */
    private boolean validMove(Board board, int from, int to, boolean blacksTurn) {

        return onBoard_AND_kingNotInCheck(board, from, to, blacksTurn)
                && (!board.fieldIsOccupied(Translators.translate144to64( to))
                || board.fieldHasOpponent(Translators.translate144to64( to), blacksTurn));
    }

    /**
     * Method to create a new Move
     * will copy the board and apply the from, to values to it before handing it over
     *
     * @param board      board before the move
     * @param from       must be in 144 format. position where the piece is
     * @param to         must be in 144 format. position where the piece will be
     * @param c          char to specify to which piece a pawn will promote or'0' for castling or '<space>' if nothing
     * @param blacksTurn site which corresponds to this move
     * @param enpassant  enpassant field. if not present -1, must be in 144 format
     * @return a Move corresponding to the parameters
     */
    private Move makeMove(int from, int to, char c, Board board, boolean blacksTurn, int enpassant) {
        Board temp = board.copy();
        int rFrom = Translators.translate144to64((byte) from);
        int rTo = Translators.translate144to64((byte) to);
        temp.applyMove(rFrom, rTo);
        return new MoveImpl(rFrom, rTo, c, temp, blacksTurn, enpassant);
    }

    /**
     * @param board      the board to check on
     * @param from       must be in 144 format. position where the piece is
     * @param to         must be in 144 format. position where the piece will be
     * @param blacksTurn site to check for
     * @return true if king is not in check and to is on board
     */
    private boolean onBoard_AND_kingNotInCheck(Board board, int from, int to, boolean blacksTurn) {
        if (!isSpace(to)) {
            Board tempboard = board.copy();
            tempboard.applyMove(Translators.translate144to64((byte) from), Translators.translate144to64((byte) to));
            return !kingInCheck(tempboard, blacksTurn);
        } else {
            return false;
        }
    }

    /**
     * @param field field to check whether it is in Space
     * @return true if the field is in space
     */
    private boolean isSpace(int field) {
        return space.contains((short) field);
    }

    /**
     * @param field      pawn position. must be in 144 format
     * @param blacksTurn site the pawn corresponds to
     * @return true if the specified pawn is on first position
     */
    private boolean pawnOnFirstPosition(int field, boolean blacksTurn) {
        if (blacksTurn) {
            return field >= 38 && field <= 45;
        } else {
            return field >= 98 && field <= 105;
        }
    }

    /**
     * @param field      pawn position. must be in 144 format
     * @param blacksTurn site the pawn corresponds to
     * @return true if the specified pawn is on last position
     */
    private boolean pawnOnLastPosition(int field, boolean blacksTurn) {
        if (blacksTurn) {
            return field >= 110 && field <= 117;
        } else {
            return field >= 26 && field <= 33;
        }
    }
}
