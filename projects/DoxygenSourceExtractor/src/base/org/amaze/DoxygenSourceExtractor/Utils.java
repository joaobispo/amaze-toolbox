/*
 *  Copyright 2010 Abstract Maze.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.amaze.DoxygenSourceExtractor;

import java.io.File;
import java.util.List;
import org.ancora.SharedLibrary.Files.LineReader;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Joao Bispo
 */
public class Utils {

   /**
    * 
    * @param folder
    * @return
    */
   public static List<File> getSourceHtml(File folder) {
      return IoUtils.getFilesRecursive(folder, HTML_SOURCE_SUFFIX);
   }

   public static void extractSource(File sourceHtml, File outputFolder) {
      // Open source file in LineReader
      LineReader lineReader = LineReader.createLineReader(sourceHtml);

      // Get name of the file
      String sourceFilename = getSourceFilename(lineReader);
      if(sourceFilename == null) {
         LoggingUtils.getLogger().
                 warning("Skipping file '"+sourceHtml.getPath()+"'.");
         return;
      }

      // Get original contents of the source file
      String sourceContents = getSourceContents(lineReader);
   }

   /**
    * Read lines until it finds the header with the name of the file
    *
    * @param lineReader
    * @return
    */
   private static String getSourceFilename(LineReader lineReader) {
      // Find Header 1
      String line;
      while((line = lineReader.nextLine()) != null) {
         line = line.trim();
         if(line.startsWith(HEADER_1_PREFIX)) {
            return processHeaderLine(line);
         }
      }

      LoggingUtils.getLogger().
              warning("Could not find name of the original source file.");

      return null;
   }

   private static String processHeaderLine(String line) {
      // Remove header tag
      line = line.substring(HEADER_1_PREFIX.length());
      // Get index of closing header tag
      int endIndex = line.indexOf(HEADER_1_END);

      // Path
      String path = line.substring(0, endIndex);
      File originalFile = new File(path);

      return originalFile.getName();
   }


   private static String getSourceContents(LineReader lineReader) {
      StringBuilder builder = new StringBuilder();

      // Go to every line, try to extract a line
      String line;

      while((line=lineReader.nextLine()) != null) {
         String code = processCodeLine(line);
         if(code == null) {
            continue;
         }

         builder.append(code);
         builder.append("\n");
      }

      return builder.toString();
   }

   private static String processCodeLine(String line) {
      // Check if line has name tag
      int nameIndex = line.indexOf(NAME_TAG_BEGIN);
      
      if(nameIndex == -1) {
         return null;
      }

      //Line has name tag; find the end of that
      int endNameIndex = line.indexOf(NAME_TAG_END)+NAME_TAG_END.length();
      line = line.substring(endNameIndex);
      // Remove lines
      line = line.substring(numberOfDigitsForLines);

      // Remove span tags
      System.out.println(line);

      return null;
   }
   
   public final static String HTML_EXTENSION = ".html";
   public final static String HTML_SOURCE_SUFFIX = "source"+HTML_EXTENSION;
   public final static String HEADER_1_PREFIX = "<h1>";
   public final static String HEADER_1_END = "</h1>";
   public final static String NAME_TAG_BEGIN = "<a name";
   public final static String NAME_TAG_END = "</a>";
   public final static String SPAN_TAG_BEGIN = "<span";
   public final static String SPAN_TAG_END = "</span>";
   public final static int numberOfDigitsForLines = 5;





}
