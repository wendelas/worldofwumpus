package agent;

import java.util.LinkedList;
import java.util.List;


import aima.util.Pair;

/**
 * @author ebertb schmidbauerk
 * Parent class of both AIs
 */
public abstract class ExplorerStrategy {
	
	
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

	
	public abstract String identify();
	
	
	public abstract Pair<Integer, Integer> resolveFringe(Pair<Integer, Integer> current, StateSpace stateSpace);
}
