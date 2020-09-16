// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.dialogs.notify;

import org.terasology.dialogs.notify.ui.DialogNotificationOverlay;
import org.terasology.dialogs.notify.ui.NotificationEvent;
import org.terasology.dialogs.notify.ui.RemoveNotificationEvent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.NUIManager;

@RegisterSystem(RegisterMode.CLIENT)
public class NotificationSystem extends BaseComponentSystem {

    @In
    private NUIManager nuiManager;

    private DialogNotificationOverlay window;

    @Override
    public void initialise() {
        window = nuiManager.addOverlay(DialogNotificationOverlay.ASSET_URI, DialogNotificationOverlay.class);
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
