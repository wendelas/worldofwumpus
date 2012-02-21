package z.wumpus;

import z.main.Statistics;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 * This class plays the wumpus game and is the main AI controller
 */
public abstract class WumpusPlayer {
	
	private static final LogLevel DEFAULT_LOG_LEVEL = LogLevel.OFF;

	public double calcInitialWumpusProb() {
		double numWumpus = (double)WumpusWorld.NUM_WUMPUS;
		double numCells = (double)((WumpusWorld.WORLD_WIDTH * WumpusWorld.WORLD_HEIGHT) - 1);
		return (numWumpus / numCells);
	}
	
	public double calcInitialGoldProbs() {
		double numGold = (double)WumpusWorld.NUM_GOLD;
		double numCells = (double)((WumpusWorld.WORLD_WIDTH * WumpusWorld.WORLD_HEIGHT) - 1);
		return (numGold / numCells);
	}
	
	public double calcInitialPitProbs() {
		double numPits = (double)WumpusWorld.NUM_PITS;
		double numCells = (double)((WumpusWorld.WORLD_WIDTH * WumpusWorld.WORLD_HEIGHT) - 1);
		return (numPits / numCells);
	}
	
	private int x;
	private int y;
	private Direction direction;
	
	private boolean stopped;

	private Statistics results;
	
	private WumpusWorld world;
	private LogLevel logLevel;
	
	public WumpusPlayer(WumpusWorld world) {
		this.world = world;
		x = WumpusWorld.START_X;
		y = WumpusWorld.START_Y;
		direction = Direction.NORTH;
		stopped = false;
		results = new Statistics();
		logLevel = DEFAULT_LOG_LEVEL;
		this.logMessage("You are in a maze of blocky passageways, all alike. Behind you is the door you entered through, now locked. Two doors lead further into the maze.");
	}
	
	// The entry point for evaluating the next move.
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
		while (WumpusWorld.inBounds(ax, ay)) {
			if (world.killWumpus(ax, ay)) {
				wumpusHit = true;
				break;
			} else {
				ax += direction.dx;
				ay += direction.dy;
			}
		}
		
		if (wumpusHit) {
			results.KilledWumpus();
			logMessage("You hear a hearty scream, signifying that your arrow has flown true into the beast's heart.");
			onScream();
		} else {
			logMessage("The arrow goes sailing on into nothingness without a sound, aside from a dry clang and rattle of an arrow bouncing off a wall.");
		}
		
		return true;
	}
	
	// Called when the arrow kills the wumpus.
	public abstract void onScream();
	
	public boolean moveForward() {
		int mx = x + direction.dx;
		int my = y + direction.dy;
		
		if (!WumpusWorld.inBounds(mx, my)) {
			// We've bumped into the edge of the world.
			logMessage("Your blindness sends you careening into a solid wall. That, or your stupidity.");
			onBump();
			return false;
		}
		
		// Take that step forward.
		x = mx;
		y = my;
		results.addStops();
		logMessage("You enter the next room.");
		onMove();
		
		// If the world has a Wumpus at that point, we die.
		if (world.hasWumpus(mx, my)) {
			results.addDeath();
			logMessage("You have been eaten by a Grue... er, I mean, a Wumpus. Suddenly, you wake up... at the main entrance. Finding the gold at this point wouldn't be worth it.");
			onDeath();
			
			// On death, move back to the starting position.
			x = WumpusWorld.START_X;
			y = WumpusWorld.START_Y;
			logMessage("You find yourself in the starting room, with some nasty bite marks.");
			onMove();
		} else if (world.hasPit(mx, my)) {

			// If the world has a pitfall at that point, we die.
			results.addDeath();
			logMessage("Walking carelessly into a large pit, you fall, screaming... and land at the main entrance. ");
			onPitfall();
			
			// On pitfall, move back to the starting position.
			x = WumpusWorld.START_X;
			y = WumpusWorld.START_Y;
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
	
	// To be called when the agent is completed.
	public void stop(boolean giveUp) {
		logMessage("Knowing that this is as good as you can do, you shout the magic words, and are carried out of the maze through a swirling vortex of magic.");
		logMessage("Final Score: " + results.getScore());
		stopped = true;
		if (giveUp) {
			results.markUnwinnable();
		}
	}
	
	// Called when the player bumps into the edge of the Wumpus World.
	public abstract void onBump();
	
	// Called when the player falls into a pit.
	public abstract void onPitfall();
	
	// Called when the player is killed by bumping into the Wumpus.
	public abstract void onDeath();
	
	// Called when the player safely enters a space.
	public abstract void onMove();
	
	// Called when the player safely retrieves the gold.
	public abstract void onGrab();
	
	// Called when the player drops the gold.
	public abstract void onDrop();
	
	// On-demand sensors:
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
