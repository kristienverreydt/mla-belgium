package com.ibm.mla.mapping.concept;

public class BodyPartSet extends ConceptSet<BodyPart> {
	private static final long serialVersionUID = -6041665652191718789L;

	public BodyPartSet(String anatomicalterm) {
		super(anatomicalterm);
	}

	@Override
	protected boolean addElement(String elementAsString) {
		return this.add(new BodyPart(elementAsString));
	}
}
