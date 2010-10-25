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

package org.amaze.ArdorTest;

import com.ardor3d.example.PropertiesGameSettings;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.amaze.ArdorTest.Utilities.BaseRunner;
import org.amaze.ArdorTest.Utilities.BaseApp;
import org.amaze.ArdorTest.Utilities.ExtendedApp;
import org.amaze.ArdorTest.Utilities.Settings;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class LoadModelTest implements ExtendedApp {

   public LoadModelTest() {
   }



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      LoadModelTest modelTest = new LoadModelTest();
      modelTest.execute();
    }

   private void execute() {
      LoggingUtils.setupConsoleOnly();
      init();
   }

   /**
    * Initialization
    */
   private void init() {
      // Load game properties
      PropertiesGameSettings gameProperties = new PropertiesGameSettings("ardorSettings.properties", null);
      final PropertiesGameSettings prefs = Settings.getPreferencesWithWindow(gameProperties, this.getClass());

      // Get BaseScene
      BaseApp baseScene = BaseApp.newBaseScene(prefs, this);
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.submit(new BaseRunner(baseScene));
   }

   public void initApp() {
      throw new UnsupportedOperationException("Not supported yet.");
   }


}
