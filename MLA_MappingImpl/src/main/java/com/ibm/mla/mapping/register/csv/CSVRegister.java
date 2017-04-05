package com.ibm.mla.mapping.register.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.uima.jcas.tcas.Annotation;

import com.ibm.mla.mapping.register.Register;
import com.ibm.mla.mapping.register.RegisterKey;
import com.ibm.mla.mapping.register.RegisterValue;
import com.ibm.mla.mapping.register.RegisterValueSet;

public abstract class CSVRegister<K extends RegisterKey<K>, V extends RegisterValue<V>> extends Register<K, V> {
	private File outputFile;
	/**
	 * @param inIS : the source from which to read the register, as a Stream so it can be provided by the class loader
	 * @param outputFile : the output file to which to write a new version on flush() or shutdown()
	 * @throws IOException
	 *
	 */
	public CSVRegister(InputStream inIS, File outputFile) throws IOException {
		if (inIS != null) {
			readInRegister(inIS, null);
		}
		this.outputFile = outputFile;
	}
	/**
	 * @param inIS : the source from which to read the register, as a Stream so it can be provided by the class loader
	 * @param outputFile : the output file to which to write a new version on flush() or shutdown()
	 * @throws IOException
	 *
	 */
	public CSVRegister(InputStream inIS, String codeSystem, File outputFile) throws IOException {
		if (inIS != null) {
			readInRegister(inIS, codeSystem);
		}
		this.outputFile = outputFile;
	}
	protected boolean dirty = false;
	// Due to Java type erasure, we cannot simply create a new a K(s) or V(s) when reading in the register,
	// so any subclass must provide factory methods to create keys and values from strings
	// What a kludge!
	protected abstract K newKeyInstance(String s);
	protected abstract V newValueInstance(String s);

// not really needed - register file can be read in constructor
//
//	public void startup(InputStream inIS) throws IOException {
//		if (inIS == null) {
//			logger.log(Level.WARNING, "starting with empty register");
//			return;
//		}
//		readInRegister(inIS);
//	}

	@Deprecated
	public abstract RegisterValueSet<V> getMatchingEncodings(Annotation a);

	/*
	 * Note: getResourceAsStream() uses the Java class loader to locate a resource.
	 * The class loader will look for the resource in all locations of the class path.
	 * If this class was loaded from a jar file, the resource must be packed into the same jar.
	 * If the program is run within the Eclipse IDE, the resource path must be added to the class path of the run configuration
	 * Can be configured via:
	 *  Run -->
	 *  	Run configurations...
	 *  		(select config name) -->
	 *  			 Class path -->
	 *  				user entries -->
	 *  					Advanced -->
	 *  						Add Folder -->
	 *  							(select the path containing the resource)
	 */


	public void readInRegister(InputStream inIS, String codeSystem) throws IOException {
		try {
			Reader in =
					new BufferedReader(
							new InputStreamReader(
									inIS,
									Charset.forName(CSVRegisterConstants.REGISTERCHARSET)
									)
							);
			CSVParser inparser = CSVFormat.DEFAULT
					.withDelimiter(CSVRegisterConstants.OUTDIAGKVSEP)
					.withQuote(CSVRegisterConstants.OUTDIAGQUOTE)
					.withSkipHeaderRecord(false)
					.withIgnoreEmptyLines(true)
					.parse(in);
			for (CSVRecord record : inparser) {
				logger.trace("line: [0]={}; [1]={}", record.get(0), record.get(1));
				// each record contains one entry
				K k = newKeyInstance(record.get(0));   // k = new K(record.get(0)) would be nicer
				V v = newValueInstance(record.get(1)); // same for v
				// add existing record to register, but do not set "dirty" flag
				if (codeSystem != null) {
					v.setCodeSystem(codeSystem);
				}
				registerInternally(k,v);
			}
			inparser.close();
		} catch (java.io.FileNotFoundException e) {
			logger.error("error reading input stream", e);
		}
	}
	/**
	 * @author Georg Temnitschka
	 * @return true if the register did not already contain the new (k,v) entry
	 * set the "dirty" flag if this is a new entry
	 */
	public boolean register(K k, V v) {

		boolean isNew = registerInternally(k, v);
		dirty |= isNew;
		return isNew;
	}

	public void flush() throws IOException {
		if (dirty) {
			writeOutRegister();
			dirty = false;
		}
	}

	/**
	 * @deprecated
	 * @throws IOException
	 */
	@Deprecated
	public void shutdown() throws IOException {
		flush();
	}

	protected void writeOutRegister() throws IOException {
		if (outputFile == null) {
			logger.warn("Called for output file set to NULL");
		} else {

			Writer out = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(outputFile),
							Charset.forName(CSVRegisterConstants.REGISTERCHARSET)
							)
					);

			for (Set<KeyValueBox> kvSet : myRegisterMap.values()) {
				for (KeyValueBox kvb : kvSet) {
					K k = kvb.getKey();
					V v = kvb.getValue();
					//					logger.log(Level.FINEST,"before k.toString");
					//					logger.log(Level.FINEST,"key: " + k.toString());
					//					logger.log(Level.FINEST,"before v.toString");
					//					logger.log(Level.FINEST,"value: " + v.toString());
					//					logger.log(Level.FINEST,"after v.toString");
					out.write(k.toString());
					out.write(CSVRegisterConstants.OUTDIAGKVSEP);
					out.write(v.toString());
					out.write(CSVRegisterConstants.OUTDIAGLINESEP);
				}
			}
			out.close();
		}
	}
}
