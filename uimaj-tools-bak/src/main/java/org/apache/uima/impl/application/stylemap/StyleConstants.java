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

package org.apache.uima.impl.application.stylemap;

/**
 * @author J W Cooper - IBM T J Watson Research
 *
 * 
 */
public class StyleConstants {
	public static final int NR_TABLE_COLUMNS = 7;
	// Column zero is for visual indication of selection
	public static final int LABEL_COLUMN = 1;
	public static final int TYPE_NAME_COLUMN = 2;
	//public static final int FEATURE_VALUE_COLUMN = 3;
	public static final int BG_COLUMN = 3;
	public static final int FG_COLUMN = 4;
	public static final int CHECK_COLUMN = 5;	//check box column
	public static final int HIDDEN_COLUMN = 6;	//hide these
	static final String[] columnNames =
		{ "|", "Annotation Label", "Annotation Type / Feature",  "Background", "Foreground", "Checked", "Hidden" };

}
