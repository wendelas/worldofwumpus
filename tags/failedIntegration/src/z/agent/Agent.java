package z.agent;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import z.wumpus.StateSpace;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 * This class is the agent that the AI's extend
 */
public abstract class Agent {
	
	/**
	 * Gets a subset of "safe" spaces from the given fringe set.
	 * @param stateSpace The state space.
	 * @return A list of "safe" spaces.
	 */
	public static List<Point> getSafeMoves(StateSpace stateSpace) {
		List<Point> safeMoves = new LinkedList<Point>();
		List<Point> fringe = stateSpace.getFringe();
		for (Point move : fringe) {
			int x = move.x;
			int y = move.y;
			if (stateSpace.isSafe(x, y)) {
				safeMoves.add(move);
			}
		}
		return safeMoves;
	}

	/**
	 * Gets a string name which identifies the specific explorer implementation.
	 * @return A string name.
	 */
	public abstract String toString();
	
	/**
	 * Chooses a next state from the fringe of unexplored states using a user-defined selection algorithm.
	 * May return <b>null</b> to indicate that the explorer cannot proceed further.
	 * @param current The current space.
	 * @param stateSpace The state space.
	 * @return A fringe space to move to, or <b>null</b> if no space is selected.
	 */
	public abstract Point search(Point current, StateSpace stateSpace);
}
