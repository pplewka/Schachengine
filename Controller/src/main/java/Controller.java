import Exceptions.EngineQuitSignal;
import Exceptions.UnknownOptionException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;

public class Controller implements UCIListener {
    private static Object lock = new Object();
    private static volatile Controller instance;
    private final int numberCores;
    private final int numberWorkerThreads;
    private final List<OptionValuePair> options;
    private final Thread UCIThread;
    private List<SearchThread> WorkerThreads;
    private ConcurrentMap<Long, LinkedBlockingDeque<Command>> CommandQueues;

    /**
     * Constructor
     *
     * @throws EngineQuitSignal if engine received the quit command
     */
    private Controller() throws EngineQuitSignal {
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
        InfoHandler.sendDebugMessage("Working with " + numberWorkerThreads + " worker threads");
        // create worker threads
        CommandQueues = new ConcurrentHashMap<Long, LinkedBlockingDeque<Command>>();
        WorkerThreads = new ArrayList<>(numberWorkerThreads);
        for (int i = 0; i < numberWorkerThreads; i++) {
            WorkerThreads.add(new SearchThread(lock));
            WorkerThreads.get(i).setName("SearchThread #" + i);
            CommandQueues.put(WorkerThreads.get(i).getId(), new LinkedBlockingDeque<>());
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
                    try {
                        instance = new Controller();
                    } catch (EngineQuitSignal e) {
                        System.exit(0);
                    }
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
                for (SearchThread workerThread : WorkerThreads) {
                    Command command = takeNextCommand(workerThread.getId());
                    if (command.getType() == Command.CommandEnum.GO) {
                        InfoHandler.sendDebugMessage("Controller Thread: sending go command to worker thread:" + workerThread.getName());
                        //TODO Parameter wie infinite
                        startSearching();
                    } else if (command.getType() == Command.CommandEnum.STOP) {
                        InfoHandler.sendDebugMessage("Controller Thread: sending stop command to worker thread:" + workerThread.getName());
                        stopSearching();
                        Move best_move = SearchImpl.getSearch().getBestMove();
                        InfoHandler.sendDebugMessage(best_move.toString());
                        if (workerThread.getId() == WorkerThreads.get(0).getId()) { // only send for the first thread
                            UCI.getInstance().sendBestMove(best_move);
                        }
                    } else if (command.getType() == Command.CommandEnum.UCINEWGAME) {
                        if (workerThread.getId() == WorkerThreads.get(0).getId()) { //only once
                            InfoHandler.sendDebugMessage("Controller Thread: sending ucinewgame command to worker thread:" + workerThread.getName());
                            SearchImpl.getSearch().clear();
                            Move move = new MoveImpl(Board.START_FEN);
                            SearchImpl.getSearch().setRoot(move);
                            SearchImpl.getSearch().getLookUpTable().add(move);
                        }
                    } else if (command.getType() == Command.CommandEnum.POSITION) {

                        if (workerThread.getId() == WorkerThreads.get(0).getId()) { //only once
                            InfoHandler.sendDebugMessage("Controller Thread: sending ucinewgame command");
                            SearchImpl.getSearch().clear();
                            SearchImpl.getSearch().setRoot(command.getMove());
                            SearchImpl.getSearch().getLookUpTable().add(command.getMove());
                        }
                    }
                }
            } catch (InterruptedException e) {
            }
        }
        //wait for uci instructions
    }

    public boolean hasNewCommand(Long thread_id) {
        return !CommandQueues.get(thread_id).isEmpty();
    }

    public Command takeNextCommand(Long thread_id) throws InterruptedException {
        return CommandQueues.get(thread_id).take();
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
        for (Long thread_id : CommandQueues.keySet()) {
            Command c = new Command(Command.CommandEnum.UCINEWGAME);
            CommandQueues.get(thread_id).add(c);
            InfoHandler.sendDebugMessage("Thread: " + thread_id + " " + c.toString());
        }
    }

    /**
     * Gets called, when the "stop" command was entered
     */
    @Override
    public void receivedStop() {
        for (Long thread_id : CommandQueues.keySet()) {
            Command c = new Command(Command.CommandEnum.STOP);
            CommandQueues.get(thread_id).add(c);
            InfoHandler.sendDebugMessage(c.toString());
        }
    }

    /**
     * Gets called, when the "position" command was entered
     *
     * @param board
     * @param move
     */
    @Override
    public void receivedPosition(Board board, Move move) {
        for (Long thread_id : CommandQueues.keySet()) {
            Command c = new Command(Command.CommandEnum.POSITION);
            c.setBoard(board);
            c.setMove(move);
            CommandQueues.get(thread_id).add(c);
            InfoHandler.sendDebugMessage(c.toString());
        }

    }

    /**
     * Gets called, when the "go" command was entered
     */
    @Override
    public void receivedGo(String options) {
        options = options.toLowerCase();
        for (Long thread_id : CommandQueues.keySet()) {
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

                    for (i++; i < splitted.length; i++) {
                        String[] notMoves = new String[]{"infinite", "ponder", "winc",
                                "binc", "wtime", "btime", "movestogo",
                                "nodes", "depth", "mate", "movetime"};
                        boolean isnotmove = false;
                        for (String notMove : notMoves) {
                            if (notMove.equals(splitted[i])) {
                                isnotmove = true;
                                break;
                            }
                        }
                        if (isnotmove) {
                            break;
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
            CommandQueues.get(thread_id).add(c);
            InfoHandler.sendDebugMessage(c.toString());
        }
    }

    private void startSearching() {
        SearchThread.setSearching(true);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    private void stopSearching() {
        SearchThread.setSearching(false);
    }
}
