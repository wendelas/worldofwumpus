package agent;

import java.util.List;
import java.util.Random;


import Gameboard.PathNavigator;
import Gameboard.GameBoard;
import aima.logic.propositional.algorithms.KnowledgeBase;
import aima.logic.propositional.algorithms.PLFCEntails;
import aima.util.Pair;

/**
 * @author ebertb schmidbauerk
 * Handle the knowledge connector for the board
 */
public class KBWumpusAgent extends WumpusPlayer {

	public static PLFCEntails plfce = new PLFCEntails();
	
	private static final int MAXIMUM_CRUMB_STEPS = 30;
	
	private StateSpace stateSpace;
	private KnowledgeBase kb;
	private ExplorerStrategy explorer;
	private PathNavigator navigator;
	private boolean firstTurn;
	private int numberCrumbsEncountered;
	
	private List<Pair<Integer, Integer>> currentPath;
	
	private boolean printMoves = false;
	
	
	public KBWumpusAgent(GameBoard world, ExplorerStrategy explorer) {
		super(world);
		this.explorer = explorer;
		kb = generateInitialKB();
		stateSpace = new StateSpace(kb);
		navigator = new PathNavigator(stateSpace);
		numberCrumbsEncountered = 0;
		firstTurn = true;
		onMove();
	}
	
	
	public String identify() {
		return explorer.identify();
	}

	
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
			
