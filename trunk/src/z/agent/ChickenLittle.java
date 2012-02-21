package z.agent;

import java.awt.Point;
import java.util.List;

import z.wumpus.StateSpace;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 * This class is the Chicken Little AI
 */
public class ChickenLittle extends Agent {

	@Override
	public Point search(Point current, StateSpace stateSpace) {
		Point resolved = null;
		
		List<Point> safeMoves = Agent.getSafeMoves(stateSpace);
		
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
