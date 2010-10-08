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
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.html.HTMLEditorKit.Parser;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import org.ancora.SharedLibrary.Files.LineReader;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Tag;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.TextExtractingVisitor;

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
      File sourceFile = getSourceFile(lineReader);
      if(sourceFile == null) {
         LoggingUtils.getLogger().
                 warning("Skipping file '"+sourceHtml.getPath()+"'.");
         return;
      }

      // Get original contents of the source file
      String sourceContents = parseHtml(sourceHtml);

      // Get output file
      File outputFile = new File(outputFolder, sourceFile.getPath());
      IoUtils.write(outputFile, sourceContents);
   }

   /**
    * Read lines until it finds the header with the name of the file
    *
    * @param lineReader
    * @return
    */
   private static File getSourceFile(LineReader lineReader) {
      // Find Header 1
      String line;
      while((line = lineReader.nextLine()) != null) {
         line = line.trim();
         if(!line.startsWith(HEADER_1_PREFIX)) {
            continue;
         }

         String fileName = processHeaderLine(line);
         // Check if there is a drive letter
         int twoPointsIndex = fileName.indexOf(":");
         if(twoPointsIndex != -1) {
            fileName = fileName.substring(twoPointsIndex+1);
         }
         
         return new File(fileName);
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
      return path;
      //File originalFile = new File(path);
      //return originalFile.getName();
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
      /*
      while ((nameIndex = line.indexOf(NAME_TAG_BEGIN)) != -1) {
         int endNameIndex = line.indexOf(NAME_TAG_END) + NAME_TAG_END.length();
         line = line.substring(endNameIndex);
      }
       *
       */

      int endNameIndex = line.indexOf(NAME_TAG_END)+NAME_TAG_END.length();
      line = line.substring(endNameIndex);

      // Check if it has another name tag
      nameIndex = line.indexOf(NAME_TAG_BEGIN);
      if (nameIndex != -1) {
         endNameIndex = line.indexOf(NAME_TAG_END) + NAME_TAG_END.length();
         line = line.substring(endNameIndex);
      } else {
         // Remove line numbers
         line = line.substring(numberOfDigitsForLines);
      }
      


      // Remove span tags
      int spanIndex;
      /*
      while((spanIndex = line.indexOf(SPAN_TAG_BEGIN)) != -1) {
         // A span tag was found. Get text up to tag, and between tag.
         String preSpan = line.substring(0, spanIndex);
         line = line.substring(spanIndex);
         // Get end of tag
         int tagEndIndex =
      }
*/
      System.out.println(line);

      return null;
   }

   private static String parseHtml(File sourceHtml) {

      // Get String with line numbers
      String sourceWithLineNumbers = htmlToNumberedSource(sourceHtml);

      if(sourceWithLineNumbers == null) {
         return null;
      }

      LineReader lineReader = LineReader.createLineReader(sourceWithLineNumbers);
      StringBuilder builder = new StringBuilder();

      // Get size of line identifier
      String line;
      while((line = lineReader.nextLine()) != null) {
         builder.append(line.substring(numberOfDigitsForLines));
         builder.append("\n");
      }

      return builder.toString();
   }

   private static String parseHtmlV1(File sourceHtml) {
      Html2Text parser = new Html2Text();
      LineReader lineReader = LineReader.createLineReader(sourceHtml);

      // Read lines until we find Header 1
      String line;

      while((line=lineReader.nextLine()) != null) {
         if(line.startsWith(HEADER_1_PREFIX)) {
            break;
         }
      }

      if(line == null) {
         return null;
      }

      StringBuilder builder = new StringBuilder();

      // Parse first line
      line = line.substring(line.indexOf(HEADER_1_END)+HEADER_1_END.length());
      builder.append(parser.parseHtmlString(line));
      builder.append("\n");

      while((line=lineReader.nextLine()) != null) {
         String parsedLine = parser.parseHtmlString(line);
         //String code = processCodeLine(line);
         //if(code == null) {
         //   continue;
         //}

         builder.append(parsedLine);
         builder.append("\n");
      }

      //String out = parser.parseHtmlString(IoUtils.read(sourceHtml));
      //System.out.println(out);
      //return out;
      System.out.println(builder.toString());
      return builder.toString();
   }

   private static String parsePreNode(Node node) {
      System.out.println(node.getChildren());
      return null;
   }

  private static String htmlToNumberedSource(File sourceHtml) {
      String htmlString = IoUtils.read(sourceHtml);
      org.htmlparser.Parser parser;
      try {

         parser = new org.htmlparser.Parser(htmlString);

         /*
         TextExtractingVisitor visitor = new TextExtractingVisitor();
         visitor.
         parser.visitAllNodesWith(visitor);
         String textInPage = visitor.getExtractedText();
         */


               NodeFilter preFilter = new TagNameFilter("DIV");
               NodeList nodeList = parser.parse(preFilter);

               NodeIterator iterator = nodeList.elements();
               Node node;
               while((node = iterator.nextNode()) != null){
                  // Check if first child is "pre"
                  if(node.getFirstChild().getText().startsWith("pre ")) {
                     return node.toPlainTextString();
                  }
               }

               //NodeList nodeList = parser.parse(null);
               //System.out.println(nodeList);
               //System.out.println(nodeList.elementAt(0).getChildren());

      } catch (ParserException ex) {
         LoggingUtils.getLogger().
                 warning("Error while parsing html file:"+ex.getMessage());
         return null;
      }

LoggingUtils.getLogger().
        warning("Could not find source code in html file.");
return null;

      //Lexer lexer = new Lexer(htmlString);
      //Node node;

      // Find "pre" node
      /*
      try {
         while ((node = lexer.nextNode()) != null) {
               Set<Class> interfaces = new HashSet<Class>(Arrays.asList(node.getClass().getInterfaces()));
               if(interfaces.contains(Tag.class)) {
                  String preText = "pre ";
                  if(node.getText().startsWith(preText)) {
                     return parsePreNode(node);
                  }
               }
            //System.out.println("Node:"+node.getText());
            //System.out.println("Interfaces:"+Arrays.toString(node.getClass().getInterfaces()));
         }
      } catch (ParserException ex) {
         LoggingUtils.getLogger().
                 warning("ParserException:"+ex.getMessage());
      }
       *
       */
   }

   public final static String HTML_EXTENSION = ".html";
   public final static String HTML_SOURCE_SUFFIX = "source"+HTML_EXTENSION;
   public final static String HEADER_1_PREFIX = "<h1>";
   public final static String HEADER_1_END = "</h1>";
   public final static String NAME_TAG_BEGIN = "<a ";
   public final static String NAME_TAG_END = "</a>";
   public final static String SPAN_TAG_BEGIN = "<span ";
   public final static String SPAN_TAG_END = "</span>";
   public final static String TAG_FINALIZER = ">";
   public final static int numberOfDigitsForLines = 5;











}
