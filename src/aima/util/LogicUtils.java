/*
 * Created on May 3, 2003 by Ravi Mohan
 *  
 */
package aima.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import aima.logic.propositional.parsing.ast.BinarySentence;
import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.parsing.ast.Symbol;
import aima.logic.propositional.parsing.ast.SymbolComparator;
import aima.logic.propositional.parsing.ast.UnarySentence;

/**
 * @author Ravi Mohan
 * 
 */

public class LogicUtils {

	public static Sentence chainWith(String connector, List<Sentence> sentences) {
		if (sentences.size() == 0) {
			return null;
		} else if (sentences.size() == 1) {
			return sentences.get(0);
		} else {
			Sentence soFar = sentences.get(0);
			for (int i = 1; i < sentences.size(); i++) {
				Sentence next = sentences.get(i);
				soFar = new BinarySentence(connector, soFar, next);
			}
			return soFar;
		}
	}

	public static Sentence reorderCNFTransform(Set<Symbol> positiveSymbols,
			Set<Symbol> negativeSymbols) {
		List<Symbol> plusList = new Converter<Symbol>()
				.setToList(positiveSymbols);
		List<Symbol> minusList = new Converter<Symbol>()
				.setToList(negativeSymbols);
		Collections.sort(plusList, new SymbolComparator());
		Collections.sort(minusList, new SymbolComparator());

		List<Sentence> sentences = new ArrayList<Sentence>();
		for (int i = 0; i < positiveSymbols.size(); i++) {
			sentences.add(plusList.get(i));
		}
		for (int i = 0; i < negativeSymbols.size(); i++) {
			sentences.add(new UnarySentence(minusList.get(i)));
		}
		if (sentences.size() == 0) {
			return new Symbol("EMPTY_CLAUSE"); // == empty clause
		} else {
			return LogicUtils.chainWith("OR", sentences);
		}
	}


}