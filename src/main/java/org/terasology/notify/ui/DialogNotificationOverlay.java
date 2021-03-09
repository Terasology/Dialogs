// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notify.ui;

import org.terasology.assets.ResourceUrn;
import org.terasology.engine.core.Time;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.CoreScreenLayer;
import org.terasology.nui.widgets.UIList;

import java.util.ArrayList;
import java.util.List;

public class DialogNotificationOverlay extends CoreScreenLayer {

    public static final ResourceUrn ASSET_URI = new ResourceUrn("Dialogs:NotificationOverlay");

    @In
    private Time time;

    private UIList<Notification> list;

    private List<Notification> notifications = new ArrayList<>();

    @Override
    public void initialise() {
        list = find("notificationList", UIList.class);
        list.setList(notifications);
        list.setItemRenderer(new NotificationRenderer(time));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        long removeThreshold = time.getGameTimeInMs() - Notification.FADE_OUT_TIME;
        notifications.removeIf(n -> removeThreshold > n.getTimeRemoved());
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

    public void addNotification(String text) {
        long currentTime = time.getGameTimeInMs();
        for (Notification n : notifications) {
            if (n.getText().equals(text)) {
                n.setTimeAdded(currentTime);
                return;
            }
        }
        notifications.add(new Notification(text, currentTime));
    }

    public void removeNotification(String text) {
        for (Notification n : notifications) {
            if (n.getText().equals(text)) {
                startRemoval(n);
            }
        }
    }

    private void startRemoval(Notification n) {
        if (n.getTimeRemoved() != Long.MAX_VALUE) {   // HACK: check if removal has already been started
            return;
        }

        long currentTime = time.getGameTimeInMs();
        long timeIn = currentTime - n.getTimeAdded();
        if (timeIn > Notification.FADE_IN_TIME) {
            n.setTimeRemoved(currentTime);
        } else {
            float delta = 1f - timeIn / (float) Notification.FADE_IN_TIME;
            long offset = (long) (delta * Notification.FADE_OUT_TIME);
            n.setTimeRemoved(currentTime - offset);
        }
    }
}
