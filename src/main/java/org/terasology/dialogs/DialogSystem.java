// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.dialogs;

import org.terasology.dialogs.components.DialogComponent;
import org.terasology.dialogs.components.DialogPage;
import org.terasology.dialogs.components.DialogResponse;
import org.terasology.engine.core.SimpleUri;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.input.InputSystem;
import org.terasology.engine.logic.characters.CharacterComponent;
import org.terasology.engine.logic.characters.events.ActivationRequest;
import org.terasology.engine.logic.common.DisplayNameComponent;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.logic.players.PlayerTargetChangedEvent;
import org.terasology.engine.network.ClientComponent;
import org.terasology.engine.network.ColorComponent;
import org.terasology.engine.persistence.TemplateEngine;
import org.terasology.engine.persistence.TemplateEngineImpl;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.assets.texture.TextureRegion;
import org.terasology.engine.rendering.nui.NUIManager;
import org.terasology.engine.rendering.nui.widgets.browser.data.basic.HTMLLikeParser;
import org.terasology.engine.rendering.nui.widgets.browser.data.html.HTMLDocument;
import org.terasology.engine.rendering.nui.widgets.browser.ui.style.ParagraphRenderStyle;
import org.terasology.engine.unicode.EnclosedAlphanumerics;
import org.terasology.engine.utilities.Assets;
import org.terasology.gestalt.assets.management.AssetManager;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;
import org.terasology.input.Input;
import org.terasology.notify.ui.NotificationEvent;
import org.terasology.notify.ui.RemoveNotificationEvent;
import org.terasology.nui.Color;
import org.terasology.nui.FontColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TODO Type description
 */
@RegisterSystem(RegisterMode.CLIENT)
public class DialogSystem extends BaseComponentSystem {

    @In
    private NUIManager nuiManager;

    @In
    private InputSystem inputSystem;

    @In
    private AssetManager assetManager;

    @In
    private LocalPlayer localPlayer;

    private String talkText;

    private final ParagraphRenderStyle titleStyle = new DefaultTitleParagraphStyle();

    private final Map<String, String> mappings = new HashMap<>();

    private final TemplateEngine templateEngine = new TemplateEngineImpl(id -> mappings.getOrDefault(id, "?" + id + "?"));

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

        if (character != localPlayer.getCharacterEntity()) {
            return;
        }

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
        TextureRegion image = Assets.getTextureRegion("Dialogs:answerArrow").get();
        for (DialogResponse r : page.responses) {
            String imageURN = r.responseImage;
            if (imageURN != null) {
                image = Assets.getTextureRegion(imageURN).get();
            }
            String text = templateEngine.transform(r.text);
            window.addResponseOption(character, event.getTalkTo(), text, image, r.action);
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

    private String lookupInteractionButton() {
        List<Input> inputs = inputSystem.getInputsForBindButton(new SimpleUri("engine:frob"));
        return inputs.stream().findFirst().map(input -> input.getDisplayName()).orElse("n/a");
    }

    private String createTalkText() {
        String text = "Press ";
        String button = lookupInteractionButton();
        if (button.length() == 1) {
            int off = button.charAt(0) - 'A';
            char code = (char) (EnclosedAlphanumerics.CIRCLED_LATIN_CAPITAL_LETTER_A + off);
            text += FontColor.getColored(String.valueOf(code), new Color(0xFFFF00FF));
        } else {
            text += FontColor.getColored(button, new Color(0xFFFF00FF));
        }
        text += " to talk";
        return text;
    }
}
