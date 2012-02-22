/**
 * 
 */
package z.wumpus;

import java.util.List;
import java.util.Random;

import aima.util.Pair;

/**
 * @author iannonen
 *
 */
public class RamboExplorer extends ExplorerStrategy {

	public static Random r = new Random();
	
	/**
	 * @see z.wumpus.ExplorerStrategy#resolveFringe(aima.util.Pair, java.util.List, z.wumpus.StateSpace)
	 */
	@Override
	public Pair<Integer, Integer> resolveFringe(Pair<Integer, Integer> current, StateSpace stateSpace) {
		
		Pair<Integer, Integer> resolved = null;
		
		List<Pair<Integer, Integer>> safeMoves = ExplorerStrategy.getSafeMoves(stateSpace);
		
		if (safeMoves.size() < 1) {
			System.out.println("I'm trapped. Crap.");
			return null;
		} else if (safeMoves.size() == 1) {
			resolved = safeMoves.get(0);
		} else {
			resolved = safeMoves.get(r.nextInt(safeMoves.size()));
		}
		
		return resolved;
	}
	
	
	@Override
	public String identify() {
		// TODO Auto-generated method stub
		return "Rambo the Random-Resolving Wumpus Hunter";
	}

}
