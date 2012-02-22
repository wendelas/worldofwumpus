/**
 * 
 */
package main;

import Gameboard.GameBoard;

/**
 * @author iannonen
 *
 */
public class Statistics {

	private int score;
	private int numDeaths;
	private int numSteps;
	
	private boolean hasArrow;
	private boolean hasGold;
	private boolean killedWumpus;
	private boolean unwinnable;
	
	private long time;
	
	public Statistics() {
		score = 0;
		numDeaths = 0;
		numSteps = 0;
		hasArrow = true;
		hasGold = false;
		killedWumpus = false;
		unwinnable = false;
		time = 0L;
	}
	
	public void addStep() {
		numSteps++;
		score -= GameBoard.STEP_COST;
	}
	
	public boolean hasGold() {
		return (hasGold);
	}
	
	public boolean hasKilledWumpus() {
		return (killedWumpus);
	}
	
	public int getNumDeaths() {
		return numDeaths;
	}
	
	public int getNumSteps() {
		return numSteps;
	}
	
	public int getScore() {
		return score;
	}
	
	public boolean hasArrow() {
		return hasArrow;
	}

	public void useArrow() {
		if (hasArrow) {
			hasArrow = false;
			score -= GameBoard.ARROW_COST;
		}
	}
	
	public void confirmKill() {
		killedWumpus = true;
	}
	
	public void addDeath() {
		numDeaths++;
		score -= GameBoard.DEATH_COST;
	}

	public void acquireGold() {
		if (!hasGold) {
			hasGold = true;
			score += GameBoard.GOLD_VALUE;
		}
	}

	public void releaseGold() {
		if (hasGold) {
			hasGold = false;
			score -= GameBoard.GOLD_VALUE;
		}
	}
	
	public void markUnwinnable() {
		unwinnable = true;
	}
	
	public boolean isUnwinnable() {
		return unwinnable;
	}
	
	public static String getHeader() {
		return "Score:,Deaths:,Steps:,Has Arrow?:,Has Gold?:,Wumpus Dead?:,Unwinnable?,Time (ms):,";
	}
	
	@Override
	public String toString() {
		return String.format("%d,%d,%d,%b,%b,%b,%b,%f,",
				score,
				numDeaths,
				numSteps,
				hasArrow,
				hasGold,
				killedWumpus,
				unwinnable,
				((double)time) / 1000000f
			);
	}

	public void markTime(long t) {
		time = t;
	}
	
	public long getTime() {
		return time;
	}
}