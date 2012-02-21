package z.agent;

import java.util.List;

import z.wumpus.StateSpace;

import aima.util.Pair;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 * This class is the Chicken Little AI
 */
public class ChickenLittle extends Agent {

	@Override
	public Pair<Integer, Integer> resolveFringe(Pair<Integer, Integer> current, StateSpace stateSpace) {
		Pair<Integer, Integer> resolved = null;
		
		List<Pair<Integer, Integer>> safeMoves = Agent.getSafeMoves(stateSpace);
		
		if (safeMoves.size() > 0) {
			resolved = safeMoves.get(0);
		} else {
			return null;
		}
		
		return resolved;
	}
	
	@Override
	public String identify() {
		return "Chicken Little";
	}

}
