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

package org.amaze.ArdorTests.Test3;

import com.ardor3d.example.ui.BMFontLoader;
import com.ardor3d.framework.Canvas;
import com.ardor3d.input.MouseButton;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.MouseButtonClickedCondition;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TwoInputStates;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.ui.text.BMFont;
import com.ardor3d.ui.text.BMText;
import com.ardor3d.util.ReadOnlyTimer;
import com.ardor3d.util.resource.ResourceLocator;
import com.ardor3d.util.resource.ResourceLocatorTool;
import com.ardor3d.util.resource.ResourceSource;
import com.ardor3d.util.resource.SimpleResourceLocator;
import com.google.common.base.Predicate;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.amaze.ArdorTest.BaseImplementations.BaseUpdater;
import org.amaze.ArdorTest.DataObjects.BaseData;

/**
 *
 * @author Joao Bispo
 */
public class Test3ControllerWithArdorUpdater extends BaseUpdater {


   public Test3ControllerWithArdorUpdater(BaseData baseData) {
      super(baseData);

   }

   @Override
   protected void updateExample(ReadOnlyTimer timer) {
      
   }

   @Override
   protected void initExample() {
      addFontResources();


      textExampleNode = new Node("textExampleNode");
      textExampleNode.setTranslation(0, 0, 0);

      font = BMFontLoader.defaultFont();

      text = new BMText("textSpatial1", "Hello World", font, BMText.Align.SouthWest,
	                    BMText.Justify.Right);
/*
      100	            text.setFontScale(fontScale);
101	            text.setAutoFade(AutoFade.CapScreenSize);
102	            text.setAutoFadeFalloff(1.0f);
103	            text.setAutoScale(AutoScale.CapScreenSize);
104	            text.setAutoRotate(true);
105	            textExampleNode.attachChild(text);
106	            text.addController(fontChanger);
 * 
 */
      textExampleNode.attachChild(text);
      baseData.screenData._root.attachChild(textExampleNode);

      registerTrigger();

   }



   /**
    *
    */
   private Node textExampleNode;
   private BMFont font;
   private BMText text;

   public static final String RESOURCES_LOCATION = "org/amaze/ArdorTest/";

   private void addFontResources() {
       SimpleResourceLocator srl = null;
      try {
         srl = new SimpleResourceLocator(ResourceLocatorTool.getClassPathResource(this.getClass(), RESOURCES_LOCATION));
      } catch (URISyntaxException ex) {
         Logger.getLogger(Test3ControllerWithArdorUpdater.class.getName()).log(Level.SEVERE, null, ex);
      }
      ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, srl);
   }

   private void registerTrigger() {
      baseData.basicInput.logicalLayer.registerTrigger(new InputTrigger(new MouseButtonClickedCondition(MouseButton.LEFT),
              new TriggerAction() {

         @Override
         public void perform(Canvas source, TwoInputStates inputState, double tpf) {
            text.setText(text.getText()+"a");
         }
      }));
   }
}
