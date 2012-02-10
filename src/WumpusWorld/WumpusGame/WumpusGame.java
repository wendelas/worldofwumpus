package WumpusWorld.WumpusGame;

import java.awt.Point;
import java.util.Observable;
import java.util.Random;

public class WumpusGame extends Observable{

	private int width;
	private int height;
	private int gold;
	private int wins;
	private int wumpusKills;
	private int deaths;
	private int numMoves;
	private int tries;
	
	private Point wumpusLocation;
	private Point goldLocation;
	private Point agentLocation;
	private Point startingLocation;

	private boolean hasGold;
	private boolean wumpusDead;
	private boolean agentDied;
	private boolean agentFinished;
	private boolean hasFired;
	
	public static final char STATUS_WUMPUS = 'W';
	public static final char STATUS_GOLD = 'G';
	public static final char STATUS_BREEZE = 'B';
	public static final char STATUS_STENCH = 'S';
	public static final char STATUS_HOLE = 'H';
	
	private String[][] status;
	
	private boolean[][] discoveredTiles;
	
	private WumpusGame(){};
	
	public WumpusGame(int width, int height)
	{
		this.startingLocation = new Point(0, height - 1);
		this.agentDied = false;
		this.agentFinished = false;
		this.gold = 0;
		this.tries = 0;
		this.hasFired = false;
		this.numMoves = 0;
		this.height = height;
		this.width = width;
		this.hasGold = false;
		this.wumpusDead = false;
		this.agentLocation = new Point(startingLocation.x, startingLocation.y);
		this.status = new String[width][height];
		this.discoveredTiles = new boolean[width][height];
		populateGame();
	}
	
	private void resetGame()
	{
		super.setChanged();
		super.notifyObservers();
		tries++;
		this.agentDied = false;
		this.agentFinished = false;
		this.hasFired = false;
		this.hasGold = false;
		this.wumpusDead = false;
		this.agentLocation = new Point(startingLocation.x, startingLocation.y);
		this.status = new String[width][height];
		this.discoveredTiles = new boolean[width][height];
		populateGame();
	}
	
	private void populateGame()
	{
		Random r = new Random();
		for(int w = 0; w < width; w++)
		{
			for(int h = 0; h < height; h++)
			{
				status[w][h] = "";
			}
		}
		for(int w = 0; w < width; w++)
		{
			for(int h = 0; h < height; h++)
			{
				if(!(w == startingLocation.x && h == startingLocation.y))
				{
					int test = r.nextInt(5);
					if(test == 0)
					{
						status[w][h] = "" + STATUS_HOLE;
						if(w > 0 && !status[w-1][h].contains("" + STATUS_HOLE) && !status[w-1][h].contains("" + STATUS_BREEZE))
						{
							status[w-1][h] += STATUS_BREEZE;
						}
						if(h > 0 && !status[w][h-1].contains("" + STATUS_HOLE) && !status[w][h-1].contains("" + STATUS_BREEZE))
						{
							status[w][h-1] += STATUS_BREEZE;
						}
						if(w < width-1 && !status[w+1][h].contains("" + STATUS_HOLE) && !status[w+1][h].contains("" + STATUS_BREEZE))
						{
							status[w+1][h] += STATUS_BREEZE;
						}
						if(h < height-1 && !status[w][h+1].contains("" + STATUS_HOLE) && !status[w][h+1].contains("" + STATUS_BREEZE))
						{
							status[w][h+1] += STATUS_BREEZE;
						}
					}
				}
				
				discoveredTiles[w][h] = false;
			}
		}
		
		discoveredTiles[agentLocation.x][agentLocation.y] = true; 
		
		Point p = new Point(r.nextInt(width),r.nextInt(height));
		while(p.x == startingLocation.x && p.y == startingLocation.y)
		{
			p = new Point(r.nextInt(width),r.nextInt(height));
		}
		
		wumpusLocation = new Point(p.x,p.y);
		
		if(wumpusLocation.x > 0)
		{
			status[wumpusLocation.x-1][wumpusLocation.y] += STATUS_STENCH;
		}
		if(wumpusLocation.y > 0)
		{
			status[wumpusLocation.x][wumpusLocation.y-1] += STATUS_STENCH;
		}
		if(wumpusLocation.x < width-1)
		{
			status[wumpusLocation.x+1][wumpusLocation.y] += STATUS_STENCH;
		}
		if(wumpusLocation.y < height-1)
		{
			status[wumpusLocation.x][wumpusLocation.y+1] += STATUS_STENCH;
		}
		status[wumpusLocation.x][wumpusLocation.y] += "" + STATUS_WUMPUS + "" + STATUS_STENCH;;
		
		goldLocation = new Point(r.nextInt(width),r.nextInt(height));
		
		status[goldLocation.x][goldLocation.y] += STATUS_GOLD;
	}
	
