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

import org.terasology.engine.bootstrap.ClassMetaLibrary;
import org.terasology.persistence.typeHandling.*;

import java.util.Iterator;
import java.util.Optional;

@RegisterTypeHandler
public class PlayerActionTypeHandler extends TypeHandler<PlayerAction> {

    private final ClassMetaLibrary classLibrary;

    public PlayerActionTypeHandler(ClassMetaLibrary classLibrary) {
        this.classLibrary = classLibrary;
    }

    @Override
    public PersistedData serialize(PlayerAction value, PersistedDataSerializer context) {
        String typeId = value.getClass().getSimpleName();
        Iterable<Class<? extends PlayerAction>> types = classLibrary.getSubtypesOf(PlayerAction.class, typeId);
        Iterator<Class<? extends PlayerAction>> it = types.iterator();
        if (!it.hasNext()) {
            throw new DeserializationException("Could not find class metadata for '" + typeId + "'");
        }
        Class<? extends PlayerAction> type = it.next();
        if (it.hasNext()) {
            throw new DeserializationException("Ambiguous type: '" + typeId + "' - found " + types);
        }

        return context.serialize(value, type);.
    }

    @Override
    public Optional<PlayerAction> deserialize(PersistedData data) {
        String typeId = data.getAsValueMap().getAsString("type");
        Iterable<Class<? extends PlayerAction>> types = classLibrary.getSubtypesOf(PlayerAction.class, typeId);
        Iterator<Class<? extends PlayerAction>> it = types.iterator();
        if (!it.hasNext()) {
            throw new DeserializationException("Could not find class metadata for '" + typeId + "'");
        }
        Class<? extends PlayerAction> type = it.next();
        if (it.hasNext()) {
            throw new DeserializationException("Ambiguous type: '" + typeId + "' - found " + types);
        }
        return data.(data, type);
    }

    @Override
    protected PersistedData serializeNonNull(PlayerAction value, PersistedDataSerializer serializer) {
        return null;
    }
}
