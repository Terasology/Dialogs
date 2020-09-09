// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.dialogs;

import com.google.common.collect.Lists;
import org.terasology.dialogs.action.PlayerAction;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.rendering.assets.texture.TextureRegion;
import org.terasology.engine.rendering.nui.CoreScreenLayer;
import org.terasology.engine.rendering.nui.widgets.browser.data.html.HTMLDocument;
import org.terasology.engine.rendering.nui.widgets.browser.ui.BrowserWidget;
import org.terasology.gestalt.assets.ResourceUrn;
import org.terasology.nui.UIWidget;
import org.terasology.nui.databinding.ReadOnlyBinding;
import org.terasology.nui.layouts.ColumnLayout;
import org.terasology.nui.widgets.UIButton;
import org.terasology.nui.widgets.UIImage;

import java.util.List;

public class DialogScreen extends CoreScreenLayer {

    public static final ResourceUrn ASSET_URI = new ResourceUrn("Dialogs:DialogScreen");
    private final HTMLDocument emptyPage = new HTMLDocument(null);
    private ColumnLayout responseButtons;
    private ColumnLayout responseImage;
    private BrowserWidget browser;

    @Override
    public void initialise() {

        responseButtons = find("responseButtons", ColumnLayout.class);
        responseImage = find("responseImage", ColumnLayout.class);
        browser = find("browser", BrowserWidget.class);

    }

    @Override
    public boolean isModal() {
        return true;
    }

    /**
     * @param documentData
     */
    public void setDocument(HTMLDocument documentData) {
        browser.navigateTo(documentData);
    }

    public void reset() {
        browser.navigateTo(emptyPage);

        for (UIWidget widget : Lists.newArrayList(responseButtons)) {
            responseButtons.removeWidget(widget);
        }
        for (UIWidget widget : Lists.newArrayList(responseImage)) {
            responseImage.removeWidget(widget);
        }
//        // HACK! implement ColumnLayout.removeWidget()
//        Iterator<UIWidget> it = responseButtons.iterator();
//        while (it.hasNext()) {
//            it.next();
//            it.remove();
//        }
    }

    public void addResponseOption(EntityRef charEnt, EntityRef talkTo, String text, TextureRegion image,
                                  List<PlayerAction> actions) {
        UIButton newButton = new UIButton();
        UIImage newImage = new UIImage();
        newButton.setText(text);
        newImage.setImage(image);
        newImage.setFamily("imageColumn");
        newImage.bindVisible(new ReadOnlyBinding<Boolean>() {
            @Override
            public Boolean get() {
                return newButton.getMode().equals(UIButton.HOVER_MODE);
            }
        });
        newButton.subscribe(widget -> {
            for (PlayerAction action : actions) {
                action.execute(charEnt, talkTo);
            }
        });
        responseImage.addWidget(newImage, null);
        responseButtons.addWidget(newButton, null);
    }
}
