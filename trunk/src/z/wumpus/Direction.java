package z.wumpus;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12 This class keeps track Directions we can go
 */
public enum Direction {
	NORTH(0, 1), SOUTH(0, -1), EAST(1, 0), WEST(-1, 0);

	int dx;
	int dy;

	Direction(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public String toString() {
		switch (this) {
		case NORTH:
			return "north";
		case WEST:
			return "west";
		case SOUTH:
			return "south";
		case EAST:
			return "east";
		default:
			return "an unknown direction";
		}
	}

	public static Direction nextChoice(Direction current) {
		switch (current) {
		case NORTH:
			return WEST;
		case WEST:
			return SOUTH;
		case SOUTH:
			return EAST;
		case EAST:
			return NORTH;
		default:
			return null;
		}
	}

	public static Direction prevChoice(Direction current) {
		switch (current) {
		case NORTH:
			return EAST;
		case EAST:
			return SOUTH;
		case SOUTH:
			return WEST;
		case WEST:
			return NORTH;
		default:
			return null;
		}
	}
}