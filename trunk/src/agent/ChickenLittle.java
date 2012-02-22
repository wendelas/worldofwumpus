package agent;

import java.util.List;


import aima.util.Pair;

/**
 * @author ebertb schmidbauerk
 * The Chicken Little agent
 */
public class ChickenLittle extends ExplorerStrategy {

	@Override
	public Pair<Integer, Integer> resolveFringe(Pair<Integer, Integer> current, StateSpace stateSpace) {
		
		Pair<Integer, Integer> resolved = null;
		
		List<Pair<Integer, Integer>> safeMoves = ExplorerStrategy.getSafeMoves(stateSpace);
		
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
