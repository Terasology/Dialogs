// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.dialogs.components;

import org.terasology.dialogs.action.PlayerAction;
import org.terasology.reflection.MappedContainer;

import java.util.List;

/**
 *
 */
@MappedContainer
public class DialogResponse {
    public String text;
    public String responseImage;
    public List<PlayerAction> action;
}
