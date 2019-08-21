// TODO mid: evaluate where to get the move count instead of counting it on it's own
// TODO low: add move importance into calculation

import Exceptions.TimeManagementException;

public class TimeManBlitzChessBased implements TimeManagement {

    private static final String ERR_NEG_INPUT_TIME = "given time is a negative value";
    private static final String ERR_NO_RESET = "The Time Management is currently already in state reset";
    private static final String ERR_NO_INIT =" The Time Management is already initialised for this move";


    private long timeFrame = -1;           // How much time for this current move ?
    private long startTime = -1;           // Start time of current search


    public long getTimeFrame() {
        return timeFrame;
    }

    public long getStartTime() {
        return startTime;
    }


    @Override
    public void init(long totalTimeLeftInMsec, int movesCnt) {
        if(isInit()) {
            throw new TimeManagementException(ERR_NO_INIT);
        }

        if(totalTimeLeftInMsec <= 0) {
            throw new TimeManagementException(ERR_NEG_INPUT_TIME);
        }

        // timeFrame calculation
        if(movesCnt < 40) {
            timeFrame = (totalTimeLeftInMsec / 2) / (40 - movesCnt);
        } else {
            timeFrame = totalTimeLeftInMsec / 50; // + increment / 2
        }

        startTime = System.currentTimeMillis();
    }

    public void init(long moveTime) {
        if(isInit()) {
            throw new TimeManagementException(ERR_NO_INIT);
        }

        if(moveTime <= 0) {
            throw new TimeManagementException(ERR_NEG_INPUT_TIME);
        }
        timeFrame = moveTime;

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
        timeFrame = -1;
        startTime = -1;
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
        return "Time Management: Maximum Time allocated for this move :" + timeFrame;
    }


}
