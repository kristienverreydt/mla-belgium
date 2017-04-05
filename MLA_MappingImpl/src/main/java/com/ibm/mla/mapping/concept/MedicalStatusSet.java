package com.ibm.mla.mapping.concept;

public class MedicalStatusSet extends ConceptSet<MedicalStatus> {

	private static final long serialVersionUID = 8984858380240955547L;
	public MedicalStatusSet(String medStatusSetAsText) {
		addElements(medStatusSetAsText);
	}
	@Override
	protected boolean addElement(String elementAsString) {
		return this.add(new MedicalStatus(elementAsString));
	}
}
