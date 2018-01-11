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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.terasology.assets.management.AssetManager;
import org.terasology.dialogs.components.DialogComponent;
import org.terasology.dialogs.components.DialogPage;
import org.terasology.dialogs.components.DialogResponse;
import org.terasology.engine.SimpleUri;
import org.terasology.engine.subsystem.config.BindsSubsystem;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.Input;
import org.terasology.input.InputType;
import org.terasology.logic.characters.CharacterComponent;
import org.terasology.logic.characters.events.ActivationRequest;
import org.terasology.logic.common.DisplayNameComponent;
import org.terasology.logic.players.PlayerTargetChangedEvent;
import org.terasology.network.ClientComponent;
import org.terasology.network.ColorComponent;
import org.terasology.notify.ui.NotificationEvent;
import org.terasology.notify.ui.RemoveNotificationEvent;
import org.terasology.persistence.TemplateEngine;
import org.terasology.persistence.TemplateEngineImpl;
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
    BindsSubsystem bindsSubsystem;

    @In
    private AssetManager assetManager;

    private String talkText;

    private ParagraphRenderStyle titleStyle = new DefaultTitleParagraphStyle();

    private Map<String, String> mappings = new HashMap<String, String>();
    private TemplateEngine templateEngine = new TemplateEngineImpl(id -> {
        String result = mappings.get(id);
        if (result != null) {
            return result;
        } else {
            return "?" + id + "?";
        }
    });

    @Override
    public void initialise() {
    }

    @ReceiveEvent
    public void onTarget(PlayerTargetChangedEvent event, EntityRef entity) {

        EntityRef target = event.getNewTarget();
        DialogComponent dialogComponent = target.getComponent(DialogComponent.class);
        updateTalkNotification(entity, dialogComponent != null);
    }

    private void updateTalkNotification(EntityRef entity, boolean active) {
        if (!active && talkText != null) {
            entity.send(new RemoveNotificationEvent(talkText));
            talkText = null;
            return;
        }

        if (active && talkText == null) {
            talkText = createTalkText();
            entity.send(new NotificationEvent(talkText));
        }
    }

    @ReceiveEvent
    public void onActivate(ActivationRequest event, EntityRef entity) {

        DialogComponent dialogComponent = event.getTarget().getComponent(DialogComponent.class);

        if (dialogComponent != null) {
            entity.send(new ShowDialogEvent(event.getTarget(), dialogComponent.firstPage));
        }
    }

    @ReceiveEvent
    public void showDialog(ShowDialogEvent event, EntityRef character) {
        DialogScreen window = nuiManager.pushScreen(DialogScreen.ASSET_URI, DialogScreen.class);
        window.reset();

        DialogComponent dialog = event.getDialog();
        DialogPage page = dialog.getPage(event.getPage());

        if (page == null) {
            return;
        }

        HTMLDocument documentData = new HTMLDocument(null);

        updateTemplateMappings(character);

        String title = templateEngine.transform(page.title);

        documentData.addParagraph(HTMLLikeParser.parseHTMLLikeParagraph(titleStyle, title));
        for (String paragraphText : page.paragraphText) {
            String text = templateEngine.transform(paragraphText);
            documentData.addParagraph(HTMLLikeParser.parseHTMLLikeParagraph(null, text));
        }

        window.setDocument(documentData);
        for (DialogResponse r : page.responses) {
            String text = templateEngine.transform(r.text);
            window.addResponseOption(character, event.getTalkTo(), text, r.action);
        }
    }

    private void updateTemplateMappings(EntityRef character) {
        EntityRef controller = character.getComponent(CharacterComponent.class).controller; // the client
        ClientComponent clientComponent = controller.getComponent(ClientComponent.class);
        EntityRef clientInfo = clientComponent.clientInfo;

        mappings.put("player.name", clientInfo.getComponent(DisplayNameComponent.class).name);
        mappings.put("player.color", "0x" + clientInfo.getComponent(ColorComponent.class).color.toHex());
    }

    @ReceiveEvent
    public void closeDialog(CloseDialogEvent event, EntityRef character) {
        nuiManager.closeScreen(DialogScreen.ASSET_URI);
    }

    @ReceiveEvent
    public void changeDialog(ChangeDialogEvent event, EntityRef character) {
        EntityRef target = event.getTarget();
        target.removeComponent(DialogComponent.class);
        boolean active = false;

        Optional<Prefab> opt2 = assetManager.getAsset(event.getPrefab(), Prefab.class);
        if (opt2.isPresent()) {
            Prefab prefab = opt2.get();
            DialogComponent newDialog = prefab.getComponent(DialogComponent.class);
            if (newDialog != null) {
                target.addComponent(newDialog);
                active = true;
            }
        }

        updateTalkNotification(character, active);
    }

    private String createTalkText() {
        SimpleUri id = new SimpleUri("engine:frob");
        List<Input> inputs = bindsSubsystem.getInputsForBindButton(id);
        String text = "Press ";
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
        text += " to talk";
        return text;
    }
}
