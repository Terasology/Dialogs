/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.dialogs.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;

import java.util.List;

/**
 * Use this to add a dialog
 */
public final class DialogComponent implements Component {

    @Replicate
    public String firstPage;

    @Replicate
    public List<DialogPage> pages;

    /**
     * Finds a page by ID.
     * @param id the page ID
     * @return the corresponding page or <code>null</code>.
     */
    public DialogPage getPage(String id) {
        for (DialogPage page : pages) {
            if (id.equals(page.id)) {
                return page;
            }
        }
        return null;
    }
}
