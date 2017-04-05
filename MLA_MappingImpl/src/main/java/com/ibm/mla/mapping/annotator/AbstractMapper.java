package com.ibm.mla.mapping.annotator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.commons.lang.StringUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.mla.mapping.register.RegisterKey;
import com.ibm.mla.mapping.register.RegisterValue;
import com.ibm.mla.mapping.register.RegisterValueSet;
import com.ibm.mla.mapping.register.csv.CSVRegister;
import com.ibm.mla.mapping.register.csv.CSVRegisterConstants;

/**
 * Acts as a abstract class for all readers / mappers.
 * The purpose of a Mapper is to load one or more register files
 * into main memory and than add a feature called codelist to all
 * annotations found in an analyzed document, which have a matching
 * representation in the register. The code list feature is in the form:
 * "codelist", "=", registername, ":", code, {"|", registername, ":" }</br>
 *
 * @author georg.temnitschka@at.ibm.com
 * @author philip.sheikh@at.ibm.com
 */
/*
 * (task 7376) Generalize process() flow between abstract and concrete mapper
 *
 */
public abstract class AbstractMapper<K extends RegisterKey<K>, V extends RegisterValue<V>>
extends JCasAnnotator_ImplBase
implements CSVRegisterFactory<K,V> // the register hierarchy needs this to create the right Register instance
{

	/** get the java.util logger for our class */
	//	private Logger logger = Logger.getLogger(this.getClass().getName());
	/*
	 * Logging must be set up by the implementing sub-class;
	 * to ensure this, the sub-class must provide a method by which the superclass can get the logger
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());
;

	/** encoding for all IO is UTF-8 */
	protected final String ENCODING = CSVRegisterConstants.REGISTERCHARSET;
	/** search for otherSpecific / unSpecific ICD-10 codes in this sequence: */
	protected static final boolean[][] codes = { { false, false }, { true, false },
		{ false, true }, { true, true } };


	// Constants defining the parameter names which will be read from the UIMA Context
	// during initialize(). Parameter group defining the register(s) to be used:
	private static final String REG_GROUP = "REGISTER";
	/* CONFIG: register configuration file to be used (optional) */
	private static final String REG_CONFIG = "CONFIG";
	/*
	 * only if CONFIG is NOT used:
	 *  CODESYSTEMS: set to true if using code systems (register file names become code system names)
	 *  NAMES: list of register file names (WITHOUT trailing ".csv")
	 */
	private static final String REG_CODESYSTEMS = "CODESYSTEMS";
	private static final String REG_NAMES = "NAMES";


	// register configuration (initialized from XML config)
	// due to Java type erasure, the register configuration cannot be declared static
	// (otherwise, mappers for different concepts would share the same registers 8--(
	private  AbstractRegisterConfigElement<K, V> myRegisters = null;

	/*
	 * (non-Javadoc)
	 * this is the helper function for the register hierarchy
	 * @see com.ibm.mla.mapping.annotator.CSVRegisterFactory#newRegisterFromInputStream(java.io.InputStream, java.lang.String)
	 * defining this method via Interface enables us to pass it through to the register configuration
	 */
	@Override
	public abstract CSVRegister<K, V> newRegisterFromInputStream(InputStream inIS, String codeSystem) throws IOException;

//	// ask concrete sub-class for target annotation type (String), e.g.: "com.ibm.mla.diagnosis.Diagnosis"
//	protected abstract Class<? extends Annotation> getTargetAnnotationClass();
//
//	// delegate transformation from linguistic anno to register key to concrete class
//	protected abstract K targetAnnotationToRegisterKey(Annotation targetAnno);


	/**
	 * Initializes the Object by getting the passed parameters from the UIMA
	 * context.</br>
	 * This method is invoked only ONCE before the process() method is invoked
	 * the first time.</br>
	 * RESPONSIBILITIES OF subclass:<ol>
	 * <li>invoke setContextLogging(myContext)
	 * before invoking call "super.initialize(myContext);"
	 * do its own init.</li></ol>
	 * </br>performs the following init steps:<ol>
	 * <li>init IO</li></ol>
	 * @param myContext UIMA context from the invoker.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(UimaContext myContext) throws ResourceInitializationException {
		super.initialize(myContext);
		// do we have an XML config file?
		String registerConfigFile = (String) myContext.getConfigParameterValue(REG_GROUP, REG_CONFIG);
		if (registerConfigFile == null || registerConfigFile.trim().isEmpty()) {
			// no config file, use simpler configuration via list of names + optional code systems
			String[] registerNames = (String[]) myContext.getConfigParameterValue(REG_GROUP, REG_NAMES);
			boolean useCodeSystem = (Boolean) myContext.getConfigParameterValue(REG_GROUP, REG_CODESYSTEMS);
			logger.info("using old configuration method: names = {}; use code systems = {}", registerNames, useCodeSystem);
			myRegisters = new GroupedRegister<K,V>(Arrays.asList(registerNames));
			myRegisters.initialize(myContext, this, useCodeSystem ? null : "");
		} else {
			// use XML config
			logger.info("using configuration from file: \"{}\"", registerConfigFile);
			try {
				InputStream xmlIS = myContext.getResourceAsStream(registerConfigFile);
				if (xmlIS == null) {
					throw new ResourceInitializationException(
							new Throwable("Cannot open config file " + registerConfigFile + " for reading"));
				}
				Reader xmlReader = new InputStreamReader(xmlIS);
				myRegisters = JAXB.unmarshal(xmlReader, RegisterSequence.class);
				myRegisters.initialize(myContext, this, null);
			} catch (ResourceAccessException e) {
				throw new ResourceInitializationException(e);
			}
		}
	}

/*
 * The abstract mapper no longer has a process() method!
 */

	/**
	 * map a register key to matching encodings
	 * called by the concrete sub-class
	 * @param registerKey
	 * @return the best-fitting sub-set of all possible encodings, including other / unspecific logic
	 */
	protected final RegisterValueSet<V> mapToRegisterValueSet(K registerKey) {
		logger.trace("matching: {}", registerKey);
		RegisterValueSet<V> allCodings = myRegisters.getMatchingEncodings(registerKey);
		logger.trace("found: {}", allCodings);
		for (int j = 0; j < 4; ++j) {
			logger.trace("matching cycle {}", j);
			boolean isOtherSpecific = codes[j][0];
			boolean isUnspecific = codes[j][1];
			RegisterValueSet<V> encodings = allCodings.getSubSet(isOtherSpecific, isUnspecific);
			if (!encodings.isEmpty()) {
				return encodings;
			}
		} // end for 0..3

		// return the set of all codings (which must be empty, if no combination matched)
		return allCodings;
	}

	/**
	 * convenience method to create the string of encodings from the match key in one call
	 * @param registerKey
	 * @return the list of best-matching encodings, separated by bar characters
	 */
	protected final String mapToString(K registerKey) {
		RegisterValueSet<V> encodings = mapToRegisterValueSet(registerKey);
		// list to store codes found
		List<String> tmp_codelist = new ArrayList<>();
		// for each register value found, concat code list
		for (RegisterValue<?> regVal : encodings) {
			String codeSystem = regVal.getCodeSystem();
			String tmp = (codeSystem == null || codeSystem.isEmpty()) ? "" : (codeSystem + ":");
			tmp_codelist.add(tmp + regVal.getCode());
		}
		// assemble output separated by a pipe
		return StringUtils.join(tmp_codelist, "|");
	}
}