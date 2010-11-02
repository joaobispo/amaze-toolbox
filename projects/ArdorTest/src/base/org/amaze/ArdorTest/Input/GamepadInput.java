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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Joao Bispo
 */
public class GamepadInput {

   public GamepadInput() {
      buttons = new ArrayList<Button>();
      analogs = new ArrayList<Analog>();
      buttonPressings = new ArrayList<Integer>();
      buttonReleases = new ArrayList<Integer>();

      pressedFrames = new ArrayList<Integer>();
      pressedButtons = new HashSet<Integer>();
   }

   public List<Analog> getAnalogs() {
      return analogs;
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

   /**
    * Sets the deadzone value for the analogs.
    *
    * @param deadzone a value between 0.0 and 1.0. Any values of the analog
    * below this number will be ignored.
    */
      public void setAnalogDeadzone(float deadzone) {
         for(Analog analog : analogs) {
            analog.setDeadZone(deadzone);
         }
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
      buttonReleases.add(index);
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
   protected List<Analog> analogs;

   protected List<Integer> buttonPressings;
   protected List<Integer> buttonReleases;
   protected Set<Integer> pressedButtons;

   public void addButton(Button newButton) {
      buttons.add(newButton);
      pressedFrames.add(0);
   }

   public void addAnalog(Analog newAnalog) {
      analogs.add(newAnalog);
   }

   public List<Integer> getButtonReleases() {
      return buttonReleases;
   }

   

}
