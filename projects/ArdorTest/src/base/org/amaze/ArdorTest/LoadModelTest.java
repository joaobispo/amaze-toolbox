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
import com.ardor3d.framework.Updater;
import com.ardor3d.renderer.state.RenderState;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.amaze.ArdorTest.DataObjects.BasicInput;
import org.amaze.ArdorTest.DataObjects.ScreenData;
import org.amaze.ArdorTest.Utilities.BaseRunner;
import org.amaze.ArdorTest.Utilities.BaseApp;
import org.amaze.ArdorTest.Utilities.BaseScene;
import org.amaze.ArdorTest.Utilities.BaseUpdater;
import org.amaze.ArdorTest.Utilities.ExtendedApp;
import org.amaze.ArdorTest.Utilities.SettingsUtils;
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
      final PropertiesGameSettings prefs = SettingsUtils.getPreferencesWithWindow(gameProperties, this.getClass());

      BaseScene baseScene = new BaseScene();
      Object[] objects = SettingsUtils.newDataObjects(prefs, baseScene);
      ScreenData screenData = (ScreenData) objects[0];
      BasicInput basicInput = (BasicInput) objects[1];
      RenderState renderState = (RenderState) objects[2];

      // Get BaseScene
      //BaseScene baseScene = BaseScene.newBaseScene(prefs);
      //BaseApp baseApp = new BaseApp(baseScene);

      Updater updater = new BaseUpdater(baseApp);
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.submit(new BaseRunner(baseScene, updater));
   }

   public void initApp() {
      throw new UnsupportedOperationException("Not supported yet.");
   }


}
