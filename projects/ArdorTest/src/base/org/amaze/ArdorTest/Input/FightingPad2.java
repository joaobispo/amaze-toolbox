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

package org.amaze.ArdorTest.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Joao Bispo
 */
public class FightingPad2 {

   public FightingPad2(GamepadInput gamepad) {
      this.gamepad = gamepad;

      buttonPresses = new ArrayList<Action>();
      buttonMapsReverse = new EnumMap<Action, Integer>(Action.class);
      buttonMaps = new HashMap<Integer, Action>();
      pressedDirections = new HashSet<Action>();

      autoMap();
   }

   public void updateFrameCount() {
      gamepad.updateFrameCount();

      if(!gamepad.getButtonPressings().isEmpty() && !gamepad.getButtonReleases().isEmpty()) {
         System.out.println("PRESSINGS AND RELEASES");
      }

      processPressings();
      processReleases();


   }

   private void autoMap() {
      // Make an auto-mapping
      List<Integer> numbers = Arrays.asList(0, 1, 2, 3);
      List<Action> actions = Arrays.asList(Action.UP, Action.RIGHT, Action.DOWN, Action.LEFT);

      for (int i = 0; i < numbers.size(); i++) {
         buttonMaps.put(numbers.get(i), actions.get(i));
         buttonMapsReverse.put(actions.get(i), numbers.get(i));
      }
   }

   /**
    *
    * @param action
    * @return
    */
   private Action getComposedAction(Action action) {
      // Check if directional; only directional can be composed
      if(!baseDirections.contains(action)) {
         return null;
      }

      // Check if other buttons are currently pressed, for current action
      switch(action) {
         case DOWN:
            //if(pressedDirections.contains(Action.LEFT)gamepad.getButtons().get(buttonMapsReverse.get(Action.LEFT)).isCurrentlyPressed()) {
            if(pressedDirections.contains(Action.LEFT)) {
               return Action.DOWNLEFT;
            }
//            if(gamepad.getButtons().get(buttonMapsReverse.get(Action.RIGHT)).isCurrentlyPressed()) {
            if(pressedDirections.contains(Action.RIGHT)) {
               return Action.DOWNRIGHT;
            }
            return null;
         case UP:
            //if(pressedDirections.contains(Action.LEFT)gamepad.getButtons().get(buttonMapsReverse.get(Action.LEFT)).isCurrentlyPressed()) {
            if(pressedDirections.contains(Action.LEFT)) {
               return Action.UPLEFT;
            }
//            if(gamepad.getButtons().get(buttonMapsReverse.get(Action.RIGHT)).isCurrentlyPressed()) {
            if(pressedDirections.contains(Action.RIGHT)) {
               return Action.UPRIGHT;
            }
            return null;
         case RIGHT:
            //if(pressedDirections.contains(Action.LEFT)gamepad.getButtons().get(buttonMapsReverse.get(Action.LEFT)).isCurrentlyPressed()) {
            if(pressedDirections.contains(Action.UP)) {
               return Action.UPRIGHT;
            }
//            if(gamepad.getButtons().get(buttonMapsReverse.get(Action.RIGHT)).isCurrentlyPressed()) {
            if(pressedDirections.contains(Action.DOWN)) {
               return Action.DOWNRIGHT;
            }
            return null;
         case LEFT:
            //if(pressedDirections.contains(Action.LEFT)gamepad.getButtons().get(buttonMapsReverse.get(Action.LEFT)).isCurrentlyPressed()) {
            if(pressedDirections.contains(Action.UP)) {
               return Action.UPLEFT;
            }
//            if(gamepad.getButtons().get(buttonMapsReverse.get(Action.RIGHT)).isCurrentlyPressed()) {
            if(pressedDirections.contains(Action.DOWN)) {
               return Action.DOWNLEFT;
            }
            return null;
         default:
            return null;
      }

   }


   private Action getComposedRelease(Action action) {
// Check if directional; only directional can be composed
      if (!baseDirections.contains(action)) {
         return null;
      }

      // Check if other buttons are currently pressed, for current action
      switch (action) {
         case LEFT:
            if(pressedDirections.contains(Action.DOWNLEFT)) {
               return Action.DOWN;
            }
            if(pressedDirections.contains(Action.UPLEFT)) {
               return Action.UP;
            }
            return null;
         case RIGHT:
            if(pressedDirections.contains(Action.DOWNRIGHT)) {
               return Action.DOWN;
            }
            if(pressedDirections.contains(Action.UPRIGHT)) {
               return Action.UP;
            }
            return null;
         case DOWN:
            if(pressedDirections.contains(Action.DOWNLEFT)) {
               return Action.LEFT;
            }
            if(pressedDirections.contains(Action.DOWNRIGHT)) {
               return Action.RIGHT;
            }
            return null;
         case UP:
            if(pressedDirections.contains(Action.UPLEFT)) {
               return Action.LEFT;
            }
            if(pressedDirections.contains(Action.UPRIGHT)) {
               return Action.RIGHT;
            }
            return null;
         default:
            return null;
      }

   }

   private void processPressings() {
            while(gamepad.getButtonPressings().size() > 0) {

         Integer buttonId = gamepad.getButtonPressings().get(0);
         gamepad.getButtonPressings().remove(0);

         Action action = buttonMaps.get(buttonId);

         // Check if is composed action
         Action composedAction = getComposedAction(action);

         if(composedAction != null) {
            buttonPresses.add(composedAction);
            pressedDirections.add(composedAction);
            pressedDirections.add(action);
         }  else {
         if(action != null) {
            buttonPresses.add(action);
            if(baseDirections.contains(action)) {
               pressedDirections.add(action);
            }
         } else {
            //buttonPresses.add(Action.UNDEFINED);
         }
         }
      }

   }

   private void processReleases() {
      while (gamepad.getButtonReleases().size() > 0) {

         Integer buttonId = gamepad.getButtonReleases().get(0);
         gamepad.getButtonReleases().remove(0);

         Action action = buttonMaps.get(buttonId);

         // Check if is composed release
         Action composedRelease = getComposedRelease(action);

         if (composedRelease != null) {
            buttonPresses.remove(composedRelease);
            // Remove action which was released
            //pressedDirections.remove(action);
         }

         // Removed base direction
         if (baseDirections.contains(action)) {
            pressedDirections.remove(action);
         }

      }

   }


    public enum Action {
      UP,
      DOWN,
      RIGHT,
      LEFT,
      DOWNLEFT,
      DOWNRIGHT,
      UPRIGHT,
      UPLEFT,
      UNDEFINED;
   }

   public List<Action> getButtonPresses() {
      return buttonPresses;
   }

    private static List<Action> baseDirections =
            Arrays.asList(Action.DOWN, Action.LEFT, Action.UP, Action.RIGHT);

    private static List<Action> composedDirections =
            Arrays.asList(Action.DOWNLEFT, Action.DOWNRIGHT, Action.UPLEFT, Action.UPRIGHT);

   private GamepadInput gamepad;
   private List<Action> buttonPresses;
   private Map<Action, Integer> buttonMapsReverse;
   private Map<Integer, Action> buttonMaps;

   private Set<Action> pressedFires;
   private Set<Action> pressedDirections;
}
