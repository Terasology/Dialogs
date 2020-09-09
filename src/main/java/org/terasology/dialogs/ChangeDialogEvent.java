// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.dialogs;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.Event;

/**
 * Fired when the dialog component of an entity should change.
 */
public class ChangeDialogEvent implements Event {

    private final String prefab;
    private final EntityRef target;

    public ChangeDialogEvent(EntityRef target, String prefab) {
        this.prefab = prefab;
        this.target = target;
    }

    public EntityRef getTarget() {
        return target;
    }

    public String getPrefab() {
        return prefab;
    }
}
