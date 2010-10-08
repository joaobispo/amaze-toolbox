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

/**
 *
 * @author Joao Bispo
 */
import java.io.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;
import org.ancora.SharedLibrary.LoggingUtils;

public class Html2Text extends HTMLEditorKit.ParserCallback {
 StringBuffer s;

 public Html2Text() {

 }

 public void parse(Reader in) throws IOException {
   s = new StringBuffer();
   ParserDelegator delegator = new ParserDelegator();
   // the third parameter is TRUE to ignore charset directive
   delegator.parse(in, this, Boolean.TRUE);
 }

   @Override
 public void handleText(char[] text, int pos) {
   s.append(text);
 }



 public String getText() {
   return s.toString();
 }

 public String parseHtmlString(String htmlString) {
    StringReader r = new StringReader(htmlString);
      try {
         parse(r);
      } catch (IOException ex) {
         LoggingUtils.getLogger().
                 warning("IOException:"+ex.getMessage());
      }
    r.close();
    return getText();
 }

 /*
 public static void main (String[] args) {
   try {
     // the HTML to convert
     FileReader in = new FileReader("java-new.html");
     Html2Text parser = new Html2Text();
     parser.parse(in);
     in.close();
     System.out.println(parser.getText());
   }
   catch (Exception e) {
     e.printStackTrace();
   }
 }
  *
  */
}
