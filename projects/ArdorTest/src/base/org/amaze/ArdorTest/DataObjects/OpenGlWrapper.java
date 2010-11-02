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
package org.amaze.ArdorTest.DataObjects;

import com.ardor3d.framework.DisplaySettings;
import com.ardor3d.framework.NativeCanvas;
import com.ardor3d.framework.Scene;
import com.ardor3d.framework.jogl.JoglCanvas;
import com.ardor3d.framework.jogl.JoglCanvasRenderer;
import com.ardor3d.framework.lwjgl.LwjglCanvas;
import com.ardor3d.framework.lwjgl.LwjglCanvasRenderer;
import com.ardor3d.input.MouseManager;
import com.ardor3d.input.PhysicalLayer;
import com.ardor3d.input.awt.AwtFocusWrapper;
import com.ardor3d.input.awt.AwtKeyboardWrapper;
import com.ardor3d.input.awt.AwtMouseManager;
import com.ardor3d.input.awt.AwtMouseWrapper;
import com.ardor3d.input.logical.DummyControllerWrapper;
import com.ardor3d.input.lwjgl.LwjglControllerWrapper;
import com.ardor3d.input.lwjgl.LwjglKeyboardWrapper;
import com.ardor3d.input.lwjgl.LwjglMouseManager;
import com.ardor3d.input.lwjgl.LwjglMouseWrapper;
import com.ardor3d.renderer.ContextCapabilities;
import com.ardor3d.renderer.ContextManager;
import com.ardor3d.renderer.TextureRendererFactory;
import com.ardor3d.renderer.jogl.JoglTextureRendererProvider;
import com.ardor3d.renderer.lwjgl.LwjglTextureRendererProvider;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 * Provides implementations of classes which depend on OpenGL wrappers, such
 * as JOGL and LWJGL.
 *
 * @author Joao Bispo
 */
public class OpenGlWrapper {


   public OpenGlWrapper(NativeCanvas nativeCanvas, MouseManager mouseManager, PhysicalLayer physicalLayer) {
      this.nativeCanvas = nativeCanvas;
      this.mouseManager = mouseManager;
      this.physicalLayer = physicalLayer;
   }
   final public NativeCanvas nativeCanvas;
   final public MouseManager mouseManager;
   final public PhysicalLayer physicalLayer;

   public static OpenGlWrapper configLWJGL(DisplaySettings displaySettings,
           Scene dataScene, TextureRendererFactory textureRenderer) {
      // Check if library is present
      List<String> unfoundFiles = new ArrayList<String>();
      for(String filename : LWJGL_FILENAMES) {
               File file = new File(filename);
               if(!file.exists()) {
                  unfoundFiles.add(filename);
               }
      }
      if(unfoundFiles.size() > 0) {
         Logger logger = LoggingUtils.getLogger();
         logger.info("The following files are missing:");
         for(String filename : unfoundFiles) {
            logger.info(filename);
         }
         return null;
      }


      NativeCanvas nativeCanvas;
      MouseManager mouseManager;
      PhysicalLayer physicalLayer;

      final LwjglCanvasRenderer canvasRenderer = new LwjglCanvasRenderer(dataScene);
      nativeCanvas = new LwjglCanvas(canvasRenderer, displaySettings);
      physicalLayer = new PhysicalLayer(new LwjglKeyboardWrapper(), new LwjglMouseWrapper(),
              new LwjglControllerWrapper(), (LwjglCanvas) nativeCanvas);
      mouseManager = new LwjglMouseManager();
      textureRenderer.setProvider(new LwjglTextureRendererProvider());

      return new OpenGlWrapper(nativeCanvas, mouseManager, physicalLayer);
   }

   /**
    * Builds objects with binding to JOGL.
    *
    * @param displaySettings
    * @param scene
    * @param textureRenderer
    * @return
    */
   public static OpenGlWrapper configJOGL(DisplaySettings displaySettings,
           Scene scene, TextureRendererFactory textureRenderer) {
      NativeCanvas nativeCanvas;
      MouseManager mouseManager;
      PhysicalLayer physicalLayer;

      final JoglCanvasRenderer canvasRenderer = new JoglCanvasRenderer(scene);
      //System.out.println("Jogl Height:"+displaySettings.getHeight());
      //System.out.println("Jogl Width:"+displaySettings.getWidth());
      nativeCanvas = new JoglCanvas(canvasRenderer, displaySettings);
      final JoglCanvas canvas = (JoglCanvas) nativeCanvas;
      mouseManager = new AwtMouseManager(canvas);
      physicalLayer = new PhysicalLayer(new AwtKeyboardWrapper(canvas), new AwtMouseWrapper(canvas,
              mouseManager), DummyControllerWrapper.INSTANCE, new AwtFocusWrapper(canvas));
      textureRenderer.setProvider(new JoglTextureRendererProvider());



      return new OpenGlWrapper(nativeCanvas, mouseManager, physicalLayer);
   }


   /**
    * Builds a new OpenGlWrapper, based on the name of the renderer (JOGL, LWJGL).
    *
    * @param rendererName
    * @param displaySettings
    * @param scene
    * @param textureRenderer
    * @return
    */
   public static OpenGlWrapper newData(String rendererName,
           DisplaySettings displaySettings, Scene scene, TextureRendererFactory textureRenderer) {
      if (LWJGL_STRING.equalsIgnoreCase(rendererName)) {
         return configLWJGL(displaySettings, scene, textureRenderer);
      } else if (JOGL_STRING.equalsIgnoreCase(rendererName)) {
         return configJOGL(displaySettings, scene, textureRenderer);
      } else {
         LoggingUtils.getLogger().
                 warning("Renderer not supported: '" + rendererName + "'.");
         return null;
      }
   }

   public static final String LWJGL_STRING = "LWJGL";
   public static final String JOGL_STRING = "JOGL";

   public static final String[] LWJGL_FILENAMES = {"jinput-dx8.dll", "lwjgl.dll",
   "jinput-raw.dll", "OpenAL32.dll"};
}
