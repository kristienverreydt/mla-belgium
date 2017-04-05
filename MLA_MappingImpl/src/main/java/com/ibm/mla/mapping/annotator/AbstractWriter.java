package com.ibm.mla.mapping.annotator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.slf4j.Logger;

import uima.tt.ParagraphAnnotation;

import com.ibm.mla.mapping.Code;
import com.ibm.mla.mapping.register.csv.CSVRegisterConstants;
/**
 * Acts as a abstract super type for all writers.
 *
 * @param <A> NLP Diagnosis or Procedure; generated from a type system by the JCasGen tool
 *
 * @author georg.temnitschk@at.ibm.com
 * @author philip.sheikh@at.ibm.com
 */
public abstract class AbstractWriter<A extends Annotation>
									 extends JCasAnnotator_ImplBase {

	/** get the java.util logger for our class */
	private Logger logger ;
	protected abstract Logger getSubLogger();

	/** encoding for all IO is UTF-8 */
	protected final String ENCODING = CSVRegisterConstants.REGISTERCHARSET;
	/** file the correctly processed paragraphSpans are written to*/
	protected final File unprocessed_file = new File("/test", "unprocessed.csv");
	/** file the incorrectly processed paragraphSpans are written to*/
	protected final File processed_file = new File("/test", "processed.csv");

	/** UIMA type of the type parameter A */
	protected Type annotationType;
	/** list holding all ParagraphSpans; used to pass values to sub class */
	protected List<ParagraphSpan<Code, A>> paraSpanList;

	// ################ METHODS INHERTITED FROM SUPER CLASS ################
	/**
	 * Initializes the Object by getting the passed parameters from the UIMA
	 * context.</br> Is done only ONCE before the process() method is invoked
	 * the first time.</br> Any subclass should call "super.initialize(myContext);"
	 * before doing its own init.</br></br>performs the following init steps:
	 * </br><ul><li>init IO</li>
	 * </ul>
	 * @param myContext UIMA context from the invoker.
	 */
	@Override
	public void initialize(UimaContext myContext) throws ResourceInitializationException {
		logger = getSubLogger(); // ask sub-class for logger
		super.initialize(myContext);
		// try if we can write to the (un)processed files else throw exception
		initFiles(processed_file, unprocessed_file);
	}

	/**
	 * May be called <b>one or more</b> times after the initialize() method
	 * by the UIMA pipeline.</br>
	 */
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		logger.info("process() in abstract class invoked.");

		try {

			// get types via reflection from generic params.
			annotationType = GenericTypeHelper.getAnnotationType(this, jcas, 0);
			logger.info("Type of gen[0] = {}", annotationType.getName());

			// write all annotations of type <A> to map and list as PS
			paraSpanList = new ArrayList<>();
			Map<Integer, ParagraphSpan<Code, A>> paraSpanMap = new HashMap<>();
			addParagraphSpansToListAndMap(jcas, paraSpanList, paraSpanMap);
			logger.debug("no. of elems in list: {}", paraSpanList.size());

			// collect codes and annotations and add if suitable to the
			// corresponding ParagraphSpans stored in the map.
			collectMatchingCodes(jcas, paraSpanMap);
			collectMatchingAnnotations(jcas, annotationType, paraSpanMap);

			writeResultsToFiles(paraSpanList, processed_file, unprocessed_file);

		} catch (Exception e) {
			logger.error("Processing error in AbstractWriter", e);
			throw new AnalysisEngineProcessException(e);
		}

		logger.info("process() in AbstractWriter finished");
	}


	// ################ METHODS TO COLLECT ANNOTATIONS AND CODES ##############
	/**
	 * returns a list of all Annotations of a given Type.
	 * @param t a UIMA Annotation Type
	 * @param aJCas a valid JCas Object (!= null)
	 * @return a list containing the Annotations.
	 */
	@SuppressWarnings("unchecked")
	public List<A> getAllAnnotationsByType(JCas aJCas, Type t) {
		List<A> result = new ArrayList<A>();
		// Collect all paragraphs, store in List
		for (Annotation currentAnnotation : aJCas.getAnnotationIndex(annotationType)) {
			A paragraph = (A) currentAnnotation; // cast to A
			result.add(paragraph); // add to list
		}
		return result;
	}

	/**
	 * Collect Annotations of Type Catalog<<Concept>>, store in containing spans.
	 * codes are assumed to start at the first position of a sentence span!
	 * Assigns the PS the annotation with the span that has the latest beginning.
	 * @param jcas must not be null
	 * @param t
	 * @param paraSpanMap
	 */
	public void collectMatchingAnnotations(JCas jcas, Type t,
			Map<Integer, ParagraphSpan<Code, A>> paraSpanMap) {

		logger.debug("in collectMatchingAnnotations");

		// save all annos in hash map for fast lookup
		Map<Integer, A> annoLookup = new HashMap<>();
		addAnnotationsToMap(jcas, annoLookup);

		// for each PS look for Annotations that seem valid and add to PS
		// decision: this code has a (small) n**2 part,
		// but is much more readable than nested iterators over different anno's
		// ==> stick with this implementation ( ~5' for 70.000 lines is good enough)
		for (ParagraphSpan<Code, A> currentPS : paraSpanMap.values()) { // loop 1
			if (currentPS.getCode() != null && currentPS.getAnnotation() == null) {
				// for each position in the document that overlaps the PS check for
				// start of annotation in hash map
				for (int i=currentPS.getCode().getEnd(); i<= currentPS.getEnd(); i++) {
					if (annoLookup.containsKey(i)) {
						currentPS.setAnnotation(annoLookup.get(i));
						break;
					}
				}
			}
		}
	}

	// same functionality as above but slower
	//			for(Annotation fs : jcas.getAnnotationIndex(t)) { // loop 2
	//				A anno = (A) fs;
	//				if (currentPS.getCode() != null && currentPS.getAnnotation() == null) {
	//					if (anno.getBegin() >= currentPS.getCode().getEnd() && anno.getEnd() <= currentPS.getEnd()) {
	//						logger.warning(currentPS.getAnnotation() + "/ " + currentPS.toString());
	//						currentPS.setAnnotation(anno);
	//						break;
	//					}
	//				}
	//			}
	//} // end loop 1
	//	logger.info("out collectMatchingAnnotations");
	//}

	/**
	 * Collect Annotations of Type A, store in containing spans.
	 * codes are assumed to start at the first position of a sentence span!
	 * @param aJCas must not be null
	 */
	public void collectMatchingCodes(JCas jcas, Map<Integer, ParagraphSpan<Code, A>> map) {

		for (Annotation currentAnnno : jcas.getAnnotationIndex(Code.type)) {
			Code currentAnnotation = (Code) currentAnnno;
			logger.debug("Code processed: {}", currentAnnotation);
			if (map.containsKey(currentAnnotation.getBegin())) {
				ParagraphSpan<Code, A> currentPS = map.get(currentAnnotation.getBegin());
				currentPS.setCode(currentAnnotation);
			}
		}
	}

	/**
	 * For all ParagraphAnnotations write the ParagraphSpans into a List and HashMap.</br>
	 * The list simply contains all the annotations found.</br>
	 * The HashMap's key is the begin of the span of the found which is stored in the value.</br>
	 * The type of the annotations is determined by {@link AbstractWriter#annotationType}
	 * @param list
	 * @param map
	 * @param aJCas must not be null
	 */
	public void addParagraphSpansToListAndMap(JCas aJCas,
											  List<ParagraphSpan<Code, A>> list,
											  Map<Integer, ParagraphSpan<Code, A>> map) {

		// iterate over all annotations of type uima.tt.ParagraphAnnotation.type
		for (Annotation currentAnno : aJCas.getAnnotationIndex(uima.tt.ParagraphAnnotation.type)) {
			ParagraphAnnotation paragraph = (ParagraphAnnotation) currentAnno; // cast
			ParagraphSpan<Code, A> newParaSpan = new ParagraphSpan<Code, A>(paragraph);
			// put begin of span as key and object as value
			map.put(paragraph.getBegin(), newParaSpan);
			list.add(newParaSpan);
			logger.debug("added to list: {} value in list", newParaSpan);
			logger.debug("added to map: {}", newParaSpan);
		}
	}

	/**
	 * Add all Annotations to a Hash map where the key is the begin and the
	 * value is the Annotation. If two annotations have the same begin index
	 * choose the one with the longer span.
	 * @param map
	 * @param aJCas must not be null
	 */
	public void addAnnotationsToMap(JCas aJCas, Map<Integer, A> map) {
		// iterate over all annotations of type uima.tt.ParagraphAnnotation.type
		for (Annotation currentAnno : aJCas.getAnnotationIndex(annotationType)) {
			@SuppressWarnings("unchecked")
			A anno = (A) currentAnno; // cast

			// prefer anno with longer span
			if (map.containsKey(anno.getBegin())) {
				if (map.get(anno.getBegin()).getEnd() < anno.getEnd()) {
					map.put(anno.getBegin(), anno);
				}
			} else {
				// put begin of span as key and object as value
				map.put(anno.getBegin(), anno);
			}

		}
	}

	// ################ METHODS FOR I/O ################
	/**
	 * write a pair of a code and Annotation to a register.
	 * @param code
	 * @param annotation
	 * @return
	 * @throws IOException
	 * @throws AnalysisEngineProcessException
	 */
	protected abstract boolean writeToRegister(Code code, A annotation) throws IOException, AnalysisEngineProcessException;

	/**
	 * Writes the contents of a List&lt;ParagraphSpan&gt; to either the processed_file
	 * if we have a match. Otherwise it is written to the unprocessed_file.
	 * @param paraSpanList
	 * @param processed_file file to write a list of processed paragraph spans.
	 * @param unprocessed_file
	 * @throws AnalysisEngineProcessException
	 */
	public void writeResultsToFiles(List<ParagraphSpan<Code, A>> paraSpanList,
									   File processed_file, File unprocessed_file) throws AnalysisEngineProcessException {

		// init string buffers to store output
		StringBuffer sb_processed = new StringBuffer("");
		StringBuffer sb_unprocessed = new StringBuffer("");
		logger.debug("in writeResultsToFilesNew");

		// collect findings ( either processed or unprocessed SB )
		for (ParagraphSpan<Code, A> paraSpan : paraSpanList) {

			if (paraSpan.checkPara()) { // PS is OK
				sb_processed.append(paraSpan.getCoveredText());
				logger.debug("wrote to processed {}", paraSpan.getCoveredText());
			} else { // PS is NOT OK
				sb_unprocessed.append(paraSpan.getCoveredText());
				logger.debug("wrote to unprocessed {}", paraSpan.getCoveredText());
			}
		}

		// write string buffers to file
		try (OutputStream out = new FileOutputStream(processed_file, true)) {
			IOUtils.write(sb_processed.toString(), out, ENCODING);
		} catch (IOException e) {
			logger.error("COULD NOT WRITE PROCESSED FILE", e);
			throw new AnalysisEngineProcessException(e);
		}
		try (OutputStream out = new FileOutputStream(unprocessed_file, true)) {
			IOUtils.write(sb_unprocessed.toString(), out, ENCODING);
		} catch (IOException e) {
			logger.error("COULD NOT WRITE UNPROCESSED FILE", e);
			throw new AnalysisEngineProcessException(e);
		}

		logger.info("out writeResultsToFilesNew");
	}

	/**
	 * Checks if the given file is writable.
	 * If not an Exception is thrown. If a file with the same
	 * name already exists override it with a blank file. If
	 * the parent dir does not exist create it.
	 * @param fils Files to be initialized
	 * @throws ResourceInitializationException
	 */
	private void initFiles(File... files) throws ResourceInitializationException {
		for (File currentFile : files) { // for each file in list
			try {
				FileUtils.writeStringToFile(currentFile, "", ENCODING);
			} catch (Exception e) { // file not writable --> throw error
				logger.error("COULD NOT CREATE FILE.", e);
				throw new ResourceInitializationException();
			}
		}
	}


}