/**
 * 
 */
package com.ibm.mla.mapping.annotator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.uima.UimaContext;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import com.ibm.mla.mapping.register.RegisterKey;
import com.ibm.mla.mapping.register.RegisterValue;
import com.ibm.mla.mapping.register.RegisterValueSet;

/**
 * @author Georg Temnitschka
 * Contains a prioritized sequence of Register groups
 * Inherits the contract of its abstract parent @see AbstractRegisterConfigElement :
 * 1. initialize from UIMA context using a CSV register factory
 *    - this loads all configured registers from CSV files by
 *    - by delegating to all register groups in the sequence
 * 2. get matching encodings for a given annotation
 *    - go through register groups in sequence
 *    - stop as soon as a match has been found
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="RegisterSequence")
public class RegisterSequence<K extends RegisterKey<K>, V extends RegisterValue<V>>  extends AbstractRegisterConfigElement<K,V> {

	@XmlElement(name="RegisterGroup")
	List<RegisterGroup<K,V>> registerGroups = new ArrayList<RegisterGroup<K,V>>();

	public RegisterSequence() {
		super();
	}

	@SafeVarargs
	public RegisterSequence(RegisterGroup<K,V>...registerGroups) {
		super();
		this.registerGroups = Arrays.asList(registerGroups);
	}

	@Override
	public void initializeDetails (UimaContext myContext, CSVRegisterFactory<K,V> myRegisterFactory)
			 throws ResourceInitializationException {
		for (RegisterGroup<K,V> registerGroup: registerGroups) {
			registerGroup.initialize(myContext, myRegisterFactory, getCodesystem());
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.mla.mapping.annotator.AbstractRegisterConfigElement#getMatchingEncodings(org.apache.uima.jcas.tcas.Annotation)
	 */
	@Override
	public RegisterValueSet<V> getMatchingEncodings(Annotation targetAnno) {
		RegisterValueSet<V> answer = null;
		for (RegisterGroup<K,V> registerGroup: registerGroups) {
			if (answer == null) {
				answer = registerGroup.getMatchingEncodings(targetAnno);
			} else {
				// nothing, we stop after the first match has been found
			}
		}
		return answer;
	}

	@Override
	public RegisterValueSet<V> getMatchingEncodings(K targetAnno) {
		RegisterValueSet<V> answer = null;
		for (RegisterGroup<K,V> registerGroup: registerGroups) {
			if (answer == null) {
				answer = registerGroup.getMatchingEncodings(targetAnno);
			} else {
				// nothing, we stop after the first match has been found
			}
		}
		return answer;
	}
}
