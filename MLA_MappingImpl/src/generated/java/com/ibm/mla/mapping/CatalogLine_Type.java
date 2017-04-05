
/* First created by JCasGen Tue Nov 29 14:37:19 CET 2016 */
package com.ibm.mla.mapping;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Fri Dec 02 10:06:48 CET 2016
 * @generated */
public class CatalogLine_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CatalogLine_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CatalogLine_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CatalogLine(addr, CatalogLine_Type.this);
  			   CatalogLine_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CatalogLine(addr, CatalogLine_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = CatalogLine.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.ibm.mla.mapping.CatalogLine");
 
  /** @generated */
  final Feature casFeat_code;
  /** @generated */
  final int     casFeatCode_code;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getCode(int addr) {
        if (featOkTst && casFeat_code == null)
      jcas.throwFeatMissing("code", "com.ibm.mla.mapping.CatalogLine");
    return ll_cas.ll_getRefValue(addr, casFeatCode_code);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setCode(int addr, int v) {
        if (featOkTst && casFeat_code == null)
      jcas.throwFeatMissing("code", "com.ibm.mla.mapping.CatalogLine");
    ll_cas.ll_setRefValue(addr, casFeatCode_code, v);}
    
  
 
  /** @generated */
  final Feature casFeat_isOtherSpecific;
  /** @generated */
  final int     casFeatCode_isOtherSpecific;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getIsOtherSpecific(int addr) {
        if (featOkTst && casFeat_isOtherSpecific == null)
      jcas.throwFeatMissing("isOtherSpecific", "com.ibm.mla.mapping.CatalogLine");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_isOtherSpecific);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setIsOtherSpecific(int addr, boolean v) {
        if (featOkTst && casFeat_isOtherSpecific == null)
      jcas.throwFeatMissing("isOtherSpecific", "com.ibm.mla.mapping.CatalogLine");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_isOtherSpecific, v);}
    
  
 
  /** @generated */
  final Feature casFeat_isUnspecific;
  /** @generated */
  final int     casFeatCode_isUnspecific;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getIsUnspecific(int addr) {
        if (featOkTst && casFeat_isUnspecific == null)
      jcas.throwFeatMissing("isUnspecific", "com.ibm.mla.mapping.CatalogLine");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_isUnspecific);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setIsUnspecific(int addr, boolean v) {
        if (featOkTst && casFeat_isUnspecific == null)
      jcas.throwFeatMissing("isUnspecific", "com.ibm.mla.mapping.CatalogLine");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_isUnspecific, v);}
    
  
 
  /** @generated */
  final Feature casFeat_processingState;
  /** @generated */
  final int     casFeatCode_processingState;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getProcessingState(int addr) {
        if (featOkTst && casFeat_processingState == null)
      jcas.throwFeatMissing("processingState", "com.ibm.mla.mapping.CatalogLine");
    return ll_cas.ll_getStringValue(addr, casFeatCode_processingState);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setProcessingState(int addr, String v) {
        if (featOkTst && casFeat_processingState == null)
      jcas.throwFeatMissing("processingState", "com.ibm.mla.mapping.CatalogLine");
    ll_cas.ll_setStringValue(addr, casFeatCode_processingState, v);}
    
  
 
  /** @generated */
  final Feature casFeat_concept;
  /** @generated */
  final int     casFeatCode_concept;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getConcept(int addr) {
        if (featOkTst && casFeat_concept == null)
      jcas.throwFeatMissing("concept", "com.ibm.mla.mapping.CatalogLine");
    return ll_cas.ll_getRefValue(addr, casFeatCode_concept);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setConcept(int addr, int v) {
        if (featOkTst && casFeat_concept == null)
      jcas.throwFeatMissing("concept", "com.ibm.mla.mapping.CatalogLine");
    ll_cas.ll_setRefValue(addr, casFeatCode_concept, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public CatalogLine_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_code = jcas.getRequiredFeatureDE(casType, "code", "uima.tcas.Annotation", featOkTst);
    casFeatCode_code  = (null == casFeat_code) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_code).getCode();

 
    casFeat_isOtherSpecific = jcas.getRequiredFeatureDE(casType, "isOtherSpecific", "uima.cas.Boolean", featOkTst);
    casFeatCode_isOtherSpecific  = (null == casFeat_isOtherSpecific) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_isOtherSpecific).getCode();

 
    casFeat_isUnspecific = jcas.getRequiredFeatureDE(casType, "isUnspecific", "uima.cas.Boolean", featOkTst);
    casFeatCode_isUnspecific  = (null == casFeat_isUnspecific) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_isUnspecific).getCode();

 
    casFeat_processingState = jcas.getRequiredFeatureDE(casType, "processingState", "uima.cas.String", featOkTst);
    casFeatCode_processingState  = (null == casFeat_processingState) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_processingState).getCode();

 
    casFeat_concept = jcas.getRequiredFeatureDE(casType, "concept", "uima.tcas.Annotation", featOkTst);
    casFeatCode_concept  = (null == casFeat_concept) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_concept).getCode();

  }
}



    