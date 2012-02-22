package z.WumpusWorld;

import java.awt.Point;
import java.util.Observable;
import java.util.Random;




public class wumpusWorld extends Observable{
	
	
	private int sizeX;
	private int sizeY;
	private int gold;
	private int wins;
	private int wumpusKills;
	private int deaths;
	private int numMoves;
	private int tries;
	
	
	private boolean hasGold;
	private boolean wumpusDead;
	private boolean agentDead;
	private boolean agentFinished;
	private boolean firedArrow;
	
	private Point wumpusLocation;
	private Point goldLocation;
	private Point agentLocation;
	private Point startingLocation;
	
	private static final char WUMPUS ='W';
	private static final char GOLD = 'G';
	private static final char BREEZE = 'B';
	private static final char PIT = 'P';
	private static final char STENCH = 'S';
	
	
	private String[][] status;
	private boolean[][] discoveredTiles;
	
	
	public wumpusWorld(int sizeX, int sizeY){
		this.sizeX=sizeX;
		this.sizeY=sizeY;
		this.startingLocation=new Point(0, sizeY-1); //Watch out for this
		this.agentDead=false;
		this.agentFinished=false;
		this.gold=0;
		this.tries=0;
		this.firedArrow=false;
		this.numMoves=0;
		this.hasGold=false;
		this.wumpusDead=false;
		this.agentLocation=new Point(startingLocation.x,startingLocation.y);
		this.status = new String[sizeX][sizeY];
		this.discoveredTiles = new boolean[sizeX][sizeY];
		populateWorld();
	}
	
	private void populateWorld(){
		Random rand = new Random();
		for(int w=0; w<sizeX; w++){
			for(int h=0; h<sizeY; h++){
				status[w][h]="";
			}
		}
		
		for(int w=0; w<sizeX; w++){
			for(int h=0; h<sizeY; h++){
				
				if(!(w == startingLocation.x && h == startingLocation.y))
				{
					int test = rand.nextInt(5);
					if(test == 0){
						
						status[w][h] = ""+PIT;
						if(w>0 && !status[w-1][h].contains(""+PIT) && !status[w-1][h].contains(""+BREEZE)){
							status[w-1][h] += BREEZE;
						}
						if(h>0 && !status[w][h-1].contains(""+PIT) && !status[w][h-1].contains(""+BREEZE)){
							status[w][h-1] += BREEZE;
						}
						if(w<sizeX-1 && !status[w+1][h].contains(""+PIT) && !status[w+1][h].contains(""+BREEZE)){
							status[w+1][h] += BREEZE;
						}
						if(h<sizeY-1 && !status[w][h+1].contains(""+PIT) && !status[w][h+1].contains(""+BREEZE)){
							status[w][h+1] += BREEZE;
						}
					}
				}
				
				discoveredTiles[w][h] = false;
			}
		}
		
		
		discoveredTiles[agentLocation.x][agentLocation.y] =true;
		
		Point p = new Point(rand.nextInt(sizeX),rand.nextInt(sizeY));
		while(p.x == startingLocation.x  && p.y == startingLocation.y){
			p = new Point(rand.nextInt(sizeX),rand.nextInt(sizeY));
		}
		
		wumpusLocation = new Point(p.x,p.y);
		
		
		if(wumpusLocation.x>0){
			status[wumpusLocation.x-1][wumpusLocation.y] += STENCH;
		}
		if(wumpusLocation.y>0){
			status[wumpusLocation.x][wumpusLocation.y-1] += STENCH;
		}
		if(wumpusLocation.x<sizeX-1){
			status[wumpusLocation.x+1][wumpusLocation.y] += STENCH;
		}
		if(wumpusLocation.y<sizeY-1){
			status[wumpusLocation.x][wumpusLocation.y+1] += STENCH;
		}
		
		status[wumpusLocation.x][wumpusLocation.y] += "" + WUMPUS + STENCH;
		
		goldLocation = new Point(rand.nextInt(sizeX),rand.nextInt(sizeY));
		
		status[goldLocation.x][goldLocation.y] +=GOLD;
		
	}
	
	private void resetWorld(){
		super.setChanged();
		super.notifyObservers();
		tries++;
		this.agentDead=false;
		this.agentFinished=false;
		this.firedArrow=false;
		this.hasGold=false;
		this.wumpusDead=false;
		this.agentLocation=new Point(startingLocation.x, startingLocation.y);
		this.status=new String[sizeX][sizeY];
		this.discoveredTiles=new boolean[sizeX][sizeY];
		populateWorld();
	}
	
	
	
	
	public boolean[][] getDiscoveredTiles(){
		return discoveredTiles;
	}
	
	public String[][] getStatus(){
		return status;
	}
	
	public int getGold(){
		return gold;
	}
	
	public int getWumpusKills(){
		return wumpusKills;
	}
	
