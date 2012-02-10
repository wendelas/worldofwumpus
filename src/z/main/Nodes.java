package z.main;

import java.util.*;

public class Nodes {

	static enum Action{
		LEFT, RIGHT, DOWN, UP, NO_ACTION;
	}
	
	private Nodes parent;

	
	
	private Gameboard state;
	private int PATHCOST;
	private Action action;
	
	
	public Nodes(Nodes parent, Gameboard state,int pathCost, Action action){
		this.parent=parent;
		this.state=state;
		this.PATHCOST=pathCost;
		this.action=action;
	}
	
	
	
	public Nodes getParent() {
		return parent;
	}
	
	public Gameboard getState() {
		return state;
	}

	public int getPATHCOST() {
		return PATHCOST;
	}
	
	public Action getAction() {
		return action;
	}
	
}
