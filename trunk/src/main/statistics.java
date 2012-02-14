package agent.main;


public class statistics {

	
	private int numDeaths;
	
	private int numWins;
	
	private int wumpuskills;

	private int tries;
	
	public statistics(){
		numDeaths = 0;
		numWins = 0;
		wumpuskills = 0;
		tries = 0;
	}
	
	
	public int getNumDeaths(){
		return numDeaths;
	}
	
	public int getNumWins(){
		return numWins;
	}
	
	public int getWumpusKills(){
		return wumpuskills;
	}
	
	public int getTries(){
		return tries;
	}
	
	public void incrementDeaths(){
		numDeaths += 1;
	}
	
	public void incrementWins(){
		numWins += 1;
	}
	
	public void incrementKills(){
		wumpuskills += 1;
	}
	
	public void incrementTries(){
		tries += 1;
	}
	
	public void resetStats(){
		numDeaths = 0;
		numWins = 0;
		wumpuskills = 0;
		tries = 0;
	}
}
