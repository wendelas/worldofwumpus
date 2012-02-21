package z.agent;

import java.util.List;

import z.wumpus.StateSpace;

import aima.util.Pair;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 * This class is the Heuristic AI
 */
public class Heuristic extends Agent {

	
	@Override
	public Pair<Integer, Integer> resolveFringe(Pair<Integer, Integer> current, StateSpace stateSpace) {

		Pair<Integer, Integer> resolved = null;
		
		List<Pair<Integer, Integer>> safeMoves = Agent.getSafeMoves(stateSpace);
		
		if (safeMoves.isEmpty()) {
			resolved = null;
		} else {
			double leastUtility = Double.POSITIVE_INFINITY;
			for (Pair<Integer, Integer> move : safeMoves) {
				if (move == null) {
					continue;
				}
				double utility = stateSpace.getMoveCost(current, move);
				if (utility < leastUtility) {
					leastUtility = utility;
					resolved = move;
				}
			}
		}
		
		return resolved;
		
	}

	@Override
	public String identify() {
		return "Heuristic AI";
	}

}