	public boolean[][] getDiscoveredTiles()
	{
		return discoveredTiles;
	}
	
	public String[][] getStatus()
	{
		return status;
	}
	
	public int getGold()
	{
		return gold;
	}
	
	public int getWumpusKills()
	{
		return wumpusKills;
	}
	
	public int getDeaths()
	{
		return deaths;
	}
	
	public int getWins()
	{
		return wins;
	}
	
	public boolean grabGold()
	{
		boolean retval = false;
		if(hasGold)
		{
			retval = false;
		}
		else if(agentLocation.x == goldLocation.x && agentLocation.y == goldLocation.y)
		{
			status[goldLocation.x][goldLocation.y] = status[goldLocation.x][goldLocation.y].replace("" + STATUS_GOLD, "");
			hasGold = true;
			retval = true;
			this.setChanged();
			notifyObservers();
		}
		return retval;
	}
	
	public boolean climb()
	{
		boolean retval = false;
		if(agentLocation.x == startingLocation.x && agentLocation.y == startingLocation.y)
		{
			agentFinished = true;
			if(hasGold)
			{
				gold+=100;
				wins++;
			}
			resetGame();
			retval = true;
			System.out.println("Notifying");
			super.setChanged();
			notifyObservers();
			System.out.println("Notified");
		}
		return retval;
	}
	
	private void killWumpus()
	{
		status[wumpusLocation.x][wumpusLocation.y] = status[wumpusLocation.x][wumpusLocation.y].replace("" + STATUS_WUMPUS, "");
		gold+=100;
		wumpusDead = true;
		wumpusKills++;
	}
	
	public boolean fireUp()
	{
		boolean retval = false;
		if(wumpusDead)
		{
			retval = false;
		}
		else if(wumpusLocation.x == agentLocation.x && wumpusLocation.y < agentLocation.y)
		{
			killWumpus();
			retval = true;
		}
		this.hasFired = true;
		return retval;
	}
	
	public boolean fireDown()
	{
		boolean retval = false;
		if(wumpusDead)
		{
			retval = false;
		}
		else if(wumpusLocation.x == agentLocation.x && wumpusLocation.y > agentLocation.y)
		{
			killWumpus();
			retval = true;
		}
		this.hasFired = true;
		return retval;
	}
	
	public boolean fireLeft()
	{
		boolean retval = false;
		if(wumpusDead)
		{
			retval = false;
		}
		else if(wumpusLocation.y == agentLocation.y && wumpusLocation.x < agentLocation.x)
		{
			killWumpus();
			retval = true;
		}
		this.hasFired = true;
		return retval;
	}
	
	public boolean fireRight()
	{
		boolean retval = false;if(wumpusDead)
		{
			retval = false;
		}
		else if(wumpusLocation.y == agentLocation.y && wumpusLocation.x > agentLocation.x)
		{
			killWumpus();
			retval = true;
		}
		this.hasFired = true;
		return retval;
	}
	
	private void move()
	{
		discoveredTiles[agentLocation.x][agentLocation.y] = true;
		char[] agentStats = getStatusAtLocation(agentLocation);
		boolean dead = false;
		for(int i = 0; i < agentStats.length; i++)
		{
			if(agentStats[i] == STATUS_HOLE || agentStats[i] == STATUS_WUMPUS)
			{
				dead = true;
				break;
			}
		}
		if(dead)
		{
			this.agentDied = true;
			gold -= 1000;
			deaths++;
			resetGame();
		}
		else
		{
			gold--;
		}
		numMoves++;
		super.setChanged();
		super.notifyObservers();
	}
	
