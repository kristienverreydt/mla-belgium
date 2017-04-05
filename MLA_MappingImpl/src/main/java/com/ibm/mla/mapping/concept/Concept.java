package com.ibm.mla.mapping.concept;

/**
 * @author georg.temnitschk@at.ibm.com
 * @param <T>
 */
public abstract class Concept <T extends Concept<T>> {
	protected String lemma;
//	protected Logger logger = Logger.getLogger(this.getClass().getName());

	public Concept() {
		super();
		this.lemma = "";
	}

	public Concept (String lemma) {
		this.lemma = (lemma == null) ? "" : lemma;
	}

	public String getLemma() {
		return lemma;
	}

	// generated hash code and compare operators
	@Override
	public int hashCode() {
		return lemma.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		//	logger.log(Level.FINER, "comparing " + this + " to " + obj);

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
//			logger.log(Level.WARNING, "Comparing wrong class " + getClass() + " : " + obj.getClass() );
			return false;
		}
		@SuppressWarnings("unchecked")
		Concept<T> other = (Concept<T>) obj;
		//	logger.log(Level.FINER, "comparing lemma " + this.lemma + " to " + other.lemma);

		if (this.lemma != null) {
			boolean rv = this.lemma.equals(other.lemma);
			//	logger.log(Level.FINER, "equals = " + rv);
			return rv;

		} else {
			boolean rv = other.lemma == null;
			//	logger.log(Level.FINER, "this.lemma is NULL, is other NULL? " + rv);
			return rv;
		}

	}

	/** equal or more specific comparison operator:
	 * true if this object is equal to the argument, or the argument is null
	 */
	public boolean isSameOrMoreSpecific(Concept<T> arg) {
		//	logger.log(Level.FINER, "comparing" + this + " to " + arg);
		if (arg == null)
		 {
			return true; // any is more specific than none
		}
		boolean rv = this.equals(arg);
		//	logger.log(Level.FINER, "this equals arg: " + rv);
		return rv;
	}

	@Override
	public String toString() {
		return lemma ;
	}
}