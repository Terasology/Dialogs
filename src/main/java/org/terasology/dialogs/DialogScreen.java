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

import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.layouts.ColumnLayout;
import org.terasology.rendering.nui.widgets.UIButton;
import org.terasology.rendering.nui.widgets.browser.data.html.HTMLDocument;
import org.terasology.rendering.nui.widgets.browser.ui.BrowserHyperlinkListener;
import org.terasology.rendering.nui.widgets.browser.ui.BrowserWidget;

public class DialogScreen extends CoreScreenLayer {

    private ColumnLayout categoryButtons;
    private BrowserWidget browser;


    @Override
    protected void initialise() {

        categoryButtons = find("categoryButtons", ColumnLayout.class);
        if (categoryButtons != null) {
            for (int i = 0; i < 3; i++) {
                UIButton newButton = new UIButton();
                newButton.setText("Answer " + (i + 1));
                String hyperlink = "option" + i;
                newButton.subscribe(widget -> navigateTo(hyperlink));
                categoryButtons.addWidget(newButton, null);
            }
        }

        browser = find("browser", BrowserWidget.class);
        if (browser != null) {
            browser.addBrowserHyperlinkListener(new BrowserHyperlinkListener() {
                @Override
                public void hyperlinkClicked(String hyperlink) {
                    navigateTo(hyperlink);
                }
            });
        }
    }

    public void navigateTo(String hyperlink) {
//        browser.navigateTo(hyperlink);
    }

    /**
     * @param documentData
     */
    public void setDocument(HTMLDocument documentData) {
        browser.navigateTo(documentData);
    }
}
