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

import com.ardor3d.framework.NativeCanvas;
import com.ardor3d.framework.Updater;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.renderer.ContextCapabilities;
import com.ardor3d.renderer.ContextManager;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.util.Constants;
import com.ardor3d.util.GameTaskQueue;
import com.ardor3d.util.GameTaskQueueManager;
import com.ardor3d.util.ReadOnlyTimer;
import com.ardor3d.util.stat.StatCollector;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Joao Bispo
 */
public class BaseUpdater implements Updater {

   public BaseUpdater(BaseApp baseApp) {
      this.baseApp = baseApp;
   }

   // TODO: commented code
   public void init() {
      final ContextCapabilities caps = ContextManager.getCurrentContext().getCapabilities();
      Logger logger = LoggingUtils.getLogger(this);
      logger.info("Display Vendor: " + caps.getDisplayVendor());
      logger.info("Display Renderer: " + caps.getDisplayRenderer());
      logger.info("Display Version: " + caps.getDisplayVersion());
      logger.info("Shading Language Version: " + caps.getShadingLanguageVersion());

        Inputs.registerInputTriggers(baseApp);
/*
        AWTImageLoader.registerLoader();

        try {
            SimpleResourceLocator srl = new SimpleResourceLocator(ResourceLocatorTool.getClassPathResource(
                    ExampleBase.class, "com/ardor3d/example/media/"));
            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, srl);
            srl = new SimpleResourceLocator(ResourceLocatorTool.getClassPathResource(ExampleBase.class,
                    "com/ardor3d/example/media/models/"));
            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_MODEL, srl);
        } catch (final URISyntaxException ex) {
            ex.printStackTrace();
        }
*/
        /**
         * Create a ZBuffer to display pixels closest to the camera above farther ones.
         */
/*
        final ZBufferState buf = new ZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        _root.setRenderState(buf);
*/
        // ---- LIGHTS
        /** Set up a basic, default light. */
 /*
        final PointLight light = new PointLight();
        light.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
        light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        light.setLocation(new Vector3(100, 100, 100));
        light.setEnabled(true);
*/
        /** Attach the light to a lightState and the lightState to rootNode. */
 /*
                _lightState = new LightState();
        _lightState.setEnabled(true);
        _lightState.attach(light);
        _root.setRenderState(_lightState);

        _wireframeState = new WireframeState();
        _wireframeState.setEnabled(false);
        _root.setRenderState(_wireframeState);

        _root.getSceneHints().setRenderBucketType(RenderBucketType.Opaque);

        initExample();

        _root.updateGeometricState(0);
  * 
  */
   }

   public void update(ReadOnlyTimer timer) {
      NativeCanvas _canvas = baseApp.getScene().getNativeCanvas();
      Node _root = baseApp.getScene().getRootNode();
      
      if (_canvas.isClosing()) {
            baseApp.exit();
        }

        /** update stats, if enabled. */
        if (Constants.stats) {
            StatCollector.update();
        }

        updateLogicalLayer(timer);

        // Execute updateQueue item
        GameTaskQueueManager.getManager(_canvas.getCanvasRenderer().getRenderContext()).getQueue(GameTaskQueue.UPDATE)
                .execute();

        /** Call simpleUpdate in any derived classes of ExampleBase. */
        updateExample(timer);

        /** Update controllers/render states/transforms/bounds for rootNode. */
        _root.updateGeometricState(timer.getTimePerFrame(), true);
   }

   private void updateLogicalLayer(final ReadOnlyTimer timer) {
      LogicalLayer _logicalLayer = baseApp.getScene().getLogicalLayer();

        // check and execute any input triggers, if we are concerned with input
        if (_logicalLayer != null) {
            _logicalLayer.checkTriggers(timer.getTimePerFrame());
        }
    }

    private void updateExample(final ReadOnlyTimer timer) {
    // does nothing
    }

   private BaseApp baseApp;
}
