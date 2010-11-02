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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.java.games.input.Component;
import org.amaze.ArdorTests.Test2.JInputGamepad;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Joao Bispo
 */
public class GamepadOnlyButtons {


   public GamepadOnlyButtons(int numAnalogs) {
      buttons = new ArrayList<Button>();
      buttonPressings = new ArrayList<Integer>();
      //buttonReleases = new ArrayList<Integer>();

      pressedFrames = new ArrayList<Integer>();
      pressedButtons = new HashSet<Integer>();

      this.numAnalogs = numAnalogs;
   }


   public List<Button> getButtons() {
      return buttons;
   }

   public List<Integer> getButtonPressings() {
      return buttonPressings;
   }

   public List<Integer> getPressedFrames() {
      return pressedFrames;
   }


   public void addPress(Button button) {
      int index = buttons.indexOf(button);
      if(index == -1) {
         LoggingUtils.getLogger().
                 warning("Could not find button '"+button+"'");
      }

      buttonPressings.add(index);
      pressedButtons.add(index);
   }

   public void removePress(Button button) {
      int index = buttons.indexOf(button);
      if(index == -1) {
         LoggingUtils.getLogger().
                 warning("Could not find button '"+button+"'");
      }

      // Pressed frames goes to zero
      pressedFrames.set(index, 0);
      //buttonReleases.add(index);
      pressedButtons.remove(index);
   }

   public void updateFrameCount() {
      //System.out.println("Pressed Buttons:"+pressedButtons);
      //System.out.println("Before:"+pressedFrames);
      for(Integer index : pressedButtons) {
         int currentValue = pressedFrames.get(index);
         currentValue++;
         pressedFrames.set(index, currentValue);
      }
      //System.out.println("After:"+pressedFrames);
   }

   protected List<Button> buttons;
   protected List<Integer> pressedFrames;

   protected List<Integer> buttonPressings;
   //private List<Integer> buttonReleases;
   protected Set<Integer> pressedButtons;
   private int numAnalogs;

   public void addButton(Button newButton) {
      buttons.add(newButton);
      pressedFrames.add(0);
   }

   public static GamepadOnlyButtons newGamepad(JInputGamepad jinput, LogicalLayer logicalLayer) {
      GamepadOnlyButtons gamepad = new GamepadOnlyButtons(jinput.getAnalogs().size());

       for(int i=0; i<jinput.getButtons().size(); i++) {
         registerButton(jinput.getButtons().get(i), logicalLayer, gamepad);
      }

      for(int i=0; i<jinput.getAnalogs().size(); i++) {
         registerAnalogAsButtons(jinput.getAnalogs().get(i), logicalLayer, gamepad);
      }

      return gamepad;
   }


   private static void registerButton(final Component button, LogicalLayer logicalLayer, final GamepadOnlyButtons gamepad) {
      final Button newButton = new Button();
      gamepad.addButton(newButton);


      final Predicate<TwoInputStates> buttonPressed = new Predicate<TwoInputStates>() {

         @Override
         public boolean apply(final TwoInputStates states) {
            if (button.getPollData() == PRESSED_VALUE && !newButton.isCurrentlyPressed()) {
               newButton.setCurrentlyPressed(true);
               return true;
            }

            return false;
         }
      };

      final TriggerAction pressAction = new TriggerAction() {

         @Override
         public void perform(Canvas source, TwoInputStates inputState, double tpf) {
            gamepad.addPress(newButton);
         }
      };

      logicalLayer.registerTrigger(new InputTrigger(buttonPressed, pressAction));


      final Predicate<TwoInputStates> buttonReleased = new Predicate<TwoInputStates>() {

         @Override
            public boolean apply(final TwoInputStates states) {
//                for (final Component k : faceButtons) {
                    if (button.getPollData() == RELEASED_VALUE && newButton.isCurrentlyPressed()) {
                       newButton.setCurrentlyPressed(false);
                       return true;
                    }
  //              }
                return false;
            }

        };

          final TriggerAction releaseAction = new TriggerAction() {

         @Override
         public void perform(Canvas source, TwoInputStates inputState, double tpf) {
               gamepad.removePress(newButton);
         }
      };


        logicalLayer.registerTrigger(new InputTrigger(buttonReleased, releaseAction));
   }

