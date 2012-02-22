/**
 * 
 */
package agent;

import java.util.List;


import aima.util.Pair;

/**
 * @author iannonen
 *
 */
public class ChickenLittleExplorer extends ExplorerStrategy {

	@Override
	public Pair<Integer, Integer> resolveFringe(Pair<Integer, Integer> current, StateSpace stateSpace) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return "Chicken Little the Wumpus Novice";
	}

}
