import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EvaluationImpl implements Evaluation {
    private static Evaluation eval;
    Map<byte[], Integer> evalNoRepCache;

    /*
        [0, 1] = Space and Empty   = 0
        [2, 3] = idk               = 0
        [4]    = Pawn              = 1
        [5, 7] = Knight and Bishop = 3
        [8]    = Queen             = 9
        [9]    = King              = a much higher value than the others...
     */
    private int[] pieceValues = {0, 0, 0, 0, 1, 3, 5, 3, 9, 100};

    private int[] pstPawnsWhite = {
            0, 0, 0, 0, 0, 0, 0, 0,
            50, 50, 50, 50, 50, 50, 50, 50,
            10, 10, 20, 30, 30, 20, 10, 10,
            5, 5, 10, 25, 25, 10, 5, 5,
            0, 0, 0, 20, 20, 0, 0, 0,
            5, -5, -10, 0, 0, -10, -5, 5,
            5, 10, 10, -20, -20, 10, 10, 5,
            0, 0, 0, 0, 0, 0, 0, 0
    };

    private int[] pstPawnsBlack = {
            0, 0, 0, 0, 0, 0, 0, 0,
            -5, -10, -10, 20, 20, -10, -10, -5,
            -5, 5, 10, 0, 0, 10, 5, -5,
            0, 0, 0, -20, -20, 0, 0, 0,
            -5, -5, -10, -25, -25, -10, -5, -5,
            -10, -10, -20, -30, -30, -20, -10, -10,
            50, 50, -50, -50, -50, -50, -50, -50,
            0, 0, 0, 0, 0, 0, 0, 0
    };

    private int[] pstKnightsWhite = {
            -50, -40, -30, -30, -30, -30, -40, -50,
            -40, -20, 0, 0, 0, 0, -20, -40,
            -30, 0, 10, 15, 15, 10, 0, -30,
            -30, 5, 15, 20, 20, 15, 5, -30,
            -30, 0, 15, 20, 20, 15, 0, -30,
            -30, 5, 10, 15, 15, 10, 5, -30,
            -40, -20, 0, 5, 5, 0, -20, -40,
            -50, -40, -30, -30, -30, -30, -40, -50,
    };

    private int[] pstKnightsBlack = {
            50, 40, 30, 30, 30, 30, 40, 50,
            40, 20, 0, -5, -5, 0, 20, 40,
            30, -5, -10, -15, -15, -10, 5, 30,
            30, 0, -15, -20, -20, -15, 0, 30,
            30, -5, -15, -20, -20, -15, 5, 30,
            30, 0, -10, -15, -15, 10, 0, 30,
            40, 20, 0, 0, 0, 0, 20, 40,
            50, 40, 30, 30, 30, 30, 40, 50,
    };

    private int[] pstRooksWhite = {
            0, 0, 0, 0, 0, 0, 0, 0,
            5, 10, 10, 10, 10, 10, 10, 5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            0, 0, 0, 5, 5, 0, 0, 0
    };

    private int[] pstRooksBlack = {
            0, 0, 0, -5, -5, 0, 0, 0,
            5, 0, 0, 0, 0, 0, 0, 5,
            5, 0, 0, 0, 0, 0, 0, 5,
            5, 0, 0, 0, 0, 0, 0, 5,
            5, 0, 0, 0, 0, 0, 0, 5,
            5, 0, 0, 0, 0, 0, 0, 5,
            -5, -10, -10, -10, -10, -10, -10, -5,
            0, 0, 0, 0, 0, 0, 0, 0,
    };

    private int[] pstBishopWhite = {
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  5,  5, 10, 10,  5,  5,-10,
            -10,  0, 10, 10, 10, 10,  0,-10,
            -10, 10, 10, 10, 10, 10, 10,-10,
            -10,  5,  0,  0,  0,  0,  5,-10,
            -20,-10,-10,-10,-10,-10,-10,-20,
    };

    private int[] getPstBishopBlack = {
            20, 10, 10, 10, 10, 10, 10, 20,
            10, -5,  0,  0,  0,  0, -5, 10,
            10,-10,-10,-10,-10,-10,-10, 10,
            10,  0,-10,-10,-10,-10,  0, 10,
            10, -5, -5,-10,-10, -5, -5, 10,
            10,  0, -5,-10,-10, -5,  0, 10,
            10,  0,  0,  0,  0,  0,  0, 10,
            20, 10, 10, 10, 10, 10, 10, 20,
    };

    private int[] pstQueensWhite = {
            -20, -10, -10, -5, -5, -10, -10, -20,
            -10, 0, 0, 0, 0, 0, 0, -10,
            -10, 0, 5, 5, 5, 5, 0, -10,
            -5, 0, 5, 5, 5, 5, 0, -5,
            0, 0, 5, 5, 5, 5, 0, -5,
            -10, 5, 5, 5, 5, 5, 0, -10,
            -10, 0, 5, 0, 0, 0, 0, -10,
            -20, -10, -10, -5, -5, -10, -10, -20
    };

    private int[] pstQueensBlack = {
            20, 10, 10, 5, 5, 10, 10, 20,
            10, 0, -5, 0, 0, 0, 0, 10,
            10, -5, -5, -5, -5, -5, 0, 10,
            5, 0, -5, -5, -5, -5, 0, 5,
            0, 0, -5, -5, -5, -5, 0, 5,
            10, 0, -5, -5, -5, -5, 0, 10,
            10, 0, 0, 0, 0, 0, 0, 10,
            20, 10, 10, 5, 5, 10, 10, 20
    };

    private EvaluationImpl() {
        evalNoRepCache = new ConcurrentHashMap<>();
    }

    public static Evaluation getEvaluation() {
        if (eval == null) {
            eval = new EvaluationImpl();
        }
        return eval;
    }

    private int PieceSquareTablesValues(byte[] board, boolean blacksTurn) {
        int value = 0;
        for (int i = 0; i < board.length; i++) {
            if (i > 1) {

                if (!blacksTurn) {
                    switch (board[i]) {
                        case Piece.WPAWN:
                            value += pstPawnsWhite[i];
                            break;
                        case Piece.WQUEEN:
                            value += pstQueensWhite[i];
                            break;
                        case Piece.WROOK:
                            value += pstRooksWhite[i];
                            break;
                        case Piece.WKNIGHT:
                            value += pstKnightsWhite[i];
                            break;
                        case Piece.WBISHOP:
                            value += pstBishopWhite[i];

                    }
                } else {
                    switch (board[i]) {
                        case Piece.BPAWN:
                            value += pstPawnsBlack[i];
                            break;
                        case Piece.BQUEEN:
                            value += pstQueensBlack[i];
                            break;
                        case Piece.BROOK:
                            value += pstRooksBlack[i];
                            break;
                        case Piece.BKNIGHT:
                            value += pstKnightsBlack[i];
                            break;
                        case Piece.BBISHOP:
                             value += getPstBishopBlack[i];

                    }
                }

            }
        }
        return value;
    }

    private int getPSTScoreForPiece(byte[] board, int pos, boolean blacksTurn) {
        if (pos > 1) {

            if (!blacksTurn) {
                switch (board[pos]) {
                    case Piece.WPAWN:
                        return pstPawnsWhite[pos];
                    case Piece.WQUEEN:
                        return pstQueensWhite[pos];
                    case Piece.WROOK:
                        return pstRooksWhite[pos];
                    case Piece.WKNIGHT:
                        return pstKnightsWhite[pos];
                    case Piece.WBISHOP:
                        return pstBishopWhite[pos];

                }
            } else {
                switch (board[pos]) {
                    case Piece.BPAWN:
                        return pstPawnsBlack[pos];
                    case Piece.BQUEEN:
                        return pstQueensBlack[pos];
                    case Piece.BROOK:
                        return pstRooksBlack[pos];
                    case Piece.BKNIGHT:
                        return pstKnightsBlack[pos];
                    case Piece.BBISHOP:
                        return getPstBishopBlack[pos];

                }
            }
        }
        return 0;
    }

        @Override
        public int evaluate(Move toEvaluate) {
            Board board = toEvaluate.getBoard();
            boolean blacksTurn = toEvaluate.blacksTurn();

        int globalValue = material(board, blacksTurn);

        //globalValue += PieceSquareTablesValues(board.getBoard(), toEvaluate.blacksTurn()) / 2;

        globalValue = globalValue + repetitionScore(toEvaluate);

        return globalValue;
    }

    public int evaluateNoRep(Move toEvaluate) {
        Board board = toEvaluate.getBoard();
        boolean blacksTurn = toEvaluate.blacksTurn();

        if (evalNoRepCache.containsKey(board.getBoard())) {
            return blacksTurn ? -evalNoRepCache.get(board.getBoard()) : evalNoRepCache.get(board.getBoard());
        }

        int result = material(board, blacksTurn) + PieceSquareTablesValues(board.getBoard(), toEvaluate.blacksTurn());
        evalNoRepCache.put(board.getBoard(), blacksTurn ? -result : result);
        return result;
    }

    /**
     * Calculate the material value of a board for a specific site.
     *
     * @param board      the board that will be valued
     * @param blacksTurn indicates which site of the board should be calculated
     * @return the combination of all piece values of a site in centipawns (100 centipawns = 1 pawn).
     */
    @Override
    public int material(Board board, boolean blacksTurn) {
        // TODO checking for certain piece combination bonus/penalty
        // TODO unit tests of course

        byte[] boardByte = board.getBoard();

        int materialValue = 0;
        boolean secondBishop = false;
        for (int i = 0; i < boardByte.length; i++) {
            int field = boardByte[i] * -1;

            if (field > 1) { // if neither empty or "SPACE"
                materialValue += pieceValues[field] * getPSTScoreForPiece(boardByte, i, blacksTurn) * 100;

                if (field == Piece.WKING) {
                    materialValue += evaluateKingProtection(boardByte, i);
                } else if (field == Piece.WPAWN) {
                    if (pawnIsBlocked(boardByte, i, blacksTurn)) {
                        // Half a pawn penalty
                        materialValue -= (pieceValues[Piece.WPAWN] * 100) / 2;
                    }
                } else if (field == Piece.WBISHOP) {
                    if (!secondBishop) {
                        secondBishop = true;
                    } else {
                        materialValue += (pieceValues[Piece.WPAWN] * 100) / 2;
                    }
                }

            } else if (field < -1) {
                materialValue -= pieceValues[-1 * field] * 100;
            }
        }

        return materialValue;
    }

    private boolean pawnIsBlocked(byte[] board, int pos, boolean blacksTurn) {
        if (!blacksTurn) {
            if (pos >= 56) {
                return true;
            }
            boolean isBlocked =
                    board[pos + 8] != Piece.EMPTY;
            boolean cannotAttackLeft = pos % 8 == 7 || board[pos + 8 + 1] == Piece.EMPTY;
            boolean cannotAttackRight = pos % 8 == 0 || board[pos + 8 - 1] == Piece.EMPTY;
            boolean cannotAttack = cannotAttackLeft && cannotAttackRight;

            return isBlocked && cannotAttack;
        } else {
            if (pos <= 7) {
                return true;
            }
            boolean isBlocked =
                    board[pos - 8] != Piece.EMPTY;
            boolean cannotAttackLeft = pos % 8 == 7 || board[pos - 8 + 1] == Piece.EMPTY;
            boolean cannotAttackRight = pos % 8 == 0 || board[pos - 8 - 1] == Piece.EMPTY;
            boolean cannotAttack = cannotAttackLeft && cannotAttackRight;

            return isBlocked && cannotAttack;
        }
    }

    /**
     * Check whether the king still has pieces around him to protect him
     *
     * @param board to evaluate
     * @param pos   position of the king
     * @return the
     */
    private int evaluateKingProtection(byte[] board, int pos) {
        int protectionScore = 0;
        if (pos > 7) { // if not on the first rank
            if (board[pos - 8] != Piece.EMPTY) {
                protectionScore += 50;
            }
            if (pos % 8 != 0 && board[pos - 8 - 1] != Piece.EMPTY) {
                protectionScore += 50;
            }
            if ((pos + 1) % 8 != 0 && board[pos - 8 + 1] != Piece.EMPTY) {
                protectionScore += 1;
            }
        }

        if (pos < 56) { // if not on the last rank
            if (board[pos + 8] != Piece.EMPTY) {
                protectionScore += 50;
            }
            if (pos % 8 != 0 && board[pos + 8 + 1] != Piece.EMPTY) {
                protectionScore += 50;
            }
            if ((pos + 1) % 8 != 0 && board[pos + 8 - 1] != Piece.EMPTY) {
                protectionScore += 50;
            }
        }

        if (pos % 8 != 0) { // is not at leftmost file
            if (board[pos - 1] != Piece.EMPTY) {
                protectionScore += 50;
            }
        }

        if ((pos + 1) % 8 != 0) { // if not on the rightmost file
            if (board[pos + 1] != Piece.EMPTY) {
                protectionScore += 50;
            }
        }

        return protectionScore;
    }

    @Override
    public int mobility(Board board, boolean blacksTurn) {
        /*
        ignore at first

        later maybe counts attacked pieces or similar
         */
        return 0;
    }

    @Override
    public int repetitionScore(Move toEvaluate) {
        int deduction = 100;
        int score = 0;
        Move now = toEvaluate;
        Move before = toEvaluate.getParent().getParent();

        while (before != null) {
            if (now.getFrom() == before.getTo() && now.getTo() == before.getFrom()) {
                if (toEvaluate.blacksTurn()) {
                    score = score - deduction;
                } else {
                    score = score + deduction;
                }
            }

            now = before;
            if (before.getParent() != null) {
                before = before.getParent().getParent();
            } else {
                before = null;
            }
        }

        return score;
    }
}
