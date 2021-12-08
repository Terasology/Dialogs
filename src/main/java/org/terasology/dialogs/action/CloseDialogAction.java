// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.dialogs.action;

import org.terasology.dialogs.CloseDialogEvent;
import org.terasology.engine.entitySystem.entity.EntityRef;

/**
 *
 */
public class CloseDialogAction implements PlayerAction {

    @Override
    public void execute(EntityRef charEntity, EntityRef talkTo) {
        charEntity.send(new CloseDialogEvent());
    }

}
