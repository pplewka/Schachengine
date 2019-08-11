public class BoardImpl implements Board{
    //fields
    private byte[] board;
    private boolean wKingMoved;
    private boolean bKingMoved;
    private boolean wLeftRockMoved;
    private boolean wRightRockMoved;
    private boolean bLeftRockMoved;
    private boolean bRightRockMoved;

    public BoardImpl(){
        this.board= new byte[64];

        wKingMoved=true;
        bKingMoved=true;
        wLeftRockMoved=true;
        wRightRockMoved=true;
        bLeftRockMoved=true;
        bRightRockMoved=true;

        setEmptyRoom();
    }

    public BoardImpl copy(){
        BoardImpl out = new BoardImpl();
        out.board=board.clone();
        out.wKingMoved=this.wKingMoved;
        out.bKingMoved=this.bKingMoved;
        out.wLeftRockMoved=this.wLeftRockMoved;
        out.wRightRockMoved=this.wRightRockMoved;
        out.bLeftRockMoved=this.bLeftRockMoved;
        out.bRightRockMoved=this.bRightRockMoved;

        return out;
    }

    private void setEmptyRoom(){
        for(int i = 0; i < 64;i++ ) {
            board[i]=Piece.EMPTY;
        }
    }

    public void startPos(){

        //Black Pawns
        for(int i=8;i<16;i++){
            board[i]=Piece.BPAWN;
        }

        //White Pawns
        for(int i=48;i<56;i++){
            board[i]=Piece.WPAWN;
        }

        board[0]=Piece.BROOK;
        board[7]=Piece.BROOK;

        board[56]=Piece.WROOK;
        board[63]=Piece.WROOK;

        board[1]=Piece.BKNIGHT;
        board[6]=Piece.BKNIGHT;

        board[57]=Piece.WKNIGHT;
        board[62]=Piece.WKNIGHT;

        board[2]=Piece.BBISHOP;
        board[5]=Piece.BBISHOP;

        board[58]=Piece.WBISHOP;
        board[61]=Piece.WBISHOP;

        board[3]=Piece.BQUEEN;
        board[4]=Piece.BKING;

        board[59]=Piece.WQUEEN;
        board[60]=Piece.WKING;

        wKingMoved=false;
        bKingMoved=false;
        wLeftRockMoved=false;
        wRightRockMoved=false;
        bLeftRockMoved=false;
        bRightRockMoved=false;
    }

    @Override
    public void applyMove(int from, int to){
        board[to]=board[from];
        board[from]=Piece.EMPTY;
    }

    @Override
    public boolean pathHasPiece(int from, int to){
        for(int i=from+1;i<to;i++){
            if(fieldIsOccupied(i)){
                return true;
            }
        }
        return false;
    }

    /**
     *does not check if fields are attacked
     * @throws BoardException if rookPosition is invalid
     */
    @Override
    public boolean castlingDone(int rookPosition) {
        switch (rookPosition) {
            case 0:
                return ((bLeftRockMoved)||(bKingMoved)||(pathHasPiece(0,4)));
            case 7:
                return ((bRightRockMoved)||(bKingMoved)||(pathHasPiece(4,7)));
            case 56:
                return ((wLeftRockMoved)||(wKingMoved)||(pathHasPiece(56,60)));
            case 63:
                return ((wRightRockMoved)||(wKingMoved)||(pathHasPiece(60,63)));
            default:
                //should never happen
                throw new BoardException("castlingDone: invalid rook Position");
        }
    }

    public byte getPiece(int field){
        return board[field];
    }

    @Override
    public void setField(byte piece, int field){
        board[field]=piece;
    }

    public boolean fieldIsOccupied(int field){
        byte temp = board[field];
        return temp!=Piece.EMPTY;
    }

    public boolean iswKingMoved() {
        return wKingMoved;
    }

    public void setwKingMoved(boolean wKingMoved) {
        this.wKingMoved = wKingMoved;
    }

    public boolean isbKingMoved() {
        return bKingMoved;
    }

    public void setbKingMoved(boolean bKingMoved) {
        this.bKingMoved = bKingMoved;
    }

    public boolean iswLeftRockMoved() {
        return wLeftRockMoved;
    }

    public void setwLeftRockMoved(boolean wLeftRockMoved) {
        this.wLeftRockMoved = wLeftRockMoved;
    }

    public boolean iswRightRockMoved() {
        return wRightRockMoved;
    }

    public void setwRightRockMoved(boolean wRightRockMoved) {
        this.wRightRockMoved = wRightRockMoved;
    }

    public boolean isbLeftRockMoved() {
        return bLeftRockMoved;
    }

    public void setbLeftRockMoved(boolean bLeftRockMoved) {
        this.bLeftRockMoved = bLeftRockMoved;
    }

    public boolean isbRightRockMoved() {
        return bRightRockMoved;
    }

    public void setbRightRockMoved(boolean bRightRockMoved) {
        this.bRightRockMoved = bRightRockMoved;
    }

    public boolean fieldHasOpponent(int field, boolean blacksTurn) {
        int temp = board[field];
        if(blacksTurn){
            return temp<=9&&temp>=4;
        }else{
            return temp<=-4&&temp>=-9;
        }
    }

    @Override
    public byte[] getBoard() {
        return board;
    }
}
