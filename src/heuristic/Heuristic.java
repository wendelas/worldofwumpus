/*****************************************************************************
 * FILE: Heuristic.java
 * DATE: 02/08/12
 * AUTHOR: 	Karl Schmidbauer <schmidbauerk@msoe.edu>
 * 			Ben Ebert <ebertb@msoe.edu>
 * 
 * PURPOSE: Implementation of a heuristic strategy pattern to provide 
 * 			framework for the implementing heuristics to search for a 
 * 			next move for the AI.
 * 
 ****************************************************************************/
package heuristic;

import agent.Agent.direction;
import board.GameBoard;

/**
 * Framework to determine which tile to move to next
 * @author Karl Schmidbauer <schmidbauerk@msoe.edu>
 *
 */
public interface Heuristic {

	/**
	 * Determines the next move to make
	 * @param board - game board to search
	 * @return - direction to move
	 */
	public direction nextMove(GameBoard board);
}
