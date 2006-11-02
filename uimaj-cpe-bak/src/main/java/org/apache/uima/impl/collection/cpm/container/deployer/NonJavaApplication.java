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

package org.apache.uima.impl.collection.cpm.container.deployer;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.base_cpm.container.CasProcessorConfiguration;
import org.apache.uima.collection.metadata.CpeCasProcessor;
import org.apache.uima.impl.collection.cpm.container.CPEFactory;
import org.apache.uima.impl.collection.cpm.utils.CPMUtils;
import org.apache.uima.impl.collection.cpm.utils.Execute;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.util.Level;
import org.apache.uima.impl.util.SystemEnvReader;

/**
 * Component responsible for configuring command line for non-java based CasProcessor. Each CasProcessor
 * is configured via CPE descriptor either statically (xml file) or dynamically by
 * means of APIs. In both cases, the CasProcessor to be launched by the CPE 
 * must be properly setup for launching. Its environment must be setup, and command line with arguments.
 * 
 * 
 */
public class NonJavaApplication extends RunnableApplication
{
	private ArrayList environment = new ArrayList();
	private Execute exec;

	public NonJavaApplication(CasProcessorConfiguration aCasProcessorConfiguration, CpeCasProcessor aCasProcessorConfig)
	throws ResourceConfigurationException
{
		addApplicationInfo(aCasProcessorConfiguration, aCasProcessorConfig); 			
	}
	
	/**
	 * Sets up command line used to launch Cas Processor in a seperate process. 
	 * Combines environment variables setup in the CPE descriptor with a
	 * System environment variables.
	 * 
	 * @param aCasProcessorConfiguration - access to Cas Processor configuration
	 * @param aJaxbCasProcessorConfig 
	 * @throws ResourceConfigurationException
	 */
	protected void addApplicationInfo(CasProcessorConfiguration aCasProcessorConfiguration, CpeCasProcessor aCasProcessor)
	throws ResourceConfigurationException
	{
		super.addApplicationInfo( aCasProcessorConfiguration, aCasProcessor);
		if ( "local".equals( aCasProcessor.getDeployment() ) )
		{
			String[] cmdLine = addApplicationCmdLineArguments(aCasProcessorConfiguration, argList, executable);
			exec.setCmdLine(cmdLine);
		}

	}	
	
	/**
	 * Returns final command line as array of Strings.
	 * 
	 * @param aCasProcessorConfiguration - Cas Processor configuration
	 * @param argList - arguments configured for the CasProcessor in cpe descriptor
	 * @param aExecutable - name of the program to launch
	 * @return - command line as array of Strings
	 */
	protected String[] addApplicationCmdLineArguments(CasProcessorConfiguration aCasProcessorConfiguration, List argList, String aExecutable) //String[] cmdLine) //, String aDescriptor,List aDeploymentParameters )
	{
		ArrayList cmdArgs = new ArrayList();
		cmdArgs.add(aExecutable);
		
		for (int i = 0; i < argList.size(); i++)
		{
			
			String arg = (String) argList.get(i);
			
			if ( UIMAFramework.getLogger().isLoggable(Level.FINEST) )
			{
				UIMAFramework.getLogger(this.getClass()).logrb(Level.FINEST, this.getClass().getName(), "initialize",
				        CPMUtils.CPM_LOG_RESOURCE_BUNDLE, "UIMA_CPM_show_cmd_arg__FINEST",
				        new Object[] {Thread.currentThread().getName(), String.valueOf(i), arg});				
			}		
			if ("${descriptor}".equals(arg.trim()))
			{
				String descriptor = CPMUtils.convertToAbsolutePath( System.getProperty("CPM_HOME"), CPEFactory.CPM_HOME, aCasProcessorConfiguration.getDescriptor() );

				cmdArgs.add("\""+descriptor+"\"");
			}
			else
			{
				cmdArgs.add(arg);
			}
		}
		String[] cmdLine = null;
		//	Due to the special case (-cp) we possible had modified the argument list. The new argument list
		//	needs to be copied (overwrite) previous argument list.
		if ( cmdArgs.size() > 0 )
		{
			//	Overwrite the cmdLine setting with new settings
			cmdLine = new String[cmdArgs.size()];
			cmdArgs.toArray(cmdLine);
		}
		return cmdLine;
		
	}

	/**
	 * @return
	 */
	public Execute getExecSpec()
	{
		return exec;
	}

}
