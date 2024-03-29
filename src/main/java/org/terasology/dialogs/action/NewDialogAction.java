// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.dialogs.action;

import org.terasology.dialogs.ShowDialogEvent;
import org.terasology.engine.entitySystem.entity.EntityRef;

public class NewDialogAction implements PlayerAction {

    private final String target;

    public NewDialogAction(String target) {
        this.target = target;
    }

    @Override
    public void execute(EntityRef charEntity, EntityRef talkTo) {
        charEntity.send(new ShowDialogEvent(talkTo, target));
    }

    public String getTarget() {
        return target;
    }

}
