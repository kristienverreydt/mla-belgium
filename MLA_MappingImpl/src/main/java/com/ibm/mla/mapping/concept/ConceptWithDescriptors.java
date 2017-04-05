/**
 *
 */
package com.ibm.mla.mapping.concept;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author georg.temnitschk@at.ibm.com
 *
 * assertions about private fields:
 * lemma may be null: a concept with a null name or a blank name never compares successfully
 * the set of descriptor terms will never be null, all constructors must ensure this
 */
public abstract class ConceptWithDescriptors
	<T extends ConceptWithDescriptors<T>> extends Concept<T>{

	private Set<DescriptiveTerm> descriptiveTerms = null;
	static final private char odcwdC = com.ibm.mla.mapping.register.csv.CSVRegisterConstants.OUTDIAGCWDSEP;
	static final private String odcwdRE = "\\" + com.ibm.mla.mapping.register.csv.CSVRegisterConstants.OUTDIAGCWDSEP;

	/**
	 * this is the exact reverse of toString()
	 * @param string
	 */
	public ConceptWithDescriptors(String string) {
		//	logger.log(Level.FINEST, "In string constructor with " + string);
		// ensure we have a set - need a set for all following compare operations
		this.descriptiveTerms = new CopyOnWriteArraySet<DescriptiveTerm>();
		//"unknown" String used to work with a fixed value in a ConceptWithDescriptor, can't limit or use empty feature
		if (string == null || string.equals("") || string.equals("#") || string.startsWith("unknown")) {
			this.lemma = "";
			return;
		}
		String part[] = string.split(odcwdRE); // descriptors follow concept, separated by hash signs
		this.lemma = part[0];
		// copy descriptors, skipping empty parts
		for (int j = 1; j < part.length; ++j) {
			if (part[j] == null) {
				continue;
			}
			part[j] = part[j].trim();
			if (part[j].isEmpty()) {
				continue;
			}
			this.descriptiveTerms.add(new DescriptiveTerm(part[j]));
		}
	}

	/**
	 * Get a set of descriptive terms.
	 * @return
	 */
	public Set<DescriptiveTerm> getDescriptiveTerms() {
		return descriptiveTerms;
	}

	/**
	 *
	 * @param dt
	 * @return
	 */
	public boolean addDescriptiveTerm(DescriptiveTerm dt) {
		return this.descriptiveTerms.add(dt);
	}

	/**
	 *  must override the super method with a Concept<T> argument!
	 */
	@Override
	public boolean isSameOrMoreSpecific(Concept<T> arg) {
		//	logger.log(Level.FINER, "comparing" + this + " to " + arg);

		if (arg == null)
		 {
			return true; // anything is more specific than nothing
		}
		if (this.getClass() != arg.getClass()) {
//			logger.log(Level.WARNING, "Comparing wrong class " + getClass() + " : " + arg.getClass() );
			return false; // different concepts
		}
		//	logger.log(Level.FINER, "passed basic tests");
		ConceptWithDescriptors<T> other = (ConceptWithDescriptors<T>) arg;
		if (this.lemma == null) {
			if (other.lemma != null) {
				//	logger.log(Level.FINER, "this lemma is NULL, other lemma not");
				return false;
			}
		} else {
			if (!this.lemma.equals(other.lemma)) {
				//	logger.log(Level.FINER, "lemmata are different");
				return false;
			}
		}
		boolean containsAllOther = this.descriptiveTerms.containsAll(other.descriptiveTerms);
		//	logger.log(Level.FINER, "comparing " + this.descriptiveTerms + " to " + other.descriptiveTerms + " --> " + containsAllOther);
		return containsAllOther;

	}

	@Override
	public int hashCode() {
		int result = super.hashCode();

		for (DescriptiveTerm ddt : descriptiveTerms) {
			// must use a method that is independent of the sequence in which descriptive terms are returned!
			result ^= ddt.hashCode();
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		ConceptWithDescriptors<T> other = (ConceptWithDescriptors<T>) obj;
		if (descriptiveTerms == null) {
			if (other.descriptiveTerms != null) {
				return false;
			}
		} else if (!descriptiveTerms.equals(other.descriptiveTerms)) { // set comparison works as long as equals() for elements is correct
			return false;
		}
		if (lemma == null) { return false; }
		if (other.lemma == null) {return false; }

		return lemma.equals(other.lemma);
	}

	@Override
	public String toString() {
		if (lemma == null) {
			return "";
		}
		StringBuilder retStr = new StringBuilder(lemma);

		if (descriptiveTerms != null) {
			for (DescriptiveTerm dt : descriptiveTerms) {
				if (dt != null) {
					String ddts = dt.toString();
					if (ddts != null && !ddts.equals("")) {
						retStr.append(odcwdC);
						retStr.append(dt.toString());
					}
				}
			}
		}

		return retStr.toString();
	}

}