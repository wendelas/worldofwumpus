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
 * @date 2/17/12
 * This class keeps track of the spaces traveled to
 */
public class StateSpace {

	private Map<Point, Integer> spaceMap = new HashMap<Point, Integer>();
	private Map<Point, Double> safetyMap = new HashMap<Point, Double>();
	
	private KnowledgeBase kb;
	
	private boolean killedWumpus;
	
	private Point goldSpace;
	private Point wumpusSpace;
	
	/**
	 * Constructor. Initializes a blank state space, and accepts the knowledge base reference.
	 * @param kb The knowledge base.
	 */
	public StateSpace(KnowledgeBase kb) {
		this.kb = kb;
		killedWumpus = false;
		goldSpace = null;
		wumpusSpace = null;
		spaceMap.clear();
		safetyMap.clear();
	}

	/**
	 * Gets the associated knowledge base.
	 * @return The knowledge base.
	 */
	public KnowledgeBase getKnowledgeBase() {
		return kb;
	}
	
	/**
	 * Calculates a heuristic-based move cost for moving between an occupied space, a, and a separate space, b.
	 * @param a The current space.
	 * @param b The destination space.
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
	 * Updates the cached safety cost for a given space.
	 * This method is very computationally expensive for states that haven't yet been visited!
	 * @param space The space to update.
	 */
	private void updateSafetyCost(Point space) {
		
		if (space == null) {
			return;
		}
		
		double cost = 0.0;
		
		int x = space.x;
		int y = space.y;
		
		// If the pit contains a breadcrumb, increase the cost by 0.5.
		if (isCrumb(x,y)) {
			cost += WumpusWorld.CRUMB_COST;
		}
		
		// Regardless of whether we've visited it or not, if we know it's a pit or a wumpus, then we know it's deadly.
		if (isPit(x,y) || (!isWumpusDead() && isWumpus(x,y))) {
			
			cost += (double)WumpusWorld.DEATH_COST;	// Score hit taken when falling into a pit or being eaten.
			
		} else if (!isVisited(x,y)) {
			// If we haven't visited it, try it out.
			int numUnvisitedSpaces = getUnvisitedSpaces().size();
			if (numUnvisitedSpaces > 0 && !isVisited(x, y)) {
				
				// Get the number of pits/wumpi still not found.
				int numMissingPits = WumpusWorld.NUM_PITS - getKnownPitSpaces().size();
				int numMissingWumpi = WumpusWorld.NUM_WUMPUS - getKnownWumpusSpaces().size();
				
				// Compute the base likelihood that a space is a pit.
				double baseChanceOfPit = ((double)numMissingPits) / ((double)numUnvisitedSpaces);
				
				// Compute the base likelihood that a space is a wumpus.
				double baseChanceOfWumpus = 0.0;
				if (!isWumpusDead()) {
					baseChanceOfWumpus = ((double)numMissingWumpi) / ((double)numUnvisitedSpaces);
				}
			
				// Offset the likelihoods by doing a search of the neighboring spaces.
				List<Point> neighbors = getNeighbors(space);
				int numBreezes = 0;
				int numStenches = 0;
				for (Point neighbor : neighbors) {
					int nx = neighbor.x;
					int ny = neighbor.y;
					
					// If we've visited a surrounding space and found it to not have a breeze or stench, drop to 0%.
					if (isVisited(nx, ny)) {
						if (!isBreezy(nx, ny)) {
							baseChanceOfPit = 0.0;
						}
						if (!isSmelly(nx, ny)) {
							baseChanceOfWumpus = 0.0;
						}
					}
					
					// If the space is breezy, add one to the number of surrounding breezes.
					if (isBreezy(nx, ny)) {
						numBreezes++;
					}
					// If the space is smelly, add one to the number of surrounding stenches.
					if (isSmelly(nx, ny)) {
						numStenches++;
					}
				}
				
				// Recompute the estimated cost for pit guessing.
				// Take the number of confirmed breezes/stenches over the max amount, times the cost of death, times the base chance of there being a pit.
				cost += (((double)numBreezes / 4.0) * ((double)WumpusWorld.DEATH_COST) * (baseChanceOfPit))
						+ (((double)numStenches / 4.0) * ((double)WumpusWorld.DEATH_COST) * (baseChanceOfWumpus));
			}
		}
		
		// Store the computed cost.
		safetyMap.put(space, cost);
	}

