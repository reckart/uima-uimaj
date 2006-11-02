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

import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.jcas.impl.JCas;

public class IntegerList extends org.apache.uima.jcas.cas.TOP {

  public final static int typeIndexID = JCas.getNextIndex();
  public final static int type = typeIndexID;
  public           int getTypeIndexID() {return typeIndexID;}

  // Never called.  
  protected IntegerList() { //Disable default constructor
  }

  /** Internal - Constructor used by generator */
  public IntegerList(int addr, TOP_Type type) {
    super(addr, type);
  }

  public IntegerList(JCas jcas) {
    super(jcas);
  }


  public int getNthElement(int i) {
    if (this instanceof EmptyIntegerList) {
      CASRuntimeException casEx = new CASRuntimeException(CASRuntimeException.JCAS_GET_NTH_ON_EMPTY_LIST);
      casEx.addArgument("EmptyIntegerList");
      throw casEx;
    }
    if (i < 0) {
			CASRuntimeException casEx = new CASRuntimeException(
					CASRuntimeException.JCAS_GET_NTH_NEGATIVE_INDEX);
			casEx.addArgument(new Integer(i).toString());
			throw casEx;
		}
    int originali = i;
    IntegerList cg = this;
    for (;; i--) {
      if (cg instanceof EmptyIntegerList) {
        CASRuntimeException casEx = new CASRuntimeException(CASRuntimeException.JCAS_GET_NTH_PAST_END);
        casEx.addArgument(new Integer(originali).toString());
        throw casEx;
      }     
      NonEmptyIntegerList c = (NonEmptyIntegerList)cg;
      if (i == 0) return c.getHead();
      cg = c.getTail();
    }
  }
}
