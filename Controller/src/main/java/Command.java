import java.util.Arrays;

/**
 * Container class for commands in the controller command queue
 * values -1, false or null mean the parameter was not set
 */
public class Command {

    private final CommandEnum type;

    public enum CommandEnum {
        GO,
        UCINEWGAME,
        POSITION,
        STOP
    }

    private Board board;
    private boolean ponder;
    private boolean infinite;
    private String[] searchmoves;
    private long wtime;
    private long btime;
    private long winc;
    private long binc;
    private long movestogo;
    private long depth;
    private long nodes;
    private long mate;
    private long movetime;
    private Move move;

    /**
     * Constructor
     * Please initialize all necessary parameters with the corresponding set methods after the constructor
     *
     * @param type the type of command
     */
    public Command(CommandEnum type) {
        this.type = type;
        setPonder(false);
        setInfinite(false);
        setBoard(null);
        setBinc(-1);
        setDepth(-1);
        setMate(-1);
        setMovestogo(-1);
        setMovetime(-1);
        setBtime(-1);
        setWinc(-1);
        setWtime(-1);
        setSearchmoves(null);
        setMove(null);
    }

    /**
     * Getter for move
     *
     * @return move
     */
    public Move getMove() {
        return move;
    }

    /**
     * Setter for move
     *
     * @param move the move
     */
    public void setMove(Move move) {
        this.move = move;
    }

    /**
     * Getter for type
     *
     * @return the type
     */
    public CommandEnum getType() {
        return type;
    }

    /**
     * Getter for board
     *
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Setter for board
     *
     * @param board the board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Getter for ponder
     *
     * @return ponder
     */
    public boolean isPonder() {
        return ponder;
    }

    /**
     * Setter for ponder
     *
     * @param ponder ponder
     */
    public void setPonder(boolean ponder) {
        this.ponder = ponder;
    }

    /**
     * Getter for infinite
     *
     * @return infinite
     */
    public boolean isInfinite() {
        return infinite;
    }

    /**
     * Setter for infinite
     *
     * @param infinite infinite
     */
    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }

    /**
     * Getter for searchmoves
     *
     * @return searchmoves
     */
    public String[] getSearchmoves() {
        return searchmoves;
    }

    /**
     * Setter for searchmoves
     *
     * @param searchmoves searchmoves
     */
    public void setSearchmoves(String[] searchmoves) {
        this.searchmoves = searchmoves;
    }

    /**
     * Getter for wtime
     *
     * @return wtime
     */
    public long getWtime() {
        return wtime;
    }

    /**
     * Setter for wtime
     *
     * @param wtime wtime
     */
    public void setWtime(long wtime) {
        this.wtime = wtime;
    }

    /**
     * Getter for btime
     *
     * @return btime
     */
    public long getBtime() {
        return btime;
    }

    /**
     * Setter for btime
     *
     * @param btime btime
     */
    public void setBtime(long btime) {
        this.btime = btime;
    }

    /**
     * Getter for winc
     *
     * @return winc
     */
    public long getWinc() {
        return winc;
    }

    /**
     * Setter for winc
     *
     * @param winc winc
     */
    public void setWinc(long winc) {
        this.winc = winc;
    }

    /**
     * Getter for binc
     *
     * @return binc
     */
    public long getBinc() {
        return binc;
    }

    /**
     * Setter for binc
     *
     * @param binc binc
     */
    public void setBinc(long binc) {
        this.binc = binc;
    }

    /**
     * Getter for movestogo
     *
     * @return movestogo
     */
    public long getMovestogo() {
        return movestogo;
    }

    /**
     * Setter for movestogo
     *
     * @param movestogo movestogo
     */
    public void setMovestogo(long movestogo) {
        this.movestogo = movestogo;
    }

    /**
     * Getter for depth
     *
     * @return depth
     */
    public long getDepth() {
        return depth;
    }

    /**
     * Setter for depth
     *
     * @param depth depth
     */
    public void setDepth(long depth) {
        this.depth = depth;
    }

    /**
     * Getter for nodes
     *
     * @return nodes
     */
    public long getNodes() {
        return nodes;
    }

    /**
     * Setter for nodes
     *
     * @param nodes nodes
     */
    public void setNodes(long nodes) {
        this.nodes = nodes;
    }

    /**
     * Getter for mate
     *
     * @return mate
     */
    public long getMate() {
        return mate;
    }

    /**
     * Setter for mate
     *
     * @param mate mate
     */
    public void setMate(long mate) {
        this.mate = mate;
    }

    /**
     * Getter for movetime
     *
     * @return movetime
     */
    public long getMovetime() {
        return movetime;
    }

    /**
     * Setter for movetime
     *
     * @param movetime movetime
     */
    public void setMovetime(long movetime) {
        this.movetime = movetime;
    }

    /**
     * toString
     *
     * @return String representation
     */
    @Override
    public String toString() {
        String type_string;
        switch (type) {
            case GO:
                type_string = "GO";
                break;
            case STOP:
                type_string = "STOP";
                break;
            case POSITION:
                type_string = "POSITION";
                break;
            case UCINEWGAME:
                type_string = "UCINEWGAME";
                break;
            default:
                type_string = "undefined";
        }
        String board_string = board == null ? "null" : "\n" + board + "\n";
        return "Command{" +
                "type=" + type_string +
                ", board=" + board_string +
                ", ponder=" + ponder +
                ", infinite=" + infinite +
                ", searchmoves=" + Arrays.toString(searchmoves) +
                ", wtime=" + wtime +
                ", btime=" + btime +
                ", winc=" + winc +
                ", binc=" + binc +
                ", movestogo=" + movestogo +
                ", depth=" + depth +
                ", nodes=" + nodes +
                ", mate=" + mate +
                ", movetime=" + movetime +
                '}';
    }
}
