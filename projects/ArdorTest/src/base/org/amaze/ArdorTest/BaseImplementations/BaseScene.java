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

package org.amaze.ArdorTest.BaseImplementations;

import com.ardor3d.example.PropertiesGameSettings;
import com.ardor3d.framework.NativeCanvas;
import com.ardor3d.framework.Scene;
import com.ardor3d.image.TextureStoreFormat;
import com.ardor3d.image.util.ScreenShotImageExporter;
import com.ardor3d.intersection.PickResults;
import com.ardor3d.math.Ray3;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.util.ContextGarbageCollector;
import com.ardor3d.util.GameTaskQueue;
import com.ardor3d.util.GameTaskQueueManager;
import com.ardor3d.util.geom.Debugger;
import com.ardor3d.util.screen.ScreenExporter;
import org.amaze.ArdorTest.DataObjects.ScreenData;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Encapsulates
 *
 * @author Joao Bispo
 */
public class BaseScene implements Scene {

   public BaseScene() {
      //_root = new Node();
      //_lightState = new LightState();
      //_wireframeState = new WireframeState();
      _screenShotExp = new ScreenShotImageExporter();
      //logicalLayer = new LogicalLayer();

      //_exit = false;
      this.screenData = null;
   }

   public void registerScreenData(ScreenData screenData) {
      this.screenData = screenData;
   }

   /*
   public void initRenderStates() {
      _lightState.setEnabled(true);
      _root.setRenderState(_lightState);
      
      _wireframeState.setEnabled(false);
      _root.setRenderState(_wireframeState);
   }
    *
    */

   //public void initScene(PropertiesGameSettings prefs) {
     /*
      // Initialize DisplaySettings
      displaySettings = SettingsUtils.newDisplaySetting(prefs);

      // Initialize OpenGl bindings
      OpenGlWrapper wrapperData = OpenGlWrapper.newData(prefs.getRenderer(),
              displaySettings, this, TextureRendererFactory.INSTANCE);
      if(wrapperData == null) {
         LoggingUtils.getLogger().
                 warning("Could not initialize scene.");
         return;
      }
*/
      //mouseManager = wrapperData.mouseManager;
      //physicalLayer = wrapperData.physicalLayer;
      //nativeCanvas = wrapperData.nativeCanvas;

      //logicalLayer.registerInput(nativeCanvas, physicalLayer);
  // }

   /*
   public static BaseScene newBaseScene(PropertiesGameSettings prefs) {
      BaseScene baseScene = new BaseScene();
      baseScene.initRenderStates();
      baseScene.initScene(prefs);

      return baseScene;
   }
    *
    */

   public boolean renderUnto(Renderer renderer) {
      if(screenData == null) {
         LoggingUtils.getLogger().
                 warning("Cannot render: ScreenData not registered.");
         return false;
      }

      NativeCanvas _canvas = screenData.nativeCanvas;
// Execute renderQueue item
        GameTaskQueueManager.getManager(_canvas.getCanvasRenderer().getRenderContext()).getQueue(GameTaskQueue.RENDER)
                .execute(renderer);

        // Clean up card garbage such as textures, vbos, etc.
        ContextGarbageCollector.doRuntimeCleanup(renderer);

        /** Draw the rootNode and all its children. */
        if (!_canvas.isClosing()) {
            /** Call renderExample in any derived classes. */
            renderExample(renderer);
            renderDebug(renderer);

            if (_doShot) {
                // force any waiting scene elements to be renderer.
                renderer.renderBuckets();
                ScreenExporter.exportCurrentScreen(_canvas.getCanvasRenderer().getRenderer(), _screenShotExp);
                _doShot = false;
            }
            return true;
        } else {
            return false;
        }
   }

   private void renderExample(final Renderer renderer) {
      if(screenData == null) {
         LoggingUtils.getLogger().
                 warning("Cannot render: ScreenData not registered.");
         return;
      }
      //renderer.draw(_root);
      renderer.draw(screenData._root);
   }

   private void renderDebug(final Renderer renderer) {
      if (screenData == null) {
         LoggingUtils.getLogger().
                 warning("Cannot render: ScreenData not registered.");
         return;
      }

      if (_showBounds) {
         Debugger.drawBounds(screenData._root, renderer, true);
      }

      if (_showNormals) {
         Debugger.drawNormals(screenData._root, renderer);
         Debugger.drawTangents(screenData._root, renderer);
      }

      if (_showDepth) {
         renderer.renderBuckets();
         Debugger.drawBuffer(TextureStoreFormat.Depth16, Debugger.NORTHEAST, renderer);
      }
   }

   public PickResults doPick(Ray3 pickRay) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public boolean isShowBounds() {
      return _showBounds;
   }

   public boolean isShowDepth() {
      return _showDepth;
   }

   public boolean isShowNormals() {
      return _showNormals;
   }

   public boolean isDoShot() {
      return _doShot;
   }

   public void setDoShot(boolean _doShot) {
      this._doShot = _doShot;
   }

   
/*
   public LightState getLightState() {
      return _lightState;
   }

   public WireframeState getWireframeState() {
      return _wireframeState;
   }

   public Node getRootNode() {
      return _root;
   }


   NativeCanvas getNativeCanvas() {
      return nativeCanvas;
   }

   public boolean isExit() {
      return _exit;
   }
*/
   

   public void switchShowDepth() {
      _showDepth = !_showDepth;
   }

   public void switchShowBounds() {
      _showBounds = !_showBounds;
   }

   public void switchShowNormals() {
      _showNormals = !_showNormals;
   }

   /**
    * Changes the exit flag to true.
    */
   /*
   public void setExit() {
        _exit = true;
    }
    *
    */

   private boolean _showBounds = false;
   private boolean _showNormals = false;
   private boolean _showDepth = false;
   private boolean _doShot = false;

   //private Node _root;
   //private LightState _lightState;
   //private WireframeState _wireframeState;
   //private DisplaySettings displaySettings;
   //private MouseManager mouseManager;
   //private PhysicalLayer physicalLayer;
   //private LogicalLayer logicalLayer;
   //private NativeCanvas nativeCanvas;

   private ScreenShotImageExporter _screenShotExp;

   //private volatile boolean _exit;

   //private BaseApp baseApp;

   /*
   DisplaySettings getDisplaySettings() {
      return displaySettings;
   }

   MouseManager getMouseManager() {
      return mouseManager;
   }

   PhysicalLayer getPhysicalLayer() {
      return physicalLayer;
   }

   LogicalLayer getLogicalLayer() {
      return logicalLayer;
   }
*/
   private ScreenData screenData;
}