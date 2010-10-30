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

/**
 * A value between 0.0 and 1.0.
 *
 * @author Joao Bispo
 */
public class Analog {

   public Analog() {
      this(DEFAULT_DEAD_ZONE);
   }



   public Analog(float deadZone) {
      this.deadZone = deadZone;
      this.value = ZERO_VALUE;
   }



   public float getDeadZone() {
      return deadZone;
   }

   /**
    *
    * @return the analog value without the deadzone subtracted
    */
   public float getValueRaw() {
      return value;
   }

   public void setValue(float value) {
      this.value = value;
   }

   

   /**
    *
    * @return the analog value having into account the deadzone
    */
   public float getValueProcessed() {
      if(Math.abs(value) < deadZone) {
         return ZERO_VALUE;
      }

      return value;
   }

   public void setDeadZone(float deadZone) {
      this.deadZone = deadZone;
   }



   private float value;
   private float deadZone;

   public static final float DEFAULT_DEAD_ZONE = 0.2f;
   public static final float ONE_VALUE = 1.0f;
   public static final float ZERO_VALUE = 0.0f;
}
