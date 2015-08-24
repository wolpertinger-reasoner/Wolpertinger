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
 * This class shall represent a ASP constraint.
 * I.e. a rule with an empty head.
 * 
 * @author Lukas Schweizer
 * Technische Universit?t Dresden
 */
public class ASPConstraint {
	
	private Object[] m_positive;
	private Object[] m_negative;
	
	public ASPConstraint(Object[] positiveConjuncts, Object[] negativeConjuncts) {
		m_positive = positiveConjuncts;
		m_negative = negativeConjuncts;
	}
	
	public Object[] getPositives() {
		return m_positive;
	}
	
	public Object[] getNegatives() {
		return m_negative;
	}
	
}
