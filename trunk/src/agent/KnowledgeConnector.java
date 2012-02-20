/*****************************************************************************
 * FILE: KnowledgeConnector.java
 * DATE: 02/08/12
 * AUTHOR: 	Karl Schmidbauer <schmidbauerk@msoe.edu>
 * 			Ben Ebert <ebertb@msoe.edu>
 * 
 * PURPOSE: Provides a connection between the AI and its knowledge base.
 * 
 ****************************************************************************/
package agent;


import java.awt.Point;

import aima.logic.propositional.algorithms.KnowledgeBase;
import aima.logic.propositional.algorithms.PLFCEntails;


/**
 * @author ebertb
 *
 */
public class KnowledgeConnector {

	
	/**
	 * 
	 */
	//private static PEParser parser = new PEParser();
	
	/**
	 * 
	 */
	private PLFCEntails plfce;
	
	/**
	 * 
	 */
	private KnowledgeBase kb;
	
	
	private boolean foundwumpus;
	private boolean deadwumpus;
	private Point wumpusLocation;
	
	
	
	/**
	 * 
	 */
	public KnowledgeConnector(){
		
		foundwumpus = false;
		deadwumpus = false;
		
		kb = new KnowledgeBase();
		plfce = new PLFCEntails();
		
		
		
		
		
		kb.tell("((B00 AND (NB01)) => P10)");
		kb.tell("((B00 AND (NB10)) => P01)");
		
		kb.tell("((B03 AND (NB02)) => P13)");
		kb.tell("((B03 AND (NB13)) => P02)");
		
		kb.tell("((B30 AND (NB31)) => P20)");
		kb.tell("((B30 AND (NB20)) => P31)");
		
		kb.tell("((B33 AND (NB23)) => P32)");
		kb.tell("((B33 AND (NB32)) => P23)");
		
		
		
		kb.tell("(( ( B01 AND NB00) AND(NB02) ) => P11)");
		kb.tell("(( ( B02 AND NB01) AND(NB03) ) => P12)");
		kb.tell("(( ( B10 AND NB00) AND(NB20) ) => P11)");
		kb.tell("(( ( B20 AND NB10) AND(NB30) ) => P21)");
		kb.tell("(( ( B23 AND NB13) AND(NB33) ) => P22)");
		kb.tell("(( ( B31 AND NB30) AND(NB32) ) => P21)");
		kb.tell("(( ( B32 AND NB31) AND(NB33) ) => P22)");
		kb.tell("(( ( B13 AND NB03) AND(NB23) ) => P12)");
		
		kb.tell("(( (( B01 AND NB02) AND ( NB12 OR NB02) ) ) => P00)");
		kb.tell("(( (( B01 AND NB00) AND ( NB10 OR NB11) ) ) => P02)");
		kb.tell("(( (( B02 AND NB03) AND ( NB13 OR NB03) ) ) => P01)");
		kb.tell("(( (( B02 AND NB01) AND ( NB11 OR NB12) ) ) => P03)");
		kb.tell("(( (( B10 AND NB00) AND ( NB01 OR NB11) ) ) => P20)");
		kb.tell("(( (( B10 AND NB20) AND ( NB21 OR NB11) ) ) => P00)");
		kb.tell("(( (( B20 AND NB10) AND ( NB11 OR NB21) ) ) => P30)");
		kb.tell("(( (( B20 AND NB30) AND ( NB31 OR NB21) ) ) => P10)");
		
		kb.tell("(( (( B13 AND NB03) AND (NB02 OR NB12) ) AND (NB03) ) => P23)");
		kb.tell("(( (( B13 AND NB23) AND (NB22 OR NB12) ) AND (NB23) ) => P03)");
		kb.tell("(( (( B23 AND NB13) AND (NB12 OR NB22) ) AND (NB13) ) => P33)");
		kb.tell("(( (( B23 AND NB33) AND (NB32 OR NB22) ) AND (NB33) ) => P13)");
		kb.tell("(( (( B31 AND NB32) AND (NB22 OR NB21) ) AND (NB32) ) => P30)");
		kb.tell("(( (( B31 AND NB30) AND (NB20 OR NB21) ) AND (NB30) ) => P32)");
		kb.tell("(( (( B32 AND NB33) AND (NB23 OR NB22) ) AND (NB33) ) => P31)");
		kb.tell("(( (( B32 AND NB31) AND (NB21 OR NB22) ) AND (NB31) ) => P33)");
		
		kb.tell("(( (( B12 AND NB02) AND (NB01 OR NB11) ) AND (NB03 OR NB13) ) => P22)");
		kb.tell("(( (( B21 AND NB11) AND (NB10 OR NB20) ) AND (NB12 OR NB22) ) => P31)");
		kb.tell("(( (( B21 AND NB22) AND (NB12 OR NB11) ) AND (NB32 OR NB22) ) => P20)");
		kb.tell("(( (( B21 AND NB31) AND (NB30 OR NB20) ) AND (NB32 OR NB22) ) => P11)");
		kb.tell("(( (( B21 AND NB20) AND (NB10 OR NB11) ) AND (NB30 OR NB31) ) => P22)");
		kb.tell("(( (( B22 AND NB12) AND (NB11 OR NB21) ) AND (NB13 OR NB23) ) => P32)");
		kb.tell("(( (( B22 AND NB23) AND (NB13 OR NB12) ) AND (NB33 OR NB23) ) => P21)");
		kb.tell("(( (( B22 AND NB32) AND (NB31 OR NB21) ) AND (NB33 OR NB23) ) => P12)");
		kb.tell("(( (( B22 AND NB21) AND (NB11 OR NB12) ) AND (NB31 OR NB32) ) => P23)");
		kb.tell("(( (( B11 AND NB01) AND (NB00 OR NB10) ) AND (NB02 OR NB12) ) => P21)");
		kb.tell("(( (( B11 AND NB12) AND (NB02 OR NB01) ) AND (NB22 OR NB12) ) => P10)");
		kb.tell("(( (( B11 AND NB21) AND (NB20 OR NB10) ) AND (NB22 OR NB12) ) => P01)");
		kb.tell("(( (( B11 AND NB10) AND (NB00 OR NB01) ) AND (NB20 OR NB21) ) => P12)");
		kb.tell("(( (( B12 AND NB13) AND (NB03 OR NB02) ) AND (NB23 OR NB13) ) => P11)");
		kb.tell("(( (( B12 AND NB22) AND (NB21 OR NB11) ) AND (NB23 OR NB13) ) => P02)");
		kb.tell("(( (( B12 AND NB11) AND (NB01 OR NB02) ) AND (NB21 OR NB22) ) => P13)");
		
		kb.tell("((NB00)=> NP10)");
		kb.tell("((NB00)=> NP01)");
		kb.tell("((NB01)=> NP11)");
		kb.tell("((NB01)=> NP02)");
		kb.tell("((NB01)=> NP00)");
		kb.tell("((NB02)=> NP12)");
		kb.tell("((NB02)=> NP03)");
		kb.tell("((NB02)=> NP01)");
		kb.tell("((NB03)=> NP13)");
		kb.tell("((NB03)=> NP02)");
		kb.tell("((NB10)=> NP20)");
		kb.tell("((NB10)=> NP11)");
		kb.tell("((NB10)=> NP00)");
		kb.tell("((NB11)=> NP21)");
		kb.tell("((NB11)=> NP12)");
		kb.tell("((NB11)=> NP01)");
		kb.tell("((NB11)=> NP10)");
		kb.tell("((NB13)=> NP23)");
		kb.tell("((NB13)=> NP03)");
		kb.tell("((NB13)=> NP12)");
		kb.tell("((NB12)=> NP22)");
		kb.tell("((NB12)=> NP13)");
		kb.tell("((NB12)=> NP02)");
		kb.tell("((NB12)=> NP11)");
		kb.tell("((NB20)=> NP30)");
		kb.tell("((NB20)=> NP21)");
		kb.tell("((NB20)=> NP10)");
		kb.tell("((NB21)=> NP31)");
		kb.tell("((NB21)=> NP22)");
		kb.tell("((NB21)=> NP11)");
		kb.tell("((NB21)=> NP20)");
		kb.tell("((NB22)=> NP32)");
		kb.tell("((NB22)=> NP23)");
		kb.tell("((NB22)=> NP12)");
		kb.tell("((NB22)=> NP21)");
		kb.tell("((NB23)=> NP33)");
		kb.tell("((NB23)=> NP13)");
		kb.tell("((NB23)=> NP22)");
		kb.tell("((NB30)=> NP31)");
		kb.tell("((NB30)=> NP20)");
		kb.tell("((NB31)=> NP32)");
		kb.tell("((NB31)=> NP21)");
		kb.tell("((NB31)=> NP30)");
		kb.tell("((NB32)=> NP33)");
		kb.tell("((NB32)=> NP22)");
		kb.tell("((NB32)=> NP31)");
		kb.tell("((NB33)=> NP23)");
		kb.tell("((NB33)=> NP32)");
		
		kb.tell("(((S01 AND S10) AND NS00) => W11)");
		kb.tell("(((S02 AND S11) AND NS01) => W12)");
		kb.tell("(((S03 AND S12) AND NS02) => W13)");
		kb.tell("(((S11 AND S20) AND NS10) => W21)");
		kb.tell("(((S12 AND S21) AND NS11) => W22)");
		kb.tell("(((S13 AND S22) AND NS12) => W23)");
		kb.tell("(((S21 AND S30) AND NS20) => W31)");
		kb.tell("(((S22 AND S31) AND NS21) => W32)");
		kb.tell("(((S23 AND S32) AND NS22) => W33)");
		kb.tell("(((S01 AND S10) AND NS11) => W10)");
		kb.tell("(((S02 AND S11) AND NS12) => W11)");
		kb.tell("(((S03 AND S12) AND NS13) => W12)");
		kb.tell("(((S11 AND S20) AND NS21) => W20)");
		kb.tell("(((S12 AND S21) AND NS22) => W21)");
		kb.tell("(((S13 AND S22) AND NS23) => W22)");
		kb.tell("(((S21 AND S30) AND NS31) => W30)");
		kb.tell("(((S22 AND S31) AND NS32) => W31)");
		kb.tell("(((S23 AND S32) AND NS33) => W32)");
		kb.tell("(((S00 AND S11) AND NS01) => W10)");
		kb.tell("(((S01 AND S12) AND NS02) => W11)");
		kb.tell("(((S02 AND S13) AND NS03) => W12)");
		kb.tell("(((S03 AND S14) AND NS04) => W13)");
		kb.tell("(((S10 AND S21) AND NS11) => W20)");
		kb.tell("(((S11 AND S22) AND NS12) => W21)");
		kb.tell("(((S12 AND S23) AND NS13) => W22)");
		kb.tell("(((S13 AND S24) AND NS14) => W23)");
		kb.tell("(((S20 AND S31) AND NS21) => W30)");
		kb.tell("(((S21 AND S32) AND NS22) => W31)");
		kb.tell("(((S22 AND S33) AND NS23) => W32)");
		kb.tell("(((S23 AND S34) AND NS24) => W33)");
		kb.tell("(((S00 AND S11) AND NS10) => W01)");
		kb.tell("(((S01 AND S12) AND NS11) => W02)");
		kb.tell("(((S02 AND S13) AND NS12) => W03)");
		kb.tell("(((S10 AND S21) AND NS20) => W11)");
		kb.tell("(((S11 AND S22) AND NS21) => W12)");
		kb.tell("(((S12 AND S23) AND NS22) => W13)");
		kb.tell("(((S20 AND S31) AND NS30) => W21)");
		kb.tell("(((S21 AND S32) AND NS31) => W22)");
		kb.tell("(((S22 AND S33) AND NS32) => W23)");
		kb.tell("(((S30 AND S41) AND NS40) => W31)");
		kb.tell("(((S31 AND S42) AND NS41) => W32)");
		kb.tell("(((S32 AND S43) AND NS42) => W33)");
		
		kb.tell("((S00 AND (NS01)) => W10)");
		kb.tell("((S00 AND (NS10)) => W01)");
		kb.tell("((S03 AND (NS02)) => W13)");
		kb.tell("((S03 AND (NS13)) => W02)");
		kb.tell("((S30 AND (NS31)) => W20)");
		kb.tell("((S30 AND (NS20)) => W31)");
		kb.tell("((S33 AND (NS23)) => W32)");
		kb.tell("((S33 AND (NS32)) => W23)");
		
		kb.tell("(( ( S01 AND NS00) AND(NS02) ) => W11)");
		kb.tell("(( ( S02 AND NS01) AND(NS03) ) => W12)");
		kb.tell("(( ( S10 AND NS00) AND(NS20) ) => W11)");
		kb.tell("(( ( S20 AND NS10) AND(NS30) ) => W21)");
		kb.tell("(( ( S23 AND NS13) AND(NS33) ) => W22)");
		kb.tell("(( ( S31 AND NS30) AND(NS32) ) => W21)");
		kb.tell("(( ( S32 AND NS31) AND(NS33) ) => W22)");
		kb.tell("(( ( S13 AND NS03) AND(NS23) ) => W12)");
		
		kb.tell("(( (( S01 AND NS02) AND ( NS12 OR NS02) ) ) => W00)");
		kb.tell("(( (( S01 AND NS00) AND ( NS10 OR NS11) ) ) => W02)");
		kb.tell("(( (( S02 AND NS03) AND ( NS13 OR NS03) ) ) => W01)");
		kb.tell("(( (( S02 AND NS01) AND ( NS11 OR NS12) ) ) => W03)");
		kb.tell("(( (( S10 AND NS00) AND ( NS01 OR NS11) ) ) => W20)");
		kb.tell("(( (( S10 AND NS20) AND ( NS21 OR NS11) ) ) => W00)");
		kb.tell("(( (( S20 AND NS10) AND ( NS11 OR NS21) ) ) => W30)");
		kb.tell("(( (( S20 AND NS30) AND ( NS31 OR NS21) ) ) => W10)");
		
		kb.tell("(( (( S13 AND NS03) AND (NS02 OR NS12) ) AND (NS03) ) => W23)");
		kb.tell("(( (( S13 AND NS23) AND (NS22 OR NS12) ) AND (NS23) ) => W03)");
		kb.tell("(( (( S23 AND NS13) AND (NS12 OR NS22) ) AND (NS13) ) => W33)");
		kb.tell("(( (( S23 AND NS33) AND (NS32 OR NS22) ) AND (NS33) ) => W13)");
		kb.tell("(( (( S31 AND NS32) AND (NS22 OR NS21) ) AND (NS32) ) => W30)");
		kb.tell("(( (( S31 AND NS30) AND (NS20 OR NS21) ) AND (NS30) ) => W32)");
		kb.tell("(( (( S32 AND NS33) AND (NS23 OR NS22) ) AND (NS33) ) => W31)");
		kb.tell("(( (( S32 AND NS31) AND (NS21 OR NS22) ) AND (NS31) ) => W33)");
		
		kb.tell("(( (( S12 AND NS02) AND (NS01 OR NS11) ) AND (NS03 OR NS13) ) => W22)");
		kb.tell("(( (( S21 AND NS11) AND (NS10 OR NS20) ) AND (NS12 OR NS22) ) => W31)");
		kb.tell("(( (( S21 AND NS22) AND (NS12 OR NS11) ) AND (NS32 OR NS22) ) => W20)");
		kb.tell("(( (( S21 AND NS31) AND (NS30 OR NS20) ) AND (NS32 OR NS22) ) => W11)");
		kb.tell("(( (( S21 AND NS20) AND (NS10 OR NS11) ) AND (NS30 OR NS31) ) => W22)");
		kb.tell("(( (( S22 AND NS12) AND (NS11 OR NS21) ) AND (NS13 OR NS23) ) => W32)");
		kb.tell("(( (( S22 AND NS23) AND (NS13 OR NS12) ) AND (NS33 OR NS23) ) => W21)");
		kb.tell("(( (( S22 AND NS32) AND (NS31 OR NS21) ) AND (NS33 OR NS23) ) => W12)");
		kb.tell("(( (( S22 AND NS21) AND (NS11 OR NS12) ) AND (NS31 OR NS32) ) => W23)");
		kb.tell("(( (( S11 AND NS01) AND (NS00 OR NS10) ) AND (NS02 OR NS12) ) => W21)");
		kb.tell("(( (( S11 AND NS12) AND (NS02 OR NS01) ) AND (NS22 OR NS12) ) => W10)");
		kb.tell("(( (( S11 AND NS21) AND (NS20 OR NS10) ) AND (NS22 OR NS12) ) => W01)");
		kb.tell("(( (( S11 AND NS10) AND (NS00 OR NS01) ) AND (NS20 OR NS21) ) => W12)");
		kb.tell("(( (( S12 AND NS13) AND (NS03 OR NS02) ) AND (NS23 OR NS13) ) => W11)");
		kb.tell("(( (( S12 AND NS22) AND (NS21 OR NS11) ) AND (NS23 OR NS13) ) => W02)");
		kb.tell("(( (( S12 AND NS11) AND (NS01 OR NS02) ) AND (NS21 OR NS22) ) => W13)");
		
		kb.tell("((NS00)=> NW10)");
		kb.tell("((NS00)=> NW01)");
		kb.tell("((NS01)=> NW11)");
		kb.tell("((NS01)=> NW02)");
		kb.tell("((NS01)=> NW00)");
		kb.tell("((NS02)=> NW12)");
		kb.tell("((NS02)=> NW03)");
		kb.tell("((NS02)=> NW01)");
		kb.tell("((NS03)=> NW13)");
		kb.tell("((NS03)=> NW02)");
		kb.tell("((NS10)=> NW20)");
		kb.tell("((NS10)=> NW11)");
		kb.tell("((NS10)=> NW00)");
		kb.tell("((NS11)=> NW21)");
		kb.tell("((NS11)=> NW12)");
		kb.tell("((NS11)=> NW01)");
		kb.tell("((NS11)=> NW10)");
		kb.tell("((NS13)=> NW23)");
		kb.tell("((NS13)=> NW03)");
		kb.tell("((NS13)=> NW12)");
		kb.tell("((NS12)=> NW22)");
		kb.tell("((NS12)=> NW13)");
		kb.tell("((NS12)=> NW02)");
		kb.tell("((NS12)=> NW11)");
		kb.tell("((NS20)=> NW30)");
		kb.tell("((NS20)=> NW21)");
		kb.tell("((NS20)=> NW10)");
		kb.tell("((NS21)=> NW31)");
		kb.tell("((NS21)=> NW22)");
		kb.tell("((NS21)=> NW11)");
		kb.tell("((NS21)=> NW20)");
		kb.tell("((NS22)=> NW32)");
		kb.tell("((NS22)=> NW23)");
		kb.tell("((NS22)=> NW12)");
		kb.tell("((NS22)=> NW21)");
		kb.tell("((NS23)=> NW33)");
		kb.tell("((NS23)=> NW13)");
		kb.tell("((NS23)=> NW22)");
		kb.tell("((NS30)=> NW31)");
		kb.tell("((NS30)=> NW20)");
		kb.tell("((NS31)=> NW32)");
		kb.tell("((NS31)=> NW21)");
		kb.tell("((NS31)=> NW30)");
		kb.tell("((NS32)=> NW33)");
		kb.tell("((NS32)=> NW22)");
		kb.tell("((NS32)=> NW31)");
		kb.tell("((NS33)=> NW23)");
		kb.tell("((NS33)=> NW32)");

	}

	
	public void updateTile(String s, Point p){
		String temp = "" + s + "" + p.x + "" + p.y;
		kb.tell(temp);
		System.out.println(temp);
	}
	
	
	public boolean isNotPit(Point p){
		boolean isNotAPit = plfce.plfcEntails(kb, "NP" + p.x + "" + p.y);
		System.out.println(isNotAPit);
		return isNotAPit;
	}
	
