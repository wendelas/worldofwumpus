package main;


public class Statistics {

	
	private static int numDeaths;
	
	private static int numWins;
	
	private static int wumpuskills;

	private static int tries;
	
	private static Statistics Instance;
	
	private Statistics(){
		numDeaths = 0;
		numWins = 0;
		wumpuskills = 0;
		tries = 0;
	}
	
	public static synchronized Statistics getInstance(){
		if(Instance == null){
			Instance = new Statistics();
		}
		return Instance;
	}
	
	
	public static int getNumDeaths(){
		return numDeaths;
	}
	
	public static int getNumWins(){
		return numWins;
	}
	
	public static int getWumpusKills(){
		return wumpuskills;
	}
	
	public static int getTries(){
		return tries;
	}
	
	public static void incrementDeaths(){
		numDeaths += 1;
	}
	
	public static void incrementWins(){
		numWins += 1;
	}
	
	public static void incrementKills(){
		wumpuskills += 1;
	}
	
	public static void incrementTries(){
		tries += 1;
	}
	
	public static void resetStats(){
		numDeaths = 0;
		numWins = 0;
		wumpuskills = 0;
		tries = 0;
	}
}
