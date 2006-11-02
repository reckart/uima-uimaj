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

package org.apache.uima.resource.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.resource.Session;

/**
 * 
 */
public class Session_impl implements Session
{
  /**
   * 
   */
  private static final long serialVersionUID = -8525494546736102324L;
  
  /* (non-Javadoc)
   * @see org.apache.uima.resource.Session#put(java.lang.String, java.lang.Object)
   */
  public void put(String aKey, Object aValue)
  {
	mMap.put(aKey, aValue);

  }

  /* (non-Javadoc)
   * @see org.apache.uima.resource.Session#get(java.lang.String)
   */
  public Object get(String aKey)
  {
	return mMap.get(aKey);
  }
  
  private Map mMap = Collections.synchronizedMap(new HashMap());

}
