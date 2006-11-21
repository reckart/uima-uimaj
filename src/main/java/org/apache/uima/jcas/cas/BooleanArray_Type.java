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

// *********************************
// * Implementation of BooleanArray_Type for JCas
// *********************************
/**
 * The java Cas model for the CAS BooleanArray_Type
 */
public final class BooleanArray_Type extends CommonArray_Type {
  /**
   * this types ID - used to index a localTypeArray in JCas to get an index which indexes the global
   * typeArray in JCas instance to get a ref to this instance
   */
  public final static int typeIndexID = BooleanArray.typeIndexID;

  // generator used by the CAS system when it needs to make a new instance

  protected FSGenerator getFSGenerator() {
    return fsGenerator;
  }

  private final FSGenerator fsGenerator = new FSGenerator() {
    public FeatureStructure createFS(int addr, CASImpl cas) {
      if (BooleanArray_Type.this.useExistingInstance) {
        // Return eq fs instance if already created
        FeatureStructure fs = BooleanArray_Type.this.jcas.getJfsFromCaddr(addr);
        if (null == fs) {
          fs = new BooleanArray(addr, BooleanArray_Type.this);
          BooleanArray_Type.this.jcas.putJfsFromCaddr(addr, fs);
          return fs;
        }
        return fs;
      } else
        return new BooleanArray(addr, BooleanArray_Type.this);
    }
  };

  private BooleanArray_Type() {
  } // block default new operator

  public BooleanArray_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    // Do not factor to TOP_Type - requires access to instance values
    // which are not set when super is called (per JVM spec)
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());
  }

  // ******************************************************
  // * Low level interface version
  // ******************************************************

  /**
   * return the indexed value from the corresponding Cas StringArray as a Java String.
   * 
   * @see org.apache.uima.cas.BooleanArrayFS#get(int)
   */
  public boolean get(int addr, int i) {
    if (lowLevelTypeChecks)
      return ll_cas.ll_getBooleanArrayValue(addr, i, true);
    if (lowLevelArrayBoundChecks)
      casImpl.checkArrayBounds(addr, i);
    return ll_cas.ll_getBooleanArrayValue(addr, i);
  }

  /**
   * updates the Cas, setting the indexed value to the passed in Java String value.
   * 
   * @see org.apache.uima.cas.BooleanArrayFS#set(int, FeatureStructure)
   */
  public void set(int addr, int i, boolean v) {
    if (lowLevelTypeChecks)
      ll_cas.ll_setBooleanArrayValue(addr, i, v, true);
    if (lowLevelArrayBoundChecks)
      casImpl.checkArrayBounds(addr, i);
    ll_cas.ll_setBooleanArrayValue(addr, i, v);
  }

  /**
   * @see org.apache.uima.cas.BooleanArrayFS#copyFromArray(boolean[], int, int, int)
   */
  public void copyFromArray(int addr, boolean[] src, int srcOffset, int destOffset, int length) {
    if (lowLevelArrayBoundChecks)
      casImpl.checkArrayBounds(addr, destOffset, length);
    for (int i = 0; i < length; i++) {
      ll_cas.ll_setBooleanArrayValue(addr, i + destOffset, src[i + srcOffset]);
    }
  }

  /**
   * @see org.apache.uima.cas.BooleanArrayFS#copyToArray(int, boolean[], int, int)
   */
  public void copyToArray(int addr, int srcOffset, boolean[] dest, int destOffset, int length) {
    if (lowLevelArrayBoundChecks)
      casImpl.checkArrayBounds(addr, srcOffset, length);
    for (int i = 0; i < length; i++) {
      dest[i + destOffset] = ll_cas.ll_getBooleanArrayValue(addr, i + srcOffset);
    }
  }

  /**
   * @see org.apache.uima.cas.BooleanArrayFS#toArray()
   */
  public boolean[] toArray(int addr) {
    final int size = size(addr);
    boolean[] outArray = new boolean[size];
    copyToArray(addr, 0, outArray, 0, size);
    return outArray;
  }
}
