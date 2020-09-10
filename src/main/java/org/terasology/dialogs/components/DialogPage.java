// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.dialogs.components;

import org.terasology.nui.reflection.MappedContainer;

import java.util.List;

/**
 * TODO Type description
 */
@MappedContainer
public class DialogPage {
    public String id;
    public String title;
    public List<String> paragraphText;
    public List<DialogResponse> responses;
}
