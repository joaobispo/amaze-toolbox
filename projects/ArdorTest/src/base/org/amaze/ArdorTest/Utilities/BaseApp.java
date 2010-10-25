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



   public BaseApp() {
   }


   
   /**
    * Builds a BaseScene from a PropertiesGameSettings preferences
    * 
    * @param preferences
    * @return
    */
   public static BaseApp newBaseScene(PropertiesGameSettings prefs, ExtendedApp app) {
      // Create object to start filling
      BaseApp baseScene = new BaseApp();

      baseScene.dataScene = new BaseScene(baseScene);
      //baseScene._root = new Node();
      baseScene.addRenderStatesToRoot(baseScene.dataScene.getRootNode(), baseScene.dataScene);
      baseScene.app = app;
      baseScene.updaterClass = new UpdateClass(baseScene);
      baseScene._exit = false;


      baseScene.logicalLayer = new LogicalLayer();
      Timer timer = new Timer();
      baseScene.frameHandler = new FrameHandler(timer);
      

      baseScene.displaySettings = Settings.newDisplaySetting(prefs);

      if (LWJGL_STRING.equalsIgnoreCase(prefs.getRenderer())) {
         configLWJGL(baseScene, baseScene.displaySettings, baseScene.dataScene);
      } else if (JOGL_STRING.equalsIgnoreCase(prefs.getRenderer())) {
         configJOGL(baseScene, baseScene.displaySettings, baseScene.dataScene);
      } else {
         LoggingUtils.getLogger().
                 warning("Renderer not supported: '" + prefs.getRenderer() + "'");
         return null;
      }

      baseScene.logicalLayer.registerInput(baseScene.nativeCanvas,
              baseScene.physicalLayer);

      // Register our example as an updater.
      baseScene.frameHandler.addUpdater(baseScene.updaterClass);

      // register our native canvas
      baseScene.frameHandler.addCanvas(baseScene.nativeCanvas);

      return baseScene;
   }

   private static void configLWJGL(BaseApp baseScene, DisplaySettings displaySettings,
           Scene dataScene) {
      final LwjglCanvasRenderer canvasRenderer = new LwjglCanvasRenderer(dataScene);
      baseScene.nativeCanvas = new LwjglCanvas(canvasRenderer, displaySettings);
      baseScene.physicalLayer = new PhysicalLayer(new LwjglKeyboardWrapper(), new LwjglMouseWrapper(),
              new LwjglControllerWrapper(), (LwjglCanvas) baseScene.nativeCanvas);
      baseScene.mouseManager = new LwjglMouseManager();
      TextureRendererFactory.INSTANCE.setProvider(new LwjglTextureRendererProvider());
   }


   private static void configJOGL(BaseApp baseScene, DisplaySettings displaySettings,
           Scene dataScene) {
      final JoglCanvasRenderer canvasRenderer = new JoglCanvasRenderer(dataScene);
      baseScene.nativeCanvas = new JoglCanvas(canvasRenderer, displaySettings);
      final JoglCanvas canvas = (JoglCanvas) baseScene.nativeCanvas;
      baseScene.mouseManager = new AwtMouseManager(canvas);
      baseScene.physicalLayer = new PhysicalLayer(new AwtKeyboardWrapper(canvas), new AwtMouseWrapper(canvas,
              baseScene.mouseManager), DummyControllerWrapper.INSTANCE, new AwtFocusWrapper(canvas));
      TextureRendererFactory.INSTANCE.setProvider(new JoglTextureRendererProvider());
   }

   public void initFpsControl() {
      firstPersonControl = FirstPersonControl.setupTriggers(logicalLayer, _worldUp, true);
   }


    public void exit() {
        _exit = true;
    }
   
   public DisplaySettings getDisplaySettings() {
      return displaySettings;
   }

   public FrameHandler getFrameHandler() {
      return frameHandler;
   }

   public LogicalLayer getLogicalLayer() {
      return logicalLayer;
   }

   public MouseManager getMouseManager() {
      return mouseManager;
   }

   public NativeCanvas getNativeCanvas() {
      return nativeCanvas;
   }

   public PhysicalLayer getPhysicalLayer() {
      return physicalLayer;
   }

   /*
   public LightState getLightState() {
      return _lightState;
   }
    *
    */

   
   public Node getRoot() {
      return dataScene.getRootNode();
   }

   public ExtendedApp getExtendedApp() {
      return app;
   }

   public FirstPersonControl getFirstPersonControl() {
      return firstPersonControl;
   }

   public boolean isExit() {
      return _exit;
   }

   public BaseScene getDataScene() {
      return dataScene;
   }

   private void addRenderStatesToRoot(Node node, BaseScene dataScene) {
      node.setRenderState(dataScene.getLightState());
      node.setRenderState(dataScene.getWireframeState());
   }



   private DisplaySettings displaySettings;
   private NativeCanvas nativeCanvas;
   private PhysicalLayer physicalLayer;
   private MouseManager mouseManager;
   private LogicalLayer logicalLayer;
   private FrameHandler frameHandler;
//   private LightState _lightState;
//   private WireframeState _wireframeState;
   private BaseScene dataScene;
   private Updater updaterClass;
   //private Node _root;
   private ExtendedApp app;
   private FirstPersonControl firstPersonControl;

   private volatile boolean _exit;

   private static final String LWJGL_STRING = "LWJGL";
   private static final String JOGL_STRING = "JOGL";
   private static final Vector3 _worldUp = new Vector3(0, 1, 0);



}
