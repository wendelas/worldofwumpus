package main;

import gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import agent.ChickenLittle;
import agent.ExplorerStrategy;
import agent.KBWumpusAgent;
import agent.Rambo;

/**
 * @author ebertb schmidbauerk
 * The main driver for the program
 */
public class Driver {

	public static final boolean MOVE_LOGGING = false;


	public static void main(String[] args) {
		
		Random r = new Random();
		
		GameBoard w;
		boolean reset = false;
		do {
			reset = false;
			
			w = new GameBoard();
			w.randomize(r);
			if (w.hasGold(0, 1) || w.hasGold(1, 0)) {
				reset = reset || r.nextBoolean();
			} 
			if (w.hasWumpus(0, 1) || w.hasWumpus(1, 0)) {
				reset = reset || r.nextBoolean();
			}
			if (w.hasPit(0, 1) || w.hasPit(1, 0)) {
				reset = reset || r.nextBoolean();
			}
		} while (w.hasGold(0, 1) || w.hasGold(1, 0) || r.nextBoolean());
		
		System.out.println(w);
		
		Map<String, Statistics> results = new TreeMap<String, Statistics>();
		
		
		List<ExplorerStrategy> resolvers = new LinkedList<ExplorerStrategy>();
		resolvers.add(new ChickenLittle());
		resolvers.add(new Rambo());

		
		for (ExplorerStrategy resolver : resolvers) {
			KBWumpusAgent agent = new KBWumpusAgent((GameBoard)w.clone(), resolver);
			if (MOVE_LOGGING) {
				System.out.println("--------------------------------------------");
				System.out.println(resolver.identify());
				System.out.println("--------------------------------------------");
			} else {
				System.out.println("Solving for " + resolver.identify() + "...");
			}
			long time = 0;
			while (!agent.isStopped()) {
				long startTime = System.nanoTime();
				agent.update();
				time += System.nanoTime() - startTime;
				if (MOVE_LOGGING) {
					agent.printStateSpace();
				}
			}
			Statistics result = agent.getResults();
			result.markTime(time);
			results.put(agent.identify(), result);
		}
		System.out.println("--------------------------------------------");
		System.out.println("Wumpus World execution complete.");
		System.out.println("--------------------------------------------");
		System.out.println("Final results:,");
		System.out.println("--------------------------------------------");
		System.out.println("Name:," + Statistics.getHeader());
		for (String agentName : results.keySet()) {
			Statistics data = results.get(agentName);
			System.out.println(agentName + "," + data.toString());
		}
		System.out.println("--------------------------------------------");
		System.out.println("Original Puzzle:,");
		System.out.println(w.toString().replace("\t", ",").replace("\n", ",\n") + ",");
		System.out.println("--------------------------------------------");
	}
	
	
}
