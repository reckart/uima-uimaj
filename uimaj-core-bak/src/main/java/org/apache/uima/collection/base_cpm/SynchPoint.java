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

package org.apache.uima.collection.base_cpm;

import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.Serializable;

/**
 * Interface facilitating recovery of CollectionReaders to the last known entity
 * 
 * 
 */
public interface SynchPoint extends Serializable
{
	/**
	 * Sets data from which to recover 
	 * 
	 * @param aSynchPointData - arbitrary object containing recovery infromation
	 * 
	 * @throws InvalidClassException  if aSynchPointData class is not supported by implementation
	 */
	public void set( Object aSynchPointData ) throws InvalidClassException;
	/**
	 * Retrieves data to facilitate recovery
	 * 
	 * @return Object - data containing recovery information
	 */
	public Object get();
	/**
	 * Serializes internal representation of the SynchPoint to XML
	 * 
	 * @return - serialized SynchPoint as String
	 */
	public String serializeToXML();
	
	/**
	 * Ingests SynchPoint data from the InputStream. InputStream contains 
	 * xml representation of the SynchPoint previously generated by serializeToXML().
	 * 
	 * @param aInputStream - xml stream containing SynchPoint data
	 * @throws Exception - unable to process the input stream
	 */
	public void deserialize( InputStream aInputStream ) throws Exception;
	
}
