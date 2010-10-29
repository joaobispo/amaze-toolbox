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

package org.amaze.ArdorTest.DataObjects;

import com.ardor3d.framework.DisplaySettings;
import com.ardor3d.framework.NativeCanvas;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.util.Timer;
import org.amaze.ArdorTest.BaseImplementations.BaseScene;

/**
 *
 * @author Joao Bispo
 */
public class ScreenData {

   public ScreenData(DisplaySettings displaySettings, NativeCanvas nativeCanvas, Node _root,
           BaseScene scene) {
      this.displaySettings = displaySettings;
      this.nativeCanvas = nativeCanvas;
      this._root = _root;
      this.scene = scene;
      this.timer = new Timer();
   }

   public static ScreenData newScreenData(DisplaySettings displaySettings, OpenGlWrapper wrapperData,
           BaseScene scene) {

      NativeCanvas nativeCanvas = wrapperData.nativeCanvas;
      Node _root = new Node();

      ScreenData screenData = new ScreenData(displaySettings, nativeCanvas, _root, scene);
      scene.registerScreenData(screenData);
      return screenData;
   }

    public final DisplaySettings displaySettings;
    public final NativeCanvas nativeCanvas;
    public final Node _root;
    public final Timer timer;
    public final BaseScene scene;

}
