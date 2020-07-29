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

import com.google.common.collect.Lists;
import org.terasology.assets.ResourceUrn;
import org.terasology.dialogs.action.PlayerAction;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.layouts.ColumnLayout;
import org.terasology.rendering.nui.widgets.UIButton;
import org.terasology.rendering.nui.widgets.UIImage;
import org.terasology.rendering.nui.widgets.browser.data.html.HTMLDocument;
import org.terasology.rendering.nui.widgets.browser.ui.BrowserWidget;

import java.util.List;

public class DialogScreen extends CoreScreenLayer {

    public static final ResourceUrn ASSET_URI = new ResourceUrn("Dialogs:DialogScreen");

    private ColumnLayout responseButtons;
    private ColumnLayout responseImage;
    private BrowserWidget browser;
    private final HTMLDocument emptyPage = new HTMLDocument(null);

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
//        // HACK! implement ColumnLayout.removeWidget()
//        Iterator<UIWidget> it = responseButtons.iterator();
//        while (it.hasNext()) {
//            it.next();
//            it.remove();
//        }
    }

    public void addResponseOption(EntityRef charEnt, EntityRef talkTo, String text, List<PlayerAction> actions) {
        UIButton newButton = new UIButton();
        UIImage newImage = new UIImage();
        newButton.setText(text);
        newButton.subscribe(widget -> {
            for (PlayerAction action : actions) {
                action.execute(charEnt, talkTo);
            }
        });
        responseImage.addWidget(newImage, null);
        responseButtons.addWidget(newButton, null);
    }
}
