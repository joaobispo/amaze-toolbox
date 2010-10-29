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

package org.amaze.ArdorTests.Test2;

import java.util.ArrayList;
import java.util.List;
import net.java.games.input.Component;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Joao Bispo
 */
public class Dpad {
   public Dpad(Component up, Component down, Component left, Component right) {
      int numButtons = Direction.values().length;
      directions = new ArrayList<Component>(numButtons);
      for(int i=0; i<numButtons; i++) {
         directions.add(null);
      }

      directions.set(Direction.DOWN.ordinal(), down);
      directions.set(Direction.LEFT.ordinal(), left);
      directions.set(Direction.RIGHT.ordinal(), right);
      directions.set(Direction.UP.ordinal(), up);
   }

      /**
       * Assigns the last four button of the gamepad to the DPad.
       *
       * @param gamepad
       */
      public static Dpad newDpad(List<Component> buttons) {

         if(buttons.size() < 4) {
            LoggingUtils.getLogger().
                    info("Controller as less than 4 buttons ("+buttons.size()+"). "
                    + "Could not assign a D-Pad");
            return null;
         }

         int leftIndex = buttons.size()-1;
         int downIndex = buttons.size()-2;
         int rightIndex = buttons.size()-3;
         int upIndex = buttons.size()-4;

         return new Dpad(buttons.get(upIndex), buttons.get(downIndex),
                 buttons.get(leftIndex), buttons.get(rightIndex));
      }

      /**
       * Assigns the first four button of the gamepad to the DPad.
       *
       * @param gamepad
       */
      public static Dpad newDpadFirst(List<Component> buttons) {
         if(buttons.size() < 4) {
            LoggingUtils.getLogger().
                    info("Controller as less than 4 buttons ("+buttons.size()+"). "
                    + "Could not assign a D-Pad");
            return null;
         }

         int leftIndex = 3;
         int downIndex = 2;
         int rightIndex = 1;
         int upIndex = 0;

         return new Dpad(buttons.get(upIndex), buttons.get(downIndex),
                 buttons.get(leftIndex), buttons.get(rightIndex));
      }

   public List<Component> getDirections() {
      return directions;
   }

      public Component getDirection(Direction direction) {
         return directions.get(direction.ordinal());
      }

      /*
      public final Component up;
      public final Component down;
      public final Component left;
      public final Component right;
       * 
       */
      List<Component> directions;

      public enum Direction {
         UP,
         DOWN,
         LEFT,
         RIGHT
      }
}
