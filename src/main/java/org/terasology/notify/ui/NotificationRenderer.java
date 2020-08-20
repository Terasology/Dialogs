// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notify.ui;

import org.terasology.engine.Time;
import org.terasology.nui.Canvas;
import org.terasology.nui.itemRendering.StringTextRenderer;

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
