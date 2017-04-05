package com.ibm.mla.mapping.annotator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.mla.mapping.CatalogLine;

/**
 * 
 * @author Georg Temnitschka
 */
public class GenericCatalogCleanup extends JCasAnnotator_ImplBase {

	/** get logger variable for this object */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String REGISTEROUTFOLDER = "/test";
	private static final String UNPROCESSEDFILE = "unprocessed.txt";
	private static final String DONE = "done";

	PrintWriter unprocessedTxt = null;
	
	@Override
	public void initialize(UimaContext myContext) throws ResourceInitializationException {
		super.initialize(myContext);

		try {
			File unprocessedFile = new File(REGISTEROUTFOLDER, UNPROCESSEDFILE);
			unprocessedTxt = new PrintWriter(unprocessedFile);
		} catch (IOException e) {
			logger.error("Cannot create file for unprocessed records", e);
			throw new ResourceInitializationException(e);
		} 
		logger.info("initialized ok");
	}
	
	@Override
	public void process (JCas jcas) throws AnalysisEngineProcessException {
		logger.info("Generic catalog cleanup: start processing");

		List<CatalogLine> linesToRemove = new ArrayList<CatalogLine>();

		try {
			AnnotationIndex<CatalogLine> catalogLineIndex = jcas.getAnnotationIndex(CatalogLine.class);
			for (CatalogLine catalogLine: catalogLineIndex) {
				if (catalogLine.getProcessingState().equals(DONE)) {
					linesToRemove.add(catalogLine);
				} else {
					unprocessedTxt.print(catalogLine.getCoveredText());
				}
			}
// remove all catalog line annotations which were processed correctly
			for (CatalogLine catalogLine: linesToRemove) {
				catalogLine.removeFromIndexes();
			}
			
		} catch (Exception e) { 
			logger.error("Processing error in generic catalog cleanup", e);
			throw new AnalysisEngineProcessException(e);

		} finally {
			unprocessedTxt.close();
		}
		logger.info("Generic catalog cleanup: processing finished");
	}
}
