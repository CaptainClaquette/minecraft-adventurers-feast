
package fr.adventurersfeast;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import fr.adventurersfeast.catalog.CatalogLoader;
import fr.adventurersfeast.event.FoodConsumptionHandler;
import fr.adventurersfeast.client.FoodTooltipHandler;
import fr.adventurersfeast.network.CatalogSyncHandler;

import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

@Mod(AdventurersFeast.MODID)
public class AdventurersFeast {

        public static final String MODID = "adventurersfeast";

        public static final Logger LOGGER = LogUtils.getLogger();

        private static final CatalogLoader CATALOG_LOADER = new CatalogLoader();

        public AdventurersFeast(
                        IEventBus modEventBus,
                        ModContainer modContainer) {
                NeoForge.EVENT_BUS.addListener(
                                AdventurersFeast::onAddReloadListeners);

                NeoForge.EVENT_BUS.addListener(
                                new FoodConsumptionHandler(CATALOG_LOADER)::onItemFinished);

                NeoForge.EVENT_BUS.addListener(
                                new FoodTooltipHandler()::onTooltip);

                NeoForge.EVENT_BUS.addListener(
                                new CatalogSyncHandler(CATALOG_LOADER)::onDatapackSync);

                LOGGER.info("Adventurer's Feast initialized");
        }

        private static void onAddReloadListeners(
                        AddServerReloadListenersEvent event) {
                event.addListener(
                                Identifier.fromNamespaceAndPath(
                                                MODID,
                                                "food_catalog"),
                                CATALOG_LOADER);
        }
}
