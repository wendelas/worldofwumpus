/**
 * 
 */
package z.test;

import aima.logic.propositional.algorithms.KnowledgeBase;
import aima.logic.propositional.algorithms.PLFCEntails;

/**
 * @author schmidbauerk
 *
 */
public class testQuery {
	private static PLFCEntails plfce = new PLFCEntails();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KnowledgeBase kb = new KnowledgeBase();
		for(int x = 0; x<4; x++){
			for(int y = 0; y<4; y++){
				//TODO
				kb.tell("((PX"+x+"Y"+y+" AND WX"+x+"Y"+y+") => SafeX"+x+"Y"+y+")");
			}
		}
			
		kb.tell("(PX2Y2)");
		kb.tell("(WX3Y1)");
		String query = "SafeX3Y2";
		System.out.println( plfce.plfcEntails(kb, query) );
		
		
		
		
		
		
	}
}
