package main;


import aima.logic.propositional.algorithms.KnowledgeBase;
import aima.logic.propositional.algorithms.PLFCEntails;
import aima.logic.propositional.parsing.PEParser;


public class KnowledgeConnector {

	
	private PEParser parser = new PEParser();
	
	private PLFCEntails plfce = new PLFCEntails();
	
	private KnowledgeBase kb = new KnowledgeBase();
	
	
	
	public KnowledgeConnector(){
		
	}
	
	public void createkb(){
		kb.tell("W");
		kb.tell("P");
		kb.tell("B => P");
		kb.tell("S => W");
		kb.tell("G");
		kb.tell("GL");
		kb.tell("GL => G");
	}
	
	
	public void Tellkb(String line){
		kb.tell(line);
	}
	
	
	
	
	
	
}
