import java.util.ArrayList;

public interface MoveGeneration {
    /**
     * Method to generate all Moves on the board considering which site moves
     */
    public ArrayList<Move> generateAllMoves(Move parent,boolean checkAttacks);

    /**
     * Method to generate all moves of one specific piece
     */
    public ArrayList<Move> generatePawnMoves(Move parent,int field,ArrayList<Move> moves,boolean checkAttacks);
    public ArrayList<Move> generateRookMoves(Move parent,int field,ArrayList<Move> moves,boolean checkAttacks);
    public ArrayList<Move> generateBishopMoves(Move parent,int field,ArrayList<Move> moves,boolean checkAttacks);
    public ArrayList<Move> generateKnightMoves(Move parent,int field,ArrayList<Move> moves,boolean checkAttacks);
    public ArrayList<Move> generateQueenMoves(Move parent,int field,ArrayList<Move> moves,boolean checkAttacks);
    public ArrayList<Move> generateKingMoves(Move parent,int field,ArrayList<Move> moves,boolean checkAttacks);

    /**
     * method to generate special moves
     */
    public ArrayList<Move> generateEnpassant(Move parent,ArrayList<Move> moves,boolean checkAttacks);
    public ArrayList<Move> generateCastling(Move parent,ArrayList<Move> moves,boolean checkAttacks);

    /**
     * method to check if the King is in check
     */
    public boolean kingInCheck(Board board, boolean blacksTurn);

    /**
     * method to check if a field is attacked
     */
    public boolean fieldUnderAttack(Board board,int field, boolean blacksTurn);
}
