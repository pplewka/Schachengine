import java.util.Arrays;

public class Command {
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
        return "Command{" +
                "type=" + type_string +
                ", board=" + board +
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
    }

    public CommandEnum getType() {
        return type;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isPonder() {
        return ponder;
    }

    public void setPonder(boolean ponder) {
        this.ponder = ponder;
    }

    public boolean isInfinite() {
        return infinite;
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }

    public String[] getSearchmoves() {
        return searchmoves;
    }

    public void setSearchmoves(String[] searchmoves) {
        this.searchmoves = searchmoves;
    }

    public long getWtime() {
        return wtime;
    }

    public void setWtime(long wtime) {
        this.wtime = wtime;
    }

    public long getBtime() {
        return btime;
    }

    public void setBtime(long btime) {
        this.btime = btime;
    }

    public long getWinc() {
        return winc;
    }

    public void setWinc(long winc) {
        this.winc = winc;
    }

    public long getBinc() {
        return binc;
    }

    public void setBinc(long binc) {
        this.binc = binc;
    }

    public long getMovestogo() {
        return movestogo;
    }

    public void setMovestogo(long movestogo) {
        this.movestogo = movestogo;
    }

    public long getDepth() {
        return depth;
    }

    public void setDepth(long depth) {
        this.depth = depth;
    }

    public long getNodes() {
        return nodes;
    }

    public void setNodes(long nodes) {
        this.nodes = nodes;
    }

    public long getMate() {
        return mate;
    }

    public void setMate(long mate) {
        this.mate = mate;
    }

    public long getMovetime() {
        return movetime;
    }

    public void setMovetime(long movetime) {
        this.movetime = movetime;
    }
}
