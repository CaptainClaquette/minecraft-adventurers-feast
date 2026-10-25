package fr.adventurersfeast.client;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

public final class ClientConnectionHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    private ClientConnectionHandler() {
    }

    public static void onLogout(
            ClientPlayerNetworkEvent.LoggingOut event) {
        int count = ClientCatalog.getFoods().size();
        ClientCatalog.clear();
        LOGGER.info("Adventurer's Feast: cleared {} client food definitions", count);
    }
}