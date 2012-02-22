package z.main;

import z.wumpus.GameBoard;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 *This class holds all the statistics that we keep track of.
 */
public class Statistics {

	private int score;
	private int Deaths;
	private int numStops;
	
	private boolean hasArrow;
	private boolean foundGold;
	private boolean wumpusKilled;
	private boolean unwinnable;
	
	private long time;
	
	public Statistics() {
		score = 0;
		Deaths = 0;
		numStops = 0;
		hasArrow = true;
		foundGold = false;
		wumpusKilled = false;
		unwinnable = false;
		time = 0L;
	}
	
	/**
	 * 
	 */
	public void addStops() {
		numStops++;
		score += GameBoard.STOP_COST;
	}

	/**
	 * 
	 */
	public void useArrow() {
		if (hasArrow) {
			hasArrow = false;
			score += GameBoard.ARROW_COST;
		}
	}
	
	/**
	 * 
	 */
	public void killedWumpus() {
		wumpusKilled = true;
	}
	
	/**
	 * 
	 */
	public void addDeath() {
		Deaths++;
		score += GameBoard.DEATH_COST;
	}

	/**
	 * 
	 */
	public void acquireGold() {
		if (!foundGold) {
			foundGold = true;
			score += GameBoard.GOLD_VALUE;
		}
	}

	/**
	 * 
	 */
	public void releaseGold() {
		if (foundGold) {
			foundGold = false;
			score -= GameBoard.GOLD_VALUE;
		}
	}
	
	/**
	 * 
	 */
	public void markUnwinnable() {
		unwinnable = true;
	}
	
	
	/**
	 * 
	 * @param t
	 */
	public void markTime(long t) {
		time = t;
	}
	
	
	/**
	 * Getters
	 */
	
	/**
	 * 
	 * @return
	 */
	public boolean isUnwinnable() {
		return unwinnable;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasGold() {
		return (foundGold);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasKilledWumpus() {
		return (wumpusKilled);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumDeaths() {
		return Deaths;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumSteps() {
		return numStops;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasArrow() {
		return hasArrow;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getHeader() {
		return String.format("%10s %10s %10s %10s %10s %13s %10s %15s ", 
				"Score:","Deaths:","Steps:","Has Arrow?:","Has Gold?:",
				"Wumpus Dead?:","Unwinnable?","Time (ms):");
	}
	

	@Override
	public String toString() {
		return String.format("%10d %10d %10d %10b %10b %13b %10b %10.5f",
				score,Deaths,numStops,hasArrow,
				foundGold,wumpusKilled,unwinnable,((double)time)/1000000f);
	}
}
