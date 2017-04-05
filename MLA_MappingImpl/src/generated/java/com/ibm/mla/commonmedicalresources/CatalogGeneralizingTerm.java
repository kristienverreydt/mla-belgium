

/* First created by JCasGen Tue Nov 29 14:23:10 CET 2016 */
package com.ibm.mla.commonmedicalresources;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Nov 29 14:23:10 CET 2016
 * XML source: C:/Users/IBM_ADMIN/git/mla/MLA_MappingImpl/src/main/resources/CatalogGeneralizingTerms-ts.xml
 * @generated */
public class CatalogGeneralizingTerm extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(CatalogGeneralizingTerm.class);
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
  protected CatalogGeneralizingTerm() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public CatalogGeneralizingTerm(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public CatalogGeneralizingTerm(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public CatalogGeneralizingTerm(JCas jcas, int begin, int end) {
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
  //* Feature: isOtherSpecific

  /** getter for isOtherSpecific - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getIsOtherSpecific() {
    if (CatalogGeneralizingTerm_Type.featOkTst && ((CatalogGeneralizingTerm_Type)jcasType).casFeat_isOtherSpecific == null)
      jcasType.jcas.throwFeatMissing("isOtherSpecific", "com.ibm.mla.commonmedicalresources.CatalogGeneralizingTerm");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((CatalogGeneralizingTerm_Type)jcasType).casFeatCode_isOtherSpecific);}
    
  /** setter for isOtherSpecific - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setIsOtherSpecific(boolean v) {
    if (CatalogGeneralizingTerm_Type.featOkTst && ((CatalogGeneralizingTerm_Type)jcasType).casFeat_isOtherSpecific == null)
      jcasType.jcas.throwFeatMissing("isOtherSpecific", "com.ibm.mla.commonmedicalresources.CatalogGeneralizingTerm");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((CatalogGeneralizingTerm_Type)jcasType).casFeatCode_isOtherSpecific, v);}    
   
    
  //*--------------*
  //* Feature: isUnspecific

  /** getter for isUnspecific - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getIsUnspecific() {
    if (CatalogGeneralizingTerm_Type.featOkTst && ((CatalogGeneralizingTerm_Type)jcasType).casFeat_isUnspecific == null)
      jcasType.jcas.throwFeatMissing("isUnspecific", "com.ibm.mla.commonmedicalresources.CatalogGeneralizingTerm");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((CatalogGeneralizingTerm_Type)jcasType).casFeatCode_isUnspecific);}
    
  /** setter for isUnspecific - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setIsUnspecific(boolean v) {
    if (CatalogGeneralizingTerm_Type.featOkTst && ((CatalogGeneralizingTerm_Type)jcasType).casFeat_isUnspecific == null)
      jcasType.jcas.throwFeatMissing("isUnspecific", "com.ibm.mla.commonmedicalresources.CatalogGeneralizingTerm");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((CatalogGeneralizingTerm_Type)jcasType).casFeatCode_isUnspecific, v);}    
   
    
  //*--------------*
  //* Feature: generalizer

  /** getter for generalizer - gets 
   * @generated
   * @return value of the feature 
   */
  public String getGeneralizer() {
    if (CatalogGeneralizingTerm_Type.featOkTst && ((CatalogGeneralizingTerm_Type)jcasType).casFeat_generalizer == null)
      jcasType.jcas.throwFeatMissing("generalizer", "com.ibm.mla.commonmedicalresources.CatalogGeneralizingTerm");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CatalogGeneralizingTerm_Type)jcasType).casFeatCode_generalizer);}
    
  /** setter for generalizer - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setGeneralizer(String v) {
    if (CatalogGeneralizingTerm_Type.featOkTst && ((CatalogGeneralizingTerm_Type)jcasType).casFeat_generalizer == null)
      jcasType.jcas.throwFeatMissing("generalizer", "com.ibm.mla.commonmedicalresources.CatalogGeneralizingTerm");
    jcasType.ll_cas.ll_setStringValue(addr, ((CatalogGeneralizingTerm_Type)jcasType).casFeatCode_generalizer, v);}    
  }

    