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
 * Implements a simple Scene which has information about the data on the screen.
 * Draws the image on the screen.
 *
 * @author Joao Bispo
 */
public class BaseScene implements Scene {

   public BaseScene() {
      _screenShotExp = new ScreenShotImageExporter();

      this.screenData = null;
   }

   public void registerScreenData(ScreenData screenData) {
      this.screenData = screenData;
   }


   @Override
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

   @Override
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


   public void switchShowDepth() {
      _showDepth = !_showDepth;
   }

   public void switchShowBounds() {
      _showBounds = !_showBounds;
   }

   public void switchShowNormals() {
      _showNormals = !_showNormals;
   }

  
   private boolean _showBounds = false;
   private boolean _showNormals = false;
   private boolean _showDepth = false;
   private boolean _doShot = false;

   private ScreenShotImageExporter _screenShotExp;

   private ScreenData screenData;
}
