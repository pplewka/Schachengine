// TODO mid: evaluate where to get the move count instead of counting it on it's own
// TODO low: add move importance into calculation

import Exceptions.TimeManagementException;

public class TimeManBlitzChessBased implements TimeManagement {

    private static final String ERR_NEG_INPUT_TIME = "given time is a negative value";
    private static final String ERR_NO_RESET = "The Time Management is currently already in state reset";
    private static final String ERR_NO_INIT =" The Time Management is already initialised for this move";

    private int movesCnt; // Current amount of moves

    private long totalTimeLeftInMsec; // How much time is left ?
    private long timeFrame;           // How much time for this current move ?
    private long startTime;           // Start time of current search

    public int getMovesCnt() {
        return movesCnt;
    }

    public long getTimeFrame() {
        return timeFrame;
    }

    public long getTotalTimeLeftInMsec() {
        return totalTimeLeftInMsec;
    }

    public long getStartTime() {
        return startTime;
    }

    public TimeManBlitzChessBased(long totalTimeInMsec) {
        if(totalTimeInMsec <= 0) {
            throw new TimeManagementException(ERR_NEG_INPUT_TIME);
        }
        totalTimeLeftInMsec = totalTimeInMsec;
        movesCnt = 0;

        // -1 == never initialised, for checking if TM is already reset / initialised
        timeFrame = -1;
        startTime = -1;
    }

    @Override
    public void init() {
        if(isInit()) {
            throw new TimeManagementException(ERR_NO_INIT);
        }

        // timeFrame calculation
        if(movesCnt < 40) {
            timeFrame = (totalTimeLeftInMsec / 2) / (40 - movesCnt);
        } else {
            timeFrame = totalTimeLeftInMsec / 50; // + increment / 2
        }

        startTime = System.currentTimeMillis();
    }

    public boolean isInit() {
        return (timeFrame != -1 && startTime != -1);
    }

    @Override
    public void reset() {
        if (!isInit()) {
            throw new TimeManagementException(ERR_NO_RESET);
        }
        totalTimeLeftInMsec = totalTimeLeftInMsec - (startTime - System.currentTimeMillis());
        timeFrame = -1;
        startTime = -1;
        movesCnt++;
    }

    @Override
    public boolean isEnoughTime() {
        if (!isInit()) {
            return true;
        }
        // Leave 25% of time as overhead
        return (System.currentTimeMillis() - startTime) < timeFrame * 0.75;
    }

    @Override
    public long timeLeft() {
        if(isInit()) {
            return Long.MAX_VALUE;
        }
        return timeFrame - (System.currentTimeMillis() - startTime);
    }

    public String toString() {
        return "Time left: " + totalTimeLeftInMsec + "Milliseconds";
    }


}
