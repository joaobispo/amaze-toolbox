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

/**
 *
 * @author Joao Bispo
 */
public class BaseData {

   public BaseData(ScreenData screenData, BasicInput basicInput, RenderStateData renderStateData) {
      this.screenData = screenData;
      this.basicInput = basicInput;
      this.renderStateData = renderStateData;
   }

   public void setExit(boolean _exit) {
      this._exit = _exit;
   }

   public boolean isExit() {
      return _exit;
   }

   
   

   public final ScreenData screenData;
   public final BasicInput basicInput;
   public final RenderStateData renderStateData;

   private volatile boolean _exit;
}
