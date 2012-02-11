/*****************************************************************************
 * FILE: KnowledgeConnector.java
 * DATE: 02/08/12
 * AUTHOR: 	Karl Schmidbauer <schmidbauerk@msoe.edu>
 * 			Ben Ebert <ebertb@msoe.edu>
 * 
 * PURPOSE: Provides a connection between the AI and its knowledge base.
 * 
 ****************************************************************************/
package main;


import aima.logic.propositional.algorithms.KnowledgeBase;
import aima.logic.propositional.algorithms.PLFCEntails;
import aima.logic.propositional.parsing.PEParser;


/**
 * @author ebertb
 *
 */
public class KnowledgeConnector {

	
	/**
	 * 
	 */
	private PEParser parser;
	
	/**
	 * 
	 */
	private PLFCEntails plfce;
	
	/**
	 * 
	 */
	private KnowledgeBase kb;
	
	
	
	/**
	 * 
	 */
	public KnowledgeConnector(){
		kb = new KnowledgeBase();
		plfce = new PLFCEntails();
		parser = new PEParser();
	}
	
	/**
	 * 
	 */
	public void createkb(){
		kb.tell("W");
		kb.tell("P");
		kb.tell("B => P");
		kb.tell("S => W");
		kb.tell("G");
		kb.tell("GL");
		kb.tell("GL => G");
	}
	
	
	
	
	
	
	
}
