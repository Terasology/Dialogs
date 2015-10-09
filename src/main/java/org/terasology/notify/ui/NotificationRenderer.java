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

package org.terasology.notify.ui;

import org.terasology.engine.Time;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.itemRendering.StringTextRenderer;

/**
 *
 */
public class NotificationRenderer extends StringTextRenderer<Notification> {

    private final Time time;

    public NotificationRenderer(Time time) {
        this.time = time;
    }

    @Override
    public String getString(Notification value) {
        return value.getText();
    }

    @Override
    public void draw(Notification value, Canvas canvas) {
        float alpha = computeAlpha(value);
        canvas.setAlpha(alpha);
        super.draw(value, canvas);
        canvas.setAlpha(1f);
    }

    private float computeAlpha(Notification value) {
        long currentTime = time.getGameTimeInMs();

        long timeOut = currentTime - value.getTimeRemoved();
        long timeIn = currentTime - value.getTimeAdded();

        if (timeOut > 0) {
            float alphaOut = 1f - timeOut / (float) Notification.FADE_OUT_TIME;
            return Math.max(0, alphaOut);
        }

        if (timeIn < Notification.FADE_IN_TIME) {
            float alphaIn = timeIn / (float) Notification.FADE_IN_TIME;
            return alphaIn;
        }

        return 1f;
    }
}
