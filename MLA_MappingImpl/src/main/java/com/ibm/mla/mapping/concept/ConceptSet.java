package com.ibm.mla.mapping.concept;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * This is a set where each element implements the "is same or more specific" test.
 * Now, we must extend the test to two sets of such elements.
 * We do this in the following way:
 * for each element e2 in set S2 (which is supposed to be LESS specific than S1 (=this)):
 * for each element e1 in set S1:
 * if e1 is same or more specific than e2: then e2 has matched, continue with next element from e2
 * @author georg.temnitschk@at.ibm.com
 * @param <T>
 */
public abstract class ConceptSet<T extends Concept<T>>
		extends HashSet<T> {
	public ConceptSet() {
		super();
	}

	public ConceptSet(String setAsString) {
		addElements(setAsString);
	}

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final long serialVersionUID = -5744403882727137877L;
	// subfield separator
	private static final char odssC = com.ibm.mla.mapping.register.csv.CSVRegisterConstants.OUTDIAGSUBSEP;
	protected static final String odssRE = "\\" + com.ibm.mla.mapping.register.csv.CSVRegisterConstants.OUTDIAGSUBSEP;

	protected abstract boolean addElement(String elementAsString);
	protected void addElements(String setAsString) {
		if (setAsString != null) {
			String[] elementAsString = setAsString.split(odssRE);
			for (int j = 0; j < elementAsString.length; ++j) {
				if ((elementAsString[j] != null) && !elementAsString[j].trim().isEmpty()) {
					addElement(elementAsString[j].trim());
				}
			}
		}
	}

	public boolean isSameOrMoreSpecific(ConceptSet<T> s2) {
		if (s2 == null) {
			return true; // any is more specific than none
		}

		for (T e2 : s2) {
			boolean e2isSameOrLessSpecThanSomeE1 = false;
			for (T e1 : this) {
				if (e1.isSameOrMoreSpecific(e2)) {
					e2isSameOrLessSpecThanSomeE1 = true;
					break;
				}
			}
			if (!e2isSameOrLessSpecThanSomeE1) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		// logger.log(Level.FINEST, "formatting " + super.toString());
		StringBuilder retString = new StringBuilder();
		String sep = "";
		String sep1 = new String("" + odssC);
		for (Concept<T> cwd : this) {
			//	logger.log(Level.FINEST, "in toString: " + cwd);
			if (cwd == null) {
				logger.warn("Concept contains NULL element");
				continue;
			} else {
				retString.append(sep);
				retString.append(cwd.toString());
				sep = sep1;
			}
		}
		return retString.toString();
	}
}