			Pair<Integer, Integer> wumpusSpace = stateSpace.getWumpusSpace();
			int wx = wumpusSpace.getFirst();
			int wy = wumpusSpace.getSecond();
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
		Pair<Integer, Integer> current = new Pair<Integer, Integer>(getX(), getY());
		Pair<Integer, Integer> nextNode = explorer.resolveFringe(current, stateSpace);
		
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

	
	@Override
	public void onScream() {
		stateSpace.noteWumpusDead();
		stateSpace.update();
		if (printMoves) {
			System.out.println(stateSpace.makeString(getX(), getY()));
		}
		dropCurrentPath();
	}

	
	@Override
	public void onBump() {
		dropCurrentPath();
	}

	
	@Override
	public void onPitfall() {
		kb.tell("(P" + toKBCoords(getX(), getY()) + ")");
		stateSpace.update();
		if (printMoves) {
			System.out.println(stateSpace.makeString(getX(), getY()));
		}
		dropCurrentPath();
	}

	
	@Override
	public void onDeath() {
		kb.tell("(W" + toKBCoords(getX(), getY()) + ")");
		stateSpace.update();
		if (printMoves) {
			System.out.println(stateSpace.makeString(getX(), getY()));
		}
		dropCurrentPath();
	}

	
	@Override
	public void onMove() {
		
		String p = toKBCoords(getX(), getY());
		
		
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
		
		
		stateSpace.update();
		if (printMoves) {
			System.out.println(stateSpace.makeString(getX(), getY()));
		}
		
		
		if (goldFound) {
			if (grabGold()) {
				logMessage("After finding the gold, you figure you should just leave.");
				stop(false);
				return;
			}
		}
		
		
		if (maxCrumbs) {
			logMessage("After seeing your own footprints for the " + MAXIMUM_CRUMB_STEPS + "th time, you decide enough is enough.");
			stop(true);
			return;
		}
	}
	
	
	@Override
	public void onGrab() {}

	
	@Override
	public void onDrop() {}
	
	
	@Override
	public void stop(boolean giveUp) {
		logMessage("Final state space:\n" + stateSpace.makeString(getX(),getY()));
		logMessage("Final score: " + getScore());
		super.stop(giveUp);
	}

	
	public void followPath(List<Pair<Integer, Integer>> path) {
		if (path == null || path.size() == 0) {
			return;
		}
		currentPath = path;
	}
	
	
	private void proceedAlongPath() {
		if (currentPath != null) {
			if (!currentPath.isEmpty()) {
				Pair<Integer, Integer> nextSpace = currentPath.remove(0);
				for (Direction d : Direction.values()) {
					int dx = getX() + d.dx;
					int dy = getY() + d.dy;
					if (dx == nextSpace.getFirst() && dy == nextSpace.getSecond()) {
						turnToFace(d);
						moveForward();
						break;
					}
				}
				
				if (currentPath == null || currentPath.isEmpty()) {
					dropCurrentPath();
				}
			}
		}
	}
	
	
	private void dropCurrentPath() {
		if (currentPath != null) {
			currentPath.clear();
			currentPath = null;
		}
	}
	
	
	private boolean isOnPath() {
		return (currentPath != null);
	}
	
	
	public static String toKBCoords(int x, int y) {
		return StateSpace.toKBCoords(x, y);
	}

	
	public static KnowledgeBase generateInitialKB() {
		KnowledgeBase kb = new KnowledgeBase();
		
		
		for (int x = -1; x <= GameBoard.WORLD_WIDTH; x++) {
			for (int y = -1; y <= GameBoard.WORLD_HEIGHT; y++) {
				
				String p = toKBCoords(x,y);
				String north = toKBCoords(x,y+1);
				String south = toKBCoords(x,y-1);
				String east = toKBCoords(x+1,y);
				String west = toKBCoords(x-1,y);
				String northeast = toKBCoords(x+1,y+1);
				String northwest = toKBCoords(x-1,y+1);
				
				
				if (!GameBoard.inBounds(x, y)) {
					
					kb.tell("(O" + p + ")");
					
					kb.tell("(NP" + p + ")");
					kb.tell("(NW" + p + ")");
					kb.tell("(NG" + p + ")");
				} else {
					
					kb.tell("(NO" + toKBCoords(x,y) + ")");
					
					
					kb.tell("((B" + p + " AND (NP" + north + " AND (NP" + east + " AND (NP" + west + ")))) => P" + south + ")");
					kb.tell("((B" + p + " AND (NP" + north + " AND (NP" + south + " AND (NP" + west + ")))) => P" + east + ")");
					kb.tell("((B" + p + " AND (NP" + north + " AND (NP" + east + " AND (NP" + south + ")))) => P" + west + ")");
					kb.tell("((B" + p + " AND (NP" + south + " AND (NP" + east + " AND (NP" + west + ")))) => P" + north + ")");
					
					kb.tell("((S" + p + " AND (NW" + north + " AND (NW" + east + " AND (NW" + west + ")))) => W" + south + ")");
					kb.tell("((S" + p + " AND (NW" + north + " AND (NW" + south + " AND (NW" + west + ")))) => W" + east + ")");
					kb.tell("((S" + p + " AND (NW" + north + " AND (NW" + east + " AND (NW" + south + ")))) => W" + west + ")");
					kb.tell("((S" + p + " AND (NW" + south + " AND (NW" + east + " AND (NW" + west + ")))) => W" + north + ")");
					
					
					kb.tell("((S" + p + " AND (S" + northeast + " AND NW" + north + ")) => W" + east + ")");
					kb.tell("((S" + p + " AND (S" + northeast + " AND NW" + east + ")) => W" + north + ")");

					kb.tell("((S" + p + " AND (S" + northwest + " AND NW" + north + ")) => W" + west + ")");
					kb.tell("((S" + p + " AND (S" + northwest + " AND NW" + west + ")) => W" + north + ")");
					
					
					kb.tell("(P" + p + " => B" + north + ")");
					kb.tell("(P" + p + " => B" + south + ")");
					kb.tell("(P" + p + " => B" + east + ")");
					kb.tell("(P" + p + " => B" + west + ")");
					
					kb.tell("(W" + p + " => S" + north + ")");
					kb.tell("(W" + p + " => S" + south + ")");
					kb.tell("(W" + p + " => S" + east + ")");
					kb.tell("(W" + p + " => S" + west + ")");
					
					
					kb.tell("(G" + p + " => NP" + p + ")");
					kb.tell("(P" + p + " => NG" + p + ")");
					
					kb.tell("(G" + p + " => NW" + p + ")");
					kb.tell("(W" + p + " => NG" + p + ")");
					
					kb.tell("(P" + p + " => NW" + p + ")");
					kb.tell("(W" + p + " => NP" + p + ")");
					
					
				}
			}
		}
		
		
		kb.tell("(NP11)");
		kb.tell("(NW11)");
		kb.tell("(NG11)");
		
		
		kb.tell("(NB11 => NP12)");
		kb.tell("(NB11 => NP21)");
		kb.tell("(NS11 => NW12)");
		kb.tell("(NS11 => NW21)");
		
		
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
