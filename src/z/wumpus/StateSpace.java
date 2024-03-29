package z.wumpus;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aima.logic.propositional.algorithms.KnowledgeBase;
import aima.util.Pair;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12 This class keeps track of the spaces traveled to
 */
public class StateSpace {

	private Map<Point, Tile> spaceMap = new HashMap<Point, Tile>();
	private Map<Point, Double> safetyMap = new HashMap<Point, Double>();

	private KnowledgeBase kb;

	private boolean killedWumpus;

	private Point goldSpace;
	private Point wumpusSpace;
	private GameBoard board;

	/**
	 * Constructor. Initializes a blank state space, and accepts the knowledge
	 * base reference.
	 * 
	 * @param kb
	 *            The knowledge base.
	 */
	public StateSpace(KnowledgeBase kb, GameBoard game) {
		this.kb = kb;
		killedWumpus = false;
		goldSpace = null;
		wumpusSpace = null;
		spaceMap.clear();
		safetyMap.clear();
		this.board = game;
	}

	/**
	 * Gets the associated knowledge base.
	 * 
	 * @return The knowledge base.
	 */
	public KnowledgeBase getKnowledgeBase() {
		return kb;
	}

	/**
	 * Calculates a heuristic-based move cost for moving between an occupied
	 * space, a, and a separate space, b.
	 * 
	 * @param a
	 *            The current space.
	 * @param b
	 *            The destination space.
	 * @return A value representing the cost of moving to the given space.
	 */
	public double getMoveCost(Point a, Point b) {
		return distBetween(a, b) + getSafetyCost(b);
	}

	/**
	 * 
	 * @param space
	 * @return
	 */
	public double getSafetyCost(Point space) {
		if (!safetyMap.containsKey(space)) {
			updateSafetyCost(space);
		}
		return safetyMap.get(space);
	}

	/**
	 * Updates the cached safety cost for a given space. This method is very
	 * computationally expensive for states that haven't yet been visited!
	 * 
	 * @param space
	 *            The space to update.
	 */
	private void updateSafetyCost(Point space) {

		if (space == null) {
			return;
		}

		double cost = 0.0;

		int x = space.x;
		int y = space.y;

		if (isCrumb(x, y)) {
			cost += GameBoard.CRUMB_COST;
		}

		if (isPit(x, y) || (!isWumpusDead() && isWumpus(x, y))) {

			cost += (double) GameBoard.DEATH_COST;

		} else if (!isVisited(x, y)) {

			int numUnvisitedSpaces = getUnvisitedSpaces().size();
			if (numUnvisitedSpaces > 0 && !isVisited(x, y)) {

				int numMissingPits = board.NUMBER_OF_PITS
						- getKnownPitSpaces().size();
				int numMissingWumpi = board.NUMBER_OF_WUMPUS
						- getKnownWumpusSpaces().size();

				double baseChanceOfPit = ((double) numMissingPits)
						/ ((double) numUnvisitedSpaces);

				double baseChanceOfWumpus = 0.0;
				if (!isWumpusDead()) {
					baseChanceOfWumpus = ((double) numMissingWumpi)
							/ ((double) numUnvisitedSpaces);
				}

				List<Point> neighbors = getNeighbors(space);
				int numBreezes = 0;
				int numStenches = 0;
				for (Point neighbor : neighbors) {
					int nx = neighbor.x;
					int ny = neighbor.y;

					if (isVisited(nx, ny)) {
						if (!isBreeze(nx, ny)) {
							baseChanceOfPit = 0.0;
						}
						if (!isStench(nx, ny)) {
							baseChanceOfWumpus = 0.0;
						}
					}

					if (isBreeze(nx, ny)) {
						numBreezes++;
					}

					if (isStench(nx, ny)) {
						numStenches++;
					}
				}

				cost += (((double) numBreezes / 4.0)
						* ((double) GameBoard.DEATH_COST) * (baseChanceOfPit))
						+ (((double) numStenches / 4.0)
								* ((double) GameBoard.DEATH_COST) * (baseChanceOfWumpus));
			}
		}

		safetyMap.put(space, cost);
	}

	/**
	 * Calculates the ideal heuristic distance between two spaces.
	 * 
	 * @param a
	 *            The first space.
	 * @param b
	 *            The second space.
	 * @return The heuristic distance estimate between the two points.
	 */
	public static double distBetween(Point a, Point b) {
		double dx = (double) (a.x - b.x);
		double dy = (double) (a.y - b.y);
		return ((double) GameBoard.STOP_COST * Math.sqrt((dx * dx) + (dy * dy)));
	}

