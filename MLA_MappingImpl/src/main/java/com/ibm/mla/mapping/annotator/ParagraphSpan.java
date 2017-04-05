package com.ibm.mla.mapping.annotator;

import java.util.Arrays;

import org.apache.uima.jcas.tcas.Annotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class ParagraphSpan is used to collect the begin, end, and content of all
 * lines in the input file. the break rules must define line breaks as paragraph
 * breaks.</br>
 * Similar to an Annotation the ParagraphSpan contains the begin, end of span
 * and the covered text. Furthermore it can contain an Annotation (Type: Annotation)
 * and a Code (Type: Annotation)</br>
 * Old versions of the ParagraphSpan did not use Generic type arguments. The reason for
 * using the ParagraphSpan instead of a custom Annotation subclass is that it does not
 * require a Jcas for creation.</br>
 *
 * @author georg.temnitschk@at.ibm.com
 * @author philip.sheikh@at.ibm.com
 *
 * @see com.ibm.mla.mapping.annotator.AbstractWriter
 * @see com.ibm.mla.mapping.annotator.AbstractMapper
 *
 * @param <C> Code e.g: MEL/OPS or ICD10
 * @param <A> Annotation e.g: Procedure or Diagnosis
 */
public class ParagraphSpan<C extends Annotation, A extends Annotation> {

	private static Logger logger = LoggerFactory.getLogger(ParagraphSpan.class);

	/**begin of span */
	private int begin;
	/** end of span */
	private int end;
	/** text covered between begin and end */
	private String coveredText;
	/** code stored with annotation */
	private C code;
	/** diagnosis or procedure */
	private A annotation;

	// ########## Constructors ##########
	/** empty Constructor */
	public ParagraphSpan() {}

	/**
	 * Constructor
	 * @param begin of span
	 * @param end of span
	 * @param coveredText text between end and begin
	 */
	public ParagraphSpan(int begin, int end, String coveredText) {
		this.setSpan(begin, end);
		this.setCoveredText(coveredText);
	}

	/** Constructor mapping from Annotation. */
	public ParagraphSpan(Annotation a) {
		this.setSpan(a.getBegin(), a.getEnd());
		this.setCoveredText(a.getCoveredText());
	}

	/** Copy Constructor. */
	public ParagraphSpan(ParagraphSpan<C, A> other) {
		this(other.getBegin(), other.getEnd(), other.getCoveredText());
		this.code = other.getCode();
		this.annotation = other.getAnnotation();
	}

	// ########## Getter & Setter ##########
	/** Get the text covered by an annotation as a string. */
	public String getCoveredText() {
		return coveredText;
	}

	/** Get the text covered by an annotation as a string. */
	public void setCoveredText(String coveredText) {
		this.coveredText = coveredText;
	}

	/** set the begin of the span */
	public void setBegin(int begin) { this.begin = begin; }

	/** set the end of the span */
	public void setEnd(int end) { this.end = end; }

	/** set begin and end at once */
	public void setSpan(int begin, int end) {
		this.begin = begin;
		this.end = end;
	}

	/** get begin and end at once */
	public int[] getSpan() {
		return new int[]{begin, end};
	}

	/** get the begin of the span */
	public int getBegin() { return this.begin; }

	/** get the end of the span */
	public int getEnd() { return this.end; }

	public C getCode() {
		return this.code;
	}

	/**
	 * set the code (e.g: MEL or OPS)
	 * @param code
	 */
	public void setCode(C code) {
		this.code = code;
	}

	public A getAnnotation() {
		return this.annotation;
	}

	/**
	 * set the annotation (e.g: Diagnosis)
	 * @param annotation
	 */
	public void setAnnotation(A annotation) {
		this.annotation = annotation;
	}

