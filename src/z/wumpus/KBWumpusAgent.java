package z.wumpus;

import java.awt.Point;
import java.util.List;
import java.util.Random;

import z.agent.Agent;
import aima.logic.propositional.algorithms.KnowledgeBase;
import aima.logic.propositional.algorithms.PLFCEntails;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 * This class runs the interface between the WumpusPlayer and the Knowledge Database
 */
public class KBWumpusAgent extends WumpusPlayer {

	public static PLFCEntails plfce = new PLFCEntails();
	
	private static final int MAXIMUM_CRUMB_STEPS = 30;
	
	private StateSpace stateSpace;
	private KnowledgeBase kb;
	private Agent explorer;
	private PathNavigator navigator;
	private boolean firstTurn;
	private int numberCrumbsEncountered;
	
	private List<Point> currentPath;
	
	private boolean printMoves = false;
	
	/**
	 * Constructor. Takes in the Wumpus World object and the agent resolver.
	 * @param world
	 * @param explorer
	 */
	public KBWumpusAgent(WumpusWorld world, Agent explorer) {
		super(world);
		this.explorer = explorer;
		kb = generateInitialKB();
		stateSpace = new StateSpace(kb);
		navigator = new PathNavigator(stateSpace);
		numberCrumbsEncountered = 0;
		firstTurn = true;
		onMove();
	}
	
	/**
	 * @see z.agent.Agent#identify()
	 */
	public String identify() {
		return explorer.identify();
	}

	/**
	 * @see z.wumpus.WumpusPlayer#update()
	 */
	@Override
	public void update() {
		

		if (firstTurn) {
			Random r = new Random();
			if (r.nextBoolean()) {
				turnToFace(Direction.EAST);
			} else {
				turnToFace(Direction.NORTH);
			}

			moveForward();
			firstTurn = false;
			return;
		}
		

		if (hasGold()) {
			stop(false);
			return;
		}
		
		
		
		if (!stateSpace.isWumpusDead() && stateSpace.knowsWumpusSpace() && hasArrow()) {
			Point wumpusSpace = stateSpace.getWumpusSpace();
			int wx = wumpusSpace.x;
			int wy = wumpusSpace.y;
			if (wx == getX() || wy == getY()) {
				Direction d = null;
				if (wx < getX()) {
					d = Direction.WEST;
				} else if (wx > getX()) {
					d = Direction.EAST;
				} else if (wy < getY()) {
					d = Direction.SOUTH;
				} else if (wy > getY()) {
					d = Direction.NORTH;
				}
				if (d != null) {

					turnToFace(d);
					fireArrow();
				}
			}
		}
		
	
		if (isOnPath()) {
			proceedAlongPath();
			return;
		}
		
		
		dropCrumb();
		Point current = new Point(getX(), getY());
		Point nextNode = explorer.search(current, stateSpace);
		
		if (nextNode == null) {
			stop(true);
		} else {
			
			try {
				followPath(navigator.resolvePath(current, nextNode));
			} catch (IllegalStateException e) {
				
				stop(true);
			} catch (IllegalArgumentException e) {
				stop(true);
			}
		}
	}

	/**
	 * @see z.wumpus.WumpusPlayer#onScream()
	 */
	@Override
	public void onScream() {
		stateSpace.noteWumpusDead();
		stateSpace.update();
		if (printMoves) {
			System.out.println(stateSpace.makeString(getX(), getY()));
		}
		dropCurrentPath();
	}

	/**
	 * @see z.wumpus.WumpusPlayer#onBump()
	 */
	@Override
	public void onBump() {
		dropCurrentPath();
	}

	/**
	 * @see z.wumpus.WumpusPlayer#onPitfall()
	 */
	@Override
	public void onPitfall() {
		kb.tell("(P" + toKBCoords(getX(), getY()) + ")");
		stateSpace.update();
		if (printMoves) {
			System.out.println(stateSpace.makeString(getX(), getY()));
		}
		dropCurrentPath();
	}

