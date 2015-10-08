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
package org.terasology.notify;

import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.notify.ui.NotificationEvent;
import org.terasology.notify.ui.NotificationPopup;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;

/**
 */
@RegisterSystem(RegisterMode.CLIENT)
public class NotificationSystem extends BaseComponentSystem implements UpdateSubscriberSystem {

    @In
    private NUIManager nuiManager;

    @In
    private Time time;

    private final static long FULL_ALPHA_TIME = 3000;
    private final static long DIM_ALPHA_TIME = 1500;

    private long lastNotificationReceived;

    private NotificationPopup window;

    @ReceiveEvent
    public void newEntryNotificationReceived(NotificationEvent event, EntityRef character) {
        lastNotificationReceived = time.getGameTimeInMs();
        window = nuiManager.pushScreen(NotificationPopup.ASSET_URI, NotificationPopup.class);
        window.setText(event.getText());
    }

    @Override
    public void update(float delta) {
        if (window == null) {
            return;
        }

        long currentTime = time.getGameTimeInMs();
        long target = lastNotificationReceived + FULL_ALPHA_TIME;

        float alpha;
        if (currentTime > target + DIM_ALPHA_TIME) {
            alpha = 0f;
            nuiManager.closeScreen(NotificationPopup.ASSET_URI);
            window = null;
        } else if (currentTime > target) {
            alpha = 1f - (currentTime - target) / (float) DIM_ALPHA_TIME;
        } else {
            alpha = 1f;
        }

        if (alpha > 0f) {
            window.setAlpha(alpha);
        }
    }
}

