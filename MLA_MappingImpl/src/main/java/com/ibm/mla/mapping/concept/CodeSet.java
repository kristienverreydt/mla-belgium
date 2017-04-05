package com.ibm.mla.mapping.concept;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeSet extends ConceptSet<Code> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final long serialVersionUID = 6870323365571930141L;
	
	public CodeSet(String setAsString) {
		super(setAsString);
	}

	public CodeSet(Set<String> codesAsString) {
		super();
		for (String codeAsString: codesAsString) {
			this.add(new Code(codeAsString));
		}
	}

	public CodeSet() {
		super();
	}

	@Override
	protected boolean addElement(String elementAsString) {
		return this.add(new Code(elementAsString));
	}

	/**
	 * This set must contain all codes which map to codes in the specification set,
	 *  but no others
	 * @param specificationCodeSet the set of codes from the specification
	 * @return true if each code in this set is same or more specific than a code in the specification set,
	 *              and this set does not contain any codes
	 *              @deprecated use moreSpecificCodesNotIn() and lessSpecificCodesNotIn() instead
	 */
	@Deprecated
	public boolean isStrictlySameOrMoreSpecific(CodeSet specificationCodeSet) {
		// remember the codes from the specification which have matched
		CodeSet matchedSpecificationCodes = new CodeSet();
		for (Code resultCode: this) {
			boolean resultMatchesSpec = false;
			// for each code in this set, find a match in the specification set
			// add the match to the set of matched codes
			for (Code specCode: specificationCodeSet) {
				logger.trace("comparing result code {} to {}", resultCode, specCode);
				if (resultCode.isSameOrMoreSpecific(specCode)) {
					resultMatchesSpec = true;
					matchedSpecificationCodes.add(specCode);
					logger.trace("matched, set of matched codes is now {}", matchedSpecificationCodes);
					break;
				}
			}
			if (!resultMatchesSpec) {
				logger.debug("no match, return false");
				return false; // exhausted the specification set without a match
			}
		}
		// now, check if all codes in the specification set have matched
		specificationCodeSet.removeAll(matchedSpecificationCodes);
		logger.debug("Remaining unmatched specification codes: {}", specificationCodeSet);
		return specificationCodeSet.isEmpty();
	}

	/**
	 * get all additional codes from this set (supposedly a result set)
	 *  which are not in the specification code set when using same-or-more-specific comparison
	 * @param specificationCodeSet the set of codes in the test specification
	 * @return the set of additional codes not in the spec
	 */
	public CodeSet moreSpecificCodesNotIn(CodeSet specificationCodeSet) {
		CodeSet returnSet = new CodeSet();
		// loop over all codes in the result set
		for (Code resultCode: this) {
			boolean resultMatchesSpec = false;
			// find a match in the specification set
			for (Code specCode: specificationCodeSet) {
				logger.trace("comparing result code {} to {}", resultCode, specCode);
				if (resultCode.isSameOrMoreSpecific(specCode)) {
					resultMatchesSpec = true;
					break;
				}
			}
			if (!resultMatchesSpec) {
				logger.debug("no match");
				returnSet.add(resultCode);
			}
		}

		return returnSet;
	}

	/**
	 * get all  codes from this set (supposedly a specification set)
	 *  which are not in the result code set when using same-or-LESS-specific comparison
	 * @param resultCodeSet the set of codes in the test result
	 * @return the set of codes from this set which are not same-or-less-specific than a code from the result set
	 */
	public CodeSet lessSpecificCodesNotIn(CodeSet resultCodeSet) {
		CodeSet returnSet = new CodeSet();
		// loop over all codes in the specification set (this)
		for (Code specificationCode: this) {
			boolean resultMatchesSpec = false;
			// find a match in the result set
			for (Code resultCode: resultCodeSet) {
				logger.trace("comparing spec code {} to {}", specificationCode, resultCode);
				if (resultCode.isSameOrMoreSpecific(specificationCode)) {
					resultMatchesSpec = true;
					break;
				}
			}
			if (!resultMatchesSpec) {
				logger.debug("no match");
				returnSet.add(specificationCode);
			}
		}

		return returnSet;
	}
}