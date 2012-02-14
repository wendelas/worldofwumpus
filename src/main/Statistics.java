package main;


public class Statistics {

	
	private static int numDeaths;
	
	private static int numWins;
	
	private static int wumpuskills;

	private static int tries;
	
	private static Statistics stats;
	
	private Statistics(){
		numDeaths = 0;
		numWins = 0;
		wumpuskills = 0;
		tries = 0;
	}
	
	public static Statistics getInstance(){
		if(stats == null) stats = new Statistics(); 
		return stats;
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
		numDeaths++;
	}
	
	public static void incrementWins(){
		numWins++;
	}
	
	public static void incrementKills(){
		wumpuskills++;
	}
	
	public static void incrementTries(){
		tries++;
	}
	
	public static void resetStats(){
		numDeaths = 0;
		numWins = 0;
		wumpuskills = 0;
		tries = 0;
	}
}
