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

import com.ardor3d.example.PropertiesDialog;
import com.ardor3d.example.PropertiesGameSettings;
import com.ardor3d.framework.DisplaySettings;
import com.ardor3d.framework.Scene;
import com.ardor3d.renderer.TextureRendererFactory;
import com.ardor3d.renderer.state.RenderState;
import com.ardor3d.util.resource.ResourceLocatorTool;
import java.awt.EventQueue;
import java.net.URL;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.amaze.ArdorTest.DataObjects.BasicInput;
import org.amaze.ArdorTest.DataObjects.OpenGlWrapper;
import org.amaze.ArdorTest.DataObjects.RenderStateData;
import org.amaze.ArdorTest.DataObjects.ScreenData;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Joao Bispo
 */
public class SettingsUtils {

   /**
    * Calls a dialog window and asks for properties.
    *
    * @param settings
    * @param localReference
    * @return
    */
   public static PropertiesGameSettings getPreferencesWithWindow(final PropertiesGameSettings settings, Class localReference) {
        // Always show the dialog in these examples.
      Logger logger = LoggingUtils.getLogger();
      URL dialogImage = null;
        final String dflt = settings.getDefaultSettingsWidgetImage();
        if (dflt != null) {
            try {
                dialogImage = ResourceLocatorTool.getClassPathResource(localReference, dflt);
            } catch (final Exception e) {
               LoggingUtils.getLogger().
               log(Level.SEVERE, "Resource lookup of '" + dflt + "' failed.  Proceeding.");
            }
        }
        if (dialogImage == null) {
            logger.fine("No dialog image loaded");
        } else {
            logger.fine("Using dialog image '" + dialogImage + "'");
        }

        final URL dialogImageRef = dialogImage;
        final AtomicReference<PropertiesDialog> dialogRef = new AtomicReference<PropertiesDialog>();
        final Stack<Runnable> mainThreadTasks = new Stack<Runnable>();
        try {
            if (EventQueue.isDispatchThread()) {
                dialogRef.set(new PropertiesDialog(settings, dialogImageRef, mainThreadTasks));
            } else {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        dialogRef.set(new PropertiesDialog(settings, dialogImageRef, mainThreadTasks));
                    }
                });
            }
        } catch (final Exception e) {
           logger.warning("Exception:" + e);
            return null;
        }

        PropertiesDialog dialogCheck = dialogRef.get();
        while (dialogCheck == null || dialogCheck.isVisible()) {
            try {
                // check worker queue for work
                while (!mainThreadTasks.isEmpty()) {
                    mainThreadTasks.pop().run();
                }
                // go back to sleep for a while
                Thread.sleep(50);
            } catch (final InterruptedException e) {
            logger.warning("Error waiting for dialog system, using defaults.");
         }

         dialogCheck = dialogRef.get();
      }

      if (dialogCheck.isCancelled()) {
         System.exit(0);
      }
      return settings;
   }

   /**
    * Builds a new DisplaySetting object from a PropertiesGameSettings.
    *
    * @param prefs
    * @return
    */
   public static DisplaySettings newDisplaySetting(PropertiesGameSettings prefs) {
      int width = prefs.getWidth();
      int height = prefs.getHeight();
      int depth = prefs.getDepth();
      int frequency = prefs.getFrequency();

      // alpha
      int alphaBits;
      if (useMinAlphaBits) {
         alphaBits = -1;
      } else {
         alphaBits = prefs.getAlphaBits();
      }
      // depth
      int depthBits;
      if (useMinDepthBits) {
         depthBits = -1;
      } else {
         depthBits = prefs.getDepthBits();
      }
      // stencil
      int stencilBits;
      if (useMinStencilBits) {
         stencilBits = -1;
      } else {
         stencilBits = prefs.getStencilBits();
      }

      int samples = prefs.getSamples();
      boolean isFullscreen = prefs.isFullscreen();
      boolean stereo = useStereo;

      return new DisplaySettings(width, height, depth, frequency, alphaBits, depthBits, stencilBits, samples, isFullscreen, stereo);

   }

   /**
    * Builds and returns an array containing data objects.
    *
    * <p>The array contains the following objects, by this order:
    * <br>ScreenData screenData = (ScreenData)objects[0];
    * <br>BasicInput basicInput = (BasicInput)objects[1];
    * <br>RenderState renderState = (RenderState)objects[2];
    *
    * @param prefs
    * @return
    */
   public static Object[] newDataObjects(PropertiesGameSettings prefs, Scene scene) {
      Object[] objects = new Object[3];

       // Initialize DisplaySettings
      DisplaySettings displaySettings = SettingsUtils.newDisplaySetting(prefs);

      // Initialize OpenGl bindings
      OpenGlWrapper wrapperData = OpenGlWrapper.newData(prefs.getRenderer(),
              displaySettings, scene, TextureRendererFactory.INSTANCE);
      if(wrapperData == null) {
         LoggingUtils.getLogger().
                 warning("Could not initialize OpenGL wrapper objects for renderer '"+
                 prefs.getRenderer()+"'.");
         return null;
      }

      ScreenData screenData = ScreenData.newScreenData(displaySettings, wrapperData);
      objects[0] = screenData;

      BasicInput basicInput = BasicInput.newBasicInput(screenData, wrapperData);
      objects[1] = basicInput;

      RenderStateData renderStateData = RenderStateData.newRenderState(screenData._root);
      objects[2] = renderStateData;

      return objects;
   }


  

   private static boolean useMinAlphaBits = false;
   private static boolean useMinDepthBits = false;
   private static boolean useMinStencilBits = false;
   private static boolean useStereo = false;


}
