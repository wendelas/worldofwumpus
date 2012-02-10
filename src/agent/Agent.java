package agent;

import board.GameBoard;


public abstract class Agent {

	static enum direction{ goUp, goDown, goLeft, goRight }

	public abstract int search(GameBoard board);	
	
	
}
