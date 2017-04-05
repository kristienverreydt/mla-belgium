package com.ibm.mla.mapping.concept;

public class PhysProcessSet extends ConceptSet<PhysProcess> {

	private static final long serialVersionUID = -8537167823786204655L;
	public PhysProcessSet(String physProcSetAsText) {
		super(physProcSetAsText);
	}
	@Override
	protected boolean addElement(String elementAsString) {
		return this.add(new PhysProcess(elementAsString));
	}
}
