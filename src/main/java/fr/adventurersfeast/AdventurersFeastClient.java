
package fr.adventurersfeast;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = AdventurersFeast.MODID, dist = Dist.CLIENT)
public class AdventurersFeastClient {

    public AdventurersFeastClient() {
        AdventurersFeast.LOGGER.info(
            "Adventurer's Feast client initialized"
        );
    }
}
