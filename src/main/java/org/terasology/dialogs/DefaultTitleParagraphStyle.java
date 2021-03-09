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
