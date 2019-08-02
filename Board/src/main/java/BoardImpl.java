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
        this.board= new byte[144];

        wKingMoved=true;
        bKingMoved=true;
        wLeftRockMoved=true;
        wRightRockMoved=true;
        bLeftRockMoved=true;
        bRightRockMoved=true;

        setSpace();
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

    private void setSpace(){
        //above space
        for(int i=0;i<24;i++){
            board[i]=Piece.SPACE;
        }

        //left space
        for(int i=24;i<109;i+=12){
            board[i]=Piece.SPACE;
        }
        for(int i=25;i<110;i+=12){
            board[i]=Piece.SPACE;
        }

        //right space
        for(int i=34;i<119;i+=12){
            board[i]=Piece.SPACE;
        }
        for(int i=35;i<120;i+=12){
            board[i]=Piece.SPACE;
        }

        //below space
        for(int i=120;i<144;i++){
            board[i]=Piece.SPACE;
        }
    }

    private void setEmptyRoom(){
        for(int i = 26; i < 118; ) {
            board[i]=Piece.EMPTY;

            i++;
            if ((i + 2) % 12 == 0) {
                i += 4;
            }
        }
    }

    public void startPos(){

        //White Pawns
        for(int i=38;i<46;i++){
            board[i]=Piece.BPAWN;
        }

        //Black Pawns
        for(int i=98;i<106;i++){
            board[i]=Piece.WPAWN;
        }

        board[26]=Piece.BROOK;
        board[33]=Piece.BROOK;

        board[110]=Piece.WROOK;
        board[117]=Piece.WROOK;

        board[27]=Piece.BKNIGHT;
        board[32]=Piece.BKNIGHT;

        board[111]=Piece.WKNIGHT;
        board[116]=Piece.WKNIGHT;

        board[28]=Piece.BBISHOP;
        board[31]=Piece.BBISHOP;

        board[112]=Piece.WBISHOP;
        board[115]=Piece.WBISHOP;

        board[29]=Piece.BQUEEN;
        board[30]=Piece.BKING;

        board[113]=Piece.WQUEEN;
        board[114]=Piece.WKING;

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
     */
    @Override
    public boolean castlingDone(int rockPosition) {
        switch (rockPosition) {
            case 26:
                return ((bLeftRockMoved)||(bKingMoved)||(pathHasPiece(26,30)));
            case 33:
                return ((bRightRockMoved)||(bKingMoved)||(pathHasPiece(30,33)));
            case 110:
                return ((wLeftRockMoved)||(wKingMoved)||(pathHasPiece(110,114)));
            case 117:
                return ((wRightRockMoved)||(wKingMoved)||(pathHasPiece(114,117)));
            default:
                //should never happen
                throw new IllegalArgumentException("castlingPossible default value");
        }
    }

    public byte getPiece(int field){
        return board[field];
    }

    @Override
    public void setField(byte piece, int field){
        board[field]=piece;
    }

    public boolean fieldIsOnBoard(int field){
        return board[field]!=Piece.SPACE;
    }

    public boolean fieldIsOccupied(int field){
        byte temp = board[field];
        return temp!=Piece.EMPTY&&temp!=Piece.SPACE;
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
