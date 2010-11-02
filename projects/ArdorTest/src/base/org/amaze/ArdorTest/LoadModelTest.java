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

import com.ardor3d.framework.Canvas;
import com.ardor3d.framework.Updater;
import com.ardor3d.input.control.FirstPersonControl;
import com.ardor3d.input.logical.AnyKeyCondition;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TwoInputStates;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import org.amaze.ArdorTest.BaseImplementations.BaseScene;
import org.amaze.ArdorTest.DataObjects.BaseData;
import org.amaze.ArdorTest.BaseImplementations.BaseRunner;
import org.amaze.ArdorTest.BaseImplementations.BaseUpdater;
import org.amaze.ArdorTest.Utilities.ExtendedApp;
import org.amaze.ArdorTest.Utilities.InputUtils;
import org.amaze.ArdorTest.Utilities.SettingsUtils;
import org.amaze.ArdorTests.Test1.Test1Updater;
import org.amaze.ArdorTests.Test2.Test2Updater;
import org.ancora.SharedLibrary.LoggingUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.OpenGLException;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class LoadModelTest  {

   public LoadModelTest() {
   }



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      // try {
      LoadModelTest modelTest = new LoadModelTest();
      modelTest.execute(); //}
    /*
    catch(OpenGLException ex) {
          StringBuilder message = new StringBuilder();
          message.append("Found an OpenGL Exception. Make sure your graphic card ");
          message.append("supports OpenGL and that you have the latest drivers.");
          message.append("\n");
          message.append("\n");
          message.append("Exception:");
          message.append(ex.getMessage());
          JOptionPane.showMessageDialog(null, message.toString());
         System.exit(1);
       } finally {
          System.out.println("GLError:"+GL11.glGetError());
       }
     * 
     */
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
      BaseData baseData = SettingsUtils.newDataObjects(prefs, baseScene);

      //changeInput(baseData);
      // Get BaseScene
      //BaseScene baseScene = BaseScene.newBaseScene(prefs);
      //BaseApp baseApp = new BaseApp(baseScene);

      //Updater updater = new BaseUpdater(baseData);
      //Updater updater = new Test1Updater(baseData);
      Updater updater = new Test2Updater(baseData);
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.submit(new BaseRunner(updater, baseData));
   }

   public void initApp() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   
   private void changeInput(BaseData baseData) {
      LogicalLayer _logicalLayer = baseData.basicInput.logicalLayer;
      InputTrigger inputTrigger = new InputTrigger(new AnyKeyCondition(), new TriggerAction() {
            public void perform(final Canvas source, final TwoInputStates inputState, final double tpf) {
               System.out.println("Additional Message");
               //System.out.println("Key character pressed: "
               //         + inputState.getCurrent().getKeyboardState().getKeyEvent().getKeyChar());
            }
        });


      System.out.println("Triggers:");
      for(InputTrigger in : _logicalLayer.getTriggers()) {
         System.out.println(in);
         _logicalLayer.deregisterTrigger(in);
      }
      System.out.println("After:"+_logicalLayer.getTriggers());
      _logicalLayer.registerTrigger(inputTrigger);
      _logicalLayer.deregisterTrigger(inputTrigger);
   }


}
