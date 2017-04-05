/**
 * 
 */
package com.ibm.mla.mapping.annotator;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.slf4j.LoggerFactory;

import uima.tt.ParagraphAnnotation;

import com.ibm.mla.commonmedicalresources.CatalogGeneralizingTerm;
import com.ibm.mla.mapping.CatalogLine;
import com.ibm.mla.mapping.Code;


/**
 * Parse a catalog source text into its constituents
 * Every line must be a separate paragraph (defined by break rule)
 * Code and text must be separated by a TAB character
 * 
 * Structure of an input line:
 * Code <TAB> (generalizingTerm)? Concept (generalizingTerm)?
 * 
 * @author Georg Temnitschka
 *
 */
public class GenericCatalogParser extends JCasAnnotator_ImplBase {

	private static final String TYPELIST = "TYPELIST";
	// a strict sub-iterator will not include annotations which overlap to the right
	private static final String STATE_NEW = "new";
	private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	private String[] typeList = null;


	@Override
	public void initialize(UimaContext myContext)
			throws ResourceInitializationException {
		try {
			super.initialize(myContext);

			// read parameters
			typeList = (String[]) myContext.getConfigParameterValue(TYPELIST);
			if (typeList == null || typeList.length == 0)
				throw new ResourceInitializationException(new Throwable("Parameter \"" + TYPELIST + "\" not defined or empty"));
			// to bad we can only check the validity of the types at process() time...
		} catch (Exception e) {
			logger.error("Initialization error in generic catalog parser", e);
			throw new ResourceInitializationException(e);
		}
	}


	/**
	 * 
	 */
	@Override
	public void process(JCas myCas) throws AnalysisEngineProcessException {
		TreeMap<Integer,CatalogLine> linesByBegin = new TreeMap<Integer,CatalogLine>();
		logger.info("Generic catalog parser: start processing");

// --- create catalog lines, populate internal index (linesByBegin) ------------------------------------------
		try {
			AnnotationIndex<ParagraphAnnotation> paragraphIndex = myCas.getAnnotationIndex(ParagraphAnnotation.class);
			for (ParagraphAnnotation paragraph : paragraphIndex) {
				CatalogLine catalogLine = new CatalogLine(myCas, paragraph.getBegin(), paragraph.getEnd());
				catalogLine.setProcessingState(STATE_NEW);
				catalogLine.setIsOtherSpecific(false);
				catalogLine.setIsUnspecific(false);
				catalogLine.addToIndexes();
				Integer pos = catalogLine.getBegin();
				linesByBegin.put(pos, catalogLine);
			}
		} catch (Exception e) {
			logger.error("Processing error creating catalog lines", e);
			throw new AnalysisEngineProcessException(e);
		}
		logger.info("Generic catalog parser: catalog lines created");


// --- add codes to the catalog lines ------------------------------------------------------------------------
		try {
			addCodesToCatalogLines(myCas, linesByBegin);
		} catch (Exception e) {
			logger.error("Processing error assigning codes", e);
			throw new AnalysisEngineProcessException(e);
		}
		logger.info("Generic catalog parser: codes assigned");

// --- add generalizing terms to the catalog lines ----------------------------------------------------------
		try {
			addGeneralizingTermsToCatalogLines(myCas, linesByBegin);
		} catch (Exception e) {
			logger.error("Processing error assigning generalizing terms", e);
			throw new AnalysisEngineProcessException(e);
		}
		logger.info("Generic catalog parser: generalizing terms assigned");

// --- add concepts to catalog lines; always select the concept with the longest span ------------------------
		int matchedConcepts = 0;
		try {
			matchedConcepts = addConceptsToCatalogLines(myCas, linesByBegin);
		} catch (Exception e) {
			logger.error("Processing error assigning concepts", e);
			throw new AnalysisEngineProcessException(e);
		}
		if (matchedConcepts == 0) {
			String message = "Error: no single concept matched";
			logger.error(message);
			throw new AnalysisEngineProcessException(new Throwable(message));
		}
		logger.info("Generic catalog parser: concepts assigned");

// --- check that catalog lines have a code and a concept and that no additional text exists anywhere in the line
		try {
			AnnotationIndex<CatalogLine> lineIndex = myCas.getAnnotationIndex(CatalogLine.class);
			for (CatalogLine catalogLine: lineIndex) {
				if (catalogLine.getConcept() == null) {
					String message = "Error: no concept in paragraph";
					catalogLine.setProcessingState(message);
				} else if (catalogLine.getCode() == null) {
					String message = "Error: no code in paragraph";
					catalogLine.setProcessingState(message);
				} else {
					checkAlignment(catalogLine);
					
				}
			}
		} catch (Exception e) {
			logger.error("Processing error during completeness check", e);
			throw new AnalysisEngineProcessException(e);
		}
		logger.info("Generic catalog parser: completeness check done, finished");
	}


