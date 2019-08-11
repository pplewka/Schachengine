import java.util.ArrayList;

public interface MoveGeneration {
    /**
     * Method to generate all Moves on the board considering which site moves
     * @param parent not Null Move to generate Children Moves from
     *
     * @return ArrayList<Move> containing all legal Moves that can be generated from the parent Move
     */
    public ArrayList<Move> generateAllMoves(Move parent);

    /**
     * Method to generate all moves of one specific piece
     * @param parent not Null Move to generate Children Moves from
     * @param field must be in 144 format and o<= field >=143
     * @param moves notNull ArrayList to add all generated Moves
     *
     * @return ArrayList<Move> containing only Moves generated in this Method
     */
    public ArrayList<Move> generatePawnMoves(Move parent,int field,ArrayList<Move> moves);
    public ArrayList<Move> generateRookMoves(Move parent,int field,ArrayList<Move> moves);
    public ArrayList<Move> generateBishopMoves(Move parent,int field,ArrayList<Move> moves);
    public ArrayList<Move> generateKnightMoves(Move parent,int field,ArrayList<Move> moves);
    public ArrayList<Move> generateQueenMoves(Move parent,int field,ArrayList<Move> moves);
    public ArrayList<Move> generateKingMoves(Move parent,int field,ArrayList<Move> moves);

    /**
     * method to generate special moves
     * @param parent not Null Move to generate special Moves from
     * @param moves notNull ArrayList to add all generated Moves
     *
     * @return ArrayList<Move> containing only Moves generated in this Method
     */
    public ArrayList<Move> generateEnpassant(Move parent,ArrayList<Move> moves);
    public ArrayList<Move> generateCastling(Move parent,ArrayList<Move> moves);

    /**
     * method to check if the King is in check
     * @param blacksTurn site to check for King
     * @param board not Null Board to check on
     *
     * @return true if a corresponding King is on the board and attacked
     *          false if no corresponding King is on board or if the King is not attacked
     */
    public boolean kingInCheck(Board board, boolean blacksTurn);

    /**
     * method to check if a field is attacked
     * @param blacksTurn site to check for King
     * @param board not Null Board to check on
     * @param field must be in 144 format. can be any field on board no mater if empty or not
     *
     * @return true if this field is attacked, false if not
     *
     */
    public boolean fieldUnderAttack(Board board,int field, boolean blacksTurn);
}
