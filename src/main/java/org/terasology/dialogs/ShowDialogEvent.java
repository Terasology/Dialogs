// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.dialogs;

import org.terasology.dialogs.components.DialogComponent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.gestalt.entitysystem.event.Event;

public class ShowDialogEvent implements Event {

    private final String page;
    private final EntityRef talkTo;

    /**
     * @param talkTo the other entity of the dialog
     * @param page
     */
    public ShowDialogEvent(EntityRef talkTo, String page) {
        this.talkTo = talkTo;
        this.page = page;
    }

    public DialogComponent getDialog() {
        return talkTo.getComponent(DialogComponent.class);
    }

    public String getPage() {
        return page;
    }

    /**
     * @return the other entity of the dialog
     */
    public EntityRef getTalkTo() {
        return talkTo;
    }
}
