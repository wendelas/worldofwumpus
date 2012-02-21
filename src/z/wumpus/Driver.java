/**
 * 
 */
package z.wumpus;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author iannonen
 *
 */
public class Driver {

	public static final LogLevel STOPPING_LOG_LEVEL = LogLevel.OFF;
	public static final boolean MOVE_LOGGING = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random r = new Random();
		
		WumpusWorld w;
		boolean reset = false;
		do {
			reset = false;
			w = new WumpusWorld();
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
		/*
		do {
			w = new WumpusWorld();
			w.randomize(r);
		} while (!w.hasGold(3, 3) || !w.hasPit(3, 2) || !w.hasPit(2, 3));
		*/
		/*
		do {
			w = new WumpusWorld();
			w.randomize(r);
		} while (!w.hasGold(2, 2) || !(w.hasPit(2, 3) || w.hasWumpus(2, 3)) || !(w.hasPit(2,1) || w.hasWumpus(2,1)) || !(w.hasPit(1,2) || w.hasWumpus(1,2)) || !(w.hasPit(3,2) || w.hasWumpus(3,2)));
		*/
		System.out.println(w);
		
		Map<String, ResultsData> results = new TreeMap<String, ResultsData>();
		
		// Generate the list of agents.
		List<ExplorerStrategy> resolvers = new LinkedList<ExplorerStrategy>();
		resolvers.add(new ChickenLittleExplorer());
		resolvers.add(new RamboExplorer());
		resolvers.add(new HeuristicExplorer());
		resolvers.add(new GoldSeekingExplorer());
		
		for (ExplorerStrategy resolver : resolvers) {
			KBWumpusAgent agent = new KBWumpusAgent((WumpusWorld)w.clone(), resolver);
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
			ResultsData result = agent.getResults();
			result.markTime(time);
			results.put(agent.identify(), result);
		}
		System.out.println("--------------------------------------------");
		System.out.println("Wumpus World execution complete.");
		System.out.println("--------------------------------------------");
		System.out.println("Final results:,");
		System.out.println("--------------------------------------------");
		System.out.println("Name:," + ResultsData.getHeader());
		for (String agentName : results.keySet()) {
			ResultsData data = results.get(agentName);
			System.out.println(agentName + "," + data.toString());
		}
		System.out.println("--------------------------------------------");
		System.out.println("Original Puzzle:,");
		System.out.println(w.toString().replace("\t", ",").replace("\n", ",\n") + ",");
		System.out.println("--------------------------------------------");
	}
	
	
}
