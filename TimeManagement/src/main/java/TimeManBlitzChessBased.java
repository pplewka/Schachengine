public class TimeManBlitzChessBased implements TimeManagement {

    // TODO need unit tests

    int movesCnt; // Current amount of moves

    long totalTimeLeftInMsec; // How much time is left ?
    long timeFrame;           // How much time for this current move ?
    long startTime;           // Start time of current search

    // TODO implement time increment
    long increment = 0;

    public TimeManBlitzChessBased(long totalTimeInMsec) {
        totalTimeLeftInMsec = totalTimeInMsec;
        movesCnt = 1;
    }

    @Override
    public void init() {
        if(movesCnt < 40) {
            timeFrame = (totalTimeLeftInMsec / 2) / movesCnt + increment;
        } else {
            timeFrame = totalTimeLeftInMsec / 50; // + increment / 2
        }
        startTime = System.currentTimeMillis();
    }

    @Override
    public void reset() {
        totalTimeLeftInMsec = totalTimeLeftInMsec - (startTime - System.currentTimeMillis());
        timeFrame = Long.MAX_VALUE;
        startTime = Long.MAX_VALUE;
        movesCnt++;
    }

    @Override
    public boolean isEnoughTime() {
        if (timeFrame == Long.MAX_VALUE) {
            return true;
        }

        // Leave 25% of time as overhead
        return (System.currentTimeMillis() - startTime) < timeFrame * 0.75;
    }
}
