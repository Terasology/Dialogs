// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

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
     *
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
