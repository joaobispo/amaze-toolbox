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

import net.java.games.input.Component;

/**
 *
 * @author Joao Bispo
 */
public class Button {

   public Button() {
      currentlyPressed = false;
      component = null;
   }

   public boolean isCurrentlyPressed() {
      return currentlyPressed;
   }

   public void setCurrentlyPressed(boolean currentlyPressed) {
      this.currentlyPressed = currentlyPressed;
   }

   public void setComponent(Component component) {
      this.component = component;
   }

   public Component getComponent() {
      return component;
   }

   @Override
   public String toString() {
      return Boolean.toString(isCurrentlyPressed());
   }

   

   protected boolean currentlyPressed;
   protected Component component;
}
