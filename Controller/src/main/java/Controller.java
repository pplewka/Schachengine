import Exceptions.EngineQuitSignal;
import Exceptions.UnknownOptionException;

import java.util.ArrayList;
import java.util.List;

public class Controller implements UCIListener {

    private final int numberCores;
    private final int numberWorkerThreads;
    private final List<OptionValuePair> options;
    private final Thread UCIThread;
    private List<SearchThread> WorkerThreads;

    private static volatile Controller instance;

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
        WorkerThreads = new ArrayList<>(numberWorkerThreads);
        for (int i = 0; i < numberWorkerThreads; i++) {
            WorkerThreads.add(new SearchThread());
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
     * Main loop
     */
    private void run() {
        UCIThread.start();
        for (SearchThread workerThread : WorkerThreads) {
            workerThread.start();
        }
        //wait for uci instructions
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
     * Main method
     *
     * @param args command line args
     */
    public static void main(String[] args) {
        getInstance().run();
    }

    /**
     * Gets called, when the "ucinewgame" command was entered
     */
    @Override
    public void receivedNewGame() {

    }

    /**
     * Gets called, when the "stop" command was entered
     */
    @Override
    public void receivedStop() {

    }

    /**
     * Gets called, when the "position" command was entered
     *
     * @param position the complete entered command (with "position" as first token)
     */
    @Override
    public void receivedPosition(String position) {

    }

    /**
     * Gets called, when the "go" command was entered
     */
    @Override
    public void receivedGo(String options) {

    }
}