	public boolean isNotWumpus(Point p){
		boolean isNotAWumpus;
		if(deadwumpus){
			isNotAWumpus = true;
		}else if(foundwumpus){
			isNotAWumpus = !p.equals(wumpusLocation);
		}else{
			isNotAWumpus = plfce.plfcEntails(kb, "NW" + p.x + "" + p.y);
			System.out.println(isNotAWumpus);
		}
		return isNotAWumpus;
	}
	
	
	public void setWumpusDead(boolean deadwumpus){
		this.deadwumpus = deadwumpus;
	}
	
	
	
	public boolean isSafe(Point p){
		boolean isSafeToGo = true;
		if(p.x > 3 || p.y > 3 || p.x < 0 || p.y < 0){
			isSafeToGo = false;
		}else{
			isSafeToGo = isNotPit(p) && isNotWumpus(p);
		}
		return isSafeToGo;
	}
	
	
	public void setWumpusLocation(Point p){
		foundwumpus = true;
		wumpusLocation = p;
	}
	
	public boolean foundWumpus(){
		return foundwumpus;
	}
	
	public double safeProbability(Point p){
		if(isSafe(p)){
			return 1.0;
		}
		return 0;
	}
	
	
	public Point getWumpusLocation(){
		return wumpusLocation;
	}
	
	public void findWumpus(){
		for(int x = 0; x < 4; x++){
			for(int y = 0; y < 4; y++){
				if(this.plfce.plfcEntails(kb, "W" + x + "" + y)){
					foundwumpus = true;
					wumpusLocation = new Point(x,y);
					return;
				}
			}
		}
	}


	public boolean wumpusFound() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
}
