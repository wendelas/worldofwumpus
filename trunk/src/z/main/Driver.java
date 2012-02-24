package z.main;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import z.agent.Agent;
import z.agent.ChickenLittle;
import z.agent.Rambo;
import z.wumpus.GameBoard;
import z.wumpus.Solver;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 *This class is the main driver for the program
 */
public class Driver {

	public static final boolean MOVE_LOGGING = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int g = 9;
		while(g>0){
			GameBoard w= new GameBoard();

			System.out.println(w);

			Map<String, Statistics> results = new TreeMap<String, Statistics>();


			List<Agent> agents = new LinkedList<Agent>();
			agents.add(new ChickenLittle());
			//	agents.add(new Rambo());


			for (Agent resolver : agents) {
				Solver agent = new Solver((GameBoard)w.clone(), resolver);
				if (MOVE_LOGGING) {
					System.out.println("--------------------------------------------");
					System.out.println(resolver.toString());
					System.out.println("--------------------------------------------");
				} else {
					System.out.println("Solving for " + resolver.toString() + "...");
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
				results.put(agent.toString(), result);
			}

			String spacer = "--------------------------------------------";
			System.out.printf("%s\n%s\n%s\n%s\n", spacer, "Trials complete.", "Results:", 
					String.format("%10s %88s","Name:",Statistics.getHeader()));
			for (String agentName : results.keySet()) {
				Statistics data = results.get(agentName);
				System.out.println(String.format("%10s %88s", agentName ,data.toString()));
			}
			g--;
		}
		
	}
}
