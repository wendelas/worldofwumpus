package agent;

import main.Statistics;
import Gameboard.LogLevel;
import Gameboard.GameBoard;

/**
 * @author ebertb schmidbauerk
 * This class plays the wumpus game
 */
public abstract class WumpusPlayer {
	
	private static final LogLevel DEFAULT_LOG_LEVEL = LogLevel.OFF;

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
	private LogLevel logLevel;
	
	public WumpusPlayer(GameBoard world) {
		this.world = world;
		x = GameBoard.START_X;
		y = GameBoard.START_Y;
		direction = Direction.NORTH;
		stopped = false;
		results = new Statistics();
		logLevel = DEFAULT_LOG_LEVEL;
		this.logMessage("You are in a maze of blocky passageways, all alike. Behind you is the door you entered through, now locked. Two doors lead further into the maze.");
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
		logMessage("You turn left, towards the " + direction.toString() + ".");
	}
	
	public void turnRight() {
		direction = Direction.rightOf(direction);
		logMessage("You turn right, towards the " + direction.toString() + ".");
	}
	
	public void turnToFace(Direction d) {
		if (d == null) {
			return;
		}
		if (d != direction) {
			logMessage("You turn to face " + d.toString() + ".");
		}
		direction = d;
	}
	
	public boolean hasArrow() {
		return (results.hasArrow());
	}
	
	public boolean fireArrow() {
		if (!hasArrow()) {
			logMessage("You reach into your quiver for an arrow... only to realize you have none. Drats.");
			return false;
		}
		
		logMessage("You draw your lone arrow from your quiver, nock it, and draw back...");
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
			logMessage("You hear a hearty scream, signifying that your arrow has flown true into the beast's heart.");
			onScream();
		} else {
			logMessage("The arrow goes sailing on into nothingness without a sound, aside from a dry clang and rattle of an arrow bouncing off a wall.");
		}
		
		return true;
	}
	
	
	public abstract void onScream();
	
	public boolean moveForward() {
		int mx = x + direction.dx;
		int my = y + direction.dy;
		
		if (!GameBoard.inBounds(mx, my)) {
			
			logMessage("Your blindness sends you careening into a solid wall. That, or your stupidity.");
			onBump();
			return false;
		}
		
		
		x = mx;
		y = my;
		results.addStep();
		logMessage("You enter the next room.");
		onMove();
		
		
		if (world.hasWumpus(mx, my)) {
			results.addDeath();
			logMessage("You have been eaten by a Grue... er, I mean, a Wumpus. Suddenly, you wake up... at the main entrance. Finding the gold at this point wouldn't be worth it.");
			onDeath();
			
			
			x = GameBoard.START_X;
			y = GameBoard.START_Y;
			logMessage("You find yourself in the starting room, with some nasty bite marks.");
			onMove();
		} else if (world.hasPit(mx, my)) {

			
			results.addDeath();
			logMessage("Walking carelessly into a large pit, you fall, screaming... and land at the main entrance. ");
			onPitfall();
			
			
			x = GameBoard.START_X;
			y = GameBoard.START_Y;
			logMessage("You find yourself in the starting room, with a large bruise where you landed.");
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
			logMessage("You heft the solid lump of gold, and stash it in your pack. Victory!");
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
			logMessage("For some unknown reason, you decide to drop the golden prize and run for your life. From what? Who knows.");
			onDrop();
			return true;
		}
		return false;
	}
	
	public void dropCrumb() {
		world.dropCrumb(x, y);
	}
	
	
	public void stop(boolean giveUp) {
		logMessage("Knowing that this is as good as you can do, you shout the magic words, and are carried out of the maze through a swirling vortex of magic.");
		logMessage("Final Score: " + results.getScore());
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
			logMessage("Something rancid taunts your nostrils, and sends chills down your spine. The Wumpus is near.");
			return true;
		}
		return false;
	}
	
	public boolean isGlittering() {
		if (world.hasGold(x, y)) {
			logMessage("You detect a faint glimmer in the corner of the room. Upon closer inspection, you determine it is a large lump of gold.");
			return true;
		}
		return false;
	}
	
	public boolean isBreezy() {
		if (world.hasBreeze(x, y)) {
			logMessage("There seems to be a stirring in the air, but you can't make out the source of the breeze.");
			return true;
		}
		return false;
	}
	
	public boolean isCrumby() {
		if (world.hasCrumb(x, y)) {
			logMessage("You see some breadcrumbs on the ground. This is getting annoying.");
			return true;
		}
		return false;
	}
	
	public boolean isVisited() {
		return world.isVisited(x, y);
	}
	
	public void logMessage(String msg) {
		switch (logLevel) {
		case WARN:
			System.out.printf("WARNING: [%d,%d]: %s\n", x, y, msg);
			break;
		case ERROR:
			System.err.printf("[%d,%d]: %s\n", x, y, msg);
			break;
		case LOG:
			System.out.printf("[%d,%d]: %s\n", x, y, msg);
			break;
		case OFF:
		default:
		} 
	}
	
	public LogLevel pushLogLevel(LogLevel level) {
		LogLevel temp = logLevel;
		logLevel = level;
		return temp;
	}
	
	public void popLogLevel(LogLevel level) {
		logLevel = level;
	}
	
	public LogLevel getLogLevel() {
		return logLevel;
	}
	
	public Statistics getResults() {
		return results;
	}
}
