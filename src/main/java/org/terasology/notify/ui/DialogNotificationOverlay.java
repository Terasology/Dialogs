/*
 * Copyright 2018 MovingBlocks
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

import java.util.ArrayList;
import java.util.List;

import org.terasology.assets.ResourceUrn;
import org.terasology.engine.Time;
import org.terasology.nui.widgets.UIList;
import org.terasology.registry.In;
import org.terasology.rendering.nui.CoreScreenLayer;

public class DialogNotificationOverlay extends CoreScreenLayer {

    public static final ResourceUrn ASSET_URI = new ResourceUrn("Dialogs:DialogNotificationOverlay");

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
