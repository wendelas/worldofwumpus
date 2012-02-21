/**
 * 
 */
package z.wumpus;

import java.util.List;

import aima.util.Pair;

/**
 * @author iannonen
 *
 */
public class GoldSeekingExplorer extends ExplorerStrategy {

	/**
	 * @see z.wumpus.ExplorerStrategy#identify()
	 */
	@Override
	public String identify() {
		return "Gandalf the Gold";
	}

	/**
	 * @see z.wumpus.ExplorerStrategy#resolveFringe(aima.util.Pair, z.wumpus.StateSpace)
	 */
	@Override
	public Pair<Integer, Integer> resolveFringe(Pair<Integer, Integer> current, StateSpace stateSpace) {
		
		Pair<Integer, Integer> resolved = null;
		
		List<Pair<Integer, Integer>> safeMoves = ExplorerStrategy.getSafeMoves(stateSpace);
		
		if (safeMoves.isEmpty()) {
			resolved = null;
		} else {
			// Calculate the approximate gold location.
			Pair<Double, Double> goldEstimate = stateSpace.estimateGoldSpace();
			// Calculate the lowest utility, as a combination of move cost and distance to the gold.
			double leastUtility = Double.POSITIVE_INFINITY;
			for (Pair<Integer, Integer> move : safeMoves) {
				if (move == null) {
					continue;
				}
				double utility = stateSpace.getMoveCost(current, move) + approximateMoveCost(move, goldEstimate);
				if (utility < leastUtility) {
					leastUtility = utility;
					resolved = move;
				}
			}
		}
		
		return resolved;
	}
	
	/**
	 * Calculates the move cost between a fixed space and a location estimate, as an average of the costs of the 1-4 bounding spaces.
	 * @param space The space to move from.
	 * @param estimate The estimated location.
	 * @return The approximate move cost for the estimate.
	 */
	private double approximateMoveCost(Pair<Integer, Integer> space, Pair<Double, Double> estimate) {
		// Null-check.
		if (space == null || estimate == null) {
			return 0.0;
		}
		
		double cost = 0.0;
		int n = 0;
		for (int x = (int)Math.floor(estimate.getFirst()); x <= Math.ceil(estimate.getFirst()); x++) {
			for (int y = (int)Math.floor(estimate.getSecond()); y <= Math.ceil(estimate.getSecond()); y++) {
				if (!WumpusWorld.inBounds(x, y)) {
					continue;
				}
				n++;
				Pair<Integer, Integer> goldGuess = new Pair<Integer, Integer>(x,y);
				cost += StateSpace.distBetween(space, goldGuess);
			}
		}
		if (n == 0) {
			return Double.POSITIVE_INFINITY;
		}
		return (cost / (double)n);
	}

}
