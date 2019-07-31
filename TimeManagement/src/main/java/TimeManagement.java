/**
 * TimeManagement class to allocate an timeframe
 */
public interface TimeManagement {

    /**
     * Allocate new time frame for next move
     * The first 40 moves will get 50% of the total time ( totalTime / (40 - doneMoves) )
     *  Each move after that will get less and less time
     */
    public void init();

    /**
     * Stop the timer, reduce the total time left and wait for an new init
     */
    public void reset();

    /**
     * Check if there is enough time to Check for another node/search depth.
     * This method should not stop an search, only tell if there is enough time
     * @return
     */
    public boolean isEnoughTime();

}