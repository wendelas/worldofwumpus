package Gameboard;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import agent.StateSpace;
import aima.util.Pair;

/**
 * @author ebertb schmidbauerk
 * Keeps track of the path it takes
 */
public class PathNavigator {

	private StateSpace stateSpace;
	
	
	public PathNavigator(StateSpace stateSpace) {
		this.stateSpace = stateSpace;
	}
	
	
	public List<Pair<Integer, Integer>> resolvePath(Pair<Integer, Integer> start, Pair<Integer, Integer> goal) throws IllegalArgumentException, IllegalStateException {
		
		
		if (start == null) {
			throw new IllegalArgumentException("Error: Start space must not be null!");
		}
		if (goal == null) {
			throw new IllegalArgumentException("Error: Goal space must not be null!");
		}
		
		
		if (start.equals(goal)) {
			return new LinkedList<Pair<Integer, Integer>>();
		}
		
		
		if (!stateSpace.isVisited(start.getFirst(), start.getSecond())) {
			throw new IllegalArgumentException("Error: Start space has not been visited!");
		}
		
		
		List<Pair<Integer, Integer>> fringe = stateSpace.getFringe();
		if (!stateSpace.isVisited(goal.getFirst(), goal.getSecond()) && !fringe.contains(goal)) {
			throw new IllegalArgumentException("Error: Goal space must be within visited set or fringe set!");
		}
		
		
		Map<Pair<Integer, Integer>, Pair<Integer, Integer>> cameFrom = new HashMap<Pair<Integer, Integer>, Pair<Integer, Integer>>();
		Map<Pair<Integer, Integer>, Double> gScore = new HashMap<Pair<Integer, Integer>, Double>();
		List<Pair<Integer, Integer>> closedSet = new LinkedList<Pair<Integer, Integer>>();
		List<Pair<Integer, Integer>> openSet = new LinkedList<Pair<Integer, Integer>>();
		
		openSet.add(start);
		gScore.put(start, 0.0);
		
		while (!openSet.isEmpty()) {
			Pair<Integer, Integer> x = lowestFScore(openSet, gScore, goal);
			if (x.equals(goal)) {
				return reconstructPath(cameFrom, goal);
			}
			
			openSet.remove(x);
			closedSet.add(x);
			
			List<Pair<Integer, Integer>> neighbors = neighborNodes(x, fringe);
			for (Pair<Integer, Integer> y : neighbors) {
				if (closedSet.contains(y)) {
					continue;
				}
				
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
	
	
	private double g(Pair<Integer, Integer> space, Map<Pair<Integer, Integer>, Double> gScore) {
		if (gScore.containsKey(space)) {
			return gScore.get(space);
		}
		return 0.0;
	}

	
	private List<Pair<Integer, Integer>> neighborNodes(Pair<Integer, Integer> space, List<Pair<Integer, Integer>> fringe) {
		List<Pair<Integer, Integer>> initialList =  stateSpace.getNeighbors(space);
		List<Pair<Integer, Integer>> finalList = new LinkedList<Pair<Integer, Integer>>();
		for (Pair<Integer, Integer> neighbor : initialList) {
			int x = neighbor.getFirst();
			int y = neighbor.getSecond();
			
			if (fringe.contains(neighbor) || (stateSpace.isVisited(x, y) && stateSpace.isSafe(x, y))) {
				finalList.add(neighbor);
			}
		}
		return finalList;
	}

	
	private Pair<Integer, Integer> lowestFScore(List<Pair<Integer, Integer>> openSet, Map<Pair<Integer, Integer>, Double> gScore, Pair<Integer, Integer> goal) {
		double lowestScore = Double.POSITIVE_INFINITY;
		Pair<Integer, Integer> bestSpace = null;
		for (Pair<Integer, Integer> space : openSet) {
			double score = f(space, gScore, goal);
			if (score < lowestScore) {
				bestSpace = space;
				lowestScore = score;
			}
		}
		return bestSpace;
	}
	
	
	private double f(Pair<Integer, Integer> space, Map<Pair<Integer, Integer>, Double> gScore, Pair<Integer, Integer> goal) {
		return g(space, gScore) + StateSpace.distBetween(space, goal);
	}

	private List<Pair<Integer, Integer>> reconstructPath(Map<Pair<Integer, Integer>, Pair<Integer, Integer>> cameFrom, Pair<Integer, Integer> target) {
		List<Pair<Integer, Integer>> path = new LinkedList<Pair<Integer, Integer>>();
		if (cameFrom.containsKey(target)) {
			path = reconstructPath(cameFrom, cameFrom.get(target));
			path.add(0, target);
		}
		return path;
	}
	
	
	public static double getSafetyCost(Pair<Integer, Integer> space, StateSpace stateSpace) {
		double cost = 0.0;
		
		if (space == null) {
			return cost;
		}
		
		int x = space.getFirst();
		int y = space.getSecond();
		
		if (stateSpace.isPit(x, y) || (!stateSpace.isWumpusDead() && stateSpace.isWumpus(x, y))) {
			cost = 15.0;
		}
		
		List<Pair<Integer, Integer>> neighbors = stateSpace.getNeighbors(space);
		for (Pair<Integer, Integer> neighbor : neighbors) {
			int nx = neighbor.getFirst();
			int ny = neighbor.getSecond();
			if (stateSpace.isBreezy(nx, ny)) {
				cost += 0.25;
			}
			if (stateSpace.isSmelly(nx, ny)) {
				cost += 0.2;
			}
		}
		
		
		return cost;
	}
}
