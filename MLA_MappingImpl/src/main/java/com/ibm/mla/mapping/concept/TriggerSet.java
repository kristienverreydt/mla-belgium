package com.ibm.mla.mapping.concept;

public class TriggerSet extends ConceptSet<Trigger> {

	private static final long serialVersionUID = 1574842992579184937L;
	public TriggerSet(String triggerSetAsText) {
		super(triggerSetAsText);
	}
	@Override
	protected boolean addElement(String elementAsString) {
		return this.add(new Trigger(elementAsString));
	}
}
