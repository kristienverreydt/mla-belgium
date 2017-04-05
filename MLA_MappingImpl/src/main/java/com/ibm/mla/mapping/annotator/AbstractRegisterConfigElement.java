/**
 * 
 */
package com.ibm.mla.mapping.annotator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.apache.uima.UimaContext;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import com.ibm.mla.mapping.register.RegisterKey;
import com.ibm.mla.mapping.register.RegisterValue;
import com.ibm.mla.mapping.register.RegisterValueSet;

/**
 * @author AT00955
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractRegisterConfigElement <K extends RegisterKey<K>, V extends RegisterValue<V>> {
	@XmlAttribute
	protected String codesystem = null;
	public String getCodesystem() {
		return codesystem;
	}
	public void setCodesystem(String codesystem) {
		this.codesystem = codesystem;
	}
	
	@Deprecated
	public abstract RegisterValueSet<V> getMatchingEncodings(Annotation targetAnno);
	/**
	 * Find matching encodings for a register key
	 * @param targetAnno
	 * @return the set of matching encodings; null-safe: set is empty (not null) if no matching encodings found
	 */
	public abstract RegisterValueSet<V> getMatchingEncodings(K targetAnno);
	
	protected abstract void initializeDetails(UimaContext myContext, CSVRegisterFactory<K,V> myRegisterFactory)
			throws ResourceInitializationException ;
	
	public void initialize (UimaContext myContext, CSVRegisterFactory<K,V> myRegisterFactory, String codesystem)
			throws ResourceInitializationException {
		if (getCodesystem() == null) {
			setCodesystem(codesystem);
		}
		initializeDetails(myContext, myRegisterFactory);
	}
	
}
