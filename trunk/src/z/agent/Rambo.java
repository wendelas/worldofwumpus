package z.agent;

import java.awt.Point;
import java.util.List;
import java.util.Random;

import z.wumpus.StateSpace;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 * This class is the Rambo AI
 */
public class Rambo extends Agent {

	public static Random r = new Random();
	
	/**
	 * @see z.agent.Agent#resolveFringe(aima.util.Pair, java.util.List, z.wumpus.StateSpace)
	 */
	@Override
	public Point search(Point current, StateSpace stateSpace) {
		List<Point> safeMoves = Agent.getSafeMoves(stateSpace);
		if (safeMoves.size() < 1) return null;
		else if (safeMoves.size() == 1) return safeMoves.get(0);
		else return safeMoves.get(r.nextInt(safeMoves.size()));
	}
	
	@Override
	public String toString() {
		return "Rambo";
	}
}
