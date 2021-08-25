// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.dialogs.components;

import java.util.List;

import org.terasology.reflection.MappedContainer;

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
