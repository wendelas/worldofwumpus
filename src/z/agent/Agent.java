package z.agent;

import z.main.Gameboard;

public abstract class Agent {

	static enum direction{ goUp, goDown, goLeft, goRight }
	
	public abstract int search(Gameboard board);
	
	
}
