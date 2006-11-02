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

package org.apache.uima.impl.application.cpm;

import java.io.File;
import java.io.IOException;

import javax.swing.JCheckBox;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.TaeDescription;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;

public class TaePanel extends MetaDataPanel
{
	JCheckBox shouldRunCheckBox = new JCheckBox((String) null, true);
	ResourceSpecifier taeSpecifier;
	File specifierFile;
	long lastFileSyncTimestamp;
	
	public TaePanel(ResourceSpecifier taeSpecifier, File specifierFile, long fileModStamp)
	{
		super(4);		// 4 columns
		this.taeSpecifier = taeSpecifier;
		this.specifierFile = specifierFile;
		this.lastFileSyncTimestamp = fileModStamp;
	}

	public ResourceSpecifier getTaeSpecifier()
	{
		return this.taeSpecifier;
	}
	
	public long getLastFileSyncTimestamp()
	{
		return this.lastFileSyncTimestamp;
	}
	
	public void setLastFileSyncTimestamp(long timestamp)
	{
		this.lastFileSyncTimestamp = timestamp;
	}
	
	public boolean hasFileChanged(long lastCheck)
	{
		return specifierFile.lastModified() > this.lastFileSyncTimestamp &&
		       specifierFile.lastModified() > lastCheck;
	}
	
	public void refreshFromFile()
	  throws InvalidXMLException, IOException
	{
		clearAll();
		this.taeSpecifier =
			UIMAFramework.getXMLParser().parseResourceSpecifier(new XMLInputSource(this.specifierFile));
		if (taeSpecifier instanceof TaeDescription)
		{
			TaeDescription taeDescription = (TaeDescription) taeSpecifier;
			populate(taeDescription.getMetaData(), null);
		}
		else
		{
			this.removeAll();		
		}
	  this.lastFileSyncTimestamp = this.specifierFile.lastModified();		
	}
}
