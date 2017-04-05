package com.ibm.mla.mapping.concept;

public class CircumstanceSet extends ConceptSet<Circumstance> {
	private static final long serialVersionUID = -2085165675120632765L;

	@Override
	protected boolean addElement(String elementAsString) {
		return this.add(new Circumstance(elementAsString));
	}

	public CircumstanceSet(String setAsString) {
		super(setAsString);
	}
	
}
