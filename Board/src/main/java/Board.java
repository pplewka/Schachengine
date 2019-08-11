public interface Board{

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
     * method to check if a direct line path from field1 to field2
     * has a valid piece in it.
     * only checks horizontal!
     * All fields in between field1 and field2 have to be checked.
     *
     * @param field1 start position of the path,needs to be smaller than field2, exclusive
     * @param field2 end position of the path,needs to be bigger than field1, exclusive
     * @return true if any piece is in between the path
     */
    public boolean pathHasPiece(int field1,int field2);

    /**
     * method to check if castling with the startposition of any rook is possible
     * @param rockPosition the startposition of any rook
     * @return true if castling is possible with the rook on the position
     */
    public boolean castlingDone(int rockPosition);

    /**
     * Method to check which Piece type is on a field
     * @param field position to check
     * @return Piece type on this field
     */
    public byte getPiece(int field);

    /**
     * Method to set a Piece type on a field
     * @param c Piece to set on field
     * @param field position to set
     */
    public void setField(byte c, int field);

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
    public boolean fieldHasOpponent(int field, boolean blacksTurn);

    public byte[] getBoard();

    public boolean iswKingMoved();
    public void setwKingMoved(boolean wKingMoved);

    public boolean isbKingMoved();
    public void setbKingMoved(boolean bKingMoved);

    public boolean iswLeftRockMoved();
    public void setwLeftRockMoved(boolean wLeftRockMoved);

    public boolean iswRightRockMoved();
    public void setwRightRockMoved(boolean wRightRockMoved);

    public boolean isbLeftRockMoved();
    public void setbLeftRockMoved(boolean bLeftRockMoved);

    public boolean isbRightRockMoved();
    public void setbRightRockMoved(boolean bRightRockMoved);
}
