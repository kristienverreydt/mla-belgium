
/* First created by JCasGen Tue Nov 29 14:23:10 CET 2016 */
package com.ibm.mla.commonmedicalresources;

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
 * Updated by JCasGen Tue Nov 29 14:23:10 CET 2016
 * @generated */
public class CatalogGeneralizingTerm_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CatalogGeneralizingTerm_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CatalogGeneralizingTerm_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CatalogGeneralizingTerm(addr, CatalogGeneralizingTerm_Type.this);
  			   CatalogGeneralizingTerm_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CatalogGeneralizingTerm(addr, CatalogGeneralizingTerm_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = CatalogGeneralizingTerm.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.ibm.mla.commonmedicalresources.CatalogGeneralizingTerm");
 
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
      jcas.throwFeatMissing("isOtherSpecific", "com.ibm.mla.commonmedicalresources.CatalogGeneralizingTerm");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_isOtherSpecific);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setIsOtherSpecific(int addr, boolean v) {
        if (featOkTst && casFeat_isOtherSpecific == null)
      jcas.throwFeatMissing("isOtherSpecific", "com.ibm.mla.commonmedicalresources.CatalogGeneralizingTerm");
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
      jcas.throwFeatMissing("isUnspecific", "com.ibm.mla.commonmedicalresources.CatalogGeneralizingTerm");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_isUnspecific);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setIsUnspecific(int addr, boolean v) {
        if (featOkTst && casFeat_isUnspecific == null)
      jcas.throwFeatMissing("isUnspecific", "com.ibm.mla.commonmedicalresources.CatalogGeneralizingTerm");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_isUnspecific, v);}
    
  
 
  /** @generated */
  final Feature casFeat_generalizer;
  /** @generated */
  final int     casFeatCode_generalizer;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getGeneralizer(int addr) {
        if (featOkTst && casFeat_generalizer == null)
      jcas.throwFeatMissing("generalizer", "com.ibm.mla.commonmedicalresources.CatalogGeneralizingTerm");
    return ll_cas.ll_getStringValue(addr, casFeatCode_generalizer);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setGeneralizer(int addr, String v) {
        if (featOkTst && casFeat_generalizer == null)
      jcas.throwFeatMissing("generalizer", "com.ibm.mla.commonmedicalresources.CatalogGeneralizingTerm");
    ll_cas.ll_setStringValue(addr, casFeatCode_generalizer, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public CatalogGeneralizingTerm_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_isOtherSpecific = jcas.getRequiredFeatureDE(casType, "isOtherSpecific", "uima.cas.Boolean", featOkTst);
    casFeatCode_isOtherSpecific  = (null == casFeat_isOtherSpecific) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_isOtherSpecific).getCode();

 
    casFeat_isUnspecific = jcas.getRequiredFeatureDE(casType, "isUnspecific", "uima.cas.Boolean", featOkTst);
    casFeatCode_isUnspecific  = (null == casFeat_isUnspecific) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_isUnspecific).getCode();

 
    casFeat_generalizer = jcas.getRequiredFeatureDE(casType, "generalizer", "uima.cas.String", featOkTst);
    casFeatCode_generalizer  = (null == casFeat_generalizer) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_generalizer).getCode();

  }
}



    