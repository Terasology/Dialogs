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

import java.util.List;

import org.jboss.netty.logging.InternalLogLevel;
import org.terasology.dialogs.components.DialogComponent;
import org.terasology.dialogs.components.DialogResponse;
import org.terasology.engine.SimpleUri;
import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.DefaultBinding;
import org.terasology.input.Input;
import org.terasology.input.InputSystem;
import org.terasology.input.InputType;
import org.terasology.input.Keyboard;
import org.terasology.input.binds.interaction.FrobButton;
import org.terasology.input.cameraTarget.CameraTargetChangedEvent;
import org.terasology.logic.characters.events.ActivationRequest;
import org.terasology.notify.ui.NotificationEvent;
import org.terasology.notify.ui.RemoveNotificationEvent;
import org.terasology.physics.events.CollideEvent;
import org.terasology.registry.In;
import org.terasology.rendering.FontColor;
import org.terasology.rendering.nui.Color;
import org.terasology.rendering.nui.NUIManager;
import org.terasology.rendering.nui.widgets.browser.data.basic.HTMLLikeParser;
import org.terasology.rendering.nui.widgets.browser.data.html.HTMLDocument;
import org.terasology.rendering.nui.widgets.browser.ui.style.ParagraphRenderStyle;
import org.terasology.unicode.EnclosedAlphanumerics;

/**
 * TODO Type description
 */
@RegisterSystem(RegisterMode.CLIENT)
public class DialogSystem extends BaseComponentSystem {

    @In
    private NUIManager nuiManager;

    @In
    private InputSystem inputSystem;

    private boolean showNotification = true;

    private ParagraphRenderStyle titleStyle = new DefaultTitleParagraphStyle();

//    @ReceiveEvent
//    public void onCollision(CollideEvent event, EntityRef beacon, DialogComponent dialogComponent) {
//    }


    @ReceiveEvent
    public void onTarget(CameraTargetChangedEvent event, EntityRef entity) {

        if (!showNotification) {
            return;
        }

        SimpleUri id = new SimpleUri("engine:frob");
        List<Input> inputs = inputSystem.getInputsForBindButton(id);
        String text = "";
        for (Input input : inputs) {
            if (input.getType() == InputType.KEY) {
                String name = input.getDisplayName();
                if (name.length() == 1) {
                    int off = name.charAt(0) - 'A';
                    char code = (char) (EnclosedAlphanumerics.CIRCLED_LATIN_CAPITAL_LETTER_A + off);
                    text += FontColor.getColored(String.valueOf(code), new Color(0xFFFF00FF));
                } else {
                    text += FontColor.getColored(name, new Color(0xFFFF00FF));
                }
            }
        }

        DialogComponent dialogComponent = event.getNewTarget().getComponent(DialogComponent.class);
        if (dialogComponent == null) {
            entity.send(new RemoveNotificationEvent("Press " + text + " to talk"));
            return;
        }

//        showNotification = false;

        entity.send(new NotificationEvent("Press " + text + " to talk"));
    }

    @ReceiveEvent
    public void onActivate(ActivationRequest event, EntityRef entity) {

        DialogComponent dialogComponent = event.getTarget().getComponent(DialogComponent.class);

        if (dialogComponent == null) {
            return;
        }

        entity.send(new ShowDialogEvent(dialogComponent));
    }

    @ReceiveEvent
    public void showDialog(ShowDialogEvent event, EntityRef character) {
        DialogScreen window = nuiManager.pushScreen(DialogScreen.ASSET_URI, DialogScreen.class);
        window.reset();

        DialogComponent dialog = event.getDialog();
        HTMLDocument documentData = new HTMLDocument(null);

        documentData.addParagraph(HTMLLikeParser.parseHTMLLikeParagraph(titleStyle, dialog.title));
        for (String paragraphText : dialog.paragraphText) {
            documentData.addParagraph(HTMLLikeParser.parseHTMLLikeParagraph(null, paragraphText));
        }

        window.setDocument(documentData);
        for (DialogResponse r : dialog.responses) {
            window.addResponseOption(r.text, r.action);
        }
    }

    @ReceiveEvent
    public void closeDialog(CloseDialogEvent event, EntityRef character) {
        nuiManager.closeScreen(DialogScreen.ASSET_URI);
    }
}
