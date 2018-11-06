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

import java.util.Map;
import java.util.Optional;

import org.terasology.persistence.typeHandling.*;

import com.google.common.collect.ImmutableMap;

@RegisterTypeHandler
public class NewDialogActionTypeHandler extends TypeHandler<NewDialogAction> {

    @Override
    public PersistedData serialize(NewDialogAction action, PersistedDataSerializer context) {
        Map<String, PersistedData> data = ImmutableMap.of(
                "type", context.serialize(action.getClass().getSimpleName()),
                "target", context.serialize(action.getTarget()));

        return context.serialize(data);.
    }

    @Override
    public Optional<NewDialogAction> deserialize(PersistedData data) {
        PersistedDataMap root = data.getAsValueMap();
        String target = root.get("target").getAsString();
        return Optional.ofNullable(new NewDialogAction(target));
    }

    @Override
    protected PersistedData serializeNonNull(NewDialogAction value, PersistedDataSerializer serializer) {
        return null;
    }
}
