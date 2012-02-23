package z.wumpus;

import z.main.Statistics;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 * This class plays the wumpus game and is the main AI controller
 */
public abstract class WumpusPlayer {
	
	public double calcInitialWumpusProb() {
		double numWumpus = (double)board.NUMBER_OF_PITS;
		double numCells = (double)((board.boardSize.width * board.boardSize.height) - 1);
		return (numWumpus / numCells);
	}
	
	public double calcInitialGoldProbs() {
		double numGold = (double)board.NUMBER_OF_GOLD;
		double numCells = (double)((board.boardSize.width * board.boardSize.height) - 1);
		return (numGold / numCells);
	}
	
	public double calcInitialPitProbs() {
		double numPits = (double)board.NUMBER_OF_PITS;
		double numCells = (double)((board.boardSize.width * board.boardSize.height) - 1);
		return (numPits / numCells);
	}
	
	private int x;
	private int y;
	private Direction direction;
	
	private boolean stopped;

	private Statistics results;
	
	protected GameBoard board;
	
	public WumpusPlayer(GameBoard board) {
		this.board = board;
		x = 0;
		y = 0;
		direction = Direction.NORTH;
		stopped = false;
		results = new Statistics();
	}
	
	
	public abstract void update();
	
	public boolean isStopped() {
		return stopped;
	}
	
	public int getGoldBars() {
		return (results.hasGold() ? 1 : 0);
	}
	
	public int getKills() {
		return (results.hasKilledWumpus() ? 1 : 0);
	}
	
	public int getDeaths() {
		return results.getNumDeaths();
	}
	
	public int getSteps() {
		return results.getNumSteps();
	}
	
	public int getScore() {
		return results.getScore();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public void turnLeft() {
		direction = Direction.leftOf(direction);
	}
	
	public void turnRight() {
		direction = Direction.rightOf(direction);
	}
	
	public void turnToFace(Direction d) {
		if (d == null) {
			return;
		}
		if (d != direction) {
		}
		direction = d;
	}
	
	public boolean hasArrow() {
		return (results.hasArrow());
	}
	
	public boolean fireArrow() {
		if (!hasArrow()) {
			return false;
		}
		
		boolean wumpusHit = false;
		results.useArrow();
		
		int ax = x + direction.dx;
		int ay = y + direction.dy;
		while (board.inBounds(ax, ay)) {
			if (board.killWumpus(ax, ay)) {
				wumpusHit = true;
				break;
			} else {
				ax += direction.dx;
				ay += direction.dy;
			}
		}
		
		if (wumpusHit) {
			results.killedWumpus();
			onScream();
		} else {
		}
		
		return true;
	}
	
	// Called when the arrow kills the wumpus.
	public abstract void onScream();
	
	public boolean moveForward() {
		int mx = x + direction.dx;
		int my = y + direction.dy;
		
		if (!board.inBounds(mx, my)) {
			// We've bumped into the edge of the world.
			onBump();
			return false;
		}
		
		// Take that step forward.
		x = mx;
		y = my;
		results.addStops();
		onMove();
		
		// If the world has a Wumpus at that point, we die.
		if (board.hasWumpus(mx, my)) {
			results.addDeath();
			onDeath();
			
			// On death, move back to the starting position.
			x = 0;
			y = 0;
			onMove();
		} else if (board.hasPit(mx, my)) {

			// If the world has a pitfall at that point, we die.
			results.addDeath();
			onPitfall();
			
			// On pitfall, move back to the starting position.
			x = 0;
			y = 0;
			onMove();
		}
		
		return true;
	}
	
	public boolean hasGold() {
		return results.hasGold();
	}
	
	public boolean grabGold() {
		if (board.grabGold(x, y)) {
			results.acquireGold();
			onGrab();
			return true;
		}
		return false;
	}
	
	public boolean dropGold() {
		
		if (!results.hasGold()) {
			return false;
		}
		if (board.placeGold(x, y)) {
			results.releaseGold();
			onDrop();
			return true;
		}
		return false;
	}
	
	public void dropMileMarker() {
		board.dropMileMarker(x, y);
	}
	
	
	public void stop(boolean giveUp) {
		stopped = true;
		if (giveUp) {
			results.markUnwinnable();
		}
	}
	
	
	public abstract void onBump();
	
	
	public abstract void onPitfall();
	
	
	public abstract void onDeath();
	
	
	public abstract void onMove();
	
	
	public abstract void onGrab();
	
	
	public abstract void onDrop();
	
	
	public boolean isSmelly() {
		if (board.hasStench(x, y)) {
			return true;
		}
		return false;
	}
	
	public boolean isGlittering() {
		if (board.hasGold(x, y)) {
			return true;
		}
		return false;
	}
	
	public boolean isBreezy() {
		if (board.hasBreeze(x, y)) {
			return true;
		}
		return false;
	}
	
	public boolean isMileMarker() {
		if (board.hasMileMarker(x, y)) {
			return true;
		}
		return false;
	}
	
	public boolean isVisited() {
		return board.isVisited(x, y);
	}
	
	public Statistics getResults() {
		return results;
	}
}
