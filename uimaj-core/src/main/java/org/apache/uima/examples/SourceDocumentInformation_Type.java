/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.examples;

import org.apache.uima.jcas.impl.JCas;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** Stores detailed information about the original source document from which the current CAS was initialized. All information (like size) refers to the source document and not to the document in the CAS which may be converted and filtered by a CAS Initializer. For example this information will be written to the Semantic Search index so that the original document contents can be retrieved by queries.
 * Updated by JCasGen Thu Dec 02 11:37:08 EST 2004
 * @generated */
public class SourceDocumentInformation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (instanceOf_Type.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = instanceOf_Type.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new SourceDocumentInformation(addr, instanceOf_Type);
  			   instanceOf_Type.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new SourceDocumentInformation(addr, instanceOf_Type);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = SourceDocumentInformation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCas.getFeatOkTst("org.apache.uima.examples.SourceDocumentInformation");
 
  /** @generated */
  final Feature casFeat_uri;
  /** @generated */
  final int     casFeatCode_uri;
  /** @generated */ 
  public String getUri(int addr) {
        if (featOkTst && casFeat_uri == null)
      JCas.throwFeatMissing("uri", "org.apache.uima.examples.SourceDocumentInformation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_uri);
  }
  /** @generated */    
  public void setUri(int addr, String v) {
        if (featOkTst && casFeat_uri == null)
      JCas.throwFeatMissing("uri", "org.apache.uima.examples.SourceDocumentInformation");
    ll_cas.ll_setStringValue(addr, casFeatCode_uri, v);}
    
  
 
  /** @generated */
  final Feature casFeat_offsetInSource;
  /** @generated */
  final int     casFeatCode_offsetInSource;
  /** @generated */ 
  public int getOffsetInSource(int addr) {
        if (featOkTst && casFeat_offsetInSource == null)
      JCas.throwFeatMissing("offsetInSource", "org.apache.uima.examples.SourceDocumentInformation");
    return ll_cas.ll_getIntValue(addr, casFeatCode_offsetInSource);
  }
  /** @generated */    
  public void setOffsetInSource(int addr, int v) {
        if (featOkTst && casFeat_offsetInSource == null)
      JCas.throwFeatMissing("offsetInSource", "org.apache.uima.examples.SourceDocumentInformation");
    ll_cas.ll_setIntValue(addr, casFeatCode_offsetInSource, v);}
    
  
 
  /** @generated */
  final Feature casFeat_documentSize;
  /** @generated */
  final int     casFeatCode_documentSize;
  /** @generated */ 
  public int getDocumentSize(int addr) {
        if (featOkTst && casFeat_documentSize == null)
      JCas.throwFeatMissing("documentSize", "org.apache.uima.examples.SourceDocumentInformation");
    return ll_cas.ll_getIntValue(addr, casFeatCode_documentSize);
  }
  /** @generated */    
  public void setDocumentSize(int addr, int v) {
        if (featOkTst && casFeat_documentSize == null)
      JCas.throwFeatMissing("documentSize", "org.apache.uima.examples.SourceDocumentInformation");
    ll_cas.ll_setIntValue(addr, casFeatCode_documentSize, v);}
    
  


  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public SourceDocumentInformation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_uri = jcas.getRequiredFeatureDE(casType, "uri", "uima.cas.String", featOkTst);
    casFeatCode_uri  = (null == casFeat_uri) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_uri).getCode();

 
    casFeat_offsetInSource = jcas.getRequiredFeatureDE(casType, "offsetInSource", "uima.cas.Integer", featOkTst);
    casFeatCode_offsetInSource  = (null == casFeat_offsetInSource) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_offsetInSource).getCode();

 
    casFeat_documentSize = jcas.getRequiredFeatureDE(casType, "documentSize", "uima.cas.Integer", featOkTst);
    casFeatCode_documentSize  = (null == casFeat_documentSize) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_documentSize).getCode();

  }
}



    
