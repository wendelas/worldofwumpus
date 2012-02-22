/**
 * 
 */
package agent;

import java.util.LinkedList;
import java.util.List;


import aima.util.Pair;

/**
 * @author iannonen
 *
 */
public abstract class ExplorerStrategy {
	
	/**
	 * Gets a subset of "safe" spaces from the given fringe set.
	 * @param stateSpace The state space.
	 * @return A list of "safe" spaces.
	 */
	public static List<Pair<Integer, Integer>> getSafeMoves(StateSpace stateSpace) {
		List<Pair<Integer, Integer>> safeMoves = new LinkedList<Pair<Integer, Integer>>();
		List<Pair<Integer, Integer>> fringe = stateSpace.getFringe();
		for (Pair<Integer, Integer> move : fringe) {
			int x = move.getFirst();
			int y = move.getSecond();
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
	public abstract String identify();
	
	/**
	 * Chooses a next state from the fringe of unexplored states using a user-defined selection algorithm.
	 * May return <b>null</b> to indicate that the explorer cannot proceed further.
	 * @param current The current space.
	 * @param stateSpace The state space.
	 * @return A fringe space to move to, or <b>null</b> if no space is selected.
	 */
	public abstract Pair<Integer, Integer> resolveFringe(Pair<Integer, Integer> current, StateSpace stateSpace);
}
