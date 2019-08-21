import Exceptions.UnknownOptionException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller implements UCIListener {
    private static Object lock = new Object();
    private static volatile Controller instance;
    private final int numberCores;
    private final int numberWorkerThreads;
    private final List<OptionValuePair> options;
    private final Thread UCIThread;
    private List<SearchThread> WorkerThreads;
    private LinkedBlockingQueue<Command> commandQueue;

    /**
     * Constructor
     */
    private Controller() {
        //get # of cpu cores
        this.numberCores = Runtime.getRuntime().availableProcessors();
        // init uci
        options = UCI.getInstance().initialize();
        // listen to uci
        UCI.getInstance().attachListener(this);
        // determine the number of worker threads
        if (getOptionsValue("workerthreads_method").equals("adaptive")) {
            this.numberWorkerThreads = numberCores > 1 ? numberCores - 1 : 1;
        } else {
            this.numberWorkerThreads = Integer.parseInt(getOptionsValue("workerthreads_count"));
        }
        InfoHandler.sendDebugMessage("ControllerThread: Working with " + numberWorkerThreads + " worker threads");
        // create worker threads
        WorkerThreads = new ArrayList<>(numberWorkerThreads);
        commandQueue = new LinkedBlockingQueue<>();
        for (int i = 0; i < numberWorkerThreads; i++) {
            WorkerThreads.add(new SearchThread(lock));
            WorkerThreads.get(i).setName("SearchThread #" + i);
        }

        //create uci thread
        UCIThread = new Thread(UCI.getInstance());
        UCIThread.setName("UCIThread");
    }

    /**
     * Returns the unique instance of the controller class
     *
     * @return the instance
     */
    public static Controller getInstance() {
        if (instance == null) {
            synchronized (Controller.class) {
                if (instance == null) {
                    instance = new Controller();

                }
            }
        }
        return instance;
    }

    /**
     * Main method
     *
     * @param args command line args
     */
    public static void main(String[] args) {
        getInstance().run();
    }

    /**
     * Main loop
     */
    private void run() {
        UCIThread.start();
        for (SearchThread workerThread : WorkerThreads) {
            workerThread.start();
        }
        while (true) {
            try {
                Command command = takeNextCommand(); //wait for uci instructions

                if (command.getType() == Command.CommandEnum.GO) {
                    InfoHandler.sendDebugMessage("ControllerThread: sending go command to worker threads");
                    //TODO Parameter wie infinite
                    startSearching();
                } else if (command.getType() == Command.CommandEnum.STOP) {
                    InfoHandler.sendDebugMessage("ControllerThread: sending stop command to worker threads");
                    stopSearching();
                    Move best_move = SearchImpl.getSearch().getBestMove();
                    InfoHandler.sendDebugMessage(best_move.toString());
                    UCI.getInstance().sendBestMove(best_move);
                } else if (command.getType() == Command.CommandEnum.UCINEWGAME) {
                    InfoHandler.sendDebugMessage("ControllerThread: sending ucinewgame command to worker threads");
                    SearchImpl.getSearch().clear();
                    Move move = new MoveImpl(Board.START_FEN);
                    SearchImpl.getSearch().setRoot(move);
                    SearchImpl.getSearch().getLookUpTable().add(move);
                } else if (command.getType() == Command.CommandEnum.POSITION) {

                    InfoHandler.sendDebugMessage("ControllerThread: sending position command to worker threads");
                    SearchImpl.getSearch().clear();
                    SearchImpl.getSearch().setRoot(command.getMove());
                    SearchImpl.getSearch().getLookUpTable().add(command.getMove());
                }
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Check if there is a new command
     *
     * @return true if there is a new command, else  false
     */
    public boolean hasNewCommand() {
        return !commandQueue.isEmpty();
    }

    /**
     * Returns the next command.
     * This blocks the current thread until there is a next command, if none is there
     *
     * @return the next command
     * @throws InterruptedException should never happen
     */
    public Command takeNextCommand() throws InterruptedException {
        return commandQueue.take();
    }

    /**
     * Gets the value of a given option
     *
     * @param option the option
     * @return the value
     */
    public String getOptionsValue(String option) {
        synchronized (options) {
            for (OptionValuePair optionValuePair : options) {
                if (optionValuePair.option.equals(option)) {
                    return optionValuePair.value;
                }
            }
        }
        throw new UnknownOptionException("Unknown option: " + option);
    }

    /**
     * Gets called, when the "ucinewgame" command was entered
     */
    @Override
    public void receivedNewGame() {
        Command c = new Command(Command.CommandEnum.UCINEWGAME);
        InfoHandler.sendDebugMessage("UCIThread: storing to commandqueue " + c.toString());
        commandQueue.add(c);

    }

    /**
     * Gets called, when the "stop" command was entered
     */
    @Override
    public void receivedStop() {
        Command c = new Command(Command.CommandEnum.STOP);
        InfoHandler.sendDebugMessage("UCIThread: storing to commandqueue " + c.toString());
        commandQueue.add(c);

    }

    /**
     * Gets called, when the "position" command was entered
     *
     * @param board
     * @param move
     */
    @Override
    public void receivedPosition(Board board, Move move) {
        Command c = new Command(Command.CommandEnum.POSITION);
        c.setBoard(board);
        c.setMove(move);
        InfoHandler.sendDebugMessage("UCIThread: storing to commandqueue " + c.toString());
        commandQueue.add(c);


    }

    /**
     * Gets called, when the "go" command was entered
     */
    @Override
    public void receivedGo(String options) {
        options = options.toLowerCase();
        Command c = new Command(Command.CommandEnum.GO);
        String[] splitted = options.split(" ");
        for (int i = 1; i < splitted.length; i++) {
            String token = splitted[i];
            if (token.equals("infinite")) {
                c.setInfinite(true);
            } else if (token.equals("ponder")) {
                c.setPonder(true);
            } else if (token.equals("searchmoves")) {
                ArrayList<String> moves = new ArrayList<>(splitted.length);
                outerloop:
                for (i++; i < splitted.length; i++) {
                    String[] notMoves = new String[]{"infinite", "ponder", "winc",
                            "binc", "wtime", "btime", "movestogo",
                            "nodes", "depth", "mate", "movetime"};
                    for (String notMove : notMoves) {
                        if (notMove.equals(splitted[i])) {
                            break outerloop;
                        }
                    }
                    moves.add(splitted[i]);
                }
                c.setSearchmoves(moves.toArray(new String[]{}));
            } else {
                if (splitted.length - 1 == i) {
                    throw new InputMismatchException("Invalid line: " + options);
                }
                long value = Long.parseLong(splitted[i + 1]);
                i++;
                switch (token) {
                    case "binc":
                        c.setBinc(value);
                        break;
                    case "wtime":
                        c.setWtime(value);
                        break;
                    case "btime":
                        c.setBtime(value);
                        break;
                    case "winc":
                        c.setWinc(value);
                        break;
                    case "movetime":
                        c.setMovetime(value);
                        break;
                    case "mate":
                        c.setMate(value);
                        break;
                    case "depth":
                        c.setDepth(value);
                        break;
                    case "movestogo":
                        c.setMovestogo(value);
                        break;
                    case "nodes":
                        c.setNodes(value);
                        break;
                    default:
                        throw new InputMismatchException("Invalid line: " + options + " unknown command");
                }
            }
        }
        InfoHandler.sendDebugMessage("UCIThread: storing to commandqueue " + c.toString());
        commandQueue.add(c);

    }

    /**
     * Tells the worker threads to start searching
     */
    private void startSearching() {
        SearchThread.setSearching(true);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    /**
     * Tells the worker threads to stop searching
     */
    private void stopSearching() {
        SearchThread.setSearching(false);
    }
}
