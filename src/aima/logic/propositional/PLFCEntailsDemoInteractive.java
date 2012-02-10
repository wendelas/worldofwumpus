package aima.logic.propositional;

import aima.logic.propositional.algorithms.KnowledgeBase;
import aima.logic.propositional.algorithms.PLFCEntails;
import aima.logic.propositional.parsing.PEParser;

import java.util.*;

/**
 * @author Jay Urbain credits: Ravi Mohan
 */
public class PLFCEntailsDemoInteractive {
	private static PEParser parser = new PEParser();

	private static PLFCEntails plfce = new PLFCEntails();

	public static void main(String[] args) {

		System.out.println("PLFCEntailsDemoInteractive");
		KnowledgeBase kb = new KnowledgeBase();
		
		Scanner scanner = new Scanner(System.in);
		do {
			String s = scanner.nextLine();
			if(s.length() == 0) {
				break;
			}
			if( s.contains("tell") ) {
				s = (s.replace("tell","")).trim();
				kb.tell(s);
				//System.out.println(s);
			}
			else if( s.contains("ask")) {
				s = (s.replace("ask","")).trim();
				System.out.println(kb);
				displayPLFCEntailment(kb, s);
			}
			else {
				System.out.println("Invalid command: use \"ask\" or \"tell\" followed by propositional statement");
			}
			
		} while (true);
		

	}

	private static void displayPLFCEntailment(KnowledgeBase kb, String q) {
		System.out.println("Running PLFCEntailment on knowledge base  "
				+ " with query " + q + " gives " + plfce.plfcEntails(kb, q));
	}
}
