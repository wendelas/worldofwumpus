package z.wumpus;

import java.util.Random;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 * This class keeps track of the wumpus world
 */
public class WumpusWorld implements Cloneable {

	public static int STENCH_FLAG = (1<<0);
	public static int BREEZE_FLAG = (1<<1);
	public static int GOLD_FLAG = (1<<2);
	public static int PIT_FLAG = (1<<3);
	public static int WUMPUS_FLAG = (1<<4);
	public static int START_FLAG = (1<<5);
	public static int CRUMB_FLAG = (1<<6);
	public static int VISITED_FLAG = (1<<7);
	
	public static int WORLD_WIDTH = 4;
	public static int WORLD_HEIGHT = 4;
	
	public static int START_X = 0;
	public static int START_Y = 0;
	
	public static int NUM_WUMPUS = 1;
	public static int NUM_GOLD = 1;
	public static int NUM_PITS = 3;
	
	public static final int DEATH_COST = 1000;
	public static final int STOP_COST = 1;
	public static final int ARROW_COST = 10;
	public static final int GOLD_VALUE = 1000;
	public static final double CRUMB_COST = 0.5;
	
	private int flags[][];
	
	/**
	 * Default constructor.
	 * Initializes the Wumpus World as empty.
	 */
	public WumpusWorld() {
		flags = new int[WORLD_WIDTH][WORLD_HEIGHT];
		
		// Designate the lower-left corner as the start location.
		flags[START_X][START_Y] = START_FLAG;
	}
	
	public Object clone() {
		WumpusWorld newWorld = new WumpusWorld();
		for (int x = 0; x < WORLD_WIDTH; x++) {
			for (int y = 0; y < WORLD_HEIGHT; y++) {
				newWorld.flags[x][y] = flags[x][y];
			}
		}
		return newWorld;
	}
	
	public static boolean inBounds(int x, int y) {
		if (x < 0 || x >= WORLD_WIDTH) {
			return false;
		}
		if (y < 0 || y >= WORLD_HEIGHT) {
			return false;
		}
		return true;
	}
	
	public boolean isVisited(int x, int y) {
		if (!inBounds(x,y)) {
			return false;
		}
		return ((flags[x][y] & VISITED_FLAG) != 0);
	}
	
	public boolean markVisited(int x, int y) {
		if (!inBounds(x,y)) {
			return false;
		}
		flags[x][y] |= VISITED_FLAG;
		return true;
	}
	
	public boolean hasCrumb(int x, int y) {
		if (!inBounds(x,y)) {
			return false;
		}
		return ((flags[x][y] & CRUMB_FLAG) != 0);
	}
	
	public boolean dropCrumb(int x, int y) {
		if (!inBounds(x,y)) {
			return false;
		}
		flags[x][y] |= CRUMB_FLAG;
		return true;
	}
	
	public boolean hasWumpus(int x, int y) {
		if (!inBounds(x,y)) {
			return false;
		}
		return ((flags[x][y] & WUMPUS_FLAG) != 0);
	}
	
	public boolean killWumpus(int x, int y) {
		if (!hasWumpus(x,y)) {
			return false;
		}
		flags[x][y] &= ~(WUMPUS_FLAG);
		return true;
	}
	
	public boolean hasPit(int x, int y) {
		if (!inBounds(x,y)) {
			return false;
		}
		return ((flags[x][y] & PIT_FLAG) != 0);
	}
	
	public boolean isStart(int x, int y) {
		return (x == START_X && y == START_Y);
	}
	
	public boolean hasGold(int x, int y) {
		if (!inBounds(x,y)) {
			return false;
		}
		return ((flags[x][y] & GOLD_FLAG) != 0);
	}
	
	public boolean placeGold(int x, int y) {
		if (!inBounds(x,y) || hasGold(x,y)) {
			return false;
		}
		flags[x][y] |= GOLD_FLAG;
		return true;
	}
	
	public boolean grabGold(int x, int y) {
		if (!inBounds(x,y) || !hasGold(x,y)) {
			return false;
		}
		flags[x][y] &= (~GOLD_FLAG);
		return true;
	}
	
	public boolean hasStench(int x, int y) {
		if (!inBounds(x,y)) {
			return false;
		}
		return ((flags[x][y] & STENCH_FLAG) != 0);
	}
	
	public boolean hasBreeze(int x, int y) {
		if (!inBounds(x,y)) {
			return false;
		}
		return ((flags[x][y] & BREEZE_FLAG) != 0);
	}
	
	public void randomize(Random r) {
		for (int numPits = 0; numPits < NUM_PITS; numPits++) {
			int px = r.nextInt(WORLD_WIDTH);
			int py = r.nextInt(WORLD_HEIGHT);
			// Check repeatedly, reselecting as necessary.
			while (hasPit(px,py) || isStart(px,py)) {
				px = r.nextInt(WORLD_WIDTH);
				py = r.nextInt(WORLD_HEIGHT);
			}
			// Mark the cell at that point as a pit.
			flags[px][py] |= PIT_FLAG;
			// Add a breeze to the surrounding cells.
			for (Direction d : Direction.values()) {
				int bx = px + d.dx;
				int by = py + d.dy;
				if (inBounds(bx, by)) {
					flags[bx][by] |= BREEZE_FLAG;
				}
			}
		}
		
		// Generate the gold.
		int gx = r.nextInt(WORLD_WIDTH);
		int gy = r.nextInt(WORLD_HEIGHT);
		while (hasPit(gx,gy) || isStart(gx,gy)) {
			gx = r.nextInt(WORLD_WIDTH);
			gy = r.nextInt(WORLD_HEIGHT);
		}
		// Mark the cell at that point as having gold.
		flags[gx][gy] |= GOLD_FLAG;
		
		// Generate the wumpus.
		int wx = r.nextInt(WORLD_WIDTH);
		int wy = r.nextInt(WORLD_HEIGHT);
		while (hasPit(wx,wy) || isStart(wx,wy) || hasGold(wx,wy)) {
			wx = r.nextInt(WORLD_WIDTH);
			wy = r.nextInt(WORLD_HEIGHT);
		}
		// Mark the cell at that point as having the wumpus.
		flags[wx][wy] |= WUMPUS_FLAG;
		// Add a stench to the surrounding cells.
		for (Direction d : Direction.values()) {
			int sx = wx + d.dx;
			int sy = wy + d.dy;
			if (inBounds(sx, sy)) {
				flags[sx][sy] |= STENCH_FLAG;
			}
		}
	}
	
	public String toString() {
		String s = "";
		for (int y = WORLD_HEIGHT - 1; y >= 0; y--) {
			for (int x = 0; x < WORLD_WIDTH; x++) {
				s += "[" +
					(hasStench(x,y) ? "S" : "") +
					(hasBreeze(x,y) ? "B" : "") +
					(hasGold(x,y) ? "G" : "") +
					(hasPit(x,y) ? "P" : "") +
					(hasWumpus(x,y) ? "W" : "") + "]\t";
			}
			s = s.trim() + "\n";
		}
		return s.trim();
	}
}
