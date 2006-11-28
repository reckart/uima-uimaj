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

package org.apache.uima.typesystem;

import java.util.StringTokenizer;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultLineTracker;
import org.eclipse.jface.text.ILineTracker;
import org.eclipse.jface.text.IRegion;

/**
 * Helper class to provide String manipulation functions not available in standard JDK.
 */
public class Strings {

  private Strings() {
  }

  /**
   * Indent char is a space char but not a line delimiters.
   * <code>== Character.isWhitespace(ch) && ch != '\n' && ch != '\r'</code>
   */
  public static boolean isIndentChar(char ch) {
    return Character.isWhitespace(ch) && !isLineDelimiterChar(ch);
  }

  /**
   * tests if a char is lower case. Fix for 26529
   */
  public static boolean isLowerCase(char ch) {
    return Character.toLowerCase(ch) == ch;
  }

  /**
   * Line delimiter chars are '\n' and '\r'.
   */
  public static boolean isLineDelimiterChar(char ch) {
    return ch == '\n' || ch == '\r';
  }

  public static String removeNewLine(String message) {
    StringBuffer result = new StringBuffer();
    int current = 0;
    int index = message.indexOf('\n', 0);
    while (index != -1) {
      result.append(message.substring(current, index));
      if (current < index && index != 0)
        result.append(' ');
      current = index + 1;
      index = message.indexOf('\n', current);
    }
    result.append(message.substring(current));
    return result.toString();
  }

  /**
   * Converts the given string into an array of lines. The lines don't contain any line delimiter
   * characters.
   * 
   * @return the string converted into an array of strings. Returns <code>
   * 	null</code> if the input
   *         string can't be converted in an array of lines.
   */
  public static String[] convertIntoLines(String input) {
    try {
      ILineTracker tracker = new DefaultLineTracker();
      tracker.set(input);
      int size = tracker.getNumberOfLines();
      String result[] = new String[size];
      for (int i = 0; i < size; i++) {
        IRegion region = tracker.getLineInformation(i);
        int offset = region.getOffset();
        result[i] = input.substring(offset, offset + region.getLength());
      }
      return result;
    } catch (BadLocationException e) {
      return null;
    }
  }

  /**
   * Returns <code>true</code> if the given string only consists of white spaces according to
   * Java. If the string is empty, <code>true
   * </code> is returned.
   * 
   * @return <code>true</code> if the string only consists of white spaces; otherwise
   *         <code>false</code> is returned
   * 
   * @see java.lang.Character#isWhitespace(char)
   */
  public static boolean containsOnlyWhitespaces(String s) {
    int size = s.length();
    for (int i = 0; i < size; i++) {
      if (!Character.isWhitespace(s.charAt(i)))
        return false;
    }
    return true;
  }

  /**
   * Removes leading tabs and spaces from the given string. If the string doesn't contain any
   * leading tabs or spaces then the string itself is returned.
   */
  public static String trimLeadingTabsAndSpaces(String line) {
    int size = line.length();
    int start = size;
    for (int i = 0; i < size; i++) {
      char c = line.charAt(i);
      if (!isIndentChar(c)) {
        start = i;
        break;
      }
    }
    if (start == 0)
      return line;
    else if (start == size)
      return ""; //$NON-NLS-1$
    else
      return line.substring(start);
  }

  public static String trimTrailingTabsAndSpaces(String line) {
    int size = line.length();
    int end = size;
    for (int i = size - 1; i >= 0; i--) {
      char c = line.charAt(i);
      if (isIndentChar(c)) {
        end = i;
      } else {
        break;
      }
    }
    if (end == size)
      return line;
    else if (end == 0)
      return ""; //$NON-NLS-1$
    else
      return line.substring(0, end);
  }

  /**
   * Returns the indent of the given string.
   * 
   * @param line
   *          the text line
   * @param tabWidth
   *          the width of the '\t' character.
   */
  public static int computeIndent(String line, int tabWidth) {
    int result = 0;
    int blanks = 0;
    int size = line.length();
    for (int i = 0; i < size; i++) {
      char c = line.charAt(i);
      if (c == '\t') {
        result++;
        blanks = 0;
      } else if (isIndentChar(c)) {
        blanks++;
        if (blanks == tabWidth) {
          result++;
          blanks = 0;
        }
      } else {
        return result;
      }
    }
    return result;
  }

