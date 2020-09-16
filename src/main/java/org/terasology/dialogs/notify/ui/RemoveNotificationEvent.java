// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.dialogs.notify.ui;

import org.terasology.engine.entitySystem.event.Event;

/**
 *
 */
public class RemoveNotificationEvent implements Event {

    private final String text;

    public RemoveNotificationEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
