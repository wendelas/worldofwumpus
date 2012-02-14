/*****************************************************************************
 * FILE: ChickenLittle.java
 * DATE: 02/08/12
 * AUTHOR: 	Karl Schmidbauer <schmidbauerk@msoe.edu>
 * 			Ben Ebert <ebertb@msoe.edu>
 * 
 * PURPOSE: Cautious board searching agent. 
 * 			Will avoid taking risks when possible.
 * 
 ****************************************************************************/
package agent;


import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import z.WumpusWorld.WumpusGame.WumpusGame;
import z.propositional_logic.WumpusWorldKB;
import board.GameBoard;
import exceptions.IllegalMove;

public class ChickenLittle extends Agent {

	
	public ChickenLittle(GameBoard board) {
		super(board);
	}

	@Override
	public direction search() {
		while (true) {
			WumpusWorldKB kb = new WumpusWorldKB();
			List<MemoryNode> visitedNodes = new LinkedList<MemoryNode>();

			MemoryNode startingNode =  new MemoryNode(null, board, 0, null);

			Random r = new Random();

			boolean goldFound = false;
			
			String statuses = board.getStatusAtLocation(board.getAgentLocation());
			try {
				Thread.sleep(250);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			if (statuses.contains("A")) {
				board.grabGold();
				board.climb("Gold");
				goldFound = true;
			}
			if(statuses.contains("B") && !statuses.contains("NB")) board.climb("Brease");

			kb.updateTile("NB", new Point(0,0));
			kb.updateTile("NS", new Point(0,0));

			MemoryNode currentNode = startingNode;

			while (!goldFound) {

				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				visitedNodes.add(currentNode);
				Point currentPoint = board.getAgentLocation();

				//TODO Rework
				MemoryNode nextNode = null;
				if (kb.isSafe(new Point(currentPoint.x, currentPoint.y - 1))
						&& memory[currentPoint.x][currentPoint.y - 1] != null) {
					// create the node where the agent moves up
					nextNode = new MemoryNode(currentNode, board, currentNode.getPathCost() + 1, direction.goUp);
					try {
						move(direction.goUp);
					} catch (IllegalMove e) {
						e.printStackTrace();
					}
				} else if (kb.isSafe(new Point(currentPoint.x + 1, currentPoint.y))
						&& memory[currentPoint.x + 1][currentPoint.y]!=null) {
					// create the node where the agent moves right
					nextNode = new MemoryNode(currentNode, board, currentNode.getPathCost() + 1, direction.goRight);
					try {
						move(direction.goRight);
					} catch (IllegalMove e) {
						e.printStackTrace();
					}
				} else if (kb.isSafe(new Point(currentPoint.x,
						currentPoint.y + 1))
						&& memory[currentPoint.x][currentPoint.y + 1]!=null) {
					// create the node where the agent moves down
					nextNode = new MemoryNode(currentNode, board, currentNode.getPathCost() + 1, direction.goDown);
					try {
						move(direction.goDown);
					} catch (IllegalMove e) {
						e.printStackTrace();
					}
				} else if (kb.isSafe(new Point(currentPoint.x - 1,
						currentPoint.y))
						&& memory[currentPoint.x - 1][currentPoint.y] !=null) {
					// create the node where the agent moves left
					nextNode = new MemoryNode(currentNode, board, currentNode.getPathCost() + 1, direction.goLeft);
					try {
						move(direction.goLeft);
					} catch (IllegalMove e) {
						e.printStackTrace();
					}
				} else {
					try {
						move(currentNode.getDirection());
					} catch (IllegalMove e) {
						e.printStackTrace();
					}

					nextNode = currentNode.getParent();
				}

				if (goldFound) {
					break;
				}
				currentPoint = nextNode.getState().getAgentLocation();
				statuses = nextNode.getState().getStatusAtLocation(currentPoint);
				if (statuses.isEmpty()) {
					kb.updateTile("NB", currentPoint);
					kb.updateTile("NS", currentPoint);
				} else {
					boolean breeze = false;
					boolean stench = false;
					if (statuses.contains("A")) {
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						board.grabGold();
						System.out.println("Gold Found");
						goldFound = true;
					}
					
					if (statuses.contains("B") && !statuses.contains("NB")) {
						breeze = true;
						kb.updateTile("B", currentPoint);
					} 
					if (statuses.contains("S") && !statuses.contains("NS")) {
						stench = true;
						if (kb.wumpusFound()) {
							break;
						}
						kb.updateTile("S", currentPoint);
						kb.findWumpus();
						if (kb.wumpusFound()) {
							System.out.println("WumpusFound");
							try {
								Thread.sleep(250);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Point wumpusLocation = kb.getWumpusLocation();

							direction whereToShoot = null;
							if (wumpusLocation.x > currentPoint.x) {
								whereToShoot = direction.goRight;
							} else if (wumpusLocation.y > currentPoint.y) {
								whereToShoot = direction.goUp;
							} else if (wumpusLocation.x < currentPoint.x) {
								whereToShoot = direction.goLeft;
							} else if (wumpusLocation.y < currentPoint.y) {
								whereToShoot = direction.goDown;
							}
							try {
								shootArrow(whereToShoot);
							} catch (IllegalMove e) {
								e.printStackTrace();
							}
							kb.setWumpusDead(board.wumpusDead());
						}
					}
					
					if(breeze) kb.updateTile("B", currentPoint);
					else kb.updateTile("NB", currentPoint);
					
					if(stench) kb.updateTile("S", currentPoint);
					else kb.updateTile("S", currentPoint);
					
				}
				currentNode = nextNode;
			}

			while (currentNode.getParent() != null) {
				try {
					move(currentNode.getDirection());
				} catch (IllegalMove e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				currentNode = currentNode.getParent();
			}
			board.climb("Give Up");
		}
	}

	private boolean containsGold(char[] statuses) {
		boolean retval = false;
		for (int i = 0; i < statuses.length; i++) {
			if (statuses[i] == WumpusGame.STATUS_GOLD) {
				retval = true;
				break;
			}
		}
		return retval;
	}

}
