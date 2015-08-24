/*  Copyright 2015 by the International Center for Computational Logic, Technical University Dresden.
 
    This file is part of Wolpertinger.

    Wolpertinger is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Wolpertinger is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Wolpertinger.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.semanticweb.wolpertinger.translation.naive;

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
