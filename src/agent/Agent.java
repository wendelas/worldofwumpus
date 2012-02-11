/*****************************************************************************
 * FILE: Agent.java
 * DATE: 02/08/12
 * AUTHOR: 	Karl Schmidbauer <schmidbauerk@msoe.edu>
 * 			Ben Ebert <ebertb@msoe.edu>
 * 
 * PURPOSE: Abstract class to provide a common framework for any 
 * 			extending agent type.
 * 
 ****************************************************************************/
package agent;

import java.awt.Point;

import exceptions.IllegalMove;

import board.GameBoard;

/**
 * Agent to traverse the game board
 * 
 * @author Karl Schmidbauer <schmidbauerk@msoe.edu>
 *
 * NOTE: Assumes that (0, 0) is in the lower left corner
 */
public abstract class Agent {

	/**
	 * Stores the current position of the Agent
	 */
	private Point currentPosition;
	
	/**
	 * The game board that the agent is operating on
	 */
	private GameBoard board;
	
	/**
	 * Directions that the agent can go
	 * 
	 * @author Karl Schmidbauer <schmidbauerk@msoe.edu>
	 * 
	 * goUp - Y+1
	 * goDown - Y-1
	 * goLeft - X-1
	 * goRight - X+1
	 */
	public static enum direction{	goUp, 		//Y+1
									goDown, 	//Y-1
									goLeft, 	//X-1
									goRight }	//X+1

	/**
	 * Creates a new agent and passes in a new game board for the agent to operate in.
	 * @param board - the game board the agent will run through
	 */
	public Agent(GameBoard board){
		currentPosition = new Point(0,0);
		this.board = board;
	}
	
	/**
	 * Abstract search method to be implemented by the concrete agents
	 * @return
	 */
	public abstract direction search();	
	
	/**
	 * Attempts to move the agent
	 * @param path - direction for the agent to move
	 * @throws IllegalMove - IllegalMove exception to state that the agent cannot move in the direction specified
	 */
	public void move(direction path) throws IllegalMove{
		switch(path){
			case goUp:
				currentPosition.y++;
				if(currentPosition.y >= board.getBoardSize().height){
					currentPosition.y--;
					throw new IllegalMove("Cannot go up.");
				}
				break;
			case goDown:
				currentPosition.y--;
				if(currentPosition.y < 0){
					currentPosition.y++;
					throw new IllegalMove("Cannot go down.");
				}
				break;
			case goRight:
				currentPosition.x++;
				if(currentPosition.x >= board.getBoardSize().width){
					currentPosition.x--;
					throw new IllegalMove("Cannot go right.");
				}
				break;
			case goLeft:
				currentPosition.x--;
				if(currentPosition.x < 0){
					currentPosition.x++;
					throw new IllegalMove("Cannot go left.");
				}
				break;
			default:
				break;
		}
	}
	
	/**
	 * Gets the current position of the agent
	 * @return - the agents current position as a Point
	 */
	public Point getPosition(){
		return currentPosition;
	}
}
