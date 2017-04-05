package com.ibm.mla.mapping.concept;

/**
 * @author georg.temnitschk@at.ibm.com
 */
public class DescriptiveTerm {
	
	private String lemma;

	public DescriptiveTerm(String lemma) {
		this.lemma = (lemma == null) ? "" : lemma;
	}

	public String getLemma() {
		return lemma;
	}

	@Override
	public String toString() {
		return lemma;
	}

	@Override
	public int hashCode() {
		return lemma.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DescriptiveTerm other = (DescriptiveTerm) obj;
		// rely on lemma never being null
		return lemma.equals(other.lemma);
	}
}