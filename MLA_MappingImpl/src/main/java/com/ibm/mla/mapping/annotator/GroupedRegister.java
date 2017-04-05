/**
 *
 */
package com.ibm.mla.mapping.annotator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.uima.UimaContext;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.mla.mapping.register.RegisterKey;
import com.ibm.mla.mapping.register.RegisterValue;
import com.ibm.mla.mapping.register.RegisterValueSet;
import com.ibm.mla.mapping.register.csv.CSVRegister;

/**
 * @author Georg Temnitschka
 * represent a register which is optionally built from multiple files, and has a distinct group name
 *
 * *
 * GroupedRegister inherits the contract of its abstract parent @see AbstractRegisterConfigElement :
 *
 * 1. initialize from UIMA context using a CSV register factory - this loads my csv register from the list of CSV files
 * 2. get matching encodings for a given annotation - return matching encodings from my csv register
 */

@XmlType(name="Register")
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupedRegister<K extends RegisterKey<K>, V extends RegisterValue<V>> extends AbstractRegisterConfigElement<K,V> {

	private static Logger logger = LoggerFactory.getLogger(GroupedRegister.class);

	@XmlElement(name="File")
	private List<String> files = new ArrayList<String>();

	/*
	 * this is why we do the whole stuff - a csv register!
	 */
	@XmlTransient
	private CSVRegister<K,V> myCSVRegister = null;

	public GroupedRegister() {
		super();
	}

	public GroupedRegister(List<String> files) {
		this.files = files;
	}


	/* (non-Javadoc)
	 * initializeDetails is called by the Initialize method of the abstract parent (AbstractRegisterConfigElement)
	 * @see com.ibm.mla.mapping.annotator.AbstractRegisterConfigElement#initializeDetails()
	 */
	@Override
	protected void initializeDetails(UimaContext myContext, CSVRegisterFactory<K,V> myRegisterFactory)
			throws ResourceInitializationException {

		for (String registerName: files) {
			String localCodesystem =
					(getCodesystem()==null) ?
							registerName : // null really means "undefined"
								getCodesystem(); // else use it (empty string means "no code systems"
			logger.info("initializing: codeystem={}, file={}", localCodesystem, registerName);

			try {
				String registerFileName = registerName+".csv";
				InputStream inIS = myContext.getResourceAsStream(registerFileName);

				if (inIS == null) {
					throw new ResourceInitializationException(new Throwable("Could not open " + registerFileName + " for reading"));
				}else {
					if (myCSVRegister == null) {
						myCSVRegister = myRegisterFactory.newRegisterFromInputStream(inIS, localCodesystem);
					} else {
						myCSVRegister.readInRegister(inIS, localCodesystem);
					}
				}
			} catch (IOException | ResourceAccessException e){
				throw new ResourceInitializationException(e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.mla.mapping.annotator.AbstractRegisterConfigElement#getMatchingEncodings(org.apache.uima.jcas.tcas.Annotation)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public RegisterValueSet<V> getMatchingEncodings(Annotation targetAnno) {
		return myCSVRegister.getMatchingEncodings(targetAnno);
	}

	@Override

	/**
	 * null-safe: set is empty (not null) if no matching encodings
	 */
	public RegisterValueSet<V> getMatchingEncodings(K targetAnno) {
		return myCSVRegister.getMatchingEncodings(targetAnno);
	}
}
