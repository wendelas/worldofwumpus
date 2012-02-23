package agent;

import gameboard.GameBoard;
import main.Statistics;

/**
 * @author ebertb schmidbauerk
 * This class plays the wumpus game
 */
public abstract class WumpusPlayer {
	
	public double calcInitialWumpusProb() {
		double numWumpus = (double)GameBoard.NUM_WUMPUS;
		double numCells = (double)((GameBoard.WORLD_WIDTH * GameBoard.WORLD_HEIGHT) - 1);
		return (numWumpus / numCells);
	}
	
	public double calcInitialGoldProbs() {
		double numGold = (double)GameBoard.NUM_GOLD;
		double numCells = (double)((GameBoard.WORLD_WIDTH * GameBoard.WORLD_HEIGHT) - 1);
		return (numGold / numCells);
	}
	
	public double calcInitialPitProbs() {
		double numPits = (double)GameBoard.NUM_PITS;
		double numCells = (double)((GameBoard.WORLD_WIDTH * GameBoard.WORLD_HEIGHT) - 1);
		return (numPits / numCells);
	}
	
	private int x;
	private int y;
	private Direction direction;
	
	private boolean stopped;

	private Statistics results;
	
	private GameBoard world;
	
	public WumpusPlayer(GameBoard world) {
		this.world = world;
		x = GameBoard.START_X;
		y = GameBoard.START_Y;
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
		while (GameBoard.inBounds(ax, ay)) {
			if (world.killWumpus(ax, ay)) {
				wumpusHit = true;
				break;
			} else {
				ax += direction.dx;
				ay += direction.dy;
			}
		}
		
		if (wumpusHit) {
			results.confirmKill();
			onScream();
		} else {
		}
		
		return true;
	}
	
	
	public abstract void onScream();
	
	public boolean moveForward() {
		int mx = x + direction.dx;
		int my = y + direction.dy;
		
		if (!GameBoard.inBounds(mx, my)) {
			
			onBump();
			return false;
		}
		
		
		x = mx;
		y = my;
		results.addStep();
		onMove();
		
		
		if (world.hasWumpus(mx, my)) {
			results.addDeath();
			onDeath();
			
			
			x = GameBoard.START_X;
			y = GameBoard.START_Y;
			onMove();
		} else if (world.hasPit(mx, my)) {

			
			results.addDeath();
			onPitfall();
			
			
			x = GameBoard.START_X;
			y = GameBoard.START_Y;
			onMove();
		}
		
		return true;
	}
	
	public boolean hasGold() {
		return results.hasGold();
	}
	
	public boolean grabGold() {
		if (world.grabGold(x, y)) {
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
		if (world.placeGold(x, y)) {
			results.releaseGold();
			onDrop();
			return true;
		}
		return false;
	}
	
	public void dropCrumb() {
		world.dropCrumb(x, y);
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
		if (world.hasStench(x, y)) {
			return true;
		}
		return false;
	}
	
	public boolean isGlittering() {
		if (world.hasGold(x, y)) {
			return true;
		}
		return false;
	}
	
	public boolean isBreezy() {
		if (world.hasBreeze(x, y)) {
			return true;
		}
		return false;
	}
	
	public boolean isCrumby() {
		if (world.hasCrumb(x, y)) {
			return true;
		}
		return false;
	}
	
	public boolean isVisited() {
		return world.isVisited(x, y);
	}
	
	public Statistics getResults() {
		return results;
	}
}
