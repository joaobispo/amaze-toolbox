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

import com.ardor3d.framework.Canvas;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TwoInputStates;
import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.List;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import org.amaze.ArdorTest.Input.Button;
import org.amaze.ArdorTest.Input.Gamepad;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Represents a JInput gamepad
 *
 * @author Joao Bispo
 */
public class JInputGamepad {




   private JInputGamepad(Controller firstGameController) {
      buttons = new ArrayList<Component>();
      analogs = new ArrayList<Component>();
      relativeAnalogs = new ArrayList<Component>();

      // Assign components to lists
      Component[] components = firstGameController.getComponents();
      for(Component comp : components) {
         if(comp.isAnalog()) {
            if(comp.isRelative()) {
               relativeAnalogs.add(comp);
            } else {
               analogs.add(comp);
            }
         } else {
            buttons.add(comp);
         }
      }

      initAnalogs();
      dpad = Dpad.newDpad(buttons);
      faceButtons = Dpad.newDpadFirst(buttons);
   }


   private void initAnalogs() {
      // First analog is Y, second is X
      if(!analogs.isEmpty()) {
         yAxis = analogs.get(0);
      } else {
         yAxis = null;
      }

      if(analogs.size() > 1) {
         xAxis = analogs.get(1);
      } else {
         xAxis = null;
      }
   }

   public Component getyAxis() {
      return yAxis;
   }

   public Component getxAxis() {
      return xAxis;
   }

   public Dpad getFaceButtons() {
      return faceButtons;
   }

   public Dpad getDpad() {
      return dpad;
   }

   public List<Component> getAnalogs() {
      return analogs;
   }

   public List<Component> getButtons() {
      return buttons;
   }

   public List<Component> getRelativeAnalogs() {
      return relativeAnalogs;
   }



   /**
    * Builds a Gamepad object for the first controller it finds that is not a
    * keyboard or a mouse.
    * 
    * @return
    */
   public static JInputGamepad firstGamepadFound() {
      // Get gamepad
      ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
      Controller[] cs = ce.getControllers();

      // Find gamepad
      Controller firstGameController = null;
      for(Controller c : cs) {
         if(c.getType() != Type.KEYBOARD && c.getType() != Type.MOUSE) {
            firstGameController = c;
            break;
         }
      }

      if(firstGameController == null) {
         LoggingUtils.getLogger().
                 info("Could not find a game controller.");
         return null;
      }


      return new JInputGamepad(firstGameController);
   }

   public static Gamepad newGamepad(JInputGamepad jinput, LogicalLayer logicalLayer) {
      Gamepad gamepad = new Gamepad();

      for(int i=0; i<jinput.buttons.size(); i++) {
         registerButton(jinput.buttons.get(i), logicalLayer, gamepad);
      }

      return gamepad;
   }

   private static void registerButton(final Component button, LogicalLayer logicalLayer, final Gamepad gamepad) {
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
         //public void perform(final Canvas source, final TwoInputStates inputStates, final double tpf) {

         @Override
         public void perform(Canvas source, TwoInputStates inputState, double tpf) {
               gamepad.removePress(newButton);
         }
      };


        logicalLayer.registerTrigger(new InputTrigger(buttonReleased, releaseAction));

   }

   private List<Component> buttons;
   private List<Component> analogs;
   private List<Component> relativeAnalogs;
   
   private Dpad dpad;
   private Dpad faceButtons;
   private Component xAxis;
   private Component yAxis;

   public static final float PRESSED_VALUE = 1.0f;
   public static final float RELEASED_VALUE = 0.0f;

}
