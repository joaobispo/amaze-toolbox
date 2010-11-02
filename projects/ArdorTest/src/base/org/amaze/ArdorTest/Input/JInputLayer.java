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
import net.java.games.input.Component;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Joao Bispo
 */
public class JInputLayer {

   public JInputLayer() {
      buttons = new ArrayList<Component>();
      analogs = new ArrayList<Component>();
      buttonPressings = new ArrayList<Integer>();
      buttonsIsPressed = new ArrayList<Boolean>();
      
      //buttonReleases = new ArrayList<Integer>();

      buttonsPressedFrames = new ArrayList<Integer>();
      pressedButtons = new HashSet<Integer>();

      deadzone = DEFAULT_DEADZONE;
   }

   public void setDeadzone(float deadzone) {
      this.deadzone = deadzone;
   }



   public List<Component> getAnalogs() {
      return analogs;
   }

   public List<Component> getButtons() {
      return buttons;
   }

   public List<Integer> getButtonPressings() {
      return buttonPressings;
   }

   public List<Integer> getPressedFrames() {
      return buttonsPressedFrames;
   }

   /**
    * Sets the deadzone value for the analogs.
    *
    * @param deadzone a value between 0.0 and 1.0. Any values of the analog
    * below this number will be ignored.
    */
   /*
      public void setAnalogDeadzone(float deadzone) {
         for(Analog analog : analogs) {
            analog.setDeadZone(deadzone);
         }
   }
    * 
    */

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
      buttonsPressedFrames.set(index, 0);
      //buttonReleases.add(index);
      pressedButtons.remove(index);
   }

   public void updateFrameCount() {
      //System.out.println("Pressed Buttons:"+pressedButtons);
      //System.out.println("Before:"+pressedFrames);
      for(Integer index : pressedButtons) {
         int currentValue = buttonsPressedFrames.get(index);
         currentValue++;
         buttonsPressedFrames.set(index, currentValue);
      }
      //System.out.println("After:"+pressedFrames);
   }

   //protected List<Button> buttons;
   private List<Component> buttons;
   private List<Integer> buttonsPressedFrames;
   private List<Boolean> buttonsIsPressed;


   private List<Integer> buttonPressings;
   private Set<Integer> pressedButtons;

   //protected List<Analog> analogs;
   private List<Component> analogs;

   private float deadzone;


   //private List<Integer> buttonReleases;
   

   public void addButton(Component component) {
      buttons.add(component);
      buttonsPressedFrames.add(0);
      buttonsIsPressed.add(Boolean.FALSE);
   }

   public void addAnalog(Component newAnalog) {
      analogs.add(newAnalog);
   }

   public static final float DEFAULT_DEADZONE = 1.3f;
}
