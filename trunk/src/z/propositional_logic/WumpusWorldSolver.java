package z.propositional_logic;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import z.WumpusWorld.WumpusGame.WumpusGame;


public class WumpusWorldSolver {

	/**
	 * 
	 * @author begergetl, binisha, chancea
	 * 
	 */
	private static enum Action 
	{
		MOVE_LEFT, MOVE_RIGHT, MOVE_DOWN, MOVE_UP, SHOOT_RIGHT, SHOOT_LEFT, SHOOT_DOWN, SHOOT_UP, GRAB_GOLD, CLIMB_OUT, NO_ACTION;
	}
	
	
	/**
	 * Creates a EightPuzzleNode to use in solving algorithms
	 * @param parrent - The parent node
	 * @param state - The current state of the game (an instance of the game)
	 * @param pathCost - The current pathcost of the node (how many nodes it took to get to this one)
	 * @param action - The action that was used to get to the node from the parent
	 * @return - The created EightPuzzleNode
	 */
	private WumpusWorldNode createNode(WumpusWorldNode parrent, WumpusGame state, int pathCost, Action action)
	{
		return new WumpusWorldNode(parrent,state,pathCost,action);
	}
	
	public static void CHICKEN_LITTLE_SEARCH(WumpusGame game)
	{
		while(true)
		{
			WumpusWorldKB kb = new WumpusWorldKB();
			List<WumpusWorldNode> visitedNodes = new LinkedList<WumpusWorldNode>();
			WumpusWorldSolver solver = new WumpusWorldSolver();
			
			WumpusWorldNode startingNode = solver.createNode(null, game, 0, Action.NO_ACTION);
			
			Random r = new Random();
			
			boolean goldFound = false;
			char[] statuses = game.getStatusAtLocation(game.getAgentLocation());
			try {
				Thread.sleep(250);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(containsGold(statuses))
			{
				game.grabGold();
				game.climb();
				goldFound = true;
			}
			for(int i = 0; i < statuses.length; i++)
			{
				if(statuses[i] == WumpusGame.STATUS_BREEZE)
				{
					game.climb();
					System.out.println("Ah oh Breeze! getting outa here!");
					goldFound = true;
					break;
				}
				else if(statuses[i] == WumpusGame.STATUS_STENCH)
				{
					int x = r.nextInt(2);
					switch(x)
					{
						case 0:
							game.fireUp();
							System.out.println("Fired up");
							break;
						case 1:
							game.fireRight();
							System.out.println("Fired right");
							break;
					}
					if(game.didWumpusDie())
					{
						System.out.println("Killed Wumpus!");
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						switch(x)
						{
							case 0:
								kb.setWumpusLocation(new Point(game.getAgentLocation().x, + game.getAgentLocation().y -1));
								break;
							case 1:
								kb.setWumpusLocation(new Point(game.getAgentLocation().x + 1, + game.getAgentLocation().y));
								break;
						}
						kb.setWumpusDead(true);
					}
					else
					{
						System.out.println("Wumpus survived =(");
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						switch(x)
						{
							case 0:
								kb.setWumpusLocation(new Point(game.getAgentLocation().x + 1, + game.getAgentLocation().y));
								break;
							case 1:
								kb.setWumpusLocation(new Point(game.getAgentLocation().x, + game.getAgentLocation().y - 1));
								break;
						}
					}
				}
				
			}
	
			kb.updateTile("NB", game.getStartLocation());
			kb.updateTile("NS", game.getStartLocation());
			
			WumpusWorldNode currentNode = startingNode;
			
			while(!goldFound)
			{			
				
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				visitedNodes.add(currentNode);
				Point currentPoint = game.getAgentLocation();
				
				WumpusWorldNode nextNode = null;
				if(kb.isSafe(new Point(currentPoint.x,currentPoint.y-1)) && !game.getDiscoveredTiles()[currentPoint.x][currentPoint.y-1])
				{
					//create the node where the agent moves up
					nextNode = solver.createNode(currentNode, game, currentNode.getPathCost() + 1, Action.MOVE_UP);
					game.moveUp();
				}
				else if(kb.isSafe(new Point(currentPoint.x+1,currentPoint.y)) && !game.getDiscoveredTiles()[currentPoint.x+1][currentPoint.y])
				{
					//create the node where the agent moves right
					nextNode = solver.createNode(currentNode, game, currentNode.getPathCost() + 1, Action.MOVE_RIGHT);
					game.moveRight();
				}
				else if(kb.isSafe(new Point(currentPoint.x,currentPoint.y+1)) && !game.getDiscoveredTiles()[currentPoint.x][currentPoint.y+1])
				{
					//create the node where the agent moves down
					nextNode = solver.createNode(currentNode, game, currentNode.getPathCost() + 1, Action.MOVE_DOWN);
					game.moveDown();
				}
				else if(kb.isSafe(new Point(currentPoint.x-1,currentPoint.y)) && !game.getDiscoveredTiles()[currentPoint.x-1][currentPoint.y])
				{
					//create the node where the agent moves left
					nextNode = solver.createNode(currentNode, game, currentNode.getPathCost() + 1, Action.MOVE_LEFT);
					game.moveLeft();
				}
				else
				{
					switch(currentNode.action)
					{
						case MOVE_LEFT:
							game.moveRight();
							break;
						case MOVE_UP:
							game.moveDown();
							break;
						case MOVE_DOWN:
							game.moveUp();
							break;
						case MOVE_RIGHT:
							game.moveLeft();
							break;
						case NO_ACTION:
							goldFound = true;
							break;
					}
					
					nextNode = currentNode.parent;
				}
				
				if(goldFound)
				{
					break;
				}
				currentPoint = nextNode.state.getAgentLocation();
				statuses = nextNode.state.getStatusAtLocation(currentPoint);
				if(statuses.length == 0)
				{
					kb.updateTile("NB", currentPoint);
					kb.updateTile("NS", currentPoint);
				}
				else
				{
					boolean breeze = false;
					boolean stench = false;
					if(containsGold(statuses))
					{
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						game.grabGold();
						System.out.println("Gold Found");
						goldFound = true;
					}
					for(int i = 0; i < statuses.length; i++)
					{
						
						if(statuses[i] == WumpusGame.STATUS_BREEZE)
						{
							breeze = true;
							kb.updateTile("B", currentPoint);
						}
						else if(statuses[i] == WumpusGame.STATUS_STENCH)
						{
							stench = true;
							if(kb.wumpusFound())
							{
								break;
							}
							kb.updateTile("S", currentPoint);
							kb.findWumpus();
							if(kb.wumpusFound())
							{
								System.out.println("WumpusFound");
								try {
									Thread.sleep(250);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Point wumpusLocation = kb.getWumpusLocation();
								
								if(wumpusLocation.x > currentPoint.x)
								{
									game.fireRight();
								}
								else if(wumpusLocation.y > currentPoint.y)
								{
									game.fireDown();
								}
								else if(wumpusLocation.x < currentPoint.x)
								{
									game.fireLeft();
								}
								else if(wumpusLocation.y < currentPoint.y)
								{
									game.fireUp();
								}
								kb.setWumpusDead(true);
							}
						}
					}
					if(breeze && !stench)
					{
						kb.updateTile("B", currentPoint);
						kb.updateTile("NS", currentPoint);
					}
					else if(!breeze && stench)
					{
						kb.updateTile("NB", currentPoint);
						kb.updateTile("S", currentPoint);
					}
					else if(stench && breeze)
					{
						kb.updateTile("B", currentPoint);
						kb.updateTile("S", currentPoint);
					}
				}
				
				currentNode = nextNode;
				
			}
			
			while(currentNode.parent != null)
			{
				switch(currentNode.action)
				{
					case MOVE_LEFT:
						game.moveRight();
						break;
					case MOVE_UP:
						game.moveDown();
						break;
					case MOVE_DOWN:
						game.moveUp();
						break;
					case MOVE_RIGHT:
						game.moveLeft();
						break;
					case NO_ACTION:
						JOptionPane.showMessageDialog(null, "boom");
						game.climb();
						break;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				currentNode = currentNode.parent;
			}
			game.climb();
			
		}
	}
	
	private static boolean containsGold(char[] statuses)
	{
		boolean retval = false;
		for(int i = 0; i < statuses.length; i++)
		{
			if(statuses[i] == WumpusGame.STATUS_GOLD)
			{
				retval = true;
				break;
			}
		}
		return retval;
	}
	
	public class WumpusWorldNode
	{
		
		private WumpusWorldNode parent;
		private WumpusGame state;
		private int pathCost;
		private Action action;
		
		public WumpusWorldNode( WumpusWorldNode parent, WumpusGame state, int pathCost, Action action)
		{
			this.parent = parent;
			this.state = state;
			this.pathCost = pathCost;
			this.action = action;
		}
		
		public WumpusWorldNode getParrent() 
		{
			return parent;
		}
		
		public WumpusGame getState() 
		{
			return state;
		}
		
		public int getPathCost()
		{
			return pathCost;
		}
		
		public Action getAction()
		{
			return action;
		}
	}
}
