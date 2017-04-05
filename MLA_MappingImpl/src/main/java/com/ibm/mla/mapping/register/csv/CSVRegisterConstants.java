package com.ibm.mla.mapping.register.csv;

/**
 * Provides common separators for String operations.<\br>
 * Example of the Diagnosis class:<\br>
 * "PrimDisease I;SekKrankheit2|SekKrankheit1;BodyPartInstance1|BodyPartInstance2;Umstand
 * ;Process1|Process2;TriggerInstance1|TriggerInstance2"
 * @author Georg Temnitschka
 * @see com.mla.mapping.concept.Diagnosis
 */
public class CSVRegisterConstants {

	/** Top-level separator (between key and value): equal sign */
	public static final char OUTDIAGKVSEP = '=';
	/** Line break Windows Style */
	public static final String OUTDIAGLINESEP = "\n";
	/** Double Quote */
	public static final char OUTDIAGQUOTE = '"';
	/** Next level (parts of diagnosis and ICD10 coding): semicolon */
	public static final char OUTDIAGSEP = ';';
	/** Next level (repeatable descriptors for elements): vertical bar */
	public static final char OUTDIAGSUBSEP = '|';
	/** Next level (concept with descriptors): paragraph */
	public static final char OUTDIAGCWDSEP = '#';
	/** Default Encoding for I/O Operations */
	public static final String REGISTERCHARSET  = "UTF-8" ;

}