	/**
	 * @see z.wumpus.WumpusPlayer#onDeath()
	 */
	@Override
	public void onDeath() {
		kb.tell("(W" + toKBCoords(getX(), getY()) + ")");
		stateSpace.update();
		if (printMoves) {
			System.out.println(stateSpace.makeString(getX(), getY()));
		}
		dropCurrentPath();
	}

	/**
	 * @see z.wumpus.WumpusPlayer#onMove()
	 */
	@Override
	public void onMove() {
		// Update the state space, if the space we're in hadn't already been visited..
		String p = toKBCoords(getX(), getY());
		
		// Tell us we've visited this space.
		kb.tell("(V" + p + ")");
		
		if (isBreezy()) {
			kb.tell("(B" + p + ")");
		} else {
			kb.tell("(NB" + p + ")");
		}
		
		boolean goldFound = false;
		if (isGlittering()) {
			kb.tell("(G" + p + ")");
			goldFound = true;
		} else {
			kb.tell("(NG" + p + ")");
		}
		
		if (isSmelly()) {
			kb.tell("(S" + p + ")");
		} else {
			kb.tell("(NS" + p + ")");
		}
		
		boolean maxCrumbs = false;
		if (isCrumby()) {
			kb.tell("(C" + p + ")");
			numberCrumbsEncountered++;
			if (numberCrumbsEncountered > KBWumpusAgent.MAXIMUM_CRUMB_STEPS) {
				maxCrumbs = true;
			}
		}
		
		// Merge into the state space.
		stateSpace.update();
		if (printMoves) {
			System.out.println(stateSpace.makeString(getX(), getY()));
		}
		
		// If we've found the gold, grab it and stop.
		if (goldFound) {
			if (grabGold()) {
				logMessage("After finding the gold, you figure you should just leave.");
				stop(false);
				return;
			}
		}
		
		// If we've reached our limit, stop.
		if (maxCrumbs) {
			logMessage("After seeing your own footprints for the " + MAXIMUM_CRUMB_STEPS + "th time, you decide enough is enough.");
			stop(true);
			return;
		}
	}
	
	/**
	 * @see z.wumpus.WumpusPlayer#onGrab()
	 */
	@Override
	public void onGrab() {}

	/**
	 * @see z.wumpus.WumpusPlayer#onDrop()
	 */
	@Override
	public void onDrop() {}
	
	/**
	 * @see z.wumpus.WumpusPlayer#stop(boolean)
	 */
	@Override
	public void stop(boolean giveUp) {
		logMessage("Final state space:\n" + stateSpace.makeString(getX(),getY()));
		logMessage("Final score: " + getScore());
		super.stop(giveUp);
	}

	/**
	 * Sets the path for the agent to follow.
	 * @param path The path.
	 */
	public void followPath(List<Point> path) {
		if (path == null || path.size() == 0) {
			return;
		}
		currentPath = path;
	}
	
	/**
	 * Proceeds along the stored path until it reaches the last element.
	 * If the next portion of the path is not adjacent to the current space, drop the path entirely.
	 */
	private void proceedAlongPath() {
		if (currentPath != null) {
			if (!currentPath.isEmpty()) {
				Point nextSpace = currentPath.remove(0);
				for (Direction d : Direction.values()) {
					int dx = getX() + d.dx;
					int dy = getY() + d.dy;
					if (dx == nextSpace.x && dy == nextSpace.y) {
						turnToFace(d);
						moveForward();
						break;
					}
				}
				// If we've empied the list, drop it.
				if (currentPath == null || currentPath.isEmpty()) {
					dropCurrentPath();
				}
			}
		}
	}
	
	/**
	 * Drops the current path, and resumes control of the program from its current location.
	 */
	private void dropCurrentPath() {
		if (currentPath != null) {
			currentPath.clear();
			currentPath = null;
		}
	}
	
