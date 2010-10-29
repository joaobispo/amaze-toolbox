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

import java.util.Arrays;
import net.java.games.input.Component;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;

/**
 *
 * @author Joao Bispo
 */
public class ControllerTest {

   public static void main(String[] args) {
      System.out.println("Hello");
      ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
      Controller[] cs = ce.getControllers();

      for(Controller c : cs) {
         if(c.getType() != Type.KEYBOARD && c.getType() != Type.MOUSE) {
            Component[] components = c.getComponents();
            for(Component comp : components) {
               System.out.println("Component");
               System.out.println("Name:"+comp.getName());
               System.out.println("Identifier:"+comp.getIdentifier());
               System.out.println("PollData:"+comp.getPollData());
               System.out.println("Analog?:"+comp.isAnalog());
               System.out.println("Relative?:"+comp.isRelative());
               //System.out.println("Dead-zone:"+comp.getDeadZone());
               System.out.println("-------------");
            }
         }
//         System.out.println("Details for: " + c.getName() + ", " +
//                c.getType() + ", " + c.getPortType() );
      }

      
   }
}
