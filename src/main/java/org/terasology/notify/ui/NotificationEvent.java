// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notify.ui;

import org.terasology.gestalt.entitysystem.event.Event;

public class NotificationEvent implements Event {

    private final String text;

    public NotificationEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
