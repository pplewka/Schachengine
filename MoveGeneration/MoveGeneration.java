import java.util.ArrayList;

public interface MoveGeneration {
    /**
     * Method to generate all Moves on the board considering which site moves
     */
    public ArrayList<Move> generateAllMoves(Board board,boolean blacksTurn);

    /**
     * Method to generate all Moves for any piece of a specific type
     */
    public ArrayList<Move> generateAllPawnMoves(Board board,boolean blacksTurn);
    public ArrayList<Move> generateAllRookMoves(Board board,boolean blacksTurn);
    public ArrayList<Move> generateAllBishopMoves(Board board,boolean blacksTurn);
    public ArrayList<Move> generateAllKnightMoves(Board board,boolean blacksTurn);
    public ArrayList<Move> generateAllQueenMoves(Board board,boolean blacksTurn);

    /**
     * Method to generate all moves of one specific piece
     */
    public ArrayList<Move> generatePawnMoves(Board board,int field,boolean blacksTurn);
    public ArrayList<Move> generateRookMoves(Board board,int field,boolean blacksTurn);
    public ArrayList<Move> generateBishopMoves(Board board,int field, boolean blacksTurn);
    public ArrayList<Move> generateKnightMoves(Board board, int field, boolean blacksTurn);
    public ArrayList<Move> generateQueenMoves(Board board, int field, boolean blacksTurn);
    public ArrayList<Move> generateKingMoves(Board board,boolean blacksTurn);
}
