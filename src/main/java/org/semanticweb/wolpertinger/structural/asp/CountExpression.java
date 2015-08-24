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

import org.semanticweb.wolpertinger.model.Atom;

/**
 * @author Lukas Schweizer
 * Technische Universit?t Dresden
 */
public class CountExpression {

	private Atom toCountAtom;
	private Atom conditionAtom;
	
	private String comperator;
	private int n;
	
	public CountExpression(Atom toCountAtom, Atom conditionAtom, String comperatror, int n) {
		this.toCountAtom = toCountAtom;
		this.conditionAtom = conditionAtom;
		this.comperator = comperatror;
		this.n = n;
	}
	
	public Atom getCountAtom() {
		return toCountAtom;
	}
	
	public Atom getConditionAtom() {
		return conditionAtom;
	}
	
	public String getComperator() {
		return comperator;
	}
	
	public int getN() {
		return n;
	}
	
	@Override
	public String toString() {
		return "#count{" + toCountAtom.toString() + " : " + conditionAtom.toString() + "} " + comperator + " " + n;
	}
}