	/**
	 * 
	 * @return <b>true</b> if the agent is following a path, <b>false</b> otherwise.
	 */
	private boolean isOnPath() {
		return (currentPath != null);
	}
	
	/**
	 * @see z.wumpus.StateSpace#toKBCoords(int, int)
	 */
	public static String toKBCoords(int x, int y) {
		return StateSpace.toKBCoords(x, y);
	}

	/**
	 * Factory method for generating an initialized knowledge base.
	 * @return An initialized knowledge base.
	 */
	public static KnowledgeBase generateInitialKB() {
		KnowledgeBase kb = new KnowledgeBase();
		
		// Symbols:
		/**
		 * B: Breeze
		 * P: Pit
		 * W: Wumpus
		 * G: Gold/Glitter
		 * S: Stench
		 * O: Out-of-Bounds position
		 * 
		 * N: Not-prefix
		 */
		
		// Out-of-Bounds/In-Bounds impositions:
		for (int x = -1; x <= WumpusWorld.WORLD_WIDTH; x++) {
			for (int y = -1; y <= WumpusWorld.WORLD_HEIGHT; y++) {
				// Basic positions.
				String p = toKBCoords(x,y);
				String north = toKBCoords(x,y+1);
				String south = toKBCoords(x,y-1);
				String east = toKBCoords(x+1,y);
				String west = toKBCoords(x-1,y);
				String northeast = toKBCoords(x+1,y+1);
				String northwest = toKBCoords(x-1,y+1);
				
				// Bounds checking.
				if (!WumpusWorld.inBounds(x, y)) {
					// Bounds in position.
					// 1. All cells outside of [0,Width),[0,Height) are out-of-bounds.
					kb.tell("(O" + p + ")");
					// Out-of-bound generation rules.
					// 1-3. There are no pits, wumpi, or gold in any out-of-bound cells.
					kb.tell("(NP" + p + ")");
					kb.tell("(NW" + p + ")");
					kb.tell("(NG" + p + ")");
				} else {
					// Bounds imposition.
					// 1. All cells within [0,Width),[0,Height) are in-bounds.
					kb.tell("(NO" + toKBCoords(x,y) + ")");
					
					// Last-left rules.
					// 1. If a breeze has no pits in three of its surrounding cells, the remaining cell must be a pit.
					kb.tell("((B" + p + " AND (NP" + north + " AND (NP" + east + " AND (NP" + west + ")))) => P" + south + ")");
					kb.tell("((B" + p + " AND (NP" + north + " AND (NP" + south + " AND (NP" + west + ")))) => P" + east + ")");
					kb.tell("((B" + p + " AND (NP" + north + " AND (NP" + east + " AND (NP" + south + ")))) => P" + west + ")");
					kb.tell("((B" + p + " AND (NP" + south + " AND (NP" + east + " AND (NP" + west + ")))) => P" + north + ")");
					// 2. If a stench has no wumpi in three of its surrounding cells, the remaining cell must be a wumpus.
					kb.tell("((S" + p + " AND (NW" + north + " AND (NW" + east + " AND (NW" + west + ")))) => W" + south + ")");
					kb.tell("((S" + p + " AND (NW" + north + " AND (NW" + south + " AND (NW" + west + ")))) => W" + east + ")");
					kb.tell("((S" + p + " AND (NW" + north + " AND (NW" + east + " AND (NW" + south + ")))) => W" + west + ")");
					kb.tell("((S" + p + " AND (NW" + south + " AND (NW" + east + " AND (NW" + west + ")))) => W" + north + ")");
					
					// Two-fourths rules, for wumpi.
					// 1. If this space, and one northeast, have stenches, and north is clear, then east is a wumpus, and vice versa.
					kb.tell("((S" + p + " AND (S" + northeast + " AND NW" + north + ")) => W" + east + ")");
					kb.tell("((S" + p + " AND (S" + northeast + " AND NW" + east + ")) => W" + north + ")");
					// 2. If this space, and one northwest, have stenches, and north is clear, then west is a wumpus, and vice versa.
					kb.tell("((S" + p + " AND (S" + northwest + " AND NW" + north + ")) => W" + west + ")");
					kb.tell("((S" + p + " AND (S" + northwest + " AND NW" + west + ")) => W" + north + ")");
					
					// Adjacency rules.
					// 1. Pits are surrounded by breezes.
					kb.tell("(P" + p + " => B" + north + ")");
					kb.tell("(P" + p + " => B" + south + ")");
					kb.tell("(P" + p + " => B" + east + ")");
					kb.tell("(P" + p + " => B" + west + ")");
					// 2. Wumpi are surrounded by stench.
					kb.tell("(W" + p + " => S" + north + ")");
					kb.tell("(W" + p + " => S" + south + ")");
					kb.tell("(W" + p + " => S" + east + ")");
					kb.tell("(W" + p + " => S" + west + ")");
					
					// Same-space rules (custom; imposed by wumpus world generation rules)
					// 1. Gold and Pits cannot be in the same space.
					kb.tell("(G" + p + " => NP" + p + ")");
					kb.tell("(P" + p + " => NG" + p + ")");
					// 2. Gold and Wumpi cannot be in the same space.
					kb.tell("(G" + p + " => NW" + p + ")");
					kb.tell("(W" + p + " => NG" + p + ")");
					// 3. Pits and Wumpi cannot be in the same space.
					kb.tell("(P" + p + " => NW" + p + ")");
					kb.tell("(W" + p + " => NP" + p + ")");
					
					// Elimination rules.
					// 1. If a Wumpus is found in one cell, no other cell may contain a Wumpus.
					// 2. If Gold is found in one cell, no other cell may contain Gold.
					/*
					for (int x2 = 0; x2 < WumpusWorld.WORLD_WIDTH; x2++) {
						for (int y2 = 0; y2 < WumpusWorld.WORLD_HEIGHT; y2++) {
							String p2 = toKBCoords(x2, y2);
							if (!p2.equals(p)) {
								kb.tell("(W" + p + " => NW" + p2 + ")");
								kb.tell("(G" + p + " => NG" + p2 + ")");
							}
						}
					}
					*/
				}
			}
		}
		
		// Initial space rules:
		// 1-3. There are no pits, wumpi, or gold in the starting space.
		kb.tell("(NP11)");
		kb.tell("(NW11)");
		kb.tell("(NG11)");
		
		// Some basic initial knowledge about pits and wumpi.
		// 1. A lack of a breeze or stench in the first cell indicates the two adjacent cells are both clear.
		kb.tell("(NB11 => NP12)");
		kb.tell("(NB11 => NP21)");
		kb.tell("(NS11 => NW12)");
		kb.tell("(NS11 => NW21)");
		
		// Corner resolution information:
		// 1-8. A breeze/stench in a corner means a pit/wumpus in at least one adjacent space.
		kb.tell("((B11 AND NP12) => P21)");
		kb.tell("((B11 AND NP21) => P12)");
		kb.tell("((S11 AND NW12) => W21)");
		kb.tell("((S11 AND NW21) => W12)");
		kb.tell("((B14 AND NP13) => P24)");
		kb.tell("((B14 AND NP24) => P13)");
		kb.tell("((S14 AND NW13) => W24)");
		kb.tell("((S14 AND NW24) => W13)");
		kb.tell("((B41 AND NP42) => P31)");
		kb.tell("((B41 AND NP31) => P42)");
		kb.tell("((S41 AND NW42) => W31)");
		kb.tell("((S41 AND NW31) => W42)");
		kb.tell("((B44 AND NP43) => P34)");
		kb.tell("((B44 AND NP34) => P43)");
		kb.tell("((S44 AND NW43) => W34)");
		kb.tell("((S44 AND NW34) => W43)");
		
		return kb;
	}

	public void printStateSpace() {
		System.out.println(stateSpace.makeString(this.getX(), this.getY()));
	}
}
