// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.dialogs.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.terasology.engine.core.module.ModuleManager;
import org.terasology.engine.persistence.typeHandling.TypeHandlerLibraryImpl;
import org.terasology.engine.persistence.typeHandling.gson.GsonPersistedData;
import org.terasology.engine.persistence.typeHandling.gson.GsonPersistedDataSerializer;
import org.terasology.engine.registry.In;
import org.terasology.moduletestingenvironment.MTEExtension;
import org.terasology.moduletestingenvironment.extension.Dependencies;
import org.terasology.persistence.typeHandling.TypeHandler;
import org.terasology.persistence.typeHandling.TypeHandlerLibrary;
import org.terasology.reflection.TypeInfo;
import org.terasology.reflection.TypeRegistry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MTEExtension.class)
@Dependencies({"Dialogs"})
public class PlayerActionSerializationTest {
    public static final GsonPersistedDataSerializer SERIALIZER = new GsonPersistedDataSerializer();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private TypeHandler<PlayerAction> playerActionTypeHandler;

    @In
    ModuleManager moduleManager;

    @In
    TypeRegistry typeRegistry;

    @BeforeEach
    public void setup() {
        TypeHandlerLibrary typeHandlerLibrary = new TypeHandlerLibraryImpl(moduleManager, typeRegistry);
        playerActionTypeHandler = typeHandlerLibrary.getBaseTypeHandler(TypeInfo.of(PlayerAction.class));
    }

    @Test
    public void testCloseDialogActionSerialize() {
        CloseDialogAction action = new CloseDialogAction();
        String json = serialize(action);
        String expected = "{\n" +
                "  \"class\": \"" + CloseDialogAction.class.getName() + "\"\n" +
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
                "  \"class\": \"" + ChangeDialogAction.class.getName() + "\",\n" +
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
                "  \"class\": \"" + NewDialogAction.class.getName() + "\",\n" +
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
