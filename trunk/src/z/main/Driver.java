package z.main;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import z.agent.Agent;
import z.agent.ChickenLittle;
import z.agent.Rambo;
import z.wumpus.GameBoard;
import z.wumpus.KBWumpusAgent;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 * This class is the main driver for the program
 */
public class Driver {

	public static final boolean MOVE_LOGGING = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Random r = new Random();
		
		GameBoard w;
		boolean reset = false;
		do {
			reset = false;
			w = new GameBoard();
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
		

		List<Agent> resolvers = new LinkedList<Agent>();
		resolvers.add(new ChickenLittle());
		resolvers.add(new Rambo());
		
		for (Agent resolver : resolvers) {
			KBWumpusAgent agent = new KBWumpusAgent((GameBoard)w.clone(), resolver);
			System.out.println("Solving for " + resolver.toString() + "...");
			long time = 0;
			while (!agent.isStopped()) {
				long startTime = System.nanoTime();
				agent.update();
				time += System.nanoTime() - startTime;
			}
			Statistics result = agent.getResults();
			result.setTime(time);
			results.put(agent.identify(), result);
		}
		System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-\n"
				+"Game Play complete.\n"
				+"-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-\n"
				+"Final results:\n"
				+"Name: \t\t" + Statistics.getHeader());
		for (String agentName : results.keySet())
			System.out.println(agentName + "\t" + results.get(agentName).toString());
	}
}
