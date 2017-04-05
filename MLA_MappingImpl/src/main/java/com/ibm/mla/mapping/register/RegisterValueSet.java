package com.ibm.mla.mapping.register;

import java.util.HashSet;


public class RegisterValueSet<V extends RegisterValue<V>> extends HashSet<V> {
	private static final long serialVersionUID = -1229607623946463557L;
	// the three booleans can only be set through the constructor
	private boolean subSet = false;
	private boolean unspecific = false;
	private boolean otherSpecific = false;
	
	public boolean isSubSet() {
		return subSet;
	}

	public boolean isUnspecific() {
		return unspecific;
	}

	public boolean isOtherSpecific() {
		return otherSpecific;
	}

	public RegisterValueSet() {
		super();
	}


	protected RegisterValueSet(boolean isOtherSpecific, boolean isUnspecific) {
		subSet = true;
		otherSpecific = isOtherSpecific;
		unspecific = isUnspecific;
	}

	public RegisterValueSet<V> getSubSet(boolean isOtherSpecific, boolean isUnspecific) {
		RegisterValueSet<V> retSet = new RegisterValueSet<V>(isOtherSpecific, isUnspecific);
		
		for (V value : this) {
			if ((value.isOtherSpecific() == isOtherSpecific) && (value.isUnspecific() == isUnspecific))
				retSet.add(value);
		}
		return retSet;
	}
}