  /**
   * Removes the given number of idents from the line. Asserts that the given line has the requested
   * number of indents. If <code>indentsToRemove <= 0</code> the line is returned.
   */
  public static String trimIndent(String line, int indentsToRemove, int tabWidth) {
    if (line == null || indentsToRemove <= 0)
      return line;

    int start = 0;
    int indents = 0;
    int blanks = 0;
    int size = line.length();
    for (int i = 0; i < size; i++) {
      char c = line.charAt(i);
      if (c == '\t') {
        indents++;
        blanks = 0;
      } else if (isIndentChar(c)) {
        blanks++;
        if (blanks == tabWidth) {
          indents++;
          blanks = 0;
        }
      } else {
        // Assert.isTrue(false, "Line does not have requested number of indents"); //$NON-NLS-1$
        start = i;
        break;
      }
      if (indents == indentsToRemove) {
        start = i + 1;
        break;
      }
    }
    if (start == size)
      return ""; //$NON-NLS-1$
    else
      return line.substring(start);
  }

  /**
   * Removes the common number of indents from all lines. If a line only consists out of white space
   * it is ignored.
   */
  public static void trimIndentation(String[] lines, int tabWidth) {
    trimIndentation(lines, tabWidth, true);
  }

  /**
   * Removes the common number of indents from all lines. If a line only consists out of white space
   * it is ignored. If <code>
   * considerFirstLine</code> is false the first line will be ignored.
   */
  public static void trimIndentation(String[] lines, int tabWidth, boolean considerFirstLine) {
    String[] toDo = new String[lines.length];
    // find indentation common to all lines
    int minIndent = Integer.MAX_VALUE; // very large
    for (int i = considerFirstLine ? 0 : 1; i < lines.length; i++) {
      String line = lines[i];
      if (containsOnlyWhitespaces(line))
        continue;
      toDo[i] = line;
      int indent = computeIndent(line, tabWidth);
      if (indent < minIndent) {
        minIndent = indent;
      }
    }

    if (minIndent > 0) {
      // remove this indent from all lines
      for (int i = considerFirstLine ? 0 : 1; i < toDo.length; i++) {
        String s = toDo[i];
        if (s != null)
          lines[i] = trimIndent(s, minIndent, tabWidth);
        else {
          String line = lines[i];
          int indent = computeIndent(line, tabWidth);
          if (indent > minIndent)
            lines[i] = trimIndent(line, minIndent, tabWidth);
          else
            lines[i] = trimLeadingTabsAndSpaces(line);
        }
      }
    }
  }

  public static String getIndentString(String line, int tabWidth) {
    int size = line.length();
    int end = 0;
    int blanks = 0;
    for (int i = 0; i < size; i++) {
      char c = line.charAt(i);
      if (c == '\t') {
        end = i + 1;
        blanks = 0;
      } else if (isIndentChar(c)) {
        blanks++;
        if (blanks == tabWidth) {
          end = i + 1;
          blanks = 0;
        }
      } else {
        break;
      }
    }
    if (end == 0)
      return ""; //$NON-NLS-1$
    else if (end == size)
      return line;
    else
      return line.substring(0, end);
  }

  /**
   * Returns the length of the string representing the number of indents in the given string
   * <code>line</code>. Returns <code>-1<code> if the line isn't prefixed with an indent of
   * the given number of indents. 
   * @throws Exception
   */
  public static int computeIndentLength(String line, int numberOfIndents, int tabWidth)
          throws Exception {
    if (!(numberOfIndents >= 0) || !(tabWidth >= 0)) {
      throw new Exception("Assert Failed");
    }
    int size = line.length();
    int result = -1;
    int indents = 0;
    int blanks = 0;
    for (int i = 0; i < size && indents < numberOfIndents; i++) {
      char c = line.charAt(i);
      if (c == '\t') {
        indents++;
        result = i;
        blanks = 0;
      } else if (isIndentChar(c)) {
        blanks++;
        if (blanks == tabWidth) {
          result = i;
          indents++;
          blanks = 0;
        }
      } else {
        break;
      }
    }
    if (indents < numberOfIndents)
      return -1;
    return result + 1;
  }

