/**
 * 
 */
package org.semanticweb.wolpertinger.translation.asp;

/**
 * ASP-2 Core Symbol constants.
 * 
 * @author Lukas Schweizer
 * Technische Universität Dresden
 *
 */
public final class ASP2CoreSymbols {

	private ASP2CoreSymbols() {}
	
	public static final String IMPLICATION 			= ":-";
	public static final String CONJUNCTION 			= ",";
	public static final String DISJUNCTION 			= "|";
	public static final String NAF 					= "not";
	public static final String NEGATION 			= NAF;
	public static final String CLASSICAL_NEGATION 	= "-";
	
	
	public static final String GEQ 	= ">=";
	public static final String LEQ 	= "<=";
	public static final String GT 	= ">";
	public static final String LT 	= "<";
	public static final String EQ 	= "=";
	public static final String NEQ 	= "!=";
	
	public static final String STMT_COUNT 	= "#count";
	public static final String STMT_MAX		= "#maximize";
	public static final String STMT_MIN		= "#minimize";
	
	public static final String BRACKET_OPEN 	= "(";
	public static final String BRACKET_CLOSE 	= ")";
	public static final String CURLY_OPEN		= "{";
	public static final String CURLY_CLOSE		= "}";
	public static final String EOR 				= ".";
	public static final String ARG_SEPERATOR 	= ",";
	public static final String SPACE 			= " ";
}
