package com.ibm.mla.mapping.concept;

public class DiseaseSet extends ConceptSet<Disease> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7053079041121182238L;
	/** 
	 * Init the DiseaseSet from a String in which the individual Diseases are delimited.
	 * @param dsText
	 */
	public DiseaseSet(String dsText) {
		super(dsText);
	}
	@Override
	protected boolean addElement(String elementAsString) {
		return this.add(new Disease(elementAsString));
	}
}
