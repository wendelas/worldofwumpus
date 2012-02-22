package z.agent;

import java.awt.Point;
import java.util.List;

import z.wumpus.StateSpace;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 * This class is the Chicken Little AI.
 * Cautious searching of the game board.
 * Will climb out if no safe moves exist.
 */
public class ChickenLittle extends Agent {

	@Override
	public Point search(Point current, StateSpace stateSpace) {
		List<Point> safe = Agent.getSafeMoves(stateSpace);
		
		if (safe.size() > 0) return safe.get(0);
		else return null;
	}
	
	@Override
	public String toString() {
		return "Chicken Little";
	}

}
