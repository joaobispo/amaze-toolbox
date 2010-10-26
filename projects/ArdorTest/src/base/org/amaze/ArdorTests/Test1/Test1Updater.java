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

package org.amaze.ArdorTests.Test1;

import com.ardor3d.bounding.BoundingBox;
import com.ardor3d.bounding.BoundingSphere;
import com.ardor3d.bounding.BoundingVolume;
import com.ardor3d.extension.animation.skeletal.SkeletonPose;
import com.ardor3d.extension.model.collada.jdom.ColladaImporter;
import com.ardor3d.extension.model.collada.jdom.data.ColladaStorage;
import com.ardor3d.extension.model.collada.jdom.data.SkinData;
import com.ardor3d.framework.NativeCanvas;
import com.ardor3d.input.control.FirstPersonControl;
import com.ardor3d.light.DirectionalLight;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.Camera;
import com.ardor3d.renderer.state.LightState;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.util.ReadOnlyTimer;
import java.util.List;
import org.amaze.ArdorTest.BaseImplementations.BaseUpdater;
import org.amaze.ArdorTest.DataObjects.BaseData;

/**
 *
 * @author Joao Bispo
 */
public class Test1Updater extends BaseUpdater {

   public Test1Updater(BaseData baseData) {
      super(baseData);

      colladaNode = null;
      pose = null;
   }



   @Override
   protected void updateExample(ReadOnlyTimer timer) {
      // Do nothing
   }

   @Override
   protected void initExample() {
      baseData.screenData.nativeCanvas.setTitle("Labirinto - Ardor Test 1");
      baseData.screenData.nativeCanvas.getCanvasRenderer().getRenderer().setBackgroundColor(ColorRGBA.GRAY);

      // Lights
      LightState _lightState = baseData.renderStateData._lightState;
       _lightState.detachAll();
        final DirectionalLight light = new DirectionalLight();
        light.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
        light.setAmbient(new ColorRGBA(0.25f, 0.25f, 0.25f, 1.0f));
        light.setDirection(new Vector3(-1, -1, -1).normalizeLocal());
        light.setEnabled(true);
        _lightState.attach(light);


      createCharacter();

      baseData.basicInput.firstPersonControl.setMoveSpeed(10.0);
   }

   private void createCharacter() {
      Node _root = baseData.screenData._root;

      if(colladaNode != null) {
      // detach the old colladaNode, if present.
            _root.detachChild(colladaNode);
      }

      final long time = System.currentTimeMillis();
      final ColladaImporter colladaImporter = new ColladaImporter();

      // Load the collada scene
      //final String mainFile = "collada/skeleton/skeleton.walk.dae";
      final String mainFile = "collada/test1/monkey.dae";
      final ColladaStorage storage = colladaImporter.load(mainFile);
      colladaNode = storage.getScene();
      final List<SkinData> skinDatas = storage.getSkins();
      //pose = skinDatas.get(0).getPose();

      //createAnimation(colladaImporter);

      System.out.println("Importing: " + mainFile);
      System.out.println("Took " + (System.currentTimeMillis() - time) + " ms");

      // Add colladaNode to root
      _root.attachChild(colladaNode);

      // TODO temp camera positioning until reading camera instances...
      ReadOnlyVector3 upAxis = Vector3.UNIT_Y;
            if (storage.getAssetData().getUpAxis() != null) {
                upAxis = storage.getAssetData().getUpAxis();
            }

      positionCamera(upAxis);
   }

   private void positionCamera(final ReadOnlyVector3 upAxis) {
        colladaNode.updateGeometricState(0.0);
        final BoundingVolume bounding = colladaNode.getWorldBound();
        if (bounding != null) {
            final ReadOnlyVector3 center = bounding.getCenter();
            double radius = 0;
            if (bounding instanceof BoundingSphere) {
                radius = ((BoundingSphere) bounding).getRadius();
            } else if (bounding instanceof BoundingBox) {
                final BoundingBox boundingBox = (BoundingBox) bounding;
                radius = Math.max(Math.max(boundingBox.getXExtent(), boundingBox.getYExtent()), boundingBox
                        .getZExtent());
            }

            final Vector3 vec = new Vector3(center);
            if (upAxis.equals(Vector3.UNIT_Z)) {
                vec.addLocal(0.0, -radius * 2, 0.0);
            } else {
                vec.addLocal(0.0, 0.0, radius * 2);
            }

            FirstPersonControl _controlHandle = baseData.basicInput.firstPersonControl;
            _controlHandle.setUpAxis(upAxis);

            NativeCanvas _canvas = baseData.screenData.nativeCanvas;
            final Camera cam = _canvas.getCanvasRenderer().getCamera();
            cam.setLocation(vec);
            cam.lookAt(center, upAxis);
            final double near = Math.max(radius / 500.0, 0.25);
            final double far = Math.min(radius * 5, 10000.0);
            cam.setFrustumPerspective(50.0, cam.getWidth() / (double) cam.getHeight(), near, far);
            cam.update();

            _controlHandle.setMoveSpeed(radius / 1.0);
        }
    }

   private Node colladaNode;
   private SkeletonPose pose;

}
