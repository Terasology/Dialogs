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

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.notify.ui.NotificationEvent;
import org.terasology.notify.ui.NotificationOverlay;
import org.terasology.notify.ui.RemoveNotificationEvent;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;

/**
 */
@RegisterSystem(RegisterMode.CLIENT)
public class NotificationSystem extends BaseComponentSystem {

    @In
    private NUIManager nuiManager;

    private NotificationOverlay window;

    @Override
    public void initialise() {
        window = nuiManager.addOverlay(NotificationOverlay.ASSET_URI, NotificationOverlay.class);
    }

    @Override
    public void shutdown() {
        nuiManager.closeScreen(window);
    }

    @ReceiveEvent
    public void newEntryNotificationReceived(NotificationEvent event, EntityRef character) {
        window.addNotification(event.getText());
    }

    @ReceiveEvent
    public void removeEntryNotificationReceived(RemoveNotificationEvent event, EntityRef character) {
        window.removeNotification(event.getText());
    }
}

