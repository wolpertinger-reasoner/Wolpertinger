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
