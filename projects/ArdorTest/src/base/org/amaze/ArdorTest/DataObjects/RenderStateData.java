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

import com.ardor3d.renderer.state.LightState;
import com.ardor3d.renderer.state.WireframeState;
import com.ardor3d.scenegraph.Node;

/**
 *
 * @author Joao Bispo
 */
public class RenderStateData {

   public RenderStateData(LightState _lightState, WireframeState _wireframeState) {
      this._lightState = _lightState;
      this._wireframeState = _wireframeState;
   }

   /**
    * Builds a new RenderState object with a lightstate and a wireframestate,
    * and attaches them to the given node.
    *
    * <p>By default, lightstate is on and wirestate is off.
    *
    * @param root
    * @return
    */
   public static RenderStateData newRenderState(Node root) {
      LightState _lightState = new LightState();
      WireframeState _wireframeState = new WireframeState();

      _lightState.setEnabled(true);
      root.setRenderState(_lightState);

      _wireframeState.setEnabled(false);
      root.setRenderState(_wireframeState);

      return new RenderStateData(_lightState, _wireframeState);
   }

   public final LightState _lightState;
   public final WireframeState _wireframeState;

}
