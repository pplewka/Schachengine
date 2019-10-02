import Exceptions.FixYourConfigFileException;
import Exceptions.UnknownOptionException;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller implements UCIListener {
    private static final Map<Command.CommandEnum, Set<Command.CommandEnum>> commandsWithAllowedFollowers = Map.of(
            Command.CommandEnum.GO, Set.of(Command.CommandEnum.STOP),
            Command.CommandEnum.STOP, Set.of(Command.CommandEnum.UCINEWGAME, Command.CommandEnum.POSITION),
            Command.CommandEnum.POSITION, Set.of(Command.CommandEnum.POSITION, Command.CommandEnum.GO, Command.CommandEnum.UCINEWGAME),
            Command.CommandEnum.UCINEWGAME, Set.of(Command.CommandEnum.POSITION, Command.CommandEnum.UCINEWGAME)
    );
    private static final Object lock = new Object();
    private static volatile Controller instance;
    private final int numberCores;
    private final int numberWorkerThreads;
    private final List<OptionValuePair> options;
    private final Thread UCIThread;
    private List<SearchThread> WorkerThreads;
    private LinkedBlockingQueue<Command> commandQueue;
    private Set<Command.CommandEnum> allowedCommands;
    private int playedMoves;
    private TimeManThread tm;

    /**
     * Constructor
     */
    private Controller() {
        playedMoves = 0;
//        currentTimeManager = TimeManBlitzChessBased.getInstance();
        //set allowed next command
        allowedCommands = commandsWithAllowedFollowers.get(Command.CommandEnum.UCINEWGAME);
        //get # of cpu cores
        this.numberCores = Runtime.getRuntime().availableProcessors();
        // init uci
        options = UCI.getInstance().initialize();
        boolean writeLogs = Boolean.parseBoolean(getOptionsValue("log"));
        String logFile = getOptionsValue("logfile");
        Log.setInstance(new Log(logFile, writeLogs));
        Log.getInstance().writeToLog(options);

        // listen to uci
        UCI.getInstance().attachListener(this);
        // determine the number of worker threads
        this.numberWorkerThreads = calcWorkerThreadCount();

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
     * Calculates the number of worker threads
     *
     * @return the number of worker threads to spawn
     */
    private int calcWorkerThreadCount() {
        return numberCores > 1 ? numberCores - 1 : 1;
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
//        long b_go = 0;
//        long e_go = 0;
//        long b_stop = 0;
//        long e_stop = 0;
        while (true) {
            try {
                Command command = takeNextCommand(); //wait for uci instructions
                if (!allowedCommands.contains(command.getType())) {
                    if (command.isFromUCI()) {  // ignore if was not send from UCI
                        InfoHandler.sendUnknownCommandMessage(
                                Command.typeToString(command.getType()).toLowerCase()
                        );
                        StringBuilder temp = new StringBuilder();
                        for (Command.CommandEnum allowedCommand : allowedCommands) {
                            temp.append(" ").append(Command.typeToString(allowedCommand).toLowerCase());
                        }
                        InfoHandler.sendDebugMessage("allowed commands are" + temp.toString());
                    }
                    continue;
                } else {
                    allowedCommands = commandsWithAllowedFollowers.get(command.getType());
                }
                if (command.getType() == Command.CommandEnum.GO) {
//                    b_go = System.nanoTime();
                    InfoHandler.sendDebugMessage("ControllerThread: sending go command to worker threads");
                    parseGo(command);
                    Log.getInstance().setIgnoreNextWrites(true);
                    startSearching();
//                    e_go = System.nanoTime();
                } else if (command.getType() == Command.CommandEnum.STOP) {
//                    b_stop = System.nanoTime();
                    playedMoves++;
                    InfoHandler.sendDebugMessage("ControllerThread: sending stop command to worker threads");
                    stopSearching();
                    tm.interrupt();
                    Move best_move = SearchImpl.getSearch().getBestMove();
                    Log.getInstance().setIgnoreNextWrites(false);
                    UCI.getInstance().sendBestMove(best_move);
//                    e_stop = System.nanoTime();
//                    long go   = (e_go     - b_go)/1000000;
//                    long stop = (e_stop - b_stop)/1000000;
//                    long time = (e_stop   - b_go)/1000000;
//                    InfoHandler.sendDebugMessage("Time go: " + go + " Time end: " + stop);
//                    InfoHandler.sendDebugMessage("total time: " + time);
                } else if (command.getType() == Command.CommandEnum.UCINEWGAME) {
                    playedMoves = 0;
                    InfoHandler.sendDebugMessage("ControllerThread: sending ucinewgame command to worker threads");
                    SearchImpl.getSearch().clear();
                    Move move = new MoveImpl(Board.START_FEN);
                    SearchImpl.getSearch().setRoot(move);
                    SearchImpl.getSearch().getOutputLookUpTable().add(move);
                } else if (command.getType() == Command.CommandEnum.POSITION) {

                    InfoHandler.sendDebugMessage("ControllerThread: sending position command to worker threads");
                    SearchImpl.getSearch().clear();
                    SearchImpl.getSearch().setRoot(command.getMove());
                    SearchImpl.getSearch().getOutputLookUpTable().add(command.getMove());
                }
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Parses a go command and initialises the currentTimeManager
     *
     * @param command the command
     */
    private void parseGo(Command command) {
        // needed parameters for tm
        long totalTimeLeftInMsec;
        long inc;
        boolean blacksturn = !SearchImpl.getSearch().getRoot().blacksTurn();
        inc = blacksturn ? command.getBinc() : command.getWinc();
        long time = blacksturn ? command.getBtime() : command.getWtime();
        long movetime = command.getMovetime();
        if (command.isInfinite()) {
            totalTimeLeftInMsec = Long.MAX_VALUE;
        } else if (movetime != -1) {
            totalTimeLeftInMsec = movetime;
        } else if (time != -1) {
            totalTimeLeftInMsec = time;
        } else {
            totalTimeLeftInMsec = Long.MAX_VALUE;
        }
        int tm_threshold = 40;
        if (time != -1) {
            tm = new TimeManThread(totalTimeLeftInMsec, inc == -1 ? 0 : inc, playedMoves, commandQueue, tm_threshold);
        } else {
            tm = new TimeManThread(totalTimeLeftInMsec, commandQueue);
        }
        tm.start();
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
        c.setFromUCI(true);
        InfoHandler.sendDebugMessage("UCIThread: storing to commandqueue " + c.toString());
        commandQueue.add(c);

    }

    /**
     * Gets called, when the "stop" command was entered
     */
    @Override
    public void receivedStop() {
        Command c = new Command(Command.CommandEnum.STOP);
        c.setFromUCI(true);
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
        c.setFromUCI(true);
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
        c.setFromUCI(true);
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
        SearchImpl.getSearch().setBestMove(MoveImpl.DUMMIEMOVE);
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
