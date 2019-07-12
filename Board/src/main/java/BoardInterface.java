public interface BoardInterface{

    /**
     * Method to get a independent copy of this board
     */
    public Board copy();

    /**
     * Method to set up the Startposition on this board
     */
    public void startPos();

    /**
     * method to move a piece
     * @param from position of the piece to move
     * @param to position where the piece should be moved to
     */
    public void applyMove(int from, int to);

    /**
     * debug use only
     * method to print the content of the entire board
     */
    public void printBoard();

    /**
     * method to check if a direct line path from field1 to field2
     * has a valid piece in it.
     * All fields in between field1 and field2 have to be checked.
     *
     * @param field1 start position of the path, exclusive
     * @param field2 end position of the path, exclusive
     * @return true if any piece is in between the path
     */
    public boolean pathHasPiece(int field1,int field2);

    /**
     * method to check if castling with the startposition of any rook is possible
     * @param rockPosition the startposition of any rook
     * @return true if castling is possible with the rook on the position
     */
    public boolean castlingPossible(int rockPosition);

    /**
     * Method to check if a field contains a specific piece type
     * @param pieceType pieceType to check against
     * @param field position to check
     * @return true if the pieceType matches the type of piece on the field
     */
    public boolean fieldIs(Piece pieceType,int field);

    /**
     * method to check if a position is on board
     *
     * @param field position to check
     * @return true if this field is not in space
     */
    public boolean fieldIsOnBoard(int field);

    /**
     * method to check if a field is occupied by a valid piece
     * @param field field to check
     * @return true if field has a valid piece
     */
    public boolean fieldIsOccupied(int field);

    /**
     * method to check if a field is occupied by an opponent piece
     * @param field field to check
     * @return true if field has an opponent piece
     */
    public boolean fieldHasOpponent(int field);
}
