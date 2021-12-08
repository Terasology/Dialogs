// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.dialogs.action;

import org.terasology.dialogs.ChangeDialogEvent;
import org.terasology.engine.entitySystem.entity.EntityRef;

/**
 *
 */
public class ChangeDialogAction implements PlayerAction {

    private final String prefab;

    public ChangeDialogAction(String prefab) {
        this.prefab = prefab;
    }

    @Override
    public void execute(EntityRef charEntity, EntityRef talkTo) {
        charEntity.send(new ChangeDialogEvent(talkTo, prefab));
    }

    public String getPrefab() {
        return prefab;
    }
}

