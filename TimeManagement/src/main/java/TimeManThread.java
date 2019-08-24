import java.util.concurrent.BlockingQueue;

public class TimeManThread extends Thread {

    private BlockingQueue<Command> queue;
    private TimeManagement timeMan;

    private long totalTimeLeft;
    private long inc;
    private int movesCnt;

    private long moveTime;

    private boolean isTotalTime;
    private long timeFrame;

    TimeManThread(long totalTimeLeft, long inc, int movesCnt, BlockingQueue<Command> queue) {
        TimeManBlitzChessBased.deleteInstance();
        timeMan = TimeManBlitzChessBased.getInstance();
        this.totalTimeLeft = totalTimeLeft;
        this.inc = inc;
        this.movesCnt = movesCnt;
        this.queue = queue;
        isTotalTime = true;
        timeMan.init(totalTimeLeft, inc, movesCnt);
        timeFrame = timeMan.getTimeFrame() - 4;
        InfoHandler.sendDebugMessage("TimeMan Thread initialised with calculated time of " + timeMan.getTimeFrame() + " msec");
    }

    TimeManThread(long moveTime, BlockingQueue<Command> queue) {
        TimeManBlitzChessBased.deleteInstance();
        UCIBridge.getInstance().sendString("test");
        timeMan = TimeManBlitzChessBased.getInstance();
        this.moveTime = moveTime;
        this.queue = queue;
        isTotalTime = false;
        timeMan.init(moveTime);
        timeFrame = timeMan.getTimeFrame() - 4;
        InfoHandler.sendDebugMessage("TimeMan Thread initialised with given moveTime");
    }

    @Override
    public void run() {
        long start = System.nanoTime();
        try {
            Thread.sleep(timeFrame); // - overhead by go/stop cmd
        } catch (InterruptedException e) {
            // jo, ne
        }
        if(!isInterrupted()) {
            queue.add(new Command(Command.CommandEnum.STOP));
        }
        long estimated = (System.nanoTime() - start) / 1000000;
        InfoHandler.sendDebugMessage("TimeMan: " + estimated);
    }
}
