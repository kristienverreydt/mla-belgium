package com.ibm.mla.mapping.register;

public class RegisterValue<V extends RegisterValue<V>> {
	protected String codeSystem = null;
	protected String code = null;
	protected boolean otherSpecific = false;
	protected boolean unspecific = false;
	
	public String getCodeSystem() {
		return codeSystem;
	}
	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}
	public String getCode() {
		return code;
	}
	public boolean isOtherSpecific() {
		return otherSpecific;
	}
	public boolean isUnspecific() {
		return unspecific;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result
				+ ((codeSystem == null) ? 0 : codeSystem.hashCode());
		result = prime * result + (otherSpecific ? 1231 : 1237);
		result = prime * result + (unspecific ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RegisterValue))
			return false;
		@SuppressWarnings("rawtypes")
		RegisterValue other = (RegisterValue) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (codeSystem == null) {
			if (other.codeSystem != null)
				return false;
		} else if (!codeSystem.equals(other.codeSystem))
			return false;
		if (otherSpecific != other.otherSpecific)
			return false;
		if (unspecific != other.unspecific)
			return false;
		return true;
	}

	
}
