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

package org.apache.uima.tools.pear.merger;

import java.io.File;
import java.io.FileNotFoundException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.test.junit_extension.TestPropertyReader;
import org.apache.uima.tools.pear.merger.PMController;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;

import org.apache.uima.analysis_engine.TaeDescription;
import org.apache.uima.analysis_engine.TextAnalysisEngine;
import org.apache.uima.cas.text.TCAS;
import org.apache.uima.pear.tools.InstallationController;
import org.apache.uima.pear.tools.InstallationDescriptor;
import org.apache.uima.pear.util.FileUtil;

/**
 * The <code>PearMergerTest</code> class provides JUnit test cases for 
 * the jedii_pear_merger component. The test cases are based on the 
 * sample input PEARs located in the 'pearTests/pearMergerTests' folder.
 * 
 * @author LK
 */
public class PearMergerTest extends TestCase {
	// relative location of sample PEARs
	private static String 
			TEST_FOLDER = "pearTests/pearMergerTests";
	// sample input PEAR files
	private static String INP_PEAR_1_FILE = "uima.example.DateTime.pear";
	private static String INP_PEAR_2_FILE = "uima.example.RoomNumber.pear";
	// output aggregate PEAR file
	private static String OUT_PEAR_ID = 
			"uima.example.RoomDateTimeAggregate";
	// JUnit test base path
	private String _junitTestBasePath;
	// Temporary working directory
	private File _tempWorkingDir = null;
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		//get test base path setting
		_junitTestBasePath = TestPropertyReader.getJUnitTestBasePath();
		// create temporary working directory
		File tempFile = File.createTempFile( "pear_merger_test_", "~tmp" );
		if( tempFile.delete() ) {
			File tempDir = tempFile;
			if( tempDir.mkdirs() )
				_tempWorkingDir = tempDir;
		}
	}
	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		if( _tempWorkingDir != null ) {
			FileUtil.deleteDirectory( _tempWorkingDir );
		}
	}
	/**
	 * Runs test for org.apache.uima.pear.merger.PMController class by merging 
	 * 2 sample input PEARs into the output aggregate PEAR. Then, the output 
	 * PEAR is installed by using 
	 * org.apache.uima.pear.tools.InstallationController, and the installed 
	 * component is verified by instantiating the aggregate TAE and creating 
	 * TCAS object.
	 */
	public void testPearMerger() throws Exception {
		// check temporary working directory
		if( _tempWorkingDir == null )
			throw new FileNotFoundException( "temp directory not found" );
		// check sample PEAR files
		File[] inpPearFiles = new File[2];
		inpPearFiles[0] = new File( _junitTestBasePath, 
				TEST_FOLDER + File.separator + INP_PEAR_1_FILE );
		if( ! inpPearFiles[0].isFile() )
			throw new FileNotFoundException( "sample PEAR 1 not found" );
		inpPearFiles[1] = new File( _junitTestBasePath, 
				TEST_FOLDER + File.separator + INP_PEAR_2_FILE );
		if( ! inpPearFiles[1].isFile() )
			throw new FileNotFoundException( "sample PEAR 2 not found" );
		// specify output aggregate PEAR file
		File outPearFile = new File( _tempWorkingDir, OUT_PEAR_ID + ".pear" );
		// create PMController instance and perform merging operation
		PMController.setLogFileEnabled( false );
		PMController pmController = 
			new PMController( inpPearFiles, OUT_PEAR_ID, outPearFile );
		boolean done = pmController.mergePears();
		// check merging results
		Assert.assertTrue( done );
		Assert.assertTrue( outPearFile.isFile() );
		// install the output PEAR file and check the results
		InstallationController insController = new InstallationController( 
				OUT_PEAR_ID, outPearFile, _tempWorkingDir );
		InstallationDescriptor insDesc = insController.installComponent();
		Assert.assertTrue( insDesc != null );
		Assert.assertTrue( 
				OUT_PEAR_ID.equals( insDesc.getMainComponentId() ) );
		// verify the installed component
		// customize ResourceManager by adding component CLASSPATH
		ResourceManager resMngr = UIMAFramework.newDefaultResourceManager();
		String compClassPath = InstallationController.buildComponentClassPath(
				insDesc.getMainComponentRoot(), insDesc );
		// instantiate the aggregate TAE
		resMngr.setExtensionClassPath( compClassPath, true );
		String compDescFilePath = insDesc.getMainComponentDesc();
		XMLParser xmlPaser = UIMAFramework.getXMLParser();
		XMLInputSource xmlInput = new XMLInputSource( compDescFilePath );
		TaeDescription taeSpec = xmlPaser.parseTaeDescription( xmlInput );
		TextAnalysisEngine tae = 
				UIMAFramework.produceTAE( taeSpec, resMngr, null );
		Assert.assertTrue( tae != null );
		// create TCAS object
		TCAS tCas = tae.newTCAS();
		Assert.assertTrue( tCas != null );
		// clean-up the results
		pmController.cleanUp();
	}
}
