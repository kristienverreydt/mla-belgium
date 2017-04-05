package com.ibm.mla.mapping.concept;

import org.apache.commons.lang.StringUtils;

public class Code extends Concept<Code> {
	private static final String COLON = ":";
	private static final CharSequence ICD10 = "ICD10";
	private static final String ANY_3RD_DECADE = ".-";
	private String codesystem;
	private String codepoint;
	public Code() {
		super();
	}

	public Code(String lemma) {
		super(lemma);
		codesystem = StringUtils.substringBefore(lemma, COLON);
		codepoint  = StringUtils.substringAfter(lemma, COLON);
	}

//	@Override
//	public String getLemma() {
//		return super.getLemma();
//	}
	
	public String getCodesystem() {
		return codesystem;
	}
	
	public boolean isIcd10Code() {
		return codesystem.contains(ICD10);
	}

	public String getCodepoint() {
		return codepoint;
	}
	
	public String getCodepointWithoutMarkers() {
		return StringUtils.substringBefore(codepoint, ANY_3RD_DECADE);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((codepoint == null) ? 0 : codepoint.hashCode());
		result = prime * result
				+ ((codesystem == null) ? 0 : codesystem.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Code other = (Code) obj;
		if (codepoint == null) {
			if (other.codepoint != null)
				return false;
		} else if (!codepoint.equals(other.codepoint))
			return false;
		if (codesystem == null) {
			if (other.codesystem != null)
				return false;
		} else if (!codesystem.equals(other.codesystem))
			return false;
		return true;
	}

	/**
	 * For ICD-10 codes only, apply the following:
	 * 1. remove trailing ".-" (same semantics as no 3rd decade) from codepoint
	 * 2. check if specification is a substring of this code
	 */
	@Override
	public boolean isSameOrMoreSpecific(Concept<Code> obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Code other = (Code) obj;
		if (isIcd10Code() && other.isIcd10Code()) {
			// remove trailing ".-" from both result and specification, then compare
			return StringUtils.startsWith(getCodepointWithoutMarkers(), other.getCodepointWithoutMarkers());
		} else {
			return equals(other);
		}
	}


	@Override
	public String toString() {
		return "{"+codesystem+":"+codepoint+"}";
	}
}
