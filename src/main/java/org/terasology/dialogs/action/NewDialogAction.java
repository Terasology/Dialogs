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

package org.terasology.dialogs.action;

import org.terasology.asset.Assets;
import org.terasology.dialogs.ShowDialogEvent;
import org.terasology.dialogs.components.DialogComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.Prefab;

/**
 *
 */
public class NewDialogAction implements PlayerAction {

    private String target;

    /**
     * @param target
     */
    public NewDialogAction(String target) {
        this.target = target;
    }

    @Override
    public void execute(EntityRef charEntity) {
        Prefab prefab = Assets.getPrefab(target).get();
        DialogComponent dialog = prefab.getComponent(DialogComponent.class);
        charEntity.send(new ShowDialogEvent(dialog));
    }

    /**
     * @return
     */
    public String getTarget() {
        return target;
    }

}
