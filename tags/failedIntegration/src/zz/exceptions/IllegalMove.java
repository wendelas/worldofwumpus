/*****************************************************************************
 * FILE: IllegalMove.java
 * DATE: 02/08/12
 * AUTHOR: 	Karl Schmidbauer <schmidbauerk@msoe.edu>
 * 			Ben Ebert <ebertb@msoe.edu>
 * 
 * PURPOSE: Implementation of an exception to state when an illegal 
 * 			move was made. 
 * 
 ****************************************************************************/
package zz.exceptions;


/**
 * Custom exception to provide a more discriptive name
 * 
 * @author Karl Schmidbauer <schmidbauerk@msoe.edu>
 *
 */
public class IllegalMove extends Exception {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = -2576539299454224000L;

	/**
	 * Generica constructor
	 * @param msg - message detailing why the move is Illegal
	 */
	public IllegalMove(String msg) {
		super(msg);
	}
	
	/**
	 * Hidden zero parameter constructor so that there must be a message
	 */
	@SuppressWarnings("unused")
	private IllegalMove(){
	}
}
