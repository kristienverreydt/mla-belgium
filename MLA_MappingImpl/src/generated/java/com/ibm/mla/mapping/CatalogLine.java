

/* First created by JCasGen Tue Nov 29 14:37:19 CET 2016 */
package com.ibm.mla.mapping;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.tcas.Annotation;

import com.ibm.mla.commonmedicalresources.CatalogGeneralizingTerm;


/** 
 * Contains hand-written code (java.util.List<CatalogGeneralizingTerm>)
 * Updated by JCasGen Fri Dec 02 10:06:48 CET 2016
 * XML source: C:/Users/IBM_ADMIN/git/mla/MLA_MappingImpl/src/main/resources/CatalogLine-ts.xml
 * @generated */
public class CatalogLine extends Annotation {
// BEGIN: extension to generated code: the List of generalizing terms
	private List<CatalogGeneralizingTerm> generalizingTerms = new ArrayList<CatalogGeneralizingTerm>() ;
		
	public List<CatalogGeneralizingTerm> getGeneralizingTerms() {
		return generalizingTerms;
	}

	/**
	 * Add a link to a generalizing term, so we can later check for unmatched text within a catalog line
	 * @param genTerm a CatalogGeneralizingTerm annotation which belongs in this catalog line 
	 */
	public void addGeneralizingTerm(CatalogGeneralizingTerm genTerm) {
		generalizingTerms.add(genTerm);
	}    
// END: extension to generated code: the List of generalizing terms
	
	
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(CatalogLine.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CatalogLine() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public CatalogLine(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public CatalogLine(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public CatalogLine(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: code

  /** getter for code - gets 
   * @generated
   * @return value of the feature 
   */
  public Annotation getCode() {
    if (CatalogLine_Type.featOkTst && ((CatalogLine_Type)jcasType).casFeat_code == null)
      jcasType.jcas.throwFeatMissing("code", "com.ibm.mla.mapping.CatalogLine");
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CatalogLine_Type)jcasType).casFeatCode_code)));}
    
  /** setter for code - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setCode(Annotation v) {
    if (CatalogLine_Type.featOkTst && ((CatalogLine_Type)jcasType).casFeat_code == null)
      jcasType.jcas.throwFeatMissing("code", "com.ibm.mla.mapping.CatalogLine");
    jcasType.ll_cas.ll_setRefValue(addr, ((CatalogLine_Type)jcasType).casFeatCode_code, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: isOtherSpecific

  /** getter for isOtherSpecific - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getIsOtherSpecific() {
    if (CatalogLine_Type.featOkTst && ((CatalogLine_Type)jcasType).casFeat_isOtherSpecific == null)
      jcasType.jcas.throwFeatMissing("isOtherSpecific", "com.ibm.mla.mapping.CatalogLine");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((CatalogLine_Type)jcasType).casFeatCode_isOtherSpecific);}
    
  /** setter for isOtherSpecific - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setIsOtherSpecific(boolean v) {
    if (CatalogLine_Type.featOkTst && ((CatalogLine_Type)jcasType).casFeat_isOtherSpecific == null)
      jcasType.jcas.throwFeatMissing("isOtherSpecific", "com.ibm.mla.mapping.CatalogLine");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((CatalogLine_Type)jcasType).casFeatCode_isOtherSpecific, v);}    
   
    
  //*--------------*
  //* Feature: isUnspecific

  /** getter for isUnspecific - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getIsUnspecific() {
    if (CatalogLine_Type.featOkTst && ((CatalogLine_Type)jcasType).casFeat_isUnspecific == null)
      jcasType.jcas.throwFeatMissing("isUnspecific", "com.ibm.mla.mapping.CatalogLine");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((CatalogLine_Type)jcasType).casFeatCode_isUnspecific);}
    
  /** setter for isUnspecific - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setIsUnspecific(boolean v) {
    if (CatalogLine_Type.featOkTst && ((CatalogLine_Type)jcasType).casFeat_isUnspecific == null)
      jcasType.jcas.throwFeatMissing("isUnspecific", "com.ibm.mla.mapping.CatalogLine");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((CatalogLine_Type)jcasType).casFeatCode_isUnspecific, v);}    
   
    
  //*--------------*
  //* Feature: processingState

  /** getter for processingState - gets 
   * @generated
   * @return value of the feature 
   */
  public String getProcessingState() {
    if (CatalogLine_Type.featOkTst && ((CatalogLine_Type)jcasType).casFeat_processingState == null)
      jcasType.jcas.throwFeatMissing("processingState", "com.ibm.mla.mapping.CatalogLine");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CatalogLine_Type)jcasType).casFeatCode_processingState);}
    
  /** setter for processingState - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setProcessingState(String v) {
    if (CatalogLine_Type.featOkTst && ((CatalogLine_Type)jcasType).casFeat_processingState == null)
      jcasType.jcas.throwFeatMissing("processingState", "com.ibm.mla.mapping.CatalogLine");
    jcasType.ll_cas.ll_setStringValue(addr, ((CatalogLine_Type)jcasType).casFeatCode_processingState, v);}    
   
    
  //*--------------*
  //* Feature: concept

  /** getter for concept - gets 
   * @generated
   * @return value of the feature 
   */
  public Annotation getConcept() {
    if (CatalogLine_Type.featOkTst && ((CatalogLine_Type)jcasType).casFeat_concept == null)
      jcasType.jcas.throwFeatMissing("concept", "com.ibm.mla.mapping.CatalogLine");
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CatalogLine_Type)jcasType).casFeatCode_concept)));}
    
  /** setter for concept - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setConcept(Annotation v) {
    if (CatalogLine_Type.featOkTst && ((CatalogLine_Type)jcasType).casFeat_concept == null)
      jcasType.jcas.throwFeatMissing("concept", "com.ibm.mla.mapping.CatalogLine");
    jcasType.ll_cas.ll_setRefValue(addr, ((CatalogLine_Type)jcasType).casFeatCode_concept, jcasType.ll_cas.ll_getFSRef(v));}

  }

    