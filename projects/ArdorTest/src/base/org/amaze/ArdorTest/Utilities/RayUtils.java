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

import com.ardor3d.intersection.PickData;
import com.ardor3d.intersection.PickResults;
import com.ardor3d.intersection.PickingUtil;
import com.ardor3d.intersection.PrimitivePickResults;
import com.ardor3d.math.Ray3;
import com.ardor3d.scenegraph.Node;

/**
 *
 * @author Joao Bispo
 */
public class RayUtils {

   public static PickResults doPick(final Ray3 pickRay, Node _root) {
      final PrimitivePickResults pickResults = new PrimitivePickResults();
      pickResults.setCheckDistance(true);
      PickingUtil.findPick(_root, pickRay, pickResults);
      processPicks(pickResults);
      return pickResults;
   }

   public static void processPicks(final PrimitivePickResults pickResults) {
      int i = 0;
      while (pickResults.getNumber() > 0
              && pickResults.getPickData(i).getIntersectionRecord().getNumberOfIntersections() == 0
              && ++i < pickResults.getNumber()) {
      }
      if (pickResults.getNumber() > i) {
         final PickData pick = pickResults.getPickData(i);
         System.err.println("picked: " + pick.getTarget() + " at: "
                 + pick.getIntersectionRecord().getIntersectionPoint(0));
      } else {
         System.err.println("picked: nothing");
      }
   }
}
