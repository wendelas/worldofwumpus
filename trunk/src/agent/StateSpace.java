package agent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import Gameboard.GameBoard;
import aima.logic.propositional.algorithms.KnowledgeBase;
import aima.util.Pair;

/**
 * @author ebertb schmidbauerk
 * Keeps track of the spaces the AIs went to
 */
public class StateSpace {

	private Map<Pair<Integer, Integer>, Integer> spaceMap = new HashMap<Pair<Integer, Integer>, Integer>();
	private Map<Pair<Integer, Integer>, Double> safetyMap = new HashMap<Pair<Integer, Integer>, Double>();
	
	private KnowledgeBase kb;
	
	private boolean killedWumpus;
	
	private Pair<Integer, Integer> goldSpace;
	private Pair<Integer, Integer> wumpusSpace;
	
	
	public StateSpace(KnowledgeBase kb) {
		this.kb = kb;
		killedWumpus = false;
		goldSpace = null;
		wumpusSpace = null;
		spaceMap.clear();
		safetyMap.clear();
	}

	
	public KnowledgeBase getKnowledgeBase() {
		return kb;
	}
	
	
	public double getMoveCost(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
		return distBetween(a, b) + getSafetyCost(b);
	}
	
	
	public double getSafetyCost(Pair<Integer, Integer> space) {
		if (!safetyMap.containsKey(space)) {
			updateSafetyCost(space);
		}
		return safetyMap.get(space);
	}
	
	
	private void updateSafetyCost(Pair<Integer, Integer> space) {
		
		if (space == null) {
			return;
		}
		
		double cost = 0.0;
		
		int x = space.getFirst();
		int y = space.getSecond();
		
		
		if (isCrumb(x,y)) {
			cost += GameBoard.CRUMB_COST;
		}
		
		
		if (isPit(x,y) || (!isWumpusDead() && isWumpus(x,y))) {
			
			cost += (double)GameBoard.DEATH_COST;	
			
		} else if (!isVisited(x,y)) {
			
			int numUnvisitedSpaces = getUnvisitedSpaces().size();
			if (numUnvisitedSpaces > 0 && !isVisited(x, y)) {
				
				
				int numMissingPits = GameBoard.NUM_PITS - getKnownPitSpaces().size();
				int numMissingWumpi = GameBoard.NUM_WUMPUS - getKnownWumpusSpaces().size();
				
				
				double baseChanceOfPit = ((double)numMissingPits) / ((double)numUnvisitedSpaces);
				
				
				double baseChanceOfWumpus = 0.0;
				if (!isWumpusDead()) {
					baseChanceOfWumpus = ((double)numMissingWumpi) / ((double)numUnvisitedSpaces);
				}
			
				
				List<Pair<Integer, Integer>> neighbors = getNeighbors(space);
				int numBreezes = 0;
				int numStenches = 0;
				for (Pair<Integer, Integer> neighbor : neighbors) {
					int nx = neighbor.getFirst();
					int ny = neighbor.getSecond();
					
					
					if (isVisited(nx, ny)) {
						if (!isBreezy(nx, ny)) {
							baseChanceOfPit = 0.0;
						}
						if (!isSmelly(nx, ny)) {
							baseChanceOfWumpus = 0.0;
						}
					}
					
					
					if (isBreezy(nx, ny)) {
						numBreezes++;
					}
					
					if (isSmelly(nx, ny)) {
						numStenches++;
					}
				}
				
				
				cost += (((double)numBreezes / 4.0) * ((double)GameBoard.DEATH_COST) * (baseChanceOfPit))
						+ (((double)numStenches / 4.0) * ((double)GameBoard.DEATH_COST) * (baseChanceOfWumpus));
			}
		}
		
		
		safetyMap.put(space, cost);
	}

	
	public static double distBetween(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
		double dx = (double)(a.getFirst() - b.getFirst());
		double dy = (double)(a.getSecond() - b.getSecond());
		return ((double)GameBoard.STEP_COST * Math.sqrt((dx * dx) + (dy * dy)));
	}
	
	
	public boolean isWumpusDead() {
		return killedWumpus;
	}
	
	
	public Pair<Double, Double> estimateGoldSpace() {
		if (goldSpace != null) {
			
			return new Pair<Double, Double>((double)goldSpace.getFirst(), (double)goldSpace.getSecond());
		} else {
			
			double dx = 0.0;
			double dy = 0.0;
			List<Pair<Integer, Integer>> unvisited = getUnvisitedSpaces();
			double n = (double)unvisited.size();
			for (Pair<Integer, Integer> space : unvisited) {
				dx += (double)space.getFirst();
				dy += (double)space.getSecond();
			}
			dx /= n;
			dy /= n;
			return new Pair<Double, Double>(dx, dy);
		}
	}
	
	
	public Pair<Double, Double> estimateWumpusSpace() {
		if (wumpusSpace != null) {
			
			return new Pair<Double, Double>((double)wumpusSpace.getFirst(), (double)wumpusSpace.getSecond());
		} else {
			
			double dx = 0.0;
			double dy = 0.0;
			List<Pair<Integer, Integer>> territory = getWumpusTerritory();
			int n = territory.size();
			for (Pair<Integer, Integer> space : territory) {
				int px = space.getFirst();
				int py = space.getSecond();
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
			return new Pair<Double, Double>(dx, dy);
		}
	}
	
	
	public Pair<Integer, Integer> getWumpusSpace() {
		return wumpusSpace;
	}
	
	
	public Pair<Integer, Integer> getGoldSpace() {
		return goldSpace;
	}
	
	
	public interface Conditional {
		
		
		public boolean evaluate(int x, int y);
		
	};
	
	
	public List<Pair<Integer, Integer>> selectMatchingSpaces(Conditional c) {
		List<Pair<Integer, Integer>> list = new LinkedList<Pair<Integer, Integer>>();
		if (c != null) {
			for (int x = 0; x < GameBoard.WORLD_WIDTH; x++) {
				for (int y = 0; y < GameBoard.WORLD_HEIGHT; y++) {
					if (c.evaluate(x, y)) {
						list.add(new Pair<Integer, Integer>(x,y));
					}
				}
			}
		}
		return list;
	}
	
	
	public List<Pair<Integer, Integer>> getTraversableSpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isVisited(x,y) && isSafe(x,y));
			}
		});
	}
	
	
	public List<Pair<Integer, Integer>> getWumpusTerritory() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isWumpus(x,y) || isSmelly(x,y) || !isVisited(x,y));
			}
		});
	}
	
	
	public List<Pair<Integer, Integer>> getUnvisitedSpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (!isVisited(x,y));
			}
		});
	}
	
	
	public List<Pair<Integer, Integer>> getKnownWumpusSpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isWumpus(x,y));
			}
		});
	}
	
	
	public List<Pair<Integer, Integer>> getKnownPitSpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isPit(x,y));
			}
		});
	}
	
	
	public List<Pair<Integer, Integer>> getKnownBreezySpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isBreezy(x,y));
			}
		});
	}
	
	
	public List<Pair<Integer, Integer>> getKnownStinkySpaces() {
		return selectMatchingSpaces(new Conditional() {
			@Override
			public boolean evaluate(int x, int y) {
				return (isSmelly(x,y));
			}
		});
	}
	
	
	public void noteWumpusDead() {
		killedWumpus = true;
		wumpusSpace = null;
	}
	
	
	public static String toKBCoords(int x, int y) {
		return "" + (x+1) + (y+1);
	}
	
	
	public void update() {
		for (int x = 0; x < GameBoard.WORLD_WIDTH; x++) {
			for (int y = 0; y < GameBoard.WORLD_HEIGHT; y++) {
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
		
		
		safetyMap.clear();
		
		
		List<Pair<Integer, Integer>> fringe = getFringe();
		for (Pair<Integer, Integer> space : fringe) {
			updateSafetyCost(space);
		}
	}

	
	private void markFlag(int x, int y, int flags) {
		Pair<Integer, Integer> p = new Pair<Integer,Integer>(x,y);
		if (!spaceMap.containsKey(p)) {
			spaceMap.put(p, flags);
		} else {
			spaceMap.put(p, spaceMap.get(p) | flags);
		}
	}
	
	
	public void markVisited(int x, int y) {
		markFlag(x,y,GameBoard.VISITED_FLAG);
	}
	
	
	public void markSmelly(int x, int y) {
		markFlag(x,y,GameBoard.STENCH_FLAG);
	}
	
	
	public void markPit(int x, int y) {
		markFlag(x,y,GameBoard.PIT_FLAG);
	}
	
	
	public void markWumpus(int x, int y) {
		markFlag(x,y,GameBoard.WUMPUS_FLAG);
		Pair<Integer, Integer> space = new Pair<Integer, Integer>(x,y);
		if (wumpusSpace == null || !wumpusSpace.equals(space)) {
			wumpusSpace = space;
		}
	}
	
	
	public void markBreezy(int x, int y) {
		markFlag(x,y,GameBoard.BREEZE_FLAG);
	}
	
	
	public void markGold(int x, int y) {
		markFlag(x,y,GameBoard.GOLD_FLAG);
		Pair<Integer, Integer> space = new Pair<Integer, Integer>(x,y);
		if (goldSpace == null || !goldSpace.equals(space)) {
			goldSpace = space;
		}
	}
	
	
	public void markCrumb(int x, int y) {
		markFlag(x,y,GameBoard.CRUMB_FLAG);
	}
	
	
	public List<Pair<Integer, Integer>> getFringe() {
		
		List<Pair<Integer,Integer>> fringe = new LinkedList<Pair<Integer,Integer>>();
		
		
		for (Pair<Integer, Integer> space : spaceMap.keySet()) {
			if (isVisited(space.getFirst(), space.getSecond())) {	
				List<Pair<Integer, Integer>> neighbors = getNeighbors(space);
				for (Pair<Integer, Integer> neighbor : neighbors) {
					int nx = neighbor.getFirst();
					int ny = neighbor.getSecond();
					
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
	
	
	private boolean isFlagSet(int x, int y, int flags) {
		Pair<Integer, Integer> space = new Pair<Integer, Integer>(x,y);
		if (!spaceMap.containsKey(space)) {
			return false;
		} else {
			return ((spaceMap.get(space) & flags) == flags);
		}
	}
	
	
	public boolean isVisited(int x, int y) {
		return isFlagSet(x,y,GameBoard.VISITED_FLAG);
	}
	
	
	public boolean isSafe(int x, int y) {
		return (!isPit(x,y) && (killedWumpus || !isWumpus(x,y)));
	}
	
	
	public boolean isPit(int x, int y) {
		return isFlagSet(x,y,GameBoard.PIT_FLAG);
	}
	
	
	public boolean isWumpus(int x, int y) {
		return isFlagSet(x,y,GameBoard.WUMPUS_FLAG);
	}
	
	
	public boolean isBreezy(int x, int y) {
		return isFlagSet(x,y,GameBoard.BREEZE_FLAG);
	}
	
	
	public boolean isSmelly(int x, int y) {
		return isFlagSet(x,y,GameBoard.STENCH_FLAG);
	}
	
	
	public boolean isCrumb(int x, int y) {
		return isFlagSet(x,y,GameBoard.CRUMB_FLAG);
	}
	
	
	public boolean isGold(int x, int y) {
		return isFlagSet(x,y,GameBoard.GOLD_FLAG);
	}
	
	
	public boolean knowsWumpusSpace() {
		return (wumpusSpace != null);
	}
	
	
	public List<Pair<Integer, Integer>> getNeighbors(Pair<Integer, Integer> space) {
		List<Pair<Integer, Integer>> list = new LinkedList<Pair<Integer, Integer>>();
		if (space == null) {
			return list;
		}
		for (Direction d : Direction.values()) {
			Pair<Integer, Integer> neighbor = new Pair<Integer, Integer>(space.getFirst() + d.dx, space.getSecond() + d.dy);
			if (GameBoard.inBounds(neighbor.getFirst(), neighbor.getSecond())) {
				list.add(neighbor);
			}
		}
		return list;
	}
	
	
	@Override
	public String toString() {
		return makeString(-1,-1);
	}
	
	
	public String makeString(int currX, int currY) {
		List<Pair<Integer, Integer>> fringe = getFringe();
		String s = "-----------------------------------\n";
		for (int y = GameBoard.WORLD_HEIGHT - 1; y >= 0; y--) {
			for (int x = 0; x < GameBoard.WORLD_WIDTH; x++) {
				Pair<Integer, Integer> space = new Pair<Integer, Integer>(x,y);
				String data = "?";
				String start = "*";
				String end = "*";
				
				if (spaceMap.containsKey(space)) {
					int code = spaceMap.get(space);
					String wumpusLabel = isWumpusDead() ? "x" : "W";
					
					data = ((code & GameBoard.STENCH_FLAG) != 0 ? "S" : "") +
							((code & GameBoard.BREEZE_FLAG) != 0 ? "B" : "") +
							((code & GameBoard.GOLD_FLAG) != 0 ? "G" : "") +
							((code & GameBoard.PIT_FLAG) != 0 ? "P" : "") +
							((code & GameBoard.WUMPUS_FLAG) != 0 ? wumpusLabel : "");
					
					if (data.isEmpty()) {
						data = "_";
					}
					
					
					if ((code & GameBoard.VISITED_FLAG) != 0) {
						start = "[";
						end = "]";
					}
					
					if ((code & GameBoard.CRUMB_FLAG) != 0) {
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
		return s.trim().replace('*', ' ') + "\n-----------------------------------";
	}
}
