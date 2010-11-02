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

import com.ardor3d.framework.Canvas;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TwoInputStates;
import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Joao Bispo
 */
public class FightingPad {

   private static void registerDirections(final FightingPad fightPad, LogicalLayer logicalLayer) {

   //final List<Action> directions = fightPad.getBasicDirections();
      final Predicate<TwoInputStates> buttonPressed = new Predicate<TwoInputStates>() {


         @Override
         public boolean apply(final TwoInputStates states) {
            if(fightPad.getGamepad().buttonPressings.isEmpty()) {
               return false;
            }
            List<Integer> presses2 = fightPad.getGamepad().buttonPressings;
            Integer press = presses2.get(0);
            presses2.remove(0);
            Action action = fightPad.buttonMaps.get(press);
            if(fightPad.getBasicDirections().contains(action)) {
               System.out.println("Action:"+action);
               return true;
            } else {
               return false;
            }

/*
            for(Action action : directions) {
               Button button = fightPad.getButton(action);
            if (button.getComponent().getPollData() == 1.0f && !button.isCurrentlyPressed()) {
               //button.setCurrentlyPressed(true);
               System.out.println("Action:"+action);
               return true;
            }
            }

            return false;
 *
 */
         }
      };

      final TriggerAction pressAction = new TriggerAction() {

         @Override
         public void perform(Canvas source, TwoInputStates inputState, double tpf) {

            //gamepad.addPress(newButton);
         }
      };

      logicalLayer.registerTrigger(new InputTrigger(buttonPressed, pressAction));

   }

   public FightingPad(GamepadInput gamepad) {
      this.gamepad = gamepad;
      this.buttonMapsReverse = new EnumMap<Action, Integer>(Action.class);
      this.buttonMaps = new HashMap<Integer, Action>();
   }

   public void updateFrameCount() {
      gamepad.updateFrameCount();

      // Check pressings for composite actions
      //List<Integer> pressings = gamepad.getButtonPressings();
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
      URA_PUNCH,
      URA_KICK,
      OMOTE_PUNCH,
      OMOTE_KICK,
      START,
      SELECT;
   }

   public List<Action> getBasicDirections() {
      List<Action> list = new ArrayList<Action>();
      list.add(Action.UP);
      list.add(Action.DOWN);
      list.add(Action.LEFT);
      list.add(Action.RIGHT);
      return list;
   }

   public static FightingPad newFightPad(GamepadInput gamepad, LogicalLayer logicalLayer) {
      FightingPad fightPad = new FightingPad(gamepad);

      // Make an auto-mapping
      List<Integer> numbers = Arrays.asList(0, 1, 2, 3);
      List<Action> actions = Arrays.asList(Action.UP, Action.RIGHT, Action.DOWN, Action.LEFT);

      for(int i=0; i<numbers.size(); i++) {
         fightPad.buttonMaps.put(numbers.get(i), actions.get(i));
         fightPad.buttonMapsReverse.put(actions.get(i), numbers.get(i));
      }

      // Register directions
      registerDirections(fightPad, logicalLayer);

      return fightPad;
   }

   public Button getButton(Action action) {
      int index = buttonMapsReverse.get(action);
      return gamepad.getButtons().get(index);
   }

   public GamepadInput getGamepad() {
      return gamepad;
   }



   private GamepadInput gamepad;
   private Map<Action, Integer> buttonMapsReverse;
   private Map<Integer, Action> buttonMaps;

   private List<Action> buttonPressings;
}
