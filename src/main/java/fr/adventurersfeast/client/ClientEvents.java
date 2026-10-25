package fr.adventurersfeast.client;

import fr.adventurersfeast.AdventurersFeast;
import fr.adventurersfeast.network.NetworkHandler;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = AdventurersFeast.MODID, value = Dist.CLIENT)
public final class ClientEvents {

    private ClientEvents() {
    }

    @SubscribeEvent
    public static void registerPayloads(
            RegisterClientPayloadHandlersEvent event) {

        NetworkHandler.registerClient(event);
    }

    @SubscribeEvent
    public static void onLogout(
            ClientPlayerNetworkEvent.LoggingOut event) {

        ClientCatalog.clear();
    }
}