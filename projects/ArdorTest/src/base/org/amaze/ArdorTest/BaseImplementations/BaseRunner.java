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

package org.amaze.ArdorTest.BaseImplementations;

import com.ardor3d.framework.CanvasRenderer;
import com.ardor3d.framework.FrameHandler;
import com.ardor3d.framework.NativeCanvas;
import com.ardor3d.framework.Updater;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.util.ContextGarbageCollector;
import com.ardor3d.util.Timer;
import org.amaze.ArdorTest.DataObjects.BaseData;

/**
 * Implements the infinite loop of the application.
 *
 * @author Joao Bispo
 */
public class BaseRunner implements Runnable {

   public BaseRunner(Updater updater, BaseData baseData) {
      //_exit = false;
      //frameHandler = new FrameHandler(new Timer());
      frameHandler = new FrameHandler(baseData.screenData.timer);
      //this.nativeCanvas = nativeCanvas;
      this.baseData = baseData;

      // Register our example as an updater.
      frameHandler.addUpdater(updater);

      // register our native canvas
      frameHandler.addCanvas(baseData.screenData.nativeCanvas);
   }



   private final FrameHandler frameHandler;
   private final BaseData baseData;
   //private final NativeCanvas nativeCanvas;
   //private volatile boolean _exit = false;

public void run() {
        try {
           frameHandler.init();

            while (!baseData.isExit()) {
//            while (!baseScene.isExit()) {
               long initialTime = baseData.screenData.timer.getTime();
                frameHandler.updateFrame();
                long finalTime = baseData.screenData.timer.getTime();
                long passedTimeNanos = finalTime-initialTime;
                long passedTimeMillis = (passedTimeNanos / 1000000) + 1;
                if(passedTimeMillis < TIME_PER_FRAME_MILLIS) {
                   long sleepTime = TIME_PER_FRAME_MILLIS - passedTimeMillis;
                   Thread.sleep(sleepTime);
                }
            }

            // grab the graphics context so cleanup will work out.
           NativeCanvas nativeCanvas = baseData.screenData.nativeCanvas;
            final CanvasRenderer cr = nativeCanvas.getCanvasRenderer();
            cr.makeCurrentContext();
            quit(nativeCanvas.getCanvasRenderer().getRenderer(), nativeCanvas);
            cr.releaseCurrentContext();
            if (QUIT_VM_ON_EXIT) {
                System.exit(0);
            }
        } catch (final Throwable t) {
            System.err.println("Throwable caught in MainThread - exiting");
            t.printStackTrace(System.err);
        }
   }

    protected void quit(final Renderer renderer, NativeCanvas canvas) {
        ContextGarbageCollector.doFinalCleanup(renderer);
        canvas.close();
    }


   /**
    * Changes the exit flag to true.
    */
    /*
   public void exit() {
      _exit = true;
   }
     *
     */

    /** If true (the default) we will call System.exit on end of demo. */
    public static boolean QUIT_VM_ON_EXIT = true;
    public static final long TIME_PER_FRAME_MILLIS = 16;
    //private volatile boolean _exit;
}
