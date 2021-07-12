// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.dialogs.components;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;

/**
 * Use this to add a dialog
 */
public final class DialogComponent implements Component<DialogComponent> {

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

    @Override
    public void copy(DialogComponent other) {
        this.firstPage = other.firstPage;
        this.pages.addAll(other.pages);
    }
}
