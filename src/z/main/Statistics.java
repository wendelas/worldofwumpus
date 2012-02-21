package z.main;

import z.wumpus.WumpusWorld;

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
	 * Modifiers
	 */
	
	
	
	/**
	 * 
	 */
	public void addStops() {
		numStops++;
		score -= WumpusWorld.STOP_COST;
	}

	/**
	 * 
	 */
	public void useArrow() {
		if (hasArrow) {
			hasArrow = false;
			score -= WumpusWorld.ARROW_COST;
		}
	}
	
	/**
	 * 
	 */
	public void KilledWumpus() {
		wumpusKilled = true;
	}
	
	/**
	 * 
	 */
	public void addDeath() {
		Deaths++;
		score -= WumpusWorld.DEATH_COST;
	}

	/**
	 * 
	 */
	public void acquireGold() {
		if (!foundGold) {
			foundGold = true;
			score += WumpusWorld.GOLD_VALUE;
		}
	}

	/**
	 * 
	 */
	public void releaseGold() {
		if (foundGold) {
			foundGold = false;
			score -= WumpusWorld.GOLD_VALUE;
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
		return "Score:,Deaths:,Steps:,Has Arrow?:,Has Gold?:,Wumpus Dead?:,Unwinnable?,Time (ms):,";
	}
	

	@Override
	public String toString() {
		return String.format("%d,%d,%d,%b,%b,%b,%b,%f,",
				score,
				Deaths,
				numStops,
				hasArrow,
				foundGold,
				wumpusKilled,
				unwinnable,
				((double)time) / 1000000f
			);
	}
}
