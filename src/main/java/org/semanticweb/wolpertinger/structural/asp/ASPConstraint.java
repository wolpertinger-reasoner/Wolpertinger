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
