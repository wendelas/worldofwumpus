/**
 * 
 */
package z.agent;

import java.util.List;

import z.wumpus.StateSpace;

import aima.util.Pair;

/**
 * @author iannonen
 *
 */
public class Heuristic extends Agent {

	/**
	 * @see z.agent.Agent#resolveFringe(aima.util.Pair, java.util.List, aima.logic.propositional.algorithms.KnowledgeBase, boolean)
	 */
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
				double utility = stateSpace.getMoveCost(current, move);//KBWumpusAgent.getHeuristic(current, move, kb, skipWumpus);
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
		// TODO Auto-generated method stub
		return "HeuristicBot-3000 the Wumpus Mastermind";
	}

}
