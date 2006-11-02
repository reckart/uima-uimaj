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

package org.apache.uima.jcas.cas;

import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.jcas.impl.JCas;

public class FSList_Type extends org.apache.uima.jcas.cas.TOP_Type {
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (FSList_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = FSList_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new FSList(addr, FSList_Type.this);
  		       FSList_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			     return fs;
  		     }
  		     return fs;
        } else return new FSList(addr, FSList_Type.this);
  	  }
    };

  public final static int typeIndexID = FSList.typeIndexID;

  public final static boolean featOkTst = JCas.getFeatOkTst("uima.cas.FSList");

  //* initialize variables to correspond with Cas Type and Features
  public FSList_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

  }

  protected FSList_Type() { //block default new operator
    throw new RuntimeException("Internal Error-this constructor should never be called.");  }

}
