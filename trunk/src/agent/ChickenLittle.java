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
			char[] statuses = board.getStatusAtLocation(board.getAgentLocation());
			try {
				Thread.sleep(250);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			if (containsGold(statuses)) {
				board.grabGold();
				board.climb();
				goldFound = true;
			}
			for (int i = 0; i < statuses.length; i++) {
				if (statuses[i] == WumpusGame.STATUS_BREEZE) {
					board.climb();
					System.out.println("Ah oh Breeze! getting outa here!");
					goldFound = true;
					break;
				} else if (statuses[i] == WumpusGame.STATUS_STENCH) {
					int x = r.nextInt(2);
					switch (x) {
					case 0:
						try {
							shootArrow(direction.goUp);
							System.out.println("Fired up");
						} catch (IllegalMove e2) {
							e2.printStackTrace();
						}
						break;
					case 1:
						try {
							shootArrow(direction.goRight);
							System.out.println("Fired right");
						} catch (IllegalMove e) {
							e.printStackTrace();
						}
						break;
					}
					if (board.wumpusDead()) {
						System.out.println("Killed Wumpus!");
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						switch (x) {
						case 0:
							kb.setWumpusLocation(new Point(board
									.getAgentLocation().x, +board
									.getAgentLocation().y - 1));
							break;
						case 1:
							kb.setWumpusLocation(new Point(board
									.getAgentLocation().x + 1, +board
									.getAgentLocation().y));
							break;
						}
						kb.setWumpusDead(true);
					} else {
						System.out.println("Wumpus survived =(");
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						switch (x) {
						case 0:
							kb.setWumpusLocation(new Point(board
									.getAgentLocation().x + 1, +board
									.getAgentLocation().y));
							break;
						case 1:
							kb.setWumpusLocation(new Point(board
									.getAgentLocation().x, +board
									.getAgentLocation().y - 1));
							break;
						}
					}
				}
			}

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
				if (statuses.length == 0) {
					kb.updateTile("NB", currentPoint);
					kb.updateTile("NS", currentPoint);
				} else {
					boolean breeze = false;
					boolean stench = false;
					if (containsGold(statuses)) {
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						board.grabGold();
						System.out.println("Gold Found");
						goldFound = true;
					}
					for (int i = 0; i < statuses.length; i++) {

						if (statuses[i] == WumpusGame.STATUS_BREEZE) {
							breeze = true;
							kb.updateTile("B", currentPoint);
						} else if (statuses[i] == WumpusGame.STATUS_STENCH) {
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
								//TODO Rework
								if (wumpusLocation.x > currentPoint.x) {
									try {
										shootArrow(direction.goRight);
									} catch (IllegalMove e) {
										e.printStackTrace();
									}
								} else if (wumpusLocation.y > currentPoint.y) {
									try {
										shootArrow(direction.goDown);
									} catch (IllegalMove e) {
										e.printStackTrace();
									}
								} else if (wumpusLocation.x < currentPoint.x) {
									try {
										shootArrow(direction.goLeft);
									} catch (IllegalMove e) {
										e.printStackTrace();
									}
								} else if (wumpusLocation.y < currentPoint.y) {
									try {
										shootArrow(direction.goLeft);
									} catch (IllegalMove e) {
										e.printStackTrace();
									}
								}
								kb.setWumpusDead(true);
							}
						}
					}
					if (breeze && !stench) {
						kb.updateTile("B", currentPoint);
						kb.updateTile("NS", currentPoint);
					} else if (!breeze && stench) {
						kb.updateTile("NB", currentPoint);
						kb.updateTile("S", currentPoint);
					} else if (stench && breeze) {
						kb.updateTile("B", currentPoint);
						kb.updateTile("S", currentPoint);
					}
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
			board.climb();

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