	/**
	 * Tells whether or not the wumpus has been killed.
	 * 
	 * @return <b>true</b> if the wumpus is dead, <b>false</b> otherwise.
	 */
	public boolean isWumpusDead() {
		return killedWumpus;
	}

	/**
	 * Calculates a most-likely gold location estimation, by averaging the
	 * coordinates of all unexplored spaces.
	 * 
	 * @return A pair of double-precision floating point values representing the
	 *         approximate location.
	 */
	public Pair<Double, Double> estimateGoldSpace() {
		if (goldSpace != null) {
			return new Pair<Double, Double>((double) goldSpace.x,
					(double) goldSpace.y);
		} else {
			double dx = 0.0;
			double dy = 0.0;
			List<Point> unvisited = getUnvisitedSpaces();
			double n = (double) unvisited.size();
			for (Point space : unvisited) {
				dx += (double) space.x;
				dy += (double) space.y;
			}
			dx /= n;
			dy /= n;
			return new Pair<Double, Double>(dx, dy);
		}
	}

	/**
	 * Calculates a most-likely wumpus location estimation, by averaging the
	 * coordinates of all unexplored spaces.
	 * 
	 * @return An approximation of the wumpus space.
	 */
	public Point estimateWumpusSpace() {
		if (wumpusSpace != null) {
			return new Point(wumpusSpace.x, wumpusSpace.y);
		} else {
			double dx = 0.0;
			double dy = 0.0;
			List<Point> territory = getWumpusTerritory();
			int n = territory.size();
			for (Point space : territory) {
				int px = space.x;
				int py = space.y;
				if (isStench(px, py) || isWumpus(px, py)) {
					px *= 3;
					py *= 3;
					n += 2;
				}
				dx += (double) px;
				dy += (double) py;
			}
			dx /= (double) n;
			dy /= (double) n;
			return new Point((int) dx, (int) dy);
		}
	}

	/**
	 * Retrieves the space determined to contain the wumpus.
	 * 
	 * @return The wumpus space.
	 */
	public Point getWumpusSpace() {
		return wumpusSpace;
	}

	/**
	 * Retrieves the space determined to contain the gold.
	 * 
	 * @return The gold space.
	 */
	public Point getGoldSpace() {
		return goldSpace;
	}

	/**
	 * Represents a conditional function object, which serves as a search
	 * criteria for the state space.
	 * 
	 * @author iannonen
	 */
	public interface Conditional {

		/**
		 * Evaluates the condition.
		 * 
		 * @return <b>true</b> if the space is to be selected, <b>false</b>
		 *         otherwise.
		 */
		public boolean evaluate(int x, int y);

	};

	/**
	 * Fetches a list of all matching spaces which satisfy a given condition.
	 * 
	 * @param c
	 *            A condition to evaluate.
	 * @return A list of matching states.
	 */
	public List<Point> selectMatchingSpaces(Conditional c) {
		List<Point> list = new LinkedList<Point>();
		if (c != null) {
			for (int x = 0; x < board.boardSize.width; x++) {
				for (int y = 0; y < board.boardSize.height; y++) {
					if (c.evaluate(x, y)) {
						list.add(new Point(x, y));
					}
				}
			}
		}
		return list;
	}