	// ########## Helper Methods ##########
	/**
	 * Logic for detecting what is a valid Annotation (Procedure, Diagnosis)</br>
	 * or Code (MEL, OPS) is.</br>
	 *
	 * <b>BNF Description:</b></br>
	 * Valid:</br>
	 * ParagraphSpan ::= Code [whitespace] Annotation [whitespace]</br>
	 * Invalid:</br>
	 * ParagraphSpan ::= Code text Annotation text
	 *
	 * @return true if valid
	 */
	public boolean checkPara() {
		C code = getCode(); // get the stored code annotation
		A diag = getAnnotation();

		// code is mandatory so false if null
		if (code != null) {
			// if code starts after PS OR ends after PS --> false
			if (code.getBegin() != getBegin() || code.getEnd() > this.getEnd()) {
				return false;
			}
		} else {
			return false;
		}

		// diag Annotation is optional
		if (diag != null) {
			// annotation starts after code or ends after code
			if (code.getEnd() > diag.getBegin() || diag.getEnd() > getEnd()) {
				return false;
			}
			// code and annotation overlap
			if (code.getEnd() < diag.getBegin()) {
				// get string between code and annotation
				String between = getCoveredText().substring(code.getEnd()-getBegin(), diag.getBegin()-getBegin());
				//string between is not whitespace only --> false
				if (!between.trim().isEmpty()) {
					return false;
				}
			}
			// diag ends before PS ends
			if (diag.getEnd() < getEnd()) {
				// get string between code and annotation
				String between = getCoveredText().substring(diag.getEnd()-getBegin());
				// if string at the end is other than Ws --> false
				if (!between.trim().isEmpty()) {
					return false;
				}
			}
		} else { // annotation is null
			if (code.getEnd() < getEnd()) { // code ends before ps
				// is the text bteween the code end and the ps end blank?

				String between = getCoveredText().substring(code.getBegin()-getBegin());
				logger.trace(">>>{}", between);
				if (!between.trim().equals("")) {
					return false;
				}
			}
		}
		return true;
	}

	// ########## Overridden Methods from Superclass ##########
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		// if null or other type not equal --> false
		if (o != null && o instanceof ParagraphSpan<?,?>) {
			ParagraphSpan<C,A> other = (ParagraphSpan<C,A>) o; // cast

			if (compareSpanAndText(other)
		        && compareAnnotation(other)
		        && compareCode(other)) {
	        	return true;
	        }
		}
	    return false;
	}

	/**
	 * Compares the stored Span and covered text
	 * Assumes that the type type params are the same.
	 * @param other
	 * @return
	 */
	private boolean compareSpanAndText(ParagraphSpan<C, A> other) {
		// if booth annotations are null --> true
		if (Arrays.equals(this.getSpan(), other.getSpan())
			&& other.getCoveredText().equals(this.getCoveredText())) {
			return true;
		}
		return false;
	}

	/**
	 * Compares the stored Span and covered text with the
	 * span and covered text of a given annotation
	 * @param an Annotation
	 * @return true if span and text match
	 */
	public boolean compareSpanAndCoveredText(Annotation other) {
		if (other != null
			&& other.getBegin() == this.getBegin()
			&& other.getEnd() == this.getEnd()
			&& other.getCoveredText().equals(this.getCoveredText())) {
			return true;
		}
		return false;
	}

	/**
	 * Compares the stored Annotation of two ParagraphSpans
	 * Assumes that the type type params are the same.
	 * @param other
	 * @return
	 */
	private boolean compareAnnotation(ParagraphSpan<C, A> other) {
		// if booth annotations are null --> true
		if (other.getAnnotation() == null && this.getAnnotation() == null) {
			return true;
		}
		// if only one of them is null --> false
		if ((other.getAnnotation() == null && this.getAnnotation() != null)
			|| (other.getAnnotation() != null && this.getAnnotation() == null)) {
			return false;
		}
		// is stored Annotations equal --> true
		if (this.getAnnotation().equals(other.getAnnotation())) {
			return true;
		}
		return false;
	}

	/**
	 * Compares the stored Code of two ParagraphSpans
	 * Assumes that the type type params are the same.
	 * @param other
	 * @return
	 */
	private boolean compareCode(ParagraphSpan<C, A> other) {
		// if booth annotations are null --> true
		if (other.getCode() == null && getCode() == null) {
			logger.info("a");
			return true;
		}
		// if only one of them is null --> false
		if ((other.getCode() == null && this.getCode() != null)
			|| (other.getCode() != null && this.getCode() == null)) {
			logger.info("b");
			return false;
		}
		// is stored Annotations equal --> true
		if (this.getCode().equals(other.getCode())) {
			logger.info("c");
			return true;
		}
		return false;
	}

	/**
	 * textual representation in the form:</br>
	 * <ul>
	 * <li>"ParagraphSpan [begin=0, end=0 - NO content!"</li>
	 * <li>"ParagraphSpan [begin=0, end=2, covered text=IBM"</li>
	 * </ul>
	 */
	@Override
	public String toString() {
		StringBuffer retString = new StringBuffer("ParagraphSpan [begin=" + begin + ", end=" + end);
		if (this.getCoveredText() == null) {
			retString.append(" - NO content!");
		} else {
			retString.append(", covered text=" + getCoveredText().trim());
		}
		retString.append("]");
		return retString.toString();
	}

}