	private int addConceptsToCatalogLines(JCas myCas, TreeMap<Integer, CatalogLine> linesByBegin)
			throws CASRuntimeException {
		int matchedConcepts = 0;
		typeListLoop:
		for (String typeName: typeList) {
			Type type = myCas.getTypeSystem().getType(typeName);
			if (type == null) {
				logger.warn("Type {} not in type system", typeName);
				continue typeListLoop;
			}
			AnnotationIndex<Annotation> annoIndex = myCas.getAnnotationIndex(type);
			for (Annotation anno: annoIndex) {
				// locate the catalog line for this concept
				Integer pos = anno.getBegin();
				Entry<Integer, CatalogLine> catalogEntry = linesByBegin.floorEntry(pos);
				if (catalogEntry == null) {
					logger.error("Error: no catalog line for concept {}", anno.getCoveredText());
				} else {
					// check if term fully within span of catalog line, update booleans
					CatalogLine catalogLine = catalogEntry.getValue();
					if (catalogLine.getEnd() >= anno.getEnd()) {
						Annotation oldConcept = catalogLine.getConcept();
						if (oldConcept == null) {
							++matchedConcepts;
							catalogLine.setConcept(anno);
						} else {
							int oldSpan = oldConcept.getEnd() - oldConcept.getBegin();
							int newSpan = anno.getEnd() - anno.getBegin();
							if (newSpan > oldSpan) {
								catalogLine.setConcept(anno);
							}
						}
					} else {
						logger.error("Concept {} extends beyond catalog line {}",
								anno.getCoveredText(), catalogLine.getCoveredText());
					}
				}
			}
		}
		return matchedConcepts;
	}


	private void addGeneralizingTermsToCatalogLines(JCas myCas,
			TreeMap<Integer, CatalogLine> linesByBegin)
			throws CASRuntimeException {
		AnnotationIndex<CatalogGeneralizingTerm> genTermIndex = myCas.getAnnotationIndex(CatalogGeneralizingTerm.class);
		for (CatalogGeneralizingTerm genTerm: genTermIndex) {
			// locate the catalog line for this generalizing term
			Integer pos = genTerm.getBegin();
			Entry<Integer, CatalogLine> catalogEntry = linesByBegin.floorEntry(pos);
			if (catalogEntry == null) {
				logger.error("Error: no catalog line for code {}", genTerm.getCoveredText());
			} else {
				// check if term fully within span of catalog line, update booleans
				CatalogLine catalogLine = catalogEntry.getValue();
				if (catalogLine.getEnd() >= genTerm.getEnd()) {
					catalogLine.setIsOtherSpecific(catalogLine.getIsOtherSpecific() || genTerm.getIsOtherSpecific());
					catalogLine.setIsUnspecific(catalogLine.getIsUnspecific() || genTerm.getIsUnspecific());
					// add the new generalizing Term to the list
					catalogLine.addGeneralizingTerm(genTerm);
				} else {
					logger.error("Generalizing term {} extends beyond catalog line {}",
							genTerm.getCoveredText(), catalogLine.getCoveredText());
				}
			}
		}
	}


	private void addCodesToCatalogLines(JCas myCas,
			TreeMap<Integer, CatalogLine> linesByBegin)
			throws CASRuntimeException {
		AnnotationIndex<Code> codeIndex = myCas.getAnnotationIndex(Code.class);
		for (Code code: codeIndex) {
			Integer pos = code.getBegin();
			Entry<Integer, CatalogLine> catalogEntry = linesByBegin.floorEntry(pos);
			if (catalogEntry == null) {
				logger.error("Error: no catalog line for code {}", code.getCoveredText());
			} else {
				CatalogLine catalogLine = catalogEntry.getValue();
				if (catalogLine.getEnd() >= code.getEnd()) {
					if (catalogLine.getCode() == null) {
						catalogLine.setCode(code);
					} else {
						String message = "Error: duplicate codes "
								+ catalogLine.getCode() + " and " + code
								+ " for catalog line " + catalogLine.getCoveredText();
						catalogLine.setProcessingState(message);
					}
				} else {
					logger.error("Code {} extends beyond catalog line {}", code.getCoveredText(), catalogLine.getCoveredText());
				}
			}
		}
	}
	
	private boolean checkAlignment(CatalogLine catalogLine) {
		// check that all content within the line aligns without any additional text
		TreeMap<Integer, Annotation> allAnnoMap = new TreeMap<Integer, Annotation>();
		allAnnoMap.put(catalogLine.getCode().getBegin(), catalogLine.getCode());
		allAnnoMap.put(catalogLine.getConcept().getBegin(), catalogLine.getConcept());
		for (CatalogGeneralizingTerm genTerm: catalogLine.getGeneralizingTerms()) {
			allAnnoMap.put(genTerm.getBegin(), genTerm);
		}
		int paraBegin = catalogLine.getBegin();
		String paraText = catalogLine.getCoveredText();
		int offset = 0;
		String oldAnnoText = "";
		for (Integer pos : allAnnoMap.keySet()) { // iterate in ascending key (= begin) order
			int newOffset = pos - paraBegin;
			Annotation anno = allAnnoMap.get(pos);
			String newAnnoText = anno.getCoveredText().trim();
			//					logger.fine("at pos="+pos+": anno="+anno.getCoveredText());
			if (newOffset < offset) {
				String message = "Error: annotations \"" + oldAnnoText + "\" and \"" + newAnnoText + "\" overlap @offset: " + newOffset;
				catalogLine.setProcessingState(message);
				return false;
			} 
			
			if (newOffset > offset) {
				String interstitial = paraText.substring(offset, newOffset).trim();
				if (!interstitial.isEmpty()) {
					String message = "Error: non-empty text \""
							+ interstitial + "\" before \"" + anno.getCoveredText().trim();
					//								logger.fine(message);
					catalogLine.setProcessingState(message);
					return false;
				}
			}
			offset = anno.getEnd() - paraBegin;
			oldAnnoText = newAnnoText;
		}

		if (offset > paraText.length()) {
			catalogLine.setProcessingState("Error: last annotation extends beyond end of paragraph");
			return false;
		}

		if (offset < paraText.length()) {
			String interstitial = paraText.substring(offset).trim();
			if (!interstitial.isEmpty()) {
				String message = "Error: non-empty text \"" + interstitial + "\" at end of paragraph";
				//						logger.fine(message);
				catalogLine.setProcessingState(message);
				return false;
			}

		}
		return true;
	}
}
