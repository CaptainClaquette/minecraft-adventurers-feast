package fr.adventurersfeast.network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.adventurersfeast.AdventurersFeast;
import fr.adventurersfeast.catalog.FoodDefinition;
import fr.adventurersfeast.client.ClientCatalog;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

import java.lang.reflect.Type;
import java.util.Map;

@EventBusSubscriber(modid = AdventurersFeast.MODID)
public final class NetworkHandler {

        private static final Gson GSON = new Gson();

        private static final Type FOOD_MAP_TYPE = new TypeToken<Map<String, FoodDefinition>>() {
        }.getType();

        private NetworkHandler() {
        }

        @SubscribeEvent
        public static void registerPayloads(
                        RegisterPayloadHandlersEvent event) {

                var registrar = event.registrar("1");

                // Catalogue alimentaire
                registrar.playToClient(
                                CatalogSyncPayload.TYPE,
                                CatalogSyncPayload.STREAM_CODEC);
        }

        public static void registerClient(
                        RegisterClientPayloadHandlersEvent event) {

                // Réception du catalogue alimentaire
                event.register(
                                CatalogSyncPayload.TYPE,
                                (payload, context) -> context.enqueueWork(() -> {

                                        Map<String, FoodDefinition> definitions = GSON.fromJson(
                                                        payload.json(),
                                                        FOOD_MAP_TYPE);

                                        ClientCatalog.replace(
                                                        definitions == null ? Map.of() : definitions);
                                }));
        }
}