	public boolean moveUp()
	{
		if(agentLocation.y <= 0)
		{
			return false;
		}
		
		agentLocation.y--;
		
		move();
		
		return true;
	}
	
	public boolean moveDown()
	{
		if(agentLocation.y >= height - 1)
		{
			return false;
		}

		agentLocation.y++;
		
		move();
		
		return true;
	}
	
	public boolean moveLeft()
	{
		if(agentLocation.x <= 0)
		{
			return false;
		}
		
		agentLocation.x--;
		
		move();
		
		return true;
	}
	
	public boolean moveRight()
	{
		if(agentLocation.x >= width - 1)
		{
			return false;
		}
		
		agentLocation.x++;
		
		move();
		
		return true;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getNumMoves()
	{
		return numMoves;
	}
	
	public char[] getStatusAtLocation(Point p)
	{
		return status[p.x][p.y].toCharArray();
	}

	public Point getWumpusLocation()
	{
		return wumpusLocation;
	}
	
	public Point getGoldLocation()
	{
		return goldLocation;
	}
	
	public Point getAgentLocation()
	{
		return agentLocation;
	}
	
	public Point getStartLocation()
	{
		return startingLocation;
	}
	
	public boolean didAgentDie()
	{
		return agentDied;
	}
	
	public boolean didAgentFinish()
	{
		return agentFinished;
	}
	
	public boolean didWumpusDie()
	{
		return wumpusDead;
	}
	
	public boolean hasGold()
	{
		return hasGold;
	}
	
	public boolean hasFired()
	{
		return hasFired;
	}
	
	@Override
	public WumpusGame clone()
	{
		WumpusGame g = new WumpusGame();
		g.agentDied = this.agentDied;
		g.agentFinished = this.agentFinished;
		g.agentLocation = new Point(this.agentLocation.x,this.agentLocation.y);
		g.deaths = this.deaths;
		g.gold = this.gold;
		g.goldLocation = new Point(this.goldLocation.x,this.goldLocation.y);
		g.discoveredTiles = new boolean[this.discoveredTiles.length][this.discoveredTiles[0].length];
		for(int i = 0; i < discoveredTiles.length; i++)
		{
			for(int h = 0; h < discoveredTiles[i].length; h++)
			{
				g.discoveredTiles[i][h] = this.discoveredTiles[i][h];
			}
		}
		g.hasFired = this.hasFired;
		g.hasGold = this.hasGold;
		g.height = this.height;
		g.width = this.height;
		g.numMoves = g.numMoves;
		g.startingLocation = new Point(this.startingLocation.x,this.startingLocation.y);
		g.status = new String[this.status.length][this.status[0].length];
		for(int i = 0; i < status.length; i++)
		{
			for(int h = 0; h < status[i].length; h++)
			{
				g.status[i][h] = "" + this.status[i][h];
			}
		}
		g.wins = this.wins;
		g.wumpusDead = this.wumpusDead;
		g.wumpusKills = this.wumpusKills;
		g.wumpusLocation = new Point(this.wumpusLocation.x,this.wumpusLocation.y);
		
		return g;
	}	

	@Override
	public boolean equals(Object o) {
		WumpusGame game;
		try{
			game = (WumpusGame) o;
		}
		catch(Exception e)
		{
			return false;
		}
		boolean retval = true;
		for(int i = 0; i < status.length; i++)
		{
			for(int x = 0; x < status[i].length; x++)
			{
				if(status[i][x] != game.status[i][x] || this.discoveredTiles[i][x] != game.discoveredTiles[i][x])
				{
					retval = false;
					break;
				}
			}
		}
		if(!this.agentLocation.equals(game.agentLocation))
		{
			retval = false;
		}
		return retval;
	}

	public int getTries() {
		return tries;
	}
}