	/**
	 * Gets a set of all previously-traversed, safe spaces. To be used by the
	 * PathResolver.
	 * 
	 * @return A list of spaces.
	 */
	public List<Point> getTraversableSpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isVisited(x, y) && isSafe(x, y));
			}
		});
	}

	/**
	 * Gets a set of all spaces which could indicate the presence of a Wumpus.
	 * To be used by the PathResolver.
	 * 
	 * @return A list of spaces.
	 */
	public List<Point> getWumpusTerritory() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isWumpus(x, y) || isStench(x, y) || !isVisited(x, y));
			}
		});
	}

	/**
	 * Gets a set of all untraversed spaces.
	 * 
	 * @return A list of spaces.
	 */
	public List<Point> getUnvisitedSpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (!isVisited(x, y));
			}
		});
	}

	/**
	 * Gets a set of all spaces known to contain wumpi.
	 * 
	 * @return A list of spaces with wumpi.
	 */
	public List<Point> getKnownWumpusSpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isWumpus(x, y));
			}
		});
	}

	/**
	 * Gets a set of all spaces known to contain pits.
	 * 
	 * @return A list of spaces with pits.
	 */
	public List<Point> getKnownPitSpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isPit(x, y));
			}
		});
	}

	/**
	 * Gets a set of all spaces known to contain breezes.
	 * 
	 * @return A list of spaces with breezes.
	 */
	public List<Point> getKnownBreezySpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isBreeze(x, y));
			}
		});
	}

	/**
	 * Gets a set of all spaces known to contain stench.
	 * 
	 * @return A list of spaces with stench.
	 */
	public List<Point> getKnownStinkySpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isStench(x, y));
			}
		});
	}

	/**
	 * Marks the wumpus as having been killed.
	 */
	public void noteWumpusDead() {
		killedWumpus = true;
		wumpusSpace = null;
	}

	/**
	 * Utility function; converts the given coordinates to a string
	 * representation, for feeding into the knowledge base. Shifts from 0-based
	 * to 1-based indexing.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @return The knowledge base coordinate string.
	 */
	public static String toKBCoords(int x, int y) {
		return "" + (x + 1) + (y + 1);
	}

	/**
	 * Updates the state space via the knowledge base. Should be called whenever
	 * new percepts are entered into the knowledge base.
	 */
	public void update() {
		for (int x = 0; x < board.boardSize.width; x++) {
			for (int y = 0; y < board.boardSize.height; y++) {
				String p = toKBCoords(x, y);
				if (Solver.plfce.plfcEntails(kb, "P" + p)) {
					markPit(x, y);
				}
				if (Solver.plfce.plfcEntails(kb, "S" + p)) {
					markSmell(x, y);
				}
				if (!killedWumpus
						&& Solver.plfce.plfcEntails(kb, "W" + p)) {
					markWumpus(x, y);
				}
				if (Solver.plfce.plfcEntails(kb, "B" + p)) {
					markBreeze(x, y);
				}
				if (Solver.plfce.plfcEntails(kb, "C" + p)) {
					markCrumb(x, y);
				}
				if (Solver.plfce.plfcEntails(kb, "G" + p)) {
					markGold(x, y);
				}
				if (Solver.plfce.plfcEntails(kb, "V" + p)) {
					markVisited(x, y);
				}
			}
		}

		safetyMap.clear();

		List<Point> fringe = getFringe();
		for (Point space : fringe) {
			updateSafetyCost(space);
		}
	}

	/**
	 * Marks the given space with the specified flags.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @param flags
	 *            The set of bit flags to mark.
	 */
	private Tile getMark(int x, int y) {
		Point p = new Point(x, y);
		if (!spaceMap.containsKey(p))
			spaceMap.put(p, new Tile());
		return spaceMap.get(p);
	}

	/**
	 * Marks the given space as having been visited by the agent.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 */
	public void markVisited(int x, int y) {
		board.markVisited(x, y);
	}

	/**
	 * Marks the given space as being smelly.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 */
	public void markSmell(int x, int y) {
		getMark(x, y).stench = true;
	}

	/**
	 * Marks the given space as having a pit.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 */
	public void markPit(int x, int y) {
		getMark(x, y).pit = true;
	}

	/**
	 * Marks the given space as having a Wumpus.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 */
	public void markWumpus(int x, int y) {
		getMark(x, y).wumpus = true;
		Point space = new Point(x, y);
		if (wumpusSpace == null || !wumpusSpace.equals(space))
			wumpusSpace = space;
	}

	/**
	 * Marks the given space as being breezy.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 */
	public void markBreeze(int x, int y) {
		getMark(x, y).breeze = true;
	}

	/**
	 * Marks the given space as having gold.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 */
	public void markGold(int x, int y) {
		getMark(x, y).gold = true;
		Point space = new Point(x, y);
		if (goldSpace == null || !goldSpace.equals(space))
			goldSpace = space;
	}

	/**
	 * Marks the given space as having a crumb.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 */
	public void markCrumb(int x, int y) {
		getMark(x, y).mileMarker++;
	}

	/**
	 * Fetches a list of spaces on the fringe of the explored region.
	 * 
	 * @return A list of fringe spaces.
	 */
	public List<Point> getFringe() {

		List<Point> fringe = new LinkedList<Point>();

		for (Point space : spaceMap.keySet()) {
			if (isVisited(space.x, space.y)) {
				List<Point> neighbors = getNeighbors(space);
				for (Point neighbor : neighbors) {
					int nx = neighbor.x;
					int ny = neighbor.y;
					if (isVisited(nx, ny) || !isSafe(nx, ny)) {
						continue;
					}

					if (fringe.contains(neighbor)) {
						continue;
					}

					fringe.add(neighbor);
				}
			}
		}

		return fringe;
	}

	/**
	 * Tells whether the given space has been visited.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @return <b>true</b> if the space has been visited; <b>false</b>
	 *         otherwise.
	 */
	public boolean isVisited(int x, int y) {
		return getMark(x, y).visited;
	}

	/**
	 * Tells whether the given space is safe; ie. not a known pit or a wumpus.
	 * Also considers whether the wumpus has been killed.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @return <b>true</b> if the space is safe; <b>false</b> otherwise.
	 */
	public boolean isSafe(int x, int y) {
		return (!isPit(x, y) && (killedWumpus || !isWumpus(x, y)));
	}

	/**
	 * Tells whether the given space is known to contain a pit.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @return <b>true</b> if the space has a pit; <b>false</b> otherwise.
	 */
	public boolean isPit(int x, int y) {
		return getMark(x, y).pit;
	}

	/**
	 * Tells whether the given space is known to contain a wumpus.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @return <b>true</b> if the space has a wumpus; <b>false</b> otherwise.
	 */
	public boolean isWumpus(int x, int y) {
		return getMark(x, y).wumpus;
	}

	/**
	 * Tells whether the given space is known to contain a breeze.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @return <b>true</b> if the space has a breeze; <b>false</b> otherwise.
	 */
	public boolean isBreeze(int x, int y) {
		return getMark(x, y).breeze;
	}

	/**
	 * Tells whether the given space is known to contain a stench.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @return <b>true</b> if the space has a stench; <b>false</b> otherwise.
	 */
	public boolean isStench(int x, int y) {
		return getMark(x, y).stench;
	}

	/**
	 * Tells whether the given space is known to contain a crumb.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @return <b>true</b> if the space has a crumb; <b>false</b> otherwise.
	 */
	public boolean isCrumb(int x, int y) {
		return getMark(x, y).mileMarker > 0;
	}

	/**
	 * Tells whether the given space is known to contain gold.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @return <b>true</b> if the space has gold; <b>false</b> otherwise.
	 */
	public boolean isGold(int x, int y) {
		return getMark(x, y).gold;
	}

	/**
	 * Tells whether or not the state space has deduced the location of the
	 * wumpus.
	 * 
	 * @return <b>true</b> if the wumpus space is known, <b>false</b> otherwise.
	 */
	public boolean knowsWumpusSpace() {
		return (wumpusSpace != null);
	}

	/**
	 * Returns a list of all existing neighbors of the given space.
	 * 
	 * @param space
	 *            The central space.
	 * @return A list of neighboring spaces.
	 */
	public List<Point> getNeighbors(Point space) {
		List<Point> list = new LinkedList<Point>();
		if (space == null) {
			return list;
		}
		for (Direction d : Direction.values()) {
			Point neighbor = new Point(space.x + d.dx, space.y + d.dy);
			if (board.inBounds(neighbor.x, neighbor.y)) {
				list.add(neighbor);
			}
		}
		return list;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return makeString(-1, -1);
	}

	/**
	 * Converts the state space into a printable string format.
	 * 
	 * @param currX
	 *            The player's x-coordinate.
	 * @param currY
	 *            The player's y-coordinate.
	 * @return The state space as a string.
	 */
	public String makeString(int currX, int currY) {
		List<Point> fringe = getFringe();
		String s = "-----------------------------------\n";
		for (int y = board.boardSize.height - 1; y >= 0; y--) {
			for (int x = 0; x < board.boardSize.width; x++) {
				Point space = new Point(x, y);
				String data = "?";
				String start = "*";
				String end = "*";

				if (spaceMap.containsKey(space)) {
					Tile code = spaceMap.get(space);

					data = code.toString();

					if (data.isEmpty()) {
						data = "_";
					}

					if (code.visited) {
						start = "[";
						end = "]";
					}

					if (code.mileMarker>0) {
						start = "(";
						end = ")";
					}

					if (x == currX && y == currY) {
						start = "<";
						end = ">";
					}
				} else {
					data = "?";
				}

				if (fringe.contains(space)) {
					start = "{";
					end = "}";
				}
				s += start + data + end + "\t";
			}
			s = s.trim() + "\n";
		}
		return s.trim().replace('*', ' ')
				+ "\n-----------------------------------";
	}
}
