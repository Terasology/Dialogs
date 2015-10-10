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

import org.terasology.dialogs.components.DialogComponent;
import org.terasology.entitySystem.event.Event;

/**
 * TODO Type description
 */
public class ShowDialogEvent implements Event {

    private final DialogComponent dialog;
    private final String page;

    /**
     * @param dialogComponent
     * @param page
     */
    public ShowDialogEvent(DialogComponent dialogComponent, String page) {
        this.dialog = dialogComponent;
        this.page = page;
    }

    /**
     * @return
     */
    public DialogComponent getDialog() {
        return dialog;
    }

    public String getPage() {
        return page;
    }
}
