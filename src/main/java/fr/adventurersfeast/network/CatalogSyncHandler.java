package fr.adventurersfeast.network;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;

import fr.adventurersfeast.catalog.CatalogLoader;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import org.slf4j.Logger;

public class CatalogSyncHandler {

    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = LogUtils.getLogger();

    private final CatalogLoader catalog;

    public CatalogSyncHandler(CatalogLoader catalog) {
        this.catalog = catalog;
    }

    public void onDatapackSync(OnDatapackSyncEvent event) {

        String json = GSON.toJson(catalog.getFoods());

        // Synchronisation avec tous les joueurs concernés
        event.getRelevantPlayers().forEach(player -> {

            PacketDistributor.sendToPlayer(
                    player,
                    new CatalogSyncPayload(json));

            LOGGER.info(
                    "Adventurer's Feast: synchronized {} food definitions with {}",
                    catalog.getFoods().size(),
                    player.getName().getString());
        });
    }
}