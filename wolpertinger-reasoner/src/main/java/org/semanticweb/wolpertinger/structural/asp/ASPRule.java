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
package org.semanticweb.wolpertinger.structural.asp;

/**
 * Represents a rule, which we create already in HermiT while handling cardinality restriction.
 * It is only used for the special case where we have cardinality restriction on the lhs of a sub-class axiom.
 * See documentation, "Special treatment of Clauses (6), (7) and (11).
 * @author Lukas Schweizer	
 * Technische Universit?t Dresden
 */
public class ASPRule {

	private Object[] m_headAtoms;
	private Object[] m_bodyAtoms;
	
	public ASPRule(Object[] m_headAtoms, Object[] m_bodyAtoms) {
		this.m_headAtoms = m_headAtoms;
		this.m_bodyAtoms = m_bodyAtoms;
	}

	/**
	 * @return the m_headAtoms
	 */
	public Object[] getM_headAtoms() {
		return m_headAtoms;
	}

	/**
	 * @return the m_bodyAtoms
	 */
	public Object[] getM_bodyAtoms() {
		return m_bodyAtoms;
	}
	
	
}
