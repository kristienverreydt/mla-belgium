package com.ibm.mla.mapping.register;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Register<K extends RegisterKey<K>, V extends RegisterValue<V>> {

	public class KeyValueBox {
		K key;
		V value;
		KeyValueBox(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}
		public K getKey() {
			return key;
		}
		public V getValue() {
			return value;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			@SuppressWarnings("unchecked")
			KeyValueBox other = (KeyValueBox) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (key == null) {
				if (other.key != null) {
					return false;
				}
			} else if (!key.equals(other.key)) {
				return false;
			}
			if (value == null) {
				if (other.value != null) {
					return false;
				}
			} else if (!value.equals(other.value)) {
				return false;
			}
			return true;
		}
		@SuppressWarnings("rawtypes")
		private Register getOuterType() {
			return Register.this;
		}

	}

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected HashMap<String,Set<KeyValueBox>> myRegisterMap = new HashMap<String,Set<KeyValueBox>>(10000);


	public Register() {
		// Auto-generated constructor stub, which is ok - no initialization required
	}

	/**
	 * @author Georg Temnitschka
	 * @return true if the register did not already contain the new entry
	 */
	protected boolean registerInternally(K myKey, V myValue) {
// all concepts which might be same or more specific must return the same partial key, e.g., the disease lemma
		String keyLemma = myKey.getPartialKey();
		logger.trace("in register: partial key = {}", keyLemma);
// retrieve set of all entries with the same partial key
		Set<KeyValueBox> entriesForPartialKey = myRegisterMap.get(keyLemma);
		if (entriesForPartialKey == null) {
			entriesForPartialKey = new CopyOnWriteArraySet<KeyValueBox>();
			myRegisterMap.put(keyLemma, entriesForPartialKey);
		}
		boolean isNew = entriesForPartialKey.add(new KeyValueBox(myKey,myValue));
		return isNew;
	}

	/**
	 * find all encodings which match the given key
	 * @param matchKey
	 * @return a set of matching encodings; null-safe: set is empty (not null) if no match found
	 */
	public RegisterValueSet<V> getMatchingEncodings(K matchKey) {
			RegisterValueSet<V> retSet = new RegisterValueSet<V>();
			if (matchKey == null) {
				logger.warn("called with matchKey == null");
				return retSet;
			}
// everything that might match must have the same partial key
			String partialKey = matchKey.getPartialKey();
			logger.trace("getting for {}", matchKey);
			Set<KeyValueBox> candidates = myRegisterMap.get(partialKey);
			if (candidates == null || candidates.size() == 0) {
				logger.trace("no encodings found for {}", matchKey);

			} else {
				logger.trace("in getMatchingEncodings: Diagnosis = {}: set size = {}", matchKey, candidates.size());
/*
 * create arrays of keys, values, and flags to store which key/value pairs to keep or drop
 */
				@SuppressWarnings("unchecked")
				RegisterKey<K>[] matchingKey = new RegisterKey[candidates.size()];
				@SuppressWarnings("unchecked")
				RegisterValue<V>[] matchingValue = new RegisterValue[candidates.size()];
				boolean[] keepKvb = new boolean[candidates.size()];
				int kvbListSize = 0;

/*
 * Copy possible key/value pairs only if the key is the same or LESS specific than the match-key we're looking for
 */
				for (KeyValueBox kvb : candidates) {
					RegisterKey<K> aKey = kvb.getKey();
					RegisterValue<V> aCode = kvb.getValue();

					logger.trace("comparing to {}", aKey);

					if (matchKey.isSameOrMoreSpecific(aKey)) {
						matchingKey[kvbListSize] = aKey;
						matchingValue[kvbListSize] = aCode;
						keepKvb[kvbListSize] = true;
						++kvbListSize;
					}
				}
				logger.trace("found {} candidates", kvbListSize);
/*
 *  Compare the possible answers (key-value-boxes) with each other
 *  Every kvb must be once on the left and once on the right side of the "same or more specific" operator
 *  Skip j==k and kvb's which have already been excluded
 *  Handle special case of two equal keys:
 *    if values are also equal, log a warning, and exclude one of the two
 *    if values are different, keep both, but also log a warning
 */
				for (int j = 0; j < kvbListSize; ++j) {
					for (int k = 0; k < kvbListSize; ++k) {
						if (j == k) {
							continue;
						}
						if (!keepKvb[j]) {
							continue;
						}
						if (!keepKvb[k]) {
							continue;
						}
						if (matchingKey[j].equals(matchingKey[k])) {
							if (matchingValue[j].equals(matchingValue[k])) {
								logger.warn("Same key ({}) and same value ({}) in Register:", matchingKey[j], matchingValue[j]);
								keepKvb[k] = false;
							} else {
//								logger.log(Level.WARNING, "Same key (" + matchingKey[j] + ") and different values (" + matchingValue[j] + "), (" + matchingValue[k] + ") in Register:");
							}
						} else if (matchingKey[j].isSameOrMoreSpecific(matchingKey[k])) {
							logger.trace("\"{}\" excludes \"{}\"", matchingKey[j], matchingKey[k]);
							keepKvb[k] = false;
						}
					}
				}
	// return the remaining answers to the caller
				for (int j = 0; j < kvbListSize; ++j) {
					if (keepKvb[j]) {
						@SuppressWarnings({ "unchecked" })
						V matchedValue =(V)matchingValue[j];
						retSet.add(matchedValue);
						logger.trace("added {}", matchingValue[j]);
					}
				}
			}

			return retSet;
		}
	/*
	 *  same as getMatchingEncodings(k matchKey) with less specific search criteria
	 *  -> only lemma of the ConceptWithDescriptor
	 */
	@Deprecated
	public RegisterValueSet<V> getUnspecificMatchingEncodings(K matchKey) {
		RegisterValueSet<V> retSet = new RegisterValueSet<V>();
		if (matchKey == null) {
			return retSet;
		}

		String partialKey = matchKey.getPartialKey();
		logger.trace("getting for {}", matchKey);
		Set<KeyValueBox> candidates = myRegisterMap.get(partialKey);
		if (candidates == null || candidates.size() == 0) {
			logger.trace("no encodings found for {}", matchKey);
		} else {
			logger.trace("in getMatchingEncodings: Diagnosis = {}: set size = {}", matchKey, candidates.size());

			@SuppressWarnings("unchecked")
			RegisterKey<K>[] matchDg = new RegisterKey[candidates.size()];
			@SuppressWarnings("unchecked")
			RegisterValue<V>[] matchI10c = new RegisterValue[candidates.size()];
			boolean[] keepKvb = new boolean[candidates.size()];
			int kvbListSize = 0;

			for (KeyValueBox kvb : candidates) {
				RegisterKey<K> keydg = kvb.getKey();
				RegisterValue<V> i10c = kvb.getValue();

				logger.trace("comparing to {}", keydg);

//				if (matchKey.isSameOrMoreSpecific(keydg)) {
					matchDg[kvbListSize] = keydg;
					matchI10c[kvbListSize] = i10c;
					keepKvb[kvbListSize] = true;
					++kvbListSize;
//				}
			}
			logger.trace("found {} candidates", kvbListSize);
// now, compare the possible answers with each other
			for (int j = 0; j < kvbListSize; ++j) {
				for (int k = 0; k < kvbListSize; ++k) {
					if (j == k) {
						continue;
					}
					if (!keepKvb[j]) {
						continue;
					}
					if (!keepKvb[k]) {
						continue;
					}
					if (matchDg[j].isSameOrMoreSpecific(matchDg[k])) {
						logger.trace("Excluded {}", matchDg[k]);
						keepKvb[k] = false;
					}
				}
			}
// return the remaining answers to the caller
			for (int j = 0; j < kvbListSize; ++j) {
				if (keepKvb[j]) {
					@SuppressWarnings({ "unchecked" })
					V matchedValue =(V)matchI10c[j];
					retSet.add(matchedValue);
					logger.trace("added {}", matchI10c[j]);
				}
			}

		}

		return retSet;
	}

	public int getKeyCount() {
		return myRegisterMap.size();
	}

}
