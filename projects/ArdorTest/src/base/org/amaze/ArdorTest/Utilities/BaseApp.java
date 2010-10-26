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

package org.amaze.ArdorTest.Utilities;

import com.ardor3d.input.control.FirstPersonControl;
import com.ardor3d.math.Vector3;

/**
 *
 * @author Joao Bispo
 */
public class BaseApp {



   public BaseApp(BaseScene baseScene) {
      dataScene = baseScene;
//      _exit = false;
   }



   public void initFpsControl() {
      firstPersonControl = FirstPersonControl.setupTriggers(dataScene.getLogicalLayer(), _worldUp, true);
   }


   /*
    public void exit() {
        _exit = true;
    }
    *
    */
 /*
   public DisplaySettings getDisplaySettings() {
      return dataScene.getDisplaySettings();
   }

   public FrameHandler getFrameHandler() {
      return frameHandler;
   }

   public LogicalLayer getLogicalLayer() {
      return dataScene.getLogicalLayer();
   }

   public MouseManager getMouseManager() {
      return dataScene.getMouseManager();
   }

   public NativeCanvas getNativeCanvas() {
      return dataScene.getNativeCanvas();
   }

   public PhysicalLayer getPhysicalLayer() {
      return dataScene.getPhysicalLayer();
   }
  
   public Node getRoot() {
      return dataScene.getRootNode();
   }
*/
   public FirstPersonControl getFirstPersonControl() {
      return firstPersonControl;
   }

   /*
   public boolean isExit() {
      return _exit;
   }
    * 
    */

   public BaseScene getScene() {
      return dataScene;
   }


   //protected FrameHandler frameHandler;
   private BaseScene dataScene;
   //protected  Updater updaterClass;
   protected  FirstPersonControl firstPersonControl;

   //private volatile boolean _exit;


   private static final Vector3 _worldUp = new Vector3(0, 1, 0);



}
