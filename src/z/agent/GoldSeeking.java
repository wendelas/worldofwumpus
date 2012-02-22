package z.agent;

import java.awt.Point;
import java.util.List;

import z.wumpus.StateSpace;
import z.wumpus.WumpusWorld;
import aima.util.Pair;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 * This class seeks the gold
 */
public class GoldSeeking extends Agent {


	@Override
	public String identify() {
		return "Gold AI";
	}

	@Override
	public Point search(Point current, StateSpace stateSpace) {
		
		Point resolved = null;
		
		List<Point> safeMoves = Agent.getSafeMoves(stateSpace);
		
		if (safeMoves.isEmpty()) {
			resolved = null;
		} else {
			// Calculate the approximate gold location.
			Pair<Double, Double> goldEstimate = stateSpace.estimateGoldSpace();
			// Calculate the lowest utility, as a combination of move cost and distance to the gold.
			double leastUtility = Double.POSITIVE_INFINITY;
			for (Point move : safeMoves) {
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
	private double approximateMoveCost(Point space, Pair<Double, Double> estimate) {
		
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
				Point goldGuess = new Point(x,y);
				cost += StateSpace.distBetween(space, goldGuess);
			}
		}
		if (n == 0) {
			return Double.POSITIVE_INFINITY;
		}
		return (cost / (double)n);
	}

}