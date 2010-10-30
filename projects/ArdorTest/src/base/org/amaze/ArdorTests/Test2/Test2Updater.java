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

import com.ardor3d.framework.Canvas;
import org.amaze.ArdorTests.Test1.*;
import com.ardor3d.bounding.BoundingBox;
import com.ardor3d.bounding.BoundingSphere;
import com.ardor3d.bounding.BoundingVolume;
import com.ardor3d.extension.animation.skeletal.AnimationManager;
import com.ardor3d.extension.animation.skeletal.SkeletonPose;
import com.ardor3d.extension.animation.skeletal.blendtree.SimpleAnimationApplier;
import com.ardor3d.extension.model.collada.jdom.ColladaImporter;
import com.ardor3d.extension.model.collada.jdom.data.AnimationItem;
import com.ardor3d.extension.model.collada.jdom.data.ColladaStorage;
import com.ardor3d.extension.model.collada.jdom.data.SkinData;
import com.ardor3d.framework.NativeCanvas;
import com.ardor3d.input.InputState;
import com.ardor3d.input.control.FirstPersonControl;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TwoInputStates;
import com.ardor3d.light.DirectionalLight;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Quaternion;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.Camera;
import com.ardor3d.renderer.state.LightState;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.util.ReadOnlyTimer;
import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.List;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import org.amaze.ArdorTest.BaseImplementations.BaseUpdater;
import org.amaze.ArdorTest.DataObjects.BaseData;
import org.amaze.ArdorTest.Input.Gamepad;

/**
 *
 * @author Joao Bispo
 */
public class Test2Updater extends BaseUpdater {

   public Test2Updater(BaseData baseData) {
      super(baseData);

      colladaNode = null;
      pose = null;

   }



