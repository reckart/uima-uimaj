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

package org.apache.uima.cas.impl;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.Arrays;

import org.apache.uima.internal.util.Misc;

import com.strobel.assembler.InputTypeLoader;
import com.strobel.assembler.metadata.Buffer;
import com.strobel.assembler.metadata.CompositeTypeLoader;
import com.strobel.assembler.metadata.ITypeLoader;
import com.strobel.decompiler.Decompiler;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;

/**
 * Decompiler
 *   - for testing
 *   - for locating customizations
 *
 * Operation:
 *   Make an instance, optionally setting
 *     - class loader to use (may pass byte array instead)
 *     - directory where to write output (may output to string instead)
 *   
 *   call decompile
 *     - argument
 *         - class name (without .class or .java suffix, fully qualified) or
 *         - byte array
 *     - return value is a byte array output stream with UTF-8 encoded value
 *     
 *   decompileToFile - writes decompiled output to a xxx.java file in output directory
 *   
 * Not thread safe
 */
public class UimaDecompiler {
  
  private final static byte[] errorMsg;
  static {
    byte[] temp = null;
    try {
      temp = "!!! ERROR: Failed to load class".getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
    }
    errorMsg = temp;
  }

  
  private final DecompilerSettings decompilerSettings = DecompilerSettings.javaDefaults();
  { 
    decompilerSettings.setMergeVariables(true);
    decompilerSettings.setSimplifyMemberReferences(true);
  }

  private final ClassLoader classLoader;
  
  private File outputDirectory = null;
  
  public UimaDecompiler() {
    classLoader = null;
  }
  
  public UimaDecompiler(ClassLoader classLoader, File outputDirectory) {
    this.classLoader = classLoader;
    this.outputDirectory = outputDirectory;
    if (classLoader != null) {
      setDecompilerSettingsForClassLoader();
    }
  }
    
  public ByteArrayOutputStream decompile(String className, byte[] byteArray) {
    setDecompilerSettingsForByteArray(className.replace('.', '/'), byteArray);
    return decompileCommon(className);
  }
  
  public ByteArrayOutputStream decompile(String className) {
    setDecompilerSettingsForClassLoader();
    return decompileCommon(className);
  }
  
  public ByteArrayOutputStream decompileCommon(String className) {
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PlainTextOutput plainTextOutput = null;
    BufferedWriter writer = null;
    try {
      plainTextOutput = 
          new PlainTextOutput(
            writer = new BufferedWriter(
              new OutputStreamWriter(baos, "UTF-8")));
      Decompiler.decompile(className.replace('.', '/'), plainTextOutput, decompilerSettings);
      writer.close();
    } catch (IOException e) {
      throw new RuntimeException(); // can't happen
    }
    
    return baos;
  }
  
  /**
   * Decompile from the file system, maybe in a Jar.
   * @param path the Path to the file to decompile
   * @return the decompiled form as a string
   */
  public String decompile(byte[] b) {
    PlainTextOutput pto = new PlainTextOutput();
    
    String classNameSlashes = extractClassNameSlashes(b);
    setDecompilerSettingsForByteArray(classNameSlashes, b);
    Decompiler.decompile(classNameSlashes, pto, decompilerSettings);
    String s = pto.toString();
    
    String packageName = "";
    String className = "";
    int ip = s.indexOf("package ");
    if (ip >= 0) {
      ip = ip + "package ".length();  // start of package name;
      int ipe = s.indexOf(";", ip);  
      packageName = s.substring(ip, ipe).replace('.', '/') + "/";
    }
    
    int ic = s.indexOf(" class ");
    if (ic >= 0) {
      ic = ic + " class ".length();  // start of class name
      int ice = s.indexOf(" ", ic);
      className = s.substring(ic, ice);
    }
    
    String classNameSlashes2 = packageName + className;
    
    if (!classNameSlashes2.equals(classNameSlashes)) {
//      System.out.println("debug trying classname: " + classNameSlashes2);
      pto = new PlainTextOutput();
      setDecompilerSettingsForByteArray(classNameSlashes2, b);
      Decompiler.decompile(classNameSlashes2, pto, decompilerSettings);
      s = pto.toString(); 
    }
    return s;
  }
  
  public String extractClassNameSlashes(byte[] b) {
    if (b[10] != 7 || b[13] != 1) { 
      return "";
    }
    int length = b[14] * 16 + b[15];
    try {
      return new String(b, 16, length, "UTF-8");      
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
  
  public boolean decompileToOutputDirectory(String className) {
    ByteArrayOutputStream baos = decompile(className);
    return writeIfOk(baos, className);
  }

  public boolean decompileToOutputDirectory(String className, byte[] byteArray) {
    ByteArrayOutputStream baos = decompile(className, byteArray);
    return writeIfOk(baos, className);
  }

  private void setDecompilerSettingsForByteArray(String classNameSlashes, byte[] byteArray) {
    ITypeLoader tl = new ITypeLoader() {
      
      @Override
      public boolean tryLoadType(String internalName, Buffer buffer) {
        if (classNameSlashes.equals(internalName)) {
          int length = byteArray.length;
          buffer.reset(length);
          System.arraycopy(byteArray, 0, buffer.array(), 0, length);
          return true;
        } else {
          return false;
        }
      }      
    }; 
    ITypeLoader tc = new CompositeTypeLoader(tl, new InputTypeLoader());
    decompilerSettings.setTypeLoader(tl);
  }
    
  public boolean writeIfOk(ByteArrayOutputStream baos, String className) {
    if (!decompiledFailed(baos)) {
      Misc.toFile(baos, new File(outputDirectory, className));
      return true;
    }
    return false;    
  }
  
  public boolean decompiledFailed(ByteArrayOutputStream baos) {
    return (baos.size() == errorMsg.length && Arrays.equals(errorMsg, baos.toByteArray())); 
  }
  
  private void setDecompilerSettingsForClassLoader() {
    ITypeLoader tl = new ITypeLoader() {
      
      @Override
      public boolean tryLoadType(String internalName, Buffer buffer) {
        
        // read the class as a resource, and put into temporary byte array output stream
        // because we need to know the length
        
        internalName = internalName.replace('.', '/') + ".class";
        InputStream stream = classLoader.getResourceAsStream(internalName);
        if (stream == null) {
          return false;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 16);        
        byte[] b = new byte[1024 * 16]; 
        int numberRead;
        try {
          while (0 <= (numberRead = stream.read(b))){
            baos.write(b, 0, numberRead);
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        
        // Copy result (based on length) into output buffer spot
        int length = baos.size();
        b = baos.toByteArray();
        buffer.reset(length);
        System.arraycopy(b, 0, buffer.array(), 0, length);
        
        return true;
      }      
    };    
    decompilerSettings.setTypeLoader(tl);
  }
}