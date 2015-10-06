/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.dialogs;

import org.terasology.dialogs.components.DialogComponent;
import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.characters.events.ActivationRequest;
import org.terasology.physics.events.CollideEvent;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;
import org.terasology.rendering.nui.widgets.browser.data.basic.HTMLLikeParser;
import org.terasology.rendering.nui.widgets.browser.data.html.HTMLDocument;

/**
 * TODO Type description
 */
@RegisterSystem(RegisterMode.CLIENT)
public class DialogSystem extends BaseComponentSystem {

    @In
    private NUIManager nuiManager;

    @ReceiveEvent
    public void onActivate(ActivationRequest event, EntityRef entity) {

        DialogComponent dialogComponent = event.getTarget().getComponent(DialogComponent.class);

        for (Component c : event.getTarget().iterateComponents()) {
            c.getClass();
        }

        if (dialogComponent == null) {
            return;
        }
//    @ReceiveEvent
//    public void onCollision(CollideEvent event, EntityRef beacon, DialogComponent dialogComponent) {

//    @ReceiveEvent
//    public void showDialog(ShowDialogEvent event, EntityRef character) {
        DialogScreen window = nuiManager.pushScreen("Dialogs:DialogScreen", DialogScreen.class);

//        for (Prefab itemPrefab : itemsCategoryInGameHelpRegistry.getKnownPrefabs()) {
        HTMLDocument documentData = new HTMLDocument(null);

        documentData.addParagraph(HTMLLikeParser.parseHTMLLikeParagraph(new DefaultTitleParagraphStyle(), dialogComponent.title));
        for (String paragraphText : dialogComponent.paragraphText) {
            documentData.addParagraph(HTMLLikeParser.parseHTMLLikeParagraph(null, paragraphText));
        }

        window.setDocument(documentData);
    }
}
