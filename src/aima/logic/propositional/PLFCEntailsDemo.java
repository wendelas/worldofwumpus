package aima.logic.propositional;

import aima.logic.propositional.algorithms.KnowledgeBase;
import aima.logic.propositional.algorithms.PLFCEntails;

/**
 * @author Jay Urbain credits: Ravi Mohan
 */
public class PLFCEntailsDemo {
//	private static PEParser parser = new PEParser();

	private static PLFCEntails plfce = new PLFCEntails();

	public static void main(String[] args) {

		System.out.println("\nPLFCEntailsDemo\n");
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("((L AND M) => P)");

		kb.tell("((A AND B) => L)");

		kb.tell("((A AND B) => M)");

		kb.tell("(A)");

		kb.tell("(B)");

		 

		//displayPLFCEntailment(kb, "L");

		//displayPLFCEntailment(kb, "M");

		displayPLFCEntailment(kb, "P");
		
		
		
		kb.tell(" (P => Q)");
		kb.tell("((L AND M) => P)");
		kb.tell("((B AND L) => M)");
		kb.tell("( (A AND P) => L)");
		kb.tell("((A AND B) => L)");
		kb.tell("(A)");
		kb.tell("(B)");

		System.out.println("Example from  page 220 of AIMA 2nd Edition");
		System.out.println("KnowledgeBsse consists of sentences");
		System.out.println("(P => Q)");
		System.out.println("((L AND M) => P)");
		System.out.println("((B AND L) => M)");
		System.out.println("( (A AND P) => L)");
		System.out.println("((A AND B) => L)");
		System.out.println("(A)");
		System.out.println("(B)");
		
		kb.tell("(Q)");
		displayPLFCEntailment(kb, "Q");
		
		System.out.print("Wumpus Example: ");
		System.out.println("B12 AND B23 AND B32 AND B21 <=> P22");
		//initial KM - a rule from the Wumpus World!
		kb.tell("((((B12 AND B23) AND B32) AND B21) => P22)");
		//percepts acquired as we explore the Wumpus World
		kb.tell("(B12)");
		kb.tell("(B23)");
		kb.tell("(B32)");
		//kb.tell("(B21)");
		
		System.out.println("((((B12 AND B23) AND B32) AND B21) => P22)");
		System.out.println("(B12)");
		System.out.println("(B23)");
		System.out.println("(B32)");
		System.out.println("(B21)");
		
		displayPLFCEntailment(kb, "P22");
	}

	private static void displayPLFCEntailment(KnowledgeBase kb, String q) {
		System.out.println("Running PLFCEntailment on knowledge base  "
				+ " with query " + q + " gives " + plfce.plfcEntails(kb, q));
	}
}