	/**
	 * Calculates the ideal heuristic distance between two spaces.
	 * @param a The first space.
	 * @param b The second space.
	 * @return The heuristic distance estimate between the two points.
	 */
	public static double distBetween(Point a, Point b) {
		double dx = (double)(a.x - b.x);
		double dy = (double)(a.y - b.y);
		return ((double)WumpusWorld.STOP_COST * Math.sqrt((dx * dx) + (dy * dy)));
	}
	
	/**
	 * Tells whether or not the wumpus has been killed.
	 * @return <b>true</b> if the wumpus is dead, <b>false</b> otherwise.
	 */
	public boolean isWumpusDead() {
		return killedWumpus;
	}
	
	/**
	 * Calculates a most-likely gold location estimation, by averaging the coordinates of all unexplored spaces.
	 * @return A pair of double-precision floating point values representing the approximate location.
	 */
	public Pair<Double, Double> estimateGoldSpace() {
		if (goldSpace != null) {
			// If we know where the gold is, give that space.
			return new Pair<Double, Double>((double)goldSpace.x, (double)goldSpace.y);
		} else {
			// If we don't, take all unexplored spaces, and retrieve a path for them.
			double dx = 0.0;
			double dy = 0.0;
			List<Point> unvisited = getUnvisitedSpaces();
			double n = (double)unvisited.size();
			for (Point space : unvisited) {
				dx += (double)space.x;
				dy += (double)space.y;
			}
			dx /= n;
			dy /= n;
			return new Pair<Double, Double>(dx, dy);
		}
	}
	
	/**
	 * Calculates a most-likely wumpus location estimation, by averaging the coordinates of all unexplored spaces.
	 * @return An approximation of the wumpus space.
	 */
	public Point estimateWumpusSpace() {
		if (wumpusSpace != null) {
			// If we know where the wumpus is, give that space.
			return new Point(wumpusSpace.x, wumpusSpace.y);
		} else {
			// If we don't, take all unexplored spaces, wumpus spaces, and smelly spaces, and average them, repeating two more times for smelly/wumpus spaces.
			double dx = 0.0;
			double dy = 0.0;
			List<Point> territory = getWumpusTerritory();
			int n = territory.size();
			for (Point space : territory) {
				int px = space.x;
				int py = space.y;
				if (isSmelly(px, py) || isWumpus(px, py)) {
					px *= 3;
					py *= 3;
					n += 2;
				}
				dx += (double)px;
				dy += (double)py;
			}
			dx /= (double)n;
			dy /= (double)n;
			return new Point((int)dx, (int)dy);
		}
	}
	
	/**
	 * Retrieves the space determined to contain the wumpus.
	 * @return The wumpus space.
	 */
	public Point getWumpusSpace() {
		return wumpusSpace;
	}
	
	/**
	 * Retrieves the space determined to contain the gold.
	 * @return The gold space.
	 */
	public Point getGoldSpace() {
		return goldSpace;
	}
	
	/**
	 * Represents a conditional function object, which serves as a search criteria for the state space.
	 * @author iannonen
	 */
	public interface Conditional {
		
		/**
		 * Evaluates the condition.
		 * @return <b>true</b> if the space is to be selected, <b>false</b> otherwise.
		 */
		public boolean evaluate(int x, int y);
		
	};
	
	/**
	 * Fetches a list of all matching spaces which satisfy a given condition.
	 * @param c A condition to evaluate.
	 * @return A list of matching states.
	 */
	public List<Point> selectMatchingSpaces(Conditional c) {
		List<Point> list = new LinkedList<Point>();
		if (c != null) {
			for (int x = 0; x < WumpusWorld.WORLD_WIDTH; x++) {
				for (int y = 0; y < WumpusWorld.WORLD_HEIGHT; y++) {
					if (c.evaluate(x, y)) {
						list.add(new Point(x,y));
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * Gets a set of all previously-traversed, safe spaces. To be used by the PathResolver.
	 * @return A list of spaces.
	 */
	public List<Point> getTraversableSpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isVisited(x,y) && isSafe(x,y));
			}
		});
	}
	
