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

package org.amaze.ArdorTest.Utilities;

import com.ardor3d.framework.CanvasRenderer;
import com.ardor3d.framework.FrameHandler;
import com.ardor3d.framework.NativeCanvas;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.util.ContextGarbageCollector;

/**
 *
 * @author Joao Bispo
 */
public class BaseRunner implements Runnable {

   public BaseRunner(BaseApp baseScene) {
      this.baseScene = baseScene;
   }



   private final BaseApp baseScene;
   //private volatile boolean _exit = false;

public void run() {
        try {
           FrameHandler frameHandler = baseScene.getFrameHandler();
           frameHandler.init();

            while (!baseScene.isExit()) {
                frameHandler.updateFrame();
                Thread.yield();
            }

            // grab the graphics context so cleanup will work out.
           NativeCanvas nativeCanvas = baseScene.getNativeCanvas();
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


    /** If true (the default) we will call System.exit on end of demo. */
    public static boolean QUIT_VM_ON_EXIT = true;
}