   private static void registerAnalogAsButtons(final Component analog, LogicalLayer logicalLayer, final GamepadOnlyButtons gamepad) {
      final AnalogButton positiveButton = new AnalogButton();
      final AnalogButton negativeButton = new AnalogButton();
      gamepad.addButton(positiveButton);
      gamepad.addButton(negativeButton);


      final Predicate<TwoInputStates> buttonPressed = new Predicate<TwoInputStates>() {

         @Override
         public boolean apply(final TwoInputStates states) {
            if (analog.getPollData() > ANALOG_SENSITIVITY &&
                    analog.getPollData() > 0.0f && !positiveButton.isCurrentlyPressed()) {
               positiveButton.setCurrentlyPressed(true);
               return true;
            }

            return false;
         }
      };

      final TriggerAction pressAction = new TriggerAction() {

         @Override
         public void perform(Canvas source, TwoInputStates inputState, double tpf) {
            gamepad.addPress(positiveButton);
         }
      };

      logicalLayer.registerTrigger(new InputTrigger(buttonPressed, pressAction));


      final Predicate<TwoInputStates> buttonReleased = new Predicate<TwoInputStates>() {

         @Override
            public boolean apply(final TwoInputStates states) {
//                for (final Component k : faceButtons) {
                    if (analog.getPollData() < ANALOG_SENSITIVITY
                            && analog.getPollData() > 0.0f && positiveButton.isCurrentlyPressed()) {
                       positiveButton.setCurrentlyPressed(false);
                       return true;
                    }
  //              }
                return false;
            }

        };

          final TriggerAction releaseAction = new TriggerAction() {

         @Override
         public void perform(Canvas source, TwoInputStates inputState, double tpf) {
               gamepad.removePress(positiveButton);
         }
      };


        logicalLayer.registerTrigger(new InputTrigger(buttonReleased, releaseAction));

      final Predicate<TwoInputStates> buttonNegPressed = new Predicate<TwoInputStates>() {

         @Override
         public boolean apply(final TwoInputStates states) {
            if (Math.abs(analog.getPollData()) > ANALOG_SENSITIVITY &&
                    analog.getPollData() < 0.0f && !negativeButton.isCurrentlyPressed()) {
               negativeButton.setCurrentlyPressed(true);
               return true;
            }

            return false;
         }
      };

      final TriggerAction pressNegAction = new TriggerAction() {

         @Override
         public void perform(Canvas source, TwoInputStates inputState, double tpf) {
            gamepad.addPress(negativeButton);
         }
      };

      logicalLayer.registerTrigger(new InputTrigger(buttonNegPressed, pressNegAction));


      final Predicate<TwoInputStates> buttonNegReleased = new Predicate<TwoInputStates>() {

         @Override
            public boolean apply(final TwoInputStates states) {
//                for (final Component k : faceButtons) {
                    if (Math.abs(analog.getPollData()) < ANALOG_SENSITIVITY
                            && analog.getPollData() < 0.0f && negativeButton.isCurrentlyPressed()) {
                       negativeButton.setCurrentlyPressed(false);
                       return true;
                    }
  //              }
                return false;
            }

        };

          final TriggerAction releaseNegAction = new TriggerAction() {

         @Override
         public void perform(Canvas source, TwoInputStates inputState, double tpf) {
               gamepad.removePress(negativeButton);
         }
      };


        logicalLayer.registerTrigger(new InputTrigger(buttonNegReleased, releaseNegAction));
   }

   public static final float ANALOG_SENSITIVITY = 0.13f;
   public static final float PRESSED_VALUE = 1.0f;
   public static final float RELEASED_VALUE = 0.0f;

}
