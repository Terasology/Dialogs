// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.dialogs.action;

import org.terasology.engine.entitySystem.entity.EntityRef;

/**
 *
 */
public interface PlayerAction {
    void execute(EntityRef charEntity, EntityRef talkTo);
}
