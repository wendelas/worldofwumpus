package z.main;

import z.wumpus.GameBoard;


/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 *This class holds all the statistics that we keep track of.
 */
public class Statistics {

	private int score;
	private int deaths;
	private int numStops;
	
	private boolean hasArrow;
	private boolean foundGold;
	private boolean wumpusKilled;
	private boolean unsolvable;
	
	private long time;
	
	
	/**
	 * Default constructor to keep track of statistics
	 */
	public Statistics() {
		hasArrow = true; //Starts with the arrow
	}
	
	/**
	 * Increases the number of stops
	 */
	public void addStops() {
		numStops++;
		score -= GameBoard.COST_TO_STEP;
	}

	/**
	 * Uses up the arrow
	 */
	public void useArrow() {
		if (hasArrow) score -= GameBoard.COST_TO_FIRE_ARROW;
		hasArrow = false;
	}
	
	/**
	 * Marks the wumpus as killed
	 */
	public void killedWumpus() {
		wumpusKilled = true;
	}
	
	/**
	 * Increases the death toll
	 */
	public void addDeath() {
		deaths++;
		score -= GameBoard.COST_OF_DEATH;
	}

	/**
	 * Gets the gold
	 */
	public void acquireGold() {
		if (!foundGold) score += GameBoard.VALUE_OF_GOLD;
		foundGold = true;
	}

	/**
	 * Drops the gold
	 */
	public void releaseGold() {
		if (foundGold) score -= GameBoard.VALUE_OF_GOLD;
		foundGold = false;
	}
	
	/**
	 * Marks the board as unsolvable
	 */
	public void markUnsolvable() {
		unsolvable = true;
	}
	
	
	/**
	 * Sets the time
	 * @param t
	 */
	public void setTime(long t) {
		time = t;
	}
	
	/**
	 * @return If the board is unsolvable
	 */
	public boolean isSolvable() {
		return unsolvable;
	}
	
	/**
	 * @return Returns if the gold was found
	 */
	public boolean hasGold() {
		return foundGold;
	}
	
	/**
	 * @return Returns if the wumpus is dead
	 */
	public boolean hasKilledWumpus() {
		return wumpusKilled;
	}
	
	/**
	 * @return returns the total number of agent deaths
	 */
	public int getNumDeaths() {
		return deaths;
	}
	
	/**
	 * @return gets the number of tile stops
	 */
	public int getNumSteps() {
		return numStops;
	}
	
	/**
	 * @return Returns the current score
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * @return Returns if the agent still has the arrow
	 */
	public boolean hasArrow() {
		return hasArrow;
	}
	
	/**
	 * @return Returns the time
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * @return Default column headers 
	 */
	public static String getHeader() {
		return "Score: \tDeaths: \tSteps: \tHas Arrow?: \tHas Gold?: \tWumpus Dead?: \tUnwinnable? \tTime (ms):";
	}
	
	@Override
	public String toString() {
		return String.format("%d \t%d \t%d\t%b \t%b \t%b \t%b \t%f", score, deaths, 
				numStops, hasArrow, foundGold, wumpusKilled, unsolvable, ((double)time)/1000000f);
	}
}
