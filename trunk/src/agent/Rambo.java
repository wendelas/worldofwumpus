/*****************************************************************************
 * FILE: Rambo.java
 * DATE: 02/08/12
 * AUTHOR: 	Karl Schmidbauer <schmidbauerk@msoe.edu>
 * 			Ben Ebert <ebertb@msoe.edu>
 * 
 * PURPOSE: Aggressive searcher willing to take risks.
 * 
 ****************************************************************************/
package agent;


import board.GameBoard;

import java.awt.Point;
import java.util.*;

import exceptions.IllegalMove;


public class Rambo extends Agent {

	Random rand = new Random();
	
	private int turn = 0;

	public Rambo(GameBoard board) {
		super(board);
	}

	@Override
	public void search() {

		boolean keepGoing = true;
		while(keepGoing){
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//Initial Starting Position
			List<MemoryNode> visitedNodes = new LinkedList<MemoryNode>();

			MemoryNode startingNode = new MemoryNode(null, board, 0, null);

			String status = board.getStatusAtLocation(currentPosition);
			
			direction temp = null;

			if(status.contains("A")){
				grabGold();
				climb("Gold");
				hasGold = true;
			}

			if(status.contains("S") && status.contains("B")  && !status.contains("NS") && !status.contains("NB")){
				int choice = rand.nextInt(2);
				if(choice==0){
					try{
						temp = direction.goUp;
						shootArrow(direction.goUp);
						move(direction.goUp);
						kb.updateTile("S", new Point(0,0));
						kb.updateTile("B", new Point(0,0));
					}catch(IllegalMove m){
						System.out.println("Attempting an Illegal Move");
					}
				}else{
					try{
						temp = direction.goRight;
						shootArrow(direction.goRight);
						move(direction.goRight);
						kb.updateTile("S", new Point(0,0));
						kb.updateTile("B", new Point(0,0));
					}catch(IllegalMove m){
						System.out.println("Attempting an Illegal Move");
					}
				}
			}else if(status.contains("S") && !status.contains("NS")){
				int choice = rand.nextInt(2);
				if(choice==0){
					try{
						temp = direction.goUp;
						shootArrow(direction.goUp);
						move(direction.goUp);
						kb.updateTile("S", new Point(0,0));
					}catch(IllegalMove m){
						System.out.println("Attempting an Illegal Move");
					}
				}else{
					try{
						temp = direction.goRight;
						shootArrow(direction.goRight);
						move(direction.goRight);
						kb.updateTile("S", new Point(0,0));
					}catch(IllegalMove m){
						System.out.println("Attempting an Illegal Move");
					}
				}
			}else if(status.contains("B") && !status.contains("NB")){
				int choice = rand.nextInt(2);
				if(choice==0){
					try{
						temp = direction.goUp;
						move(direction.goUp);
						kb.updateTile("B", new Point(0,0));
					}catch(IllegalMove m){
						System.out.println("Attempting an Illegal Move");
					}
				}else{
					try{
						temp = direction.goRight;
						move(direction.goRight);
						kb.updateTile("B", new Point(0,0));
					}catch(IllegalMove m){
						System.out.println("Attempting an Illegal Move");
					}
				}
			}else{
				int choice = rand.nextInt(2);
				kb.updateTile("NB", new Point(0,0));
				kb.updateTile("NS", new Point(0,0));
				if(choice==0){
					try {
						temp = direction.goUp;
						move(direction.goUp);
					} catch (IllegalMove e) {
						System.out.println("Attempting an Illegal Move");
					}
				}else{
					try {
						temp = direction.goRight;
						move(direction.goRight);
					} catch (IllegalMove e) {
						System.out.println("Attempting an Illegal Move");
					}
				}
				
			}
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////

			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//Any Concurrent Turns
			MemoryNode currentNode = new MemoryNode(startingNode,board,startingNode.getPathCost()+1,temp);
			turn ++;
			
			System.out.println("Finished first turn");

			while(!hasGold){

				visitedNodes.add(currentNode);
				direction choice = null;
				System.out.println("Turn: "+turn);
				System.out.println("Current Postion: "+currentPosition.x + ":"+currentPosition.y);
				
				if(kb.isSafe(new Point(currentPosition.x,currentPosition.y-1)) && discoveredTiles[currentPosition.x][currentPosition.y-1] != true ){
					choice = direction.goUp;
				}else if(kb.isSafe(new Point(currentPosition.x + 1, currentPosition.y)) && discoveredTiles[currentPosition.x + 1][currentPosition.y] != true){
					choice = direction.goRight;
				}else if(kb.isSafe(new Point(currentPosition.x, currentPosition.y + 1)) && discoveredTiles[currentPosition.x][currentPosition.y + 1] != true){
					choice = direction.goDown;
				}else if(kb.isSafe(new Point(currentPosition.x - 1, currentPosition.y)) && discoveredTiles[currentPosition.x - 1][currentPosition.y] != true){
					choice = direction.goLeft;
				}else{
					choice = currentNode.getDirection();
				}

				System.out.println("Choice :"+ choice);
				try{
					move(choice);
				//	memory[currentPosition.x][currentPosition.y] = currentNode;
					System.out.println("1");
				}catch(IllegalMove m){
					System.out.println("Attempting an Illegal Move at Position: ("+currentPosition.x+","+currentPosition.y+")");
				}


				MemoryNode nextNode = null;


				if(choice == null){
					nextNode = currentNode.getParent();
				}else{
					nextNode = new MemoryNode(currentNode, board, currentNode.getPathCost()+1, choice);
				}

				status = nextNode.getBoard().getStatusAtLocation(currentPosition);


				if(status.isEmpty()){
					kb.updateTile("NB", currentPosition);
					kb.updateTile("NS", currentPosition);
				}else{
					if(status.contains("A")){
						grabGold();
						System.out.println("Gold Found at: "+currentPosition);
						hasGold = true;
					}

					if(status.contains("B") && !status.contains("NB")){
						System.out.println("Found Breeze at: "+currentPosition);
						kb.updateTile("B", currentPosition);
					}
					if(status.contains("S") && !status.contains("NS")){
						if(kb.wumpusFound()){
							break;
						}
						kb.updateTile("S", currentPosition);
						kb.findWumpus();
						if(kb.foundWumpus()){
							System.out.println("Wumpus Found");
							Point wumpusLocation = kb.getWumpusLocation();

							direction whereToShoot = null;

							if(wumpusLocation.x > currentPosition.x){
								whereToShoot = direction.goRight;
							}else if(wumpusLocation.y > currentPosition.y){
								whereToShoot = direction.goUp;
							}else if(wumpusLocation.x < currentPosition.x){
								whereToShoot = direction.goLeft;
							}else if(wumpusLocation.y < currentPosition.y){
								whereToShoot = direction.goDown;
							}

							try{
								shootArrow(whereToShoot);
							}catch(IllegalMove m){
								System.out.println("Attempting an Illegal Move shooting");
							}
							kb.setWumpusDead(board.wumpusDead());
						}
					}
				}
				currentNode = nextNode;
				turn++;
			}

			while(currentNode.getParent() != null){
				try{
					move(currentNode.getDirection());
				}catch(IllegalMove m){
					System.out.println("Attempting an IllegalMove");
				}
				currentNode = currentNode.getParent();
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		}
	}

}
