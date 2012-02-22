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
	 * Returns a List of safe spaces from the given tile
	 * @param stateSpace The tiles.
	 * @return A list of safe tiles to go into
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
	 * Chooses where to go to next from the list of unexplored tiles using various agents.
	 * @param current The current tile.
	 * @param stateSpace The state.
	 * @return A fringe space to move to, or null if the agent cannot proceed.
	 */
	public abstract Point search(Point current, StateSpace stateSpace);
}