	public int getDeaths(){
		return deaths;
	}
	
	public int getWins(){
		return wins;
	}
	
	public boolean grabGold(){
		boolean gotten = false;
		if(hasGold){
			gotten = false;
		}else if(agentLocation.x == goldLocation.x && agentLocation.y == goldLocation.y){
			status[goldLocation.x][goldLocation.y] = status[goldLocation.x][goldLocation.y].replace(""+GOLD, "");
			hasGold=true;
			gotten=true;
			this.setChanged();
			notifyObservers();
		}
		return gotten;
	}
	
	
	
	public boolean climb(){
		boolean climbed = false;
		if(agentLocation.x == startingLocation.x && agentLocation.y == startingLocation.y){
			agentFinished = true;
			if(hasGold){
				gold += 1000;
				wins++;
			}
			resetWorld();
			climbed = true;
			System.out.println("Notifying");
			super.setChanged();
			notifyObservers();
			System.out.println("Notified");
		}
		return climbed;
	}
	
	private void killWumpus(){
		status[wumpusLocation.x][wumpusLocation.y] = status[wumpusLocation.x][wumpusLocation.y].replace(""+WUMPUS, "");
		gold+=100;
		wumpusDead = true;
		wumpusKills++;
	}
	
	
	
	public boolean fireTop(){
		boolean fired = false;
		if(wumpusDead){
			fired = false;
		}else if(agentLocation.x == wumpusLocation.x && agentLocation.y > wumpusLocation.y){
			killWumpus();
			fired = true;
		}
		this.firedArrow = true;
		return fired;
	}
	
	public boolean fireBottom(){
		boolean fired = false;
		if(wumpusDead){
			fired = false;
		}else if(agentLocation.x == wumpusLocation.x && agentLocation.y < wumpusLocation.y){
			killWumpus();
			fired = true;
		}
		this.firedArrow = true;
		return fired;
	}
	
	public boolean fireLeft(){
		boolean fired = false;
		if(wumpusDead){
			return false;
		}else if(agentLocation.x > wumpusLocation.x && agentLocation.y == wumpusLocation.y){
			killWumpus();
			fired = true;
		}
		this.firedArrow = true;
		return fired;
	}
	
	public boolean fireRight(){
		boolean fired = false;
		if(wumpusDead){
			return false;
		}else if(agentLocation.x < wumpusLocation.x && agentLocation.y == wumpusLocation.y){
			killWumpus();
			fired = true;
		}
		this.firedArrow = true;
		return fired;
	}
	
	
	private void move(){
		discoveredTiles[agentLocation.x][agentLocation.y] = true;
		char[] agentStats = getStatusAtLocation(agentLocation);
		boolean dead = false;
		for(int i = 0; i<agentStats.length; i++){
			if(agentStats[i] == PIT || agentStats[i] == WUMPUS){
				dead = true;
				break;
			}
		}
		if(dead){
			this.agentDead = true;
			gold -= 1000;
			deaths++;
			resetWorld();
		}else{
			gold--;
		}
		numMoves++;
		super.setChanged();
		super.notifyObservers();
	}
	
	
	
	public boolean moveTop(){
		if(agentLocation.y <= 0){
			return false;
		}
		
		agentLocation.y--;
		move();
		return true;
	}
	
	public boolean moveBottom(){
		if(agentLocation.y >= sizeY-1){
			return false;
		}
		
		agentLocation.y++;
		move();
		return true;
	}
	
	public boolean moveLeft(){
		if(agentLocation.x <= 0){
			return false;
		}
		
		agentLocation.x--;
		move();
		return true;
	}
	
	public boolean moveRight(){
		if(agentLocation.x >= sizeX-1){
			return false;
		}
		
		agentLocation.x++;
		move();
		return true;
	}
	
	
	 public int getWidth(){
		 return sizeX;
	 }
	 
	 public int getHeight(){
		 return sizeY;
	 }
	
	 public int getNumMoves(){
		 return numMoves;
	 }
	
	 public char[] getStatusAtLocation(Point p){
		 return status[p.x][p.y].toCharArray();
	 }
	 
	 public Point getWumpusLocation(){
		 return wumpusLocation;
	 }
	 
	 public Point getGoldLocation(){
		 return goldLocation;
	 }
	 
	 public Point getAgentLocation(){
		 return agentLocation;
	 }
	 
	 public Point getStartLocation(){
		 return startingLocation;
	 }
	 
	 public boolean didAgentDie(){
		 return agentDead;
	 }
	 
	 
	 public boolean didAgentFinish(){
		 return agentFinished;
	 }
	 
	 public boolean didWumpusDie(){
		 return wumpusDead;
	 }
	 
	 public boolean hasGold(){
		 return hasGold;
	 }
	 
	 public boolean hasFired(){
		 return firedArrow;
	 }

	 public int getTries(){
		 return tries;
	 }
	 
	 
}
