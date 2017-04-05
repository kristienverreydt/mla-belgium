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
import javax.xml.bind.annotation.XmlType;

import org.apache.uima.UimaContext;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import com.ibm.mla.mapping.register.RegisterKey;
import com.ibm.mla.mapping.register.RegisterValue;
import com.ibm.mla.mapping.register.RegisterValueSet;

/**
 * @author Georg Temnitschka
 * Represents a group of registers,
 *  where all registers in the group have equal precedence
 *
 * Inherits the contract of its abstract parent @see AbstractRegisterConfigElement :
 * 1. initialize from UIMA context using a CSV register factory
 *    - this loads all configured registers from CSV files
 *    - by delegating to the registers within the group
 * 2. get matching encodings for a given annotation
 *   - collect matching encodings from all registers within the group
 *   - merge the results
 */
@XmlType(name="Group")
@XmlAccessorType(XmlAccessType.FIELD)
public class RegisterGroup<K extends RegisterKey<K>, V extends RegisterValue<V>> extends AbstractRegisterConfigElement<K,V> {

	@XmlElement(name="Register")
	private List<GroupedRegister<K,V>> groupedRegisters = new ArrayList<GroupedRegister<K,V>>();

	public RegisterGroup(GroupedRegister<K, V> groupedRegisters) {
		super();
		this.groupedRegisters = Arrays.asList(groupedRegisters);
	}

	public RegisterGroup() {
		super();
	}

	//This method is called after all the properties (except IDREF) are unmarshalled for this object,
	//but before this object is set to the parent object.
//	void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
//		logger.debug("{}: unmarshalled by {} for parent {}", this.getClass().getName(), unmarshaller.getClass().getName(), parent.getClass().getName());
//	}
// output: "com.ibm.mla.mapping.annotator.RegisterGroup: unmarshalled by com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl for parent com.ibm.mla.mapping.annotator.RegisterSequence"

	/* (non-Javadoc)
	 * @see com.ibm.mla.mapping.annotator.AbstractRegisterConfigElement#initializeDetails()
	 */
	@Override
	public void initializeDetails(UimaContext myContext, CSVRegisterFactory<K,V> myRegisterFactory)
			 throws ResourceInitializationException {
		for (GroupedRegister<K, V> groupedRegister: groupedRegisters) {
			groupedRegister.initialize(myContext, myRegisterFactory, getCodesystem());
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.mla.mapping.annotator.AbstractRegisterConfigElement#getMatchingEncodings(org.apache.uima.jcas.tcas.Annotation)
	 */
	@Override
	public RegisterValueSet<V> getMatchingEncodings(Annotation targetAnno) {
		RegisterValueSet<V> answer = null;
		for (GroupedRegister<K, V> groupedRegister: groupedRegisters) {
			if (answer == null) {
				answer = groupedRegister.getMatchingEncodings(targetAnno);
			} else {
				answer.addAll(groupedRegister.getMatchingEncodings(targetAnno));
			}
		}
		return answer;
	}

	@Override
	public RegisterValueSet<V> getMatchingEncodings(K targetAnno) {
		RegisterValueSet<V> answer = null;
		for (GroupedRegister<K, V> groupedRegister: groupedRegisters) {
			if (answer == null) {
				answer = groupedRegister.getMatchingEncodings(targetAnno);
			} else {
				answer.addAll(groupedRegister.getMatchingEncodings(targetAnno));
			}
		}
		return answer;
	}
}