  public static String[] removeTrailingEmptyLines(String[] sourceLines) {
    int lastNonEmpty = findLastNonEmptyLineIndex(sourceLines);
    String[] result = new String[lastNonEmpty + 1];
    for (int i = 0; i < result.length; i++) {
      result[i] = sourceLines[i];
    }
    return result;
  }

  private static int findLastNonEmptyLineIndex(String[] sourceLines) {
    for (int i = sourceLines.length - 1; i >= 0; i--) {
      if (!sourceLines[i].trim().equals(""))//$NON-NLS-1$
        return i;
    }
    return -1;
  }

  /**
   * Change the indent of, possible muti-line, code range. The current indent is removed, a new
   * indent added. The first line of the code will not be changed. (It is considered to have no
   * indent as it might start in the middle of a line)
   */
  public static String changeIndent(String code, int codeIndentLevel, int tabWidth,
          String newIndent, String lineDelim) {
    try {
      ILineTracker tracker = new DefaultLineTracker();
      tracker.set(code);
      int nLines = tracker.getNumberOfLines();
      if (nLines == 1) {
        return code;
      }

      StringBuffer buf = new StringBuffer();

      for (int i = 0; i < nLines; i++) {
        IRegion region = tracker.getLineInformation(i);
        int start = region.getOffset();
        int end = start + region.getLength();
        String line = code.substring(start, end);

        if (i == 0) { // no indent for first line (contained in the formatted string)
          buf.append(line);
        } else { // no new line after last line
          buf.append(lineDelim);
          buf.append(newIndent);
          buf.append(trimIndent(line, codeIndentLevel, tabWidth));
        }
      }
      return buf.toString();
    } catch (BadLocationException e) {
      // can not happen
      return code;
    }
  }

  public static String trimIndentation(String source, int tabWidth, boolean considerFirstLine)
          throws Exception {
    try {
      ILineTracker tracker = new DefaultLineTracker();
      tracker.set(source);
      int size = tracker.getNumberOfLines();
      if (size == 1)
        return source;
      String lines[] = new String[size];
      for (int i = 0; i < size; i++) {
        IRegion region = tracker.getLineInformation(i);
        int offset = region.getOffset();
        lines[i] = source.substring(offset, offset + region.getLength());
      }
      Strings.trimIndentation(lines, tabWidth, considerFirstLine);
      StringBuffer result = new StringBuffer();
      int last = size - 1;
      for (int i = 0; i < size; i++) {
        result.append(lines[i]);
        if (i < last)
          result.append(tracker.getLineDelimiter(i));
      }
      return result.toString();
    } catch (BadLocationException e) {
      throw new Exception("Bad Location");
      // return null;
    }
  }

  /**
   * Concatenate the given strings into one strings using the passed line delimiter as a delimiter.
   * No delimiter is added to the last line.
   */
  public static String concatenate(String[] lines, String delimiter) {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < lines.length; i++) {
      if (i > 0)
        buffer.append(delimiter);
      buffer.append(lines[i]);
    }
    return buffer.toString();
  }

  public static boolean equals(String s, char[] c) {
    if (s.length() != c.length)
      return false;

    for (int i = c.length; --i >= 0;)
      if (s.charAt(i) != c[i])
        return false;
    return true;
  }

  public static String[] splitByToken(String fullString, String splitToken) {
    StringTokenizer tokenizer = new StringTokenizer(fullString, splitToken);
    String[] tokens = new String[tokenizer.countTokens()];
    for (int i = 0; tokenizer.hasMoreTokens(); i++) {
      tokens[i] = tokenizer.nextToken();
    }
    return tokens;
  }
}
