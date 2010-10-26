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

import com.ardor3d.input.MouseManager;
import com.ardor3d.input.PhysicalLayer;
import com.ardor3d.input.control.FirstPersonControl;
import com.ardor3d.input.logical.LogicalLayer;
import org.amaze.ArdorTest.Utilities.InputUtils;

/**
 *
 * @author Joao Bispo
 */
public class BasicInput {

   public BasicInput(MouseManager mouseManager, PhysicalLayer physicalLayer, 
           LogicalLayer logicalLayer, FirstPersonControl firstPersonControl) {
      this.mouseManager = mouseManager;
      this.physicalLayer = physicalLayer;
      this.logicalLayer = logicalLayer;
      this.firstPersonControl = firstPersonControl;
   }

   public static BasicInput newBasicInput(ScreenData screenData, OpenGlWrapper wrapperData){

      MouseManager mouseManager = wrapperData.mouseManager;
      PhysicalLayer physicalLayer = wrapperData.physicalLayer;
      LogicalLayer logicalLayer = new LogicalLayer();
      logicalLayer.registerInput(screenData.nativeCanvas, physicalLayer);
      FirstPersonControl firstPersonControl = InputUtils.addFpControl(logicalLayer);


      return new BasicInput(mouseManager, physicalLayer, logicalLayer, firstPersonControl);
   }

   public final MouseManager mouseManager;
   public final PhysicalLayer physicalLayer;
   public final LogicalLayer logicalLayer;
   public final FirstPersonControl firstPersonControl;
}
