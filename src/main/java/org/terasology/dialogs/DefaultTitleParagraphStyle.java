// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.dialogs;

import org.terasology.engine.rendering.assets.font.Font;
import org.terasology.engine.rendering.nui.widgets.browser.ui.style.ContainerInteger;
import org.terasology.engine.rendering.nui.widgets.browser.ui.style.FixedContainerInteger;
import org.terasology.engine.rendering.nui.widgets.browser.ui.style.ParagraphRenderStyle;
import org.terasology.engine.utilities.Assets;

public class DefaultTitleParagraphStyle implements ParagraphRenderStyle {
    @Override
    public Font getFont(boolean hyperlink) {
        return Assets.getFont("engine:NotoSans-Regular-Large").get();
    }

    @Override
    public ContainerInteger getParagraphPaddingTop() {
        return new FixedContainerInteger(10);
    }
}
