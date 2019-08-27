/**
 * TimeManagement class to allocate an timeframe
 */
public interface TimeManagement {

    /**
     * Allocate new time frame for next move
     * The first 40 moves will get 50% of the total time ( totalTime / (40 - doneMoves) )
     *  Each move after that will get less and less time
     * @param totalTimeLeftInMsec the total time left for our site in milliseconds.
     * @param inc the increment that will be given to us, optional.
     * @param movesCnt the amount of moves done
     */
    public void init(long totalTimeLeftInMsec, long inc, int movesCnt, int threshold);

    /**
     * Allocate the time by taking the moveTime given by the UCI Protocol
     * @param moveTime the exact given timeFrame
     */
    public void init(long moveTime);

    /**
     * Stop the timer, reduce the total time left and wait for an new init
     */
    public void reset();

    /**
     * Check if there is enough time to Check for another node/search depth.
     * This method should not stop an search, only tell if there is enough time
     */
    public boolean isEnoughTime();

    /**
     * Return the amount of time left in the current time-frame
     */
    public long timeLeft();

    /**
     * Get the time (in msec) allocated for current move
     */
    public long getTimeFrame();

    /**
     * Get the start time
     */
    public long getStartTime();

}