	/**
	 * Gets a set of all spaces which could indicate the presence of a Wumpus. To be used by the PathResolver.
	 * @return A list of spaces.
	 */
	public List<Point> getWumpusTerritory() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isWumpus(x,y) || isSmelly(x,y) || !isVisited(x,y));
			}
		});
	}
	
	/**
	 * Gets a set of all untraversed spaces.
	 * @return A list of spaces.
	 */
	public List<Point> getUnvisitedSpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (!isVisited(x,y));
			}
		});
	}
	
	/**
	 * Gets a set of all spaces known to contain wumpi.
	 * @return A list of spaces with wumpi.
	 */
	public List<Point> getKnownWumpusSpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isWumpus(x,y));
			}
		});
	}
	
	/**
	 * Gets a set of all spaces known to contain pits.
	 * @return A list of spaces with pits.
	 */
	public List<Point> getKnownPitSpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isPit(x,y));
			}
		});
	}
	
	/**
	 * Gets a set of all spaces known to contain breezes.
	 * @return A list of spaces with breezes.
	 */
	public List<Point> getKnownBreezySpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isBreezy(x,y));
			}
		});
	}
	
	/**
	 * Gets a set of all spaces known to contain stench.
	 * @return A list of spaces with stench.
	 */
	public List<Point> getKnownStinkySpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isSmelly(x,y));
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
	 * Utility function; converts the given coordinates to a string representation, for feeding into the knowledge base. Shifts from 0-based to 1-based indexing.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return The knowledge base coordinate string.
	 */
	public static String toKBCoords(int x, int y) {
		return "" + (x+1) + (y+1);
	}
	
	/**
	 * Updates the state space via the knowledge base. Should be called whenever new percepts are entered into the knowledge base.
	 */
	public void update() {
		for (int x = 0; x < WumpusWorld.WORLD_WIDTH; x++) {
			for (int y = 0; y < WumpusWorld.WORLD_HEIGHT; y++) {
				String p = toKBCoords(x,y);
				if (KBWumpusAgent.plfce.plfcEntails(kb, "P" + p)) {
					markPit(x,y);
				}
				if (KBWumpusAgent.plfce.plfcEntails(kb, "S" + p)) {
					markSmelly(x,y);
				}
				if (!killedWumpus && KBWumpusAgent.plfce.plfcEntails(kb, "W" + p)) {
					markWumpus(x,y);
				}
				if (KBWumpusAgent.plfce.plfcEntails(kb, "B" + p)) {
					markBreezy(x,y);
				}
				if (KBWumpusAgent.plfce.plfcEntails(kb, "C" + p)) {
					markCrumb(x,y);
				}
				if (KBWumpusAgent.plfce.plfcEntails(kb, "G" + p)) {
					markGold(x,y);
				}
				if (KBWumpusAgent.plfce.plfcEntails(kb, "V" + p)) {
					markVisited(x,y);
				}
			}
		}
		
		// Invalidate the cache of safety costs.
		safetyMap.clear();
		
		// Forward-compute the safety costs of the fringe; it's the most intensive.
		List<Point> fringe = getFringe();
		for (Point space : fringe) {
			updateSafetyCost(space);
		}
	}

	/**
	 * Marks the given space with the specified flags.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param flags The set of bit flags to mark.
	 */
	private void markFlag(int x, int y, int flags) {
		Point p = new Point(x,y);
		if (!spaceMap.containsKey(p)) {
			spaceMap.put(p, flags);
		} else {
			spaceMap.put(p, spaceMap.get(p) | flags);
		}
	}
	
	/**
	 * Marks the given space as having been visited by the agent.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public void markVisited(int x, int y) {
		markFlag(x,y,WumpusWorld.VISITED_FLAG);
	}
	
	/**
	 * Marks the given space as being smelly.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public void markSmelly(int x, int y) {
		markFlag(x,y,WumpusWorld.STENCH_FLAG);
	}
	
	/**
	 * Marks the given space as having a pit.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public void markPit(int x, int y) {
		markFlag(x,y,WumpusWorld.PIT_FLAG);
	}
	
	/**
	 * Marks the given space as having a Wumpus.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public void markWumpus(int x, int y) {
		markFlag(x,y,WumpusWorld.WUMPUS_FLAG);
		Point space = new Point(x,y);
		if (wumpusSpace == null || !wumpusSpace.equals(space)) {
			wumpusSpace = space;
		}
	}
	
	/**
	 * Marks the given space as being breezy.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public void markBreezy(int x, int y) {
		markFlag(x,y,WumpusWorld.BREEZE_FLAG);
	}
	
	/**
	 * Marks the given space as having gold.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public void markGold(int x, int y) {
		markFlag(x,y,WumpusWorld.GOLD_FLAG);
		Point space = new Point(x,y);
		if (goldSpace == null || !goldSpace.equals(space)) {
			goldSpace = space;
		}
	}
	
	/**
	 * Marks the given space as having a crumb.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public void markCrumb(int x, int y) {
		markFlag(x,y,WumpusWorld.CRUMB_FLAG);
	}
	
	/**
	 * Fetches a list of spaces on the fringe of the explored region.
	 * @return A list of fringe spaces.
	 */
	public List<Point> getFringe() {
		
		List<Point> fringe = new LinkedList<Point>();
		
		// Go through each of the neighbors of all nodes of the state space.
		for (Point space : spaceMap.keySet()) {
			if (isVisited(space.x, space.y)) {
				List<Point> neighbors = getNeighbors(space);
				for (Point neighbor : neighbors) {
					int nx = neighbor.x;
					int ny = neighbor.y;
					// If the neighbor has been visited, or isn't safe, skip it.
					if (isVisited(nx, ny) || !isSafe(nx, ny)) {
						continue;
					}
					// If the fringe already has the neighbor, skip it.
					if (fringe.contains(neighbor)) {
						continue;
					}
					// Otherwise, add it.
					fringe.add(neighbor);
				}
			}
		}
		
		return fringe;
	}
	
	/**
	 * Tells whether or not the given flags are set for the given space.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param flags The set of flags to check.
	 * @return <b>true</b> if all of the flag's bits are set; <b>false</b> otherwise.
	 */
	private boolean isFlagSet(int x, int y, int flags) {
		Point space = new Point(x,y);
		if (!spaceMap.containsKey(space)) {
			return false;
		} else {
			return ((spaceMap.get(space) & flags) == flags);
		}
	}
	
	/**
	 * Tells whether the given space has been visited.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return <b>true</b> if the space has been visited; <b>false</b> otherwise.
	 */
	public boolean isVisited(int x, int y) {
		return isFlagSet(x,y,WumpusWorld.VISITED_FLAG);
	}
	
	/**
	 * Tells whether the given space is safe; ie. not a known pit or a wumpus. Also considers whether the wumpus has been killed.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return <b>true</b> if the space is safe; <b>false</b> otherwise.
	 */
	public boolean isSafe(int x, int y) {
		return (!isPit(x,y) && (killedWumpus || !isWumpus(x,y)));
	}
	
	/**
	 * Tells whether the given space is known to contain a pit.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return <b>true</b> if the space has a pit; <b>false</b> otherwise.
	 */
	public boolean isPit(int x, int y) {
		return isFlagSet(x,y,WumpusWorld.PIT_FLAG);
	}
	
	/**
	 * Tells whether the given space is known to contain a wumpus.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return <b>true</b> if the space has a wumpus; <b>false</b> otherwise.
	 */
	public boolean isWumpus(int x, int y) {
		return isFlagSet(x,y,WumpusWorld.WUMPUS_FLAG);
	}
	
	/**
	 * Tells whether the given space is known to contain a breeze.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return <b>true</b> if the space has a breeze; <b>false</b> otherwise.
	 */
	public boolean isBreezy(int x, int y) {
		return isFlagSet(x,y,WumpusWorld.BREEZE_FLAG);
	}
	
	/**
	 * Tells whether the given space is known to contain a stench.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return <b>true</b> if the space has a stench; <b>false</b> otherwise.
	 */
	public boolean isSmelly(int x, int y) {
		return isFlagSet(x,y,WumpusWorld.STENCH_FLAG);
	}
	
	/**
	 * Tells whether the given space is known to contain a crumb.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return <b>true</b> if the space has a crumb; <b>false</b> otherwise.
	 */
	public boolean isCrumb(int x, int y) {
		return isFlagSet(x,y,WumpusWorld.CRUMB_FLAG);
	}
	
	/**
	 * Tells whether the given space is known to contain gold.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return <b>true</b> if the space has gold; <b>false</b> otherwise.
	 */
	public boolean isGold(int x, int y) {
		return isFlagSet(x,y,WumpusWorld.GOLD_FLAG);
	}
	
	/**
	 * Tells whether or not the state space has deduced the location of the wumpus.
	 * @return <b>true</b> if the wumpus space is known, <b>false</b> otherwise.
	 */
	public boolean knowsWumpusSpace() {
		return (wumpusSpace != null);
	}
	
	/**
	 * Returns a list of all existing neighbors of the given space.
	 * @param space The central space.
	 * @return A list of neighboring spaces.
	 */
	public List<Point> getNeighbors(Point space) {
		List<Point> list = new LinkedList<Point>();
		if (space == null) {
			return list;
		}
		for (Direction d : Direction.values()) {
			Point neighbor = new Point(space.x + d.dx, space.y + d.dy);
			if (WumpusWorld.inBounds(neighbor.x, neighbor.y)) {
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
		return makeString(-1,-1);
	}
	
	/**
	 * Converts the state space into a printable string format.
	 * @param currX The player's x-coordinate.
	 * @param currY The player's y-coordinate.
	 * @return The state space as a string.
	 */
	public String makeString(int currX, int currY) {
		List<Point> fringe = getFringe();
		String s = "-----------------------------------\n";
		for (int y = WumpusWorld.WORLD_HEIGHT - 1; y >= 0; y--) {
			for (int x = 0; x < WumpusWorld.WORLD_WIDTH; x++) {
				Point space = new Point(x,y);
				String data = "?";
				String start = "*";
				String end = "*";
				
				if (spaceMap.containsKey(space)) {
					int code = spaceMap.get(space);
					String wumpusLabel = isWumpusDead() ? "x" : "W";
					
					data = ((code & WumpusWorld.STENCH_FLAG) != 0 ? "S" : "") +
							((code & WumpusWorld.BREEZE_FLAG) != 0 ? "B" : "") +
							((code & WumpusWorld.GOLD_FLAG) != 0 ? "G" : "") +
							((code & WumpusWorld.PIT_FLAG) != 0 ? "P" : "") +
							((code & WumpusWorld.WUMPUS_FLAG) != 0 ? wumpusLabel : "");
					
					if (data.isEmpty()) {
						data = "_";
					}
					
					// If the space has been visited, mark it with square brackets.
					if ((code & WumpusWorld.VISITED_FLAG) != 0) {
						start = "[";
						end = "]";
					}
					// If the space has been marked with a crumb, surround it with parentheses.
					if ((code & WumpusWorld.CRUMB_FLAG) != 0) {
						start = "(";
						end = ")";
					}
					// If the space contains the player, surround it with angle braces.
					if (x == currX && y == currY) {
						start = "<";
						end = ">";
					}
				} else {
					data = "?";
				}
				
				// If the space is in the fringe, mark it with curly braces.
				if (fringe.contains(space)) {
					start = "{";
					end = "}";
				}
				s += start + data + end + "\t";
			}
			s = s.trim() + "\n";
		}
		return s.trim().replace('*', ' ') + "\n-----------------------------------";
	}
}
