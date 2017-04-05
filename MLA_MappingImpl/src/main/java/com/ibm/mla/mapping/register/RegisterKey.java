package com.ibm.mla.mapping.register;

public interface RegisterKey<K extends RegisterKey<K>> {
	
	public abstract String getPartialKey();
	public abstract boolean isSameOrMoreSpecific(RegisterKey<K> arg) ;
	public abstract int hashCode();
	public abstract boolean equals(Object o);

}
