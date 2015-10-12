/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.dialogs;

import java.util.List;

import org.terasology.assets.ResourceUrn;
import org.terasology.dialogs.action.PlayerAction;
import org.terasology.dialogs.components.DialogComponent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.In;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.layouts.ColumnLayout;
import org.terasology.rendering.nui.widgets.UIButton;
import org.terasology.rendering.nui.widgets.browser.data.html.HTMLDocument;
import org.terasology.rendering.nui.widgets.browser.ui.BrowserWidget;

import com.google.common.collect.Lists;

public class DialogScreen extends CoreScreenLayer {

    public static final ResourceUrn ASSET_URI = new ResourceUrn("Dialogs:DialogScreen");

    private ColumnLayout responseButtons;
    private BrowserWidget browser;
    private final HTMLDocument emptyPage = new HTMLDocument(null);

    @In
    private LocalPlayer localPlayer;

    @Override
    protected void initialise() {

        responseButtons = find("responseButtons", ColumnLayout.class);
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
//        // HACK! implement ColumnLayout.removeWidget()
//        Iterator<UIWidget> it = responseButtons.iterator();
//        while (it.hasNext()) {
//            it.next();
//            it.remove();
//        }
    }

    public void addResponseOption(DialogComponent dialog, String text, List<PlayerAction> actions) {
        UIButton newButton = new UIButton();
        newButton.setText(text);
        newButton.subscribe(widget -> {
            for (PlayerAction action : actions) {
                action.execute(dialog, localPlayer.getCharacterEntity());
            }
        });
        responseButtons.addWidget(newButton, null);
    }
}
