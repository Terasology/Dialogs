/*
 * Copyright 2019 MovingBlocks
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
package org.terasology.dialogs.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;
import org.terasology.ModuleEnvironmentTest;
import org.terasology.naming.Name;
import org.terasology.persistence.ModuleContext;
import org.terasology.persistence.typeHandling.TypeHandler;
import org.terasology.persistence.typeHandling.TypeHandlerLibrary;
import org.terasology.persistence.typeHandling.gson.GsonPersistedData;
import org.terasology.persistence.typeHandling.gson.GsonPersistedDataSerializer;
import org.terasology.reflection.TypeInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PlayerActionSerializationTest extends ModuleEnvironmentTest {
    public static final GsonPersistedDataSerializer SERIALIZER = new GsonPersistedDataSerializer();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private TypeHandler<PlayerAction> playerActionTypeHandler;

    @Override
    public void setup() {
        ModuleContext.setContext(moduleManager.getEnvironment().get(new Name("Dialogs")));

        TypeHandlerLibrary typeHandlerLibrary = new TypeHandlerLibrary(moduleManager, typeRegistry);
        playerActionTypeHandler = typeHandlerLibrary.getBaseTypeHandler(TypeInfo.of(PlayerAction.class));
    }

    @Test
    public void testCloseDialogAction() {
        CloseDialogAction action = new CloseDialogAction();
        String json = serialize(action);
        String expected = "{\n" +
                              "  \"class\": \"Dialogs:CloseDialogAction\"\n" +
                              "}";

        assertEquals(expected, json);

        final String CONTEXT_BASED_JSON = "{\n" +
                                              "\"class\": \"CloseDialogAction\"\n" +
                                              "}";

        action = deserialize(CONTEXT_BASED_JSON, CloseDialogAction.class);

        assertNotNull(action);
    }

    @Test
    public void testChangeDialogAction() {
        ChangeDialogAction action = new ChangeDialogAction("$prefab");
        String json = serialize(action);
        String expected = "{\n" +
                              "  \"class\": \"Dialogs:ChangeDialogAction\",\n" +
                              "  \"prefab\": \"$prefab\"\n" +
                              "}";

        assertEquals(expected, json);

        final String CONTEXT_BASED_JSON = "{\n" +
                                              "\"class\": \"ChangeDialogAction\",\n" +
                                              "\"prefab\": \"$prefab\"\n" +
                                              "}";

        action = deserialize(CONTEXT_BASED_JSON, ChangeDialogAction.class);

        assertNotNull(action);
        assertEquals("$prefab", action.getPrefab());
    }

    @Test
    public void testNewDialogAction() {
        NewDialogAction action = new NewDialogAction("$$$");
        String json = serialize(action);
        String expected = "{\n" +
                              "  \"class\": \"Dialogs:NewDialogAction\",\n" +
                              "  \"target\": \"$$$\"\n" +
                              "}";

        assertEquals(expected, json);

        final String CONTEXT_BASED_JSON = "{\n" +
                                              "  \"class\": \"NewDialogAction\",\n" +
                                              "  \"target\": \"$$$\"\n" +
                                              "}";

        action = deserialize(CONTEXT_BASED_JSON, NewDialogAction.class);

        assertNotNull(action);
        assertEquals("$$$", action.getTarget());
    }

    private <T extends PlayerAction> String serialize(T action) {
        GsonPersistedData serialized =
            (GsonPersistedData) playerActionTypeHandler.serialize(action, SERIALIZER);
        return GSON.toJson(serialized.getElement());
    }

    private <T extends PlayerAction> T deserialize(String json, Class<T> clazz) {
        GsonPersistedData data = new GsonPersistedData(GSON.fromJson(json, JsonElement.class));
        PlayerAction playerAction = playerActionTypeHandler.deserializeOrThrow(data);

        assertTrue(clazz.isInstance(playerAction));
        return clazz.cast(playerAction);
    }
}
