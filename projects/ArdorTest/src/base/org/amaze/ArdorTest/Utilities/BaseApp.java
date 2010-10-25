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

import com.ardor3d.example.PropertiesGameSettings;
import com.ardor3d.framework.DisplaySettings;
import com.ardor3d.framework.FrameHandler;
import com.ardor3d.framework.NativeCanvas;
import com.ardor3d.framework.Scene;
import com.ardor3d.framework.Updater;
import com.ardor3d.framework.jogl.JoglCanvas;
import com.ardor3d.framework.jogl.JoglCanvasRenderer;
import com.ardor3d.framework.lwjgl.LwjglCanvas;
import com.ardor3d.framework.lwjgl.LwjglCanvasRenderer;
import com.ardor3d.input.MouseManager;
import com.ardor3d.input.PhysicalLayer;
import com.ardor3d.input.awt.AwtFocusWrapper;
import com.ardor3d.input.awt.AwtKeyboardWrapper;
import com.ardor3d.input.awt.AwtMouseManager;
import com.ardor3d.input.awt.AwtMouseWrapper;
import com.ardor3d.input.control.FirstPersonControl;
import com.ardor3d.input.logical.DummyControllerWrapper;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.input.lwjgl.LwjglControllerWrapper;
import com.ardor3d.input.lwjgl.LwjglKeyboardWrapper;
import com.ardor3d.input.lwjgl.LwjglMouseManager;
import com.ardor3d.input.lwjgl.LwjglMouseWrapper;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.TextureRendererFactory;
import com.ardor3d.renderer.jogl.JoglTextureRendererProvider;
import com.ardor3d.renderer.lwjgl.LwjglTextureRendererProvider;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.util.Timer;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Joao Bispo
 */
public class BaseApp {



   public BaseApp(BaseScene baseScene) {
      dataScene = baseScene;
      _exit = false;
   }


   
   /**
    * Builds a BaseScene from a PropertiesGameSettings preferences
    * 
    * @param preferences
    * @return
    */

//   public static BaseApp newBaseScene(PropertiesGameSettings prefs, ExtendedApp app) {
      // Create object to start filling
      //BaseApp baseScene = new BaseApp();

     // baseScene.dataScene = BaseScene.newBaseScene(prefs);

   //   baseScene.updaterClass = new BaseUpdater(baseScene);
      //baseScene._exit = false;

/*
      Timer timer = new Timer();
      baseScene.frameHandler = new FrameHandler(timer);
*/

   //   baseScene.logicalLayer.registerInput(baseScene.nativeCanvas,
   //           baseScene.physicalLayer);

      // Register our example as an updater.
    //  baseScene.frameHandler.addUpdater(baseScene.updaterClass);

      // register our native canvas
     // baseScene.frameHandler.addCanvas(baseScene.dataScene.getNativeCanvas());

//      return baseScene;
   //}


   public void initFpsControl() {
      firstPersonControl = FirstPersonControl.setupTriggers(dataScene.getLogicalLayer(), _worldUp, true);
   }


    public void exit() {
        _exit = true;
    }
   
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

   public FirstPersonControl getFirstPersonControl() {
      return firstPersonControl;
   }

   public boolean isExit() {
      return _exit;
   }

   public BaseScene getScene() {
      return dataScene;
   }


   protected FrameHandler frameHandler;
   protected BaseScene dataScene;
   protected  Updater updaterClass;
   protected  FirstPersonControl firstPersonControl;

   private volatile boolean _exit;


   private static final Vector3 _worldUp = new Vector3(0, 1, 0);



}
