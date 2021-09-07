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

package org.terasology.dialogs;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.gestalt.entitysystem.event.Event;

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
