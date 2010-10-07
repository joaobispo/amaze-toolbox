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
import java.util.Map;
import org.ancora.SharedLibrary.AppBase.App;
import org.ancora.SharedLibrary.AppBase.AppUtils;
import org.ancora.SharedLibrary.AppBase.AppValue;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Joao Bispo
 */
public class DoxygenSourceExtractor implements App {

   public int execute(Map<String, AppValue> options) {
      
      // Get input folder
      String folderName = AppUtils.getString(options, AppOptions.InputFolder);
      File folder = new File(folderName);
      if(!folder.isDirectory()) {
         LoggingUtils.getLogger().
                 warning("Could not open input folder '"+folder.getPath()+"'");
         return 1;
      }
      
      // Get output folder
      String outputFoldername = AppUtils.getString(options, AppOptions.OutputFolder);
      File outputFolder = IoUtils.safeFolder(outputFoldername);
      if(outputFolder == null) {
         LoggingUtils.getLogger().
                 warning("Could not open output folder '"+outputFoldername+"'");
         return 1;
      }

      /*
      File outputFolder = new File(outputFoldername);
      if(!outputFolder.isDirectory()) {
         LoggingUtils.getLogger().
                 warning("Could not open output folder '"+outputFolder.getPath()+"'");
         return 1;
      }
       *
       */


      // Get all source html files inside folders
      List<File> sourceHtmlFiles = Utils.getSourceHtml(folder);

      // Process each file and extract the source code
      for(File sourceHtml : sourceHtmlFiles) {
         Utils.extractSource(sourceHtml, outputFolder);
      }

      //System.out.println("Hello");
      return 0;
   }

   public Class getAppOptionEnum() {
      return AppOptions.class;
   }


}