   @Override
   protected void updateExample(ReadOnlyTimer timer) {
      gamepad.updateFrameCount();
      //System.out.println("Frames:"+baseData.screenData.timer.getFrameRate());
      
      //Component component = gamepad.getFaceButtons().getDirection(Dpad.Direction.UP);
      //if(component.getPollData() > 0.0f) {
      //   System.out.println("Up Button:"+component.getPollData());
      //}
      
       

      //Quaternion quat = new Quaternion();
      //quat.addLocal(new Quaternion().fromAngleAxis(currentAngle, Vector3.UNIT_X));
      //quat.addLocal(new Quaternion().fromAngleAxis(currentAngle, Vector3.UNIT_Z));
      //quat.fromAngleAxis(currentAngle, Vector3.UNIT_Y);

      //colladaNode.setRotation(quat);
//      colladaNode.setRotation(new Quaternion().fromAngleAxis(currentAngle, Vector3.UNIT_X));

      // Change angle according to controller
      float sensitivity = 0.2f;
      
      float xPolled = xAxis.getPollData();
      if(Math.abs(xPolled) < sensitivity) {
         xPolled = 0.0f;
      }

      float yPolled = yAxis.getPollData();
      if(Math.abs(yPolled) < sensitivity) {
         yPolled = 0.0f;
      }

      double xChange = xPolled * maxRotationVel;
      double yChange = yPolled * maxRotationVel;

      /*
      if(Math.abs(xChange) > 0.0001) {
         xAngle += xChange;
      }
      if(Math.abs(yChange) > 0.0001) {
         yAngle += yChange;
      }
       * 
       */

      xAngle += xChange;
      yAngle += yChange;
       
/*
      if(xAngle > 2*Math.PI) {
         xAngle = xAngle % Math.PI;
      }

      if(xAngle < 2*Math.PI) {
         xAngle = -1.0 * (xAngle % Math.PI);
      }

      if(yAngle > 2*Math.PI) {
         yAngle = yAngle % Math.PI;
      }

      if(yAngle < 2*Math.PI) {
         yAngle = -1.0 * (yAngle % Math.PI);
      }*/

      /*
      if(yAngle > 2*Math.PI || yAngle < 2*Math.PI) {
         yAngle = yAngle % Math.PI;
      }
       * 
       */
      while(xAngle < 0) {
         xAngle += 2*Math.PI;
      }

      if(Math.abs(xAngle) > 2*Math.PI) {
         xAngle = xAngle % Math.PI;
      }

      while(yAngle < 0) {
         yAngle += 2*Math.PI;
      }

      if(Math.abs(yAngle) > 2*Math.PI) {
         yAngle = yAngle % Math.PI;
      }

if(gamepad.getButtonPressings().size() > 0) {
//      if (actions.size() > 0) {
//         actions.remove(0);
         System.out.println("Button:"+gamepad.getButtonPressings().get(0));
         gamepad.getButtonPressings().remove(0);
         yAngle = 0.0d;
         xAngle = 0.0d;

      }


      int frames = 120;
      for(int i=0; i<gamepad.getPressedFrames().size(); i++) {
         if(gamepad.getPressedFrames().get(i) == frames) {
            System.out.println("Button "+i+" has been pressed for "+frames+" frames.");
         }
      }

       

   Matrix3 matrix3 = new Matrix3();
   matrix3.fromAngles(yAngle, xAngle, 0);

      //System.out.println("X:"+xAxis.getPollData());
      //System.out.println("Y:"+yAxis.getPollData());
      //_workerMatrix.fromAngleNormalAxis(currentAngle, Vector3.UNIT_X);
      
     // _workerMatrix.fromAngleNormalAxis(currentAngle, Vector3.UNIT_Y);
     // _workerMatrix.fromAngleNormalAxis(currentAngle, Vector3.UNIT_Z);

//      if (buttonActive) {
//         colladaNode.setRotation(new Matrix3());
//      } else {
   
      colladaNode.setRotation(matrix3);
   

         //colladaNode.setRotation(matrix3);
//      }
      //colladaNode.setRotation(_workerMatrix);

      /*
      xAngle += 0.001;
      if(xAngle > 2*Math.PI) {
         xAngle = xAngle % Math.PI;
      }
       * 
       */
      // Update rotation
      //currentRotation.addLocal(new Quaternion(0, 0, 0, 0));
//      currentRotation.fromAngleAxis(3.14, Vector3.UNIT_X);
      //System.out.println("Quaternion:"+(new Quaternion(10, 0, 0, 1)).toAngleAxis((Vector3) Vector3.UNIT_Y));
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

      initGamepad();
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
      //final String mainFile = "collada/test1/bouncing_ball.dae";
      final ColladaStorage storage = colladaImporter.load(mainFile);
      colladaNode = storage.getScene();
      final List<SkinData> skinDatas = storage.getSkins();
      System.out.println("Animation:"+storage.getAnimationItemRoot());
      System.out.println("Joints:"+storage.getJointChannels());
      System.out.println("Asset:"+storage.getAssetData());
      AnimationItem ani = storage.getAnimationItemRoot();
      //System.out.println("Size:"+skinDatas);
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

      manager = new AnimationManager(baseData.screenData.timer);

      // Add our "applier logic".
        final SimpleAnimationApplier applier = new SimpleAnimationApplier();
        manager.setApplier(applier);
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

   AnimationManager manager;

   private JInputGamepad jinputGamepad;
   private Gamepad gamepad;
   private double xAngle;
   private double yAngle;
   private Component xAxis;
   private Component yAxis;
   //private Quaternion currentRotation;
   private final Matrix3 _workerMatrix = new Matrix3();

   private double maxRotationVel = 0.003;

   private void initGamepad() {
//      currentRotation = new Quaternion();
      xAngle = 0.0d;
      yAngle = 0.0d;
/*
      // Get gamepad
      ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
      Controller[] cs = ce.getControllers();

      // Find gamepad
      gamepad = null;
      for(Controller c : cs) {
         if(c.getType() != Type.KEYBOARD && c.getType() != Type.MOUSE) {
            gamepad = c;
            break;
         }
      }

      if(gamepad == null) {
         System.out.println("Could not find a game controller.");
      }


      // Get some of the components
      Component[] components = gamepad.getComponents();
      for(Component comp : components) {
         if(comp.getIdentifier().getName().equals("y")) {
            yAxis = comp;
            System.out.println("DeadZoneX:"+yAxis.getDeadZone());
         }
         
         if(comp.getIdentifier().getName().equals("x")) {
            xAxis = comp;
            System.out.println("DeadZoneX:"+xAxis.getDeadZone());
         }
      }
      System.out.println(gamepad.getComponent(null));
*/
      jinputGamepad = JInputGamepad.firstGamepadFound();
      yAxis = jinputGamepad.getyAxis();
      xAxis = jinputGamepad.getxAxis();

      gamepad = JInputGamepad.newGamepad(jinputGamepad, baseData.basicInput.logicalLayer);
      //setupController(gamepad);

   }

  /*
   private void setupController(final JInputGamepad gamepad) {
      LogicalLayer logicalLayer = baseData.basicInput.logicalLayer;

      final Predicate<TwoInputStates> keyPressed = new Predicate<TwoInputStates>() {
         //List<Component> faceButtons = gamepad.getFaceButtons().getDirections();
         Component upButton = gamepad.getFaceButtons().getDirection(Dpad.Direction.UP);
  //       Key[] keys = new Key[] { Key.W, Key.A, Key.S, Key.D, Key.LEFT, Key.RIGHT, Key.UP, Key.DOWN };

         @Override
            public boolean apply(final TwoInputStates states) {
                    if (upButton.getPollData() == 1.0 && !isButtonDirty) {
                       isButtonDirty = true;
                       return true;
                    }

                return false;
            }
        };

      final Predicate<TwoInputStates> keysHeld = new Predicate<TwoInputStates>() {
         List<Component> faceButtons = gamepad.getFaceButtons().getDirections();
  //       Key[] keys = new Key[] { Key.W, Key.A, Key.S, Key.D, Key.LEFT, Key.RIGHT, Key.UP, Key.DOWN };

         @Override
            public boolean apply(final TwoInputStates states) {
                for (final Component k : faceButtons) {
                    if (states.getCurrent() != null && k.getPollData() > 0.0) {
                       return true;
                    }
                }
                return false;
            }
        };

          final TriggerAction buttonAction = new TriggerAction() {
         //public void perform(final Canvas source, final TwoInputStates inputStates, final double tpf) {

         @Override
         public void perform(Canvas source, TwoInputStates inputState, double tpf) {
            //colladaNode.setRotation(new Matrix3());
            //System.out.println("AHHHHH");
            //buttonActive = true;
  /*
            if(!isButtonDirty) {
               isButtonDirty = true;
               buttonActive = true;
               System.out.println("Button pressed");
            }
   *
   */
   /*
            actions.add(0);
            System.out.println("Button Pressed");
         }
      };

              logicalLayer.registerTrigger(new InputTrigger(keyPressed, buttonAction));

      final Predicate<TwoInputStates> keyReleased = new Predicate<TwoInputStates>() {
         Component upButton = gamepad.getFaceButtons().getDirection(Dpad.Direction.UP);
  //       Key[] keys = new Key[] { Key.W, Key.A, Key.S, Key.D, Key.LEFT, Key.RIGHT, Key.UP, Key.DOWN };

         @Override
            public boolean apply(final TwoInputStates states) {
//                for (final Component k : faceButtons) {
                    if (upButton.getPollData() == 0.0f && isButtonDirty) {
                       isButtonDirty = false;
                       return true;
                    }
  //              }
                return false;
            }

        };

      final Predicate<TwoInputStates> keysReleased = new Predicate<TwoInputStates>() {
         List<Component> faceButtons = gamepad.getFaceButtons().getDirections();
  //       Key[] keys = new Key[] { Key.W, Key.A, Key.S, Key.D, Key.LEFT, Key.RIGHT, Key.UP, Key.DOWN };

         @Override
            public boolean apply(final TwoInputStates states) {
                for (final Component k : faceButtons) {
                    if (states.getCurrent() != null && k.getPollData() == 0.0f) {
                       return true;
                    }
                }
                return false;
            }

        };

          final TriggerAction buttonRelease = new TriggerAction() {
         //public void perform(final Canvas source, final TwoInputStates inputStates, final double tpf) {

         @Override
         public void perform(Canvas source, TwoInputStates inputState, double tpf) {
  System.out.println("Button Release");

            /*
            //buttonActive = false;
            //isButtonDirty = false;
            if(isButtonDirty) {
               buttonActive = false;
               isButtonDirty = false;
               System.out.println("Button Release");
            }
*/
/*
}

      };


        logicalLayer.registerTrigger(new InputTrigger(keyReleased, buttonRelease));
   }
*/
   //private boolean buttonActive;
   //private boolean isButtonDirty;
   //List<Integer> actions = new ArrayList<Integer>();

   /*
   private void setupController(Controller gamepad) {
      LogicalLayer logicalLayer = baseData.basicInput.logicalLayer;

      xAngle += 0.001;
      if(xAngle > 2*Math.PI) {
         xAngle = xAngle % Math.PI;
      }
   }
    *
    */

}
