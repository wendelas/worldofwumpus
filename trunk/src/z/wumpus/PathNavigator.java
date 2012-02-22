package z.wumpus;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 * This class allows the AIs to get the next state's status
 */
public class PathNavigator {

	private StateSpace stateSpace;
	
	/**
	 * Constructor. Takes in a Pathfinder, and uses it to resolve a path when requested.
	 * @param pf The Pathfinder to use.
	 */
	public PathNavigator(StateSpace stateSpace) {
		this.stateSpace = stateSpace;
	}
	
	/**
	 * Implements an A* search algorithm on previously-allocated nodes to find the selected node.
	 * @param start The starting space. Must be within the explored set.
	 * @param goal The goal space. Must be within the explored set or the fringe set.
	 * @return A list of spaces to traverse, from the start space (exclusive) to the goal space (inclusive).
	 * @throws IllegalArgumentException if the start or goal spaces are not within the appropriate sets.
	 * @throws IllegalStateException if there is no safe path between the two states. Should never be thrown.
	 */
	public List<Point> resolvePath(Point start, Point goal) throws IllegalArgumentException, IllegalStateException {
		
		
		if (start == null)
			throw new IllegalArgumentException("Error: Start space must not be null!");
		if (goal == null)
			throw new IllegalArgumentException("Error: Goal space must not be null!");
		
		
		if (start.equals(goal))
			return new LinkedList<Point>();
		
		
		if (!stateSpace.isVisited(start.x, start.y))
			throw new IllegalArgumentException("Error: Start space has not been visited!");
		
		
		List<Point> fringe = stateSpace.getFringe();
		if (!stateSpace.isVisited(goal.x, goal.y) && !fringe.contains(goal))
			throw new IllegalArgumentException("Error: Goal space must be within visited set or fringe set!");
		
		
		Map<Point, Point> cameFrom = new HashMap<Point, Point>();
		Map<Point, Double> gScore = new HashMap<Point, Double>();
		List<Point> closedSet = new LinkedList<Point>();
		List<Point> openSet = new LinkedList<Point>();
		
		openSet.add(start);
		gScore.put(start, 0.0);
		
		while (!openSet.isEmpty()) {
			Point x = lowestFScore(openSet, gScore, goal);
			if (x.equals(goal)) return reconstructPath(cameFrom, goal);
			
			openSet.remove(x);
			closedSet.add(x);
			
			List<Point> neighbors = neighborNodes(x, fringe);
			for (Point y : neighbors) {
				if (closedSet.contains(y)) continue;
				
				
				double tentativeGScore = g(x, gScore) + stateSpace.getMoveCost(x, y);
				boolean tentativeIsBetter;
				
				if (!openSet.contains(y)) {
					openSet.add(0, y);
					tentativeIsBetter = true;
				} else if (tentativeGScore < g(y, gScore)) {
					tentativeIsBetter = true;
				} else {
					tentativeIsBetter = false;
				}
				
				if (tentativeIsBetter) {
					cameFrom.put(y, x);
					gScore.put(y, tentativeGScore);
				}
			}
		}
		throw new IllegalStateException("Error: No safe path found between start and goal spaces!");
	}
	
	/**
	 * Calculates the depth score (weighted by safety cost) of the most optimal path from the starting space to the given space.
	 * @param space The space to be used for computation.
	 * @param gScore The previously-recorded map of weighted depth scores from the starting space.
	 * @return The weighted depth score for the given space.
	 */
	private double g(Point space, Map<Point, Double> gScore) {
		if (gScore.containsKey(space)) return gScore.get(space);
		return 0.0;
	}

	/**
	 * Fetches all neighboring nodes of the graph from the explored set or the fringe set.
	 * @param space The space from which to obtain the neighboring spaces.
	 * @param fringe The fringe set for the Wumpus World agent.
	 * @return A list of spaces.
	 */
	private List<Point> neighborNodes(Point space, List<Point> fringe) {
		List<Point> initialList =  stateSpace.getNeighbors(space);
		List<Point> finalList = new LinkedList<Point>();
		for (Point neighbor : initialList) {
			int x = neighbor.x;
			int y = neighbor.y;
			
			if (fringe.contains(neighbor) || (stateSpace.isVisited(x, y) && stateSpace.isSafe(x, y))) 
				finalList.add(neighbor);
		}
		return finalList;
	}

	/**
	 * Selects the space from the open set with the lowest combined utility score.
	 * @param openSet The set of open nodes along the A* breadth-first search frontier to choose from.
	 * @param gScore The breadth-first search tree, containing the shortest discovered distance to a specific point, weighted by safety costs.
	 * @param goal The goal space; used to determine the heuristic cost estimate.
	 * @return The space in the open set with the lowest combined utility score.
	 */
	private Point lowestFScore(List<Point> openSet, Map<Point, Double> gScore, Point goal) {
		double lowestScore = Double.POSITIVE_INFINITY;
		Point bestSpace = null;
		for (Point space : openSet) {
			double score = f(space, gScore, goal);
			if (score < lowestScore) {
				bestSpace = space;
				lowestScore = score;
			}
		}
		return bestSpace;
	}
	
	/**
	 * Calculates the combined utility score of the given node in the search graph, as a function of search tree depth and heuristic cost estimation.
	 * @param space The space to evaluate.
	 * @param gScore The breadth-first search tree, containing the shortest discovered distance to a specific point, weighted by safety costs.
	 * @param goal The goal space; used to determine the heuristic cost estimate.
	 * @return A utility score, as a positive double value.
	 */
	private double f(Point space, Map<Point, Double> gScore, Point goal) {
		return g(space, gScore) + StateSpace.distBetween(space, goal);
	}

	/**
	 * Reconstructs the path from the start to the goal, in an iterative fashion.
	 * @param cameFrom The map of spaces to their immediate predecessors in the breadth-first search tree.
	 * @param target The space closer to the goal for this iteration; on the first iteration, will be the goal space.
	 * @return A list of spaces comprising a path segment; for the first iteration, will end with the goal space and begin one space away from the starting space.
	 */
	private List<Point> reconstructPath(Map<Point, Point> cameFrom, Point target) {
		List<Point> path = new LinkedList<Point>();
		if (cameFrom.containsKey(target)) {
			path = reconstructPath(cameFrom, cameFrom.get(target));
			path.add(0, target);
		}
		return path;
	}
	
	/**
	 * Gets the estimated safety cost of a space; a higher safety cost indicates a higher likelihood of being dangerous.
	 * @param space The space to estimate.
	 * @param stateSpace The state space.
	 * @return The safety cost of the given space.
	 */
	public static double getSafetyCost(Point space, StateSpace stateSpace) {
		double cost = 0.0;
		
		if (space == null) return cost;
		
		int x = space.x;
		int y = space.y;
		
		if (stateSpace.isPit(x, y) || (!stateSpace.isWumpusDead() && stateSpace.isWumpus(x, y))) {
			cost = 15.0;
		}
		
		List<Point> neighbors = stateSpace.getNeighbors(space);
		for (Point neighbor : neighbors) {
			int nx = neighbor.x;
			int ny = neighbor.y;
			if (stateSpace.isBreezy(nx, ny)) {
				cost += 0.25;
			}
			if (stateSpace.isStench(nx, ny)) {
				cost += 0.2;
			}
		}
		return cost;
	}
}
