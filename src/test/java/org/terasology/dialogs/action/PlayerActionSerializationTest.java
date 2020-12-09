// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
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
import org.terasology.persistence.typeHandling.TypeHandlerLibraryImpl;
import org.terasology.persistence.typeHandling.gson.GsonPersistedData;
import org.terasology.persistence.typeHandling.gson.GsonPersistedDataSerializer;
import org.terasology.reflection.TypeInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerActionSerializationTest extends ModuleEnvironmentTest {
    public static final GsonPersistedDataSerializer SERIALIZER = new GsonPersistedDataSerializer();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private TypeHandler<PlayerAction> playerActionTypeHandler;

    @Override
    public void setup() {
        ModuleContext.setContext(moduleManager.getEnvironment().get(new Name("Dialogs")));

        TypeHandlerLibrary typeHandlerLibrary = new TypeHandlerLibraryImpl(moduleManager, typeRegistry);
        playerActionTypeHandler = typeHandlerLibrary.getBaseTypeHandler(TypeInfo.of(PlayerAction.class));
    }

    @Test
    public void testCloseDialogActionSerialize() {
        CloseDialogAction action = new CloseDialogAction();
        String json = serialize(action);
        String expected = "{\n" +
                              "  \"class\": \"Dialogs:CloseDialogAction\"\n" +
                              "}";

        assertEquals(expected, json);

    }

    @Test
    public void testCloseDialogActionDeserialize() {
        final String contextBasedJson =
                "{\n" +
                "\"class\": \"CloseDialogAction\"\n" +
                "}";

        CloseDialogAction action = deserialize(contextBasedJson, CloseDialogAction.class);

        assertNotNull(action);
    }

    @Test
    public void testChangeDialogActionSerialize() {
        ChangeDialogAction action = new ChangeDialogAction("$prefab");
        String json = serialize(action);
        String expected = "{\n" +
                              "  \"class\": \"Dialogs:ChangeDialogAction\",\n" +
                              "  \"prefab\": \"$prefab\"\n" +
                              "}";

        assertEquals(expected, json);
    }

    @Test
    public void testChangeDialogActionDeserialize() {
        final String contextBasedJson = "{\n" +
                "\"class\": \"ChangeDialogAction\",\n" +
                "\"prefab\": \"$prefab\"\n" +
                "}";

        ChangeDialogAction action = deserialize(contextBasedJson, ChangeDialogAction.class);

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
    }

    @Test
    public void testNewDialogActionDeserialize() {
      final String contextBasedJson = "{\n" +
                "  \"class\": \"NewDialogAction\",\n" +
                "  \"target\": \"$$$\"\n" +
                "}";

        NewDialogAction action = deserialize(contextBasedJson, NewDialogAction.class);

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
