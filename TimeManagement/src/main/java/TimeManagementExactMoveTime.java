import Exceptions.TimeManagementException;

import java.util.concurrent.LinkedBlockingQueue;

public class TimeManagementExactMoveTime extends Thread implements TimeManagement{
    // TODO either just implement this into the other TimeMan, ignore the old TimeMan or make an special abstract class for this sh-t

    long timeFrame = -1;
    long startTime = -1;

    LinkedBlockingQueue<Command> commandQueue;

    private static final String ERR_NEG_TIME = "given time is a negative value";
    private static final String ERR_NO_RESET = "The Time Management is currently already in state reset";
    private static final String ERR_NO_INIT =" The Time Management is already initialised for this move";
    private static final String ERR_NEG_INC = "given increment is negative";

    public TimeManagementExactMoveTime(LinkedBlockingQueue<Command> commandQueue) {
        this.commandQueue = commandQueue;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(timeFrame);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        commandQueue.add(new Command(Command.CommandEnum.STOP));
    }

    /**
     * It's currently like in TimeManagementExactMoveTime, that it calculates for Blitz Chess
     */
    @Override
    public void init(long totalTimeLeftInMsec, long inc, int movesCnt) {

        if(isInit()) {
            throw new TimeManagementException(ERR_NO_INIT);
        }
        if(totalTimeLeftInMsec <= 0) {
            throw new TimeManagementException(ERR_NEG_TIME);
        }
        if(inc < 0) {
            throw new TimeManagementException(ERR_NEG_INC);
        }

        // timeFrame calculation
        if(movesCnt < 40) {
            timeFrame = ((totalTimeLeftInMsec + inc) / 2) / (40 - movesCnt);
        } else {
            timeFrame = totalTimeLeftInMsec / 50; // + increment / 2
        }

        startTime = System.currentTimeMillis();
    }

    public boolean isInit() {
        return (timeFrame != -1 && startTime != -1);
    }

    @Override
    public void init(long moveTime) {

        if(isInit()) {
            throw new TimeManagementException(ERR_NO_INIT);
        }
        timeFrame = moveTime;
        startTime = System.currentTimeMillis();
    }

    @Override
    public void reset() {
        if(!isInit()) {
            throw new TimeManagementException(ERR_NO_INIT);
        }

        timeFrame = -1;
        startTime = -1;
    }

    @Override
    public boolean isEnoughTime() {
        // run() is doing its job here
        return true;
    }

    @Override
    public long timeLeft() {
        if(!isInit()) {
            return Long.MAX_VALUE;
        }
        return timeFrame - (System.currentTimeMillis() - startTime);
    }

    @Override
    public long getTimeFrame() {
        return timeFrame;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }
}
