package z.agent;

import java.awt.Point;
import java.util.List;

import z.wumpus.StateSpace;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 * This class is the Heuristic AI
 */
public class Heuristic extends Agent {

	
	@Override
	public Point search(Point current, StateSpace stateSpace) {

		Point resolved = null;
		
		List<Point> safeMoves = Agent.getSafeMoves(stateSpace);
		
		if (safeMoves.isEmpty()) {
			resolved = null;
		} else {
			double leastUtility = Double.POSITIVE_INFINITY;
			for (Point move : safeMoves) {
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
