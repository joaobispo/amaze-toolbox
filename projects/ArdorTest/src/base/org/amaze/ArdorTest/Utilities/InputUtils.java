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

import com.ardor3d.framework.Canvas;
import com.ardor3d.framework.NativeCanvas;
import com.ardor3d.input.GrabbedState;
import com.ardor3d.input.Key;
import com.ardor3d.input.MouseButton;
import com.ardor3d.input.MouseManager;
import com.ardor3d.input.control.FirstPersonControl;
import com.ardor3d.input.logical.AnyKeyCondition;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.KeyPressedCondition;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.input.logical.MouseButtonClickedCondition;
import com.ardor3d.input.logical.MouseButtonPressedCondition;
import com.ardor3d.input.logical.MouseButtonReleasedCondition;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TwoInputStates;
import com.ardor3d.math.Ray3;
import com.ardor3d.math.Vector2;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.state.LightState;
import com.ardor3d.renderer.state.WireframeState;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.event.DirtyType;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.amaze.ArdorTest.BaseImplementations.BaseScene;
import org.amaze.ArdorTest.DataObjects.BaseData;

/**
 *
 * @author Joao Bispo
 */
public class InputUtils {

   /**
    * Builds a new First Person Control and adds it to the given logical layer.
    * It sets the Y axis as the up axis and movement is enabled only when dragging.
    * @param logicalLayer
    * @return
    */
   public static FirstPersonControl addFpControl(LogicalLayer logicalLayer) {
      FirstPersonControl firstPersonControl = FirstPersonControl.setupTriggers(logicalLayer, _worldUp, true);
      return firstPersonControl;
   }

   public static void initBaseInputs(final BaseData baseData) {
      LogicalLayer _logicalLayer = baseData.basicInput.logicalLayer;

      final Node _root = baseData.screenData._root;
      final NativeCanvas _canvas = baseData.screenData.nativeCanvas;
      final LightState _lightState = baseData.renderStateData._lightState;
      final WireframeState _wireframeState = baseData.renderStateData._wireframeState;
      final BaseScene dataScene = baseData.screenData.scene;
      final MouseManager _mouseManager = baseData.basicInput.mouseManager;
      // check if this example worries about input at all
      if (_logicalLayer == null) {
         return;
      }

        _logicalLayer.registerTrigger(new InputTrigger(new MouseButtonClickedCondition(MouseButton.RIGHT),
                new TriggerAction() {
                    public void perform(final Canvas source, final TwoInputStates inputStates, final double tpf) {

                        final Vector2 pos = Vector2.fetchTempInstance().set(
                                inputStates.getCurrent().getMouseState().getX(),
                                inputStates.getCurrent().getMouseState().getY());
                        final Ray3 pickRay = new Ray3();
                        _canvas.getCanvasRenderer().getCamera().getPickRay(pos, false, pickRay);
                        Vector2.releaseTempInstance(pos);
                        RayUtils.doPick(pickRay, _root);
                    }
                }, "pickTrigger"));

        _logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(Key.ESCAPE), new TriggerAction() {
            public void perform(final Canvas source, final TwoInputStates inputState, final double tpf) {
                baseData.setExit(true);
            }
        }));

        _logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(Key.L), new TriggerAction() {
            public void perform(final Canvas source, final TwoInputStates inputState, final double tpf) {
                _lightState.setEnabled(!_lightState.isEnabled());
                // Either an update or a markDirty is needed here since we did not touch the affected spatial directly.
                _root.markDirty(DirtyType.RenderState);
            }
        }));

        _logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(Key.F4), new TriggerAction() {
            public void perform(final Canvas source, final TwoInputStates inputState, final double tpf) {
                dataScene.switchShowDepth();
            }
        }));

        _logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(Key.T), new TriggerAction() {
            public void perform(final Canvas source, final TwoInputStates inputState, final double tpf) {
               _wireframeState.setEnabled(!_wireframeState.isEnabled());
                // Either an update or a markDirty is needed here since we did not touch the affected spatial directly.
                _root.markDirty(DirtyType.RenderState);
            }
        }));

        _logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(Key.B), new TriggerAction() {
            public void perform(final Canvas source, final TwoInputStates inputState, final double tpf) {
               dataScene.switchShowBounds();
            }
        }));

        _logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(Key.C), new TriggerAction() {
            public void perform(final Canvas source, final TwoInputStates inputState, final double tpf) {
                System.out.println("Camera: " + _canvas.getCanvasRenderer().getCamera());
            }
        }));

        _logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(Key.N), new TriggerAction() {
            public void perform(final Canvas source, final TwoInputStates inputState, final double tpf) {
               dataScene.switchShowNormals();
            }
        }));

        _logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(Key.F1), new TriggerAction() {
            public void perform(final Canvas source, final TwoInputStates inputState, final double tpf) {
               dataScene.setDoShot(true);
            }
        }));

        final Predicate<TwoInputStates> clickLeftOrRight = Predicates.or(new MouseButtonClickedCondition(
                MouseButton.LEFT), new MouseButtonClickedCondition(MouseButton.RIGHT));

        _logicalLayer.registerTrigger(new InputTrigger(clickLeftOrRight, new TriggerAction() {
            public void perform(final Canvas source, final TwoInputStates inputStates, final double tpf) {
                System.err.println("clicked: " + inputStates.getCurrent().getMouseState().getClickCounts());
            }
        }));

        _logicalLayer.registerTrigger(new InputTrigger(new MouseButtonPressedCondition(MouseButton.LEFT),
                new TriggerAction() {
                    public void perform(final Canvas source, final TwoInputStates inputState, final double tpf) {
                        if (_mouseManager.isSetGrabbedSupported()) {
                            _mouseManager.setGrabbed(GrabbedState.GRABBED);
                        }
                    }
                }));
        _logicalLayer.registerTrigger(new InputTrigger(new MouseButtonReleasedCondition(MouseButton.LEFT),
                new TriggerAction() {
                    public void perform(final Canvas source, final TwoInputStates inputState, final double tpf) {
                        if (_mouseManager.isSetGrabbedSupported()) {
                            _mouseManager.setGrabbed(GrabbedState.NOT_GRABBED);
                        }
                    }
                }));

        _logicalLayer.registerTrigger(new InputTrigger(new AnyKeyCondition(), new TriggerAction() {
            public void perform(final Canvas source, final TwoInputStates inputState, final double tpf) {
                System.out.println("Key character pressed: "
                        + inputState.getCurrent().getKeyboardState().getKeyEvent().getKeyChar());
            }
        }));
   }

   public static final Vector3 _worldUp = new Vector3(0, 1, 0);
}
