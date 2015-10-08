/*
 * Copyright 2014 MovingBlocks
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
package org.terasology.notify.ui;

import org.terasology.assets.ResourceUrn;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.Color;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.widgets.UILabel;

/**
 */
public class NotificationPopup extends CoreScreenLayer {

    public static final ResourceUrn ASSET_URI = new ResourceUrn("Dialogs:NotificationPopup");

    private float alpha = 1f;

    private UILabel label;

    @Override
    public void initialise() {
        label = find("notificationLabel", UILabel.class);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.setAlpha(alpha);

//        canvas.drawFilledRectangle(canvas.getRegion(), Color.GREY);
        super.onDraw(canvas);

        canvas.setAlpha(1f);
    }

    @Override
    public boolean isModal() {
        return false;
    }

    @Override
    public boolean isReleasingMouse() {
        return false;
    }

    @Override
    public boolean isEscapeToCloseAllowed() {
        return false;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setText(String text) {
        label.setText(text);
    }
}
