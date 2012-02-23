package aima.logic.propositional.algorithms;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import aima.logic.propositional.parsing.ast.BinarySentence;
import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.parsing.ast.Symbol;
import aima.logic.propositional.visitors.SymbolCollector;
import aima.util.Converter;

/**
 * @author Ravi Mohan
 * 
 */

public class PLFCEntails {

	private Hashtable<HornClause, Integer> count;

	private Hashtable<Symbol, Boolean> inferred;

	private Stack<Symbol> agenda;

	public PLFCEntails() {
		count = new Hashtable<HornClause, Integer>();
		inferred = new Hashtable<Symbol, Boolean>();
		agenda = new Stack<Symbol>();
	}

	public boolean plfcEntails(KnowledgeBase kb, String s) {
		return plfcEntails(kb, new Symbol(s));
	}

	public boolean plfcEntails(KnowledgeBase kb, Symbol q) {
		
//		System.out.println(q);
//		System.out.println(agenda);
//		System.out.println(count);
//		System.out.println(inferred);
		List<HornClause> hornClauses = asHornClauses(kb.getSentences());
//		System.out.println(hornClauses);
		while (agenda.size() != 0) {
//			System.out.println(agenda);
//			System.out.println(count);
			Symbol p = agenda.pop();
//			System.out.println(p);
			while (!inferred(p)) {
				inferred.put(p, Boolean.TRUE);
//				System.out.println(inferred);

				for (int i = 0; i < hornClauses.size(); i++) {
					HornClause hornClause = hornClauses.get(i);
//					System.out.println(hornClause);
					// Handle facts in the KB - Jay Urbain
					if (countisZero(hornClause) && hornClause.head().equals(q)) {
						return true;
					}
					if (hornClause.premisesContainsSymbol(p)) {
						decrementCount(hornClause);
//						System.out.println(count);
						if (countisZero(hornClause)) {
							if (hornClause.head().equals(q)) {
								return true;
							} else {
								agenda.push(hornClause.head());
							}
						}
					}
				}
			}
		}
		return false;
	}

	private List<HornClause> asHornClauses(List<?> sentences) {
		List<HornClause> hornClauses = new ArrayList<HornClause>();
		for (int i = 0; i < sentences.size(); i++) {
			Sentence sentence = (Sentence) sentences.get(i);
			HornClause clause = new HornClause(sentence);
			hornClauses.add(clause);
		}
		return hornClauses;
	}

	private boolean countisZero(HornClause hornClause) {

		return (count.get(hornClause)).intValue() == 0;
	}

	private void decrementCount(HornClause hornClause) {
		int value = (count.get(hornClause)).intValue();
		count.put(hornClause, new Integer(value - 1));

	}

	private boolean inferred(Symbol p) {
		Object value = inferred.get(p);
		return ((value == null) || value.equals(Boolean.TRUE));
	}
	
	@SuppressWarnings("unused")
	private boolean isTrue(Symbol p) {
		Object value = inferred.get(p);
		return ((value != null) && value.equals(Boolean.TRUE));
	}

	public class HornClause {
		List<Symbol> premiseSymbols;

		Symbol head;

		public HornClause(Sentence sentence) {
			if (sentence instanceof Symbol) {
				head = (Symbol) sentence;
				agenda.push(head);
				premiseSymbols = new ArrayList<Symbol>();
				count.put(this, new Integer(0));
				inferred.put(head, Boolean.FALSE);
			} else if (!isImpliedSentence(sentence)) {
				throw new RuntimeException("Sentence " + sentence
						+ " is not a horn clause");

			} else {
				BinarySentence bs = (BinarySentence) sentence;
				head = (Symbol) bs.getSecond();
				inferred.put(head, Boolean.FALSE);
				Set<Symbol> symbolsInPremise = new SymbolCollector()
						.getSymbolsIn(bs.getFirst());
				Iterator<Symbol> iter = symbolsInPremise.iterator();
				while (iter.hasNext()) {
					inferred.put(iter.next(), Boolean.FALSE);
				}
				premiseSymbols = new Converter<Symbol>()
						.setToList(symbolsInPremise);
				count.put(this, new Integer(premiseSymbols.size()));
			}

		}

		private boolean isImpliedSentence(Sentence sentence) {
			return ((sentence instanceof BinarySentence) && ((BinarySentence) sentence)
					.getOperator().equals("=>"));
		}

		public Symbol head() {

			return head;
		}

		public boolean premisesContainsSymbol(Symbol q) {
			return premiseSymbols.contains(q);
		}

		public List<Symbol> getPremiseSymbols() {
			return premiseSymbols;
		}

		@Override
		public boolean equals(Object o) {

			if (this == o) {
				return true;
			}
			if ((o == null) || (this.getClass() != o.getClass())) {
				return false;
			}
			HornClause ohc = (HornClause) o;
			if (premiseSymbols.size() != ohc.premiseSymbols.size()) {
				return false;
			}
//			boolean result = true;
			for (Symbol s : premiseSymbols) {
				if (!ohc.premiseSymbols.contains(s)) {
					return false;
				}
			}

			return true;

		}

		@Override
		public int hashCode() {
			int result = 17;
			// for (Symbol s : premiseSymbols) { Jay Urbain 1/29/2009
			for (Symbol s : premiseSymbols) {
				result = 37 * result + s.hashCode();
			}
			// Jay Urbain 1/29/2009
			// head not necessarily unique, could use same antecedant 
			// for multiple consequents
			result = 37 * result + head.hashCode(); 
			return result;
		}

		@Override
		public String toString() {
			return premiseSymbols.toString() + " => " + head;
		}
	}
}