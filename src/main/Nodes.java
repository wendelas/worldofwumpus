package main;

import board.GameBoard;

public class Nodes {

	static enum Action{
		LEFT, RIGHT, DOWN, UP, NO_ACTION;
	}
	
	private Nodes parent;

	
	
	private GameBoard state;
	private int PATHCOST;
	private Action action;
	
	
	public Nodes(Nodes parent, GameBoard state,int pathCost, Action action){
		this.parent=parent;
		this.state=state;
		this.PATHCOST=pathCost;
		this.action=action;
	}
	
	
	
	public Nodes getParent() {
		return parent;
	}
	
	public GameBoard getState() {
		return state;
	}

	public int getPATHCOST() {
		return PATHCOST;
	}
	
	public Action getAction() {
		return action;
	}
	
}
