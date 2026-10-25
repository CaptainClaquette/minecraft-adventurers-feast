
package fr.adventurersfeast.event;

import fr.adventurersfeast.catalog.BuffDefinition;
import fr.adventurersfeast.catalog.CatalogLoader;
import fr.adventurersfeast.catalog.FoodDefinition;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class FoodConsumptionHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final CatalogLoader catalog;

    public FoodConsumptionHandler(CatalogLoader catalog) {
        this.catalog = catalog;
    }

    public void onItemFinished(LivingEntityUseItemEvent.Finish event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        ItemStack stack = event.getItem();

        if (stack.isEmpty()) {
            return;
        }

        String itemId = BuiltInRegistries.ITEM
                .getKey(stack.getItem())
                .toString();

        FoodDefinition food = catalog.getFoods().get(itemId);

        if (food == null) {
            return;
        }

        LOGGER.info("Adventurer's Feast: consumed {}", itemId);

        for (BuffDefinition buff : food.buffs()) {
            if ("effect".equals(buff.type())) {
                applyEffect(player, buff);
            }
        }
    }

    private void applyEffect(Player player, BuffDefinition buff) {

        Identifier id = Identifier.tryParse(buff.id());

        if (id == null) {
            LOGGER.warn("Invalid effect identifier: {}", buff.id());
            return;
        }

        var effectHolder = BuiltInRegistries.MOB_EFFECT.get(id);

        if (effectHolder.isEmpty()) {
            LOGGER.warn("Unknown effect: {}", buff.id());
            return;
        }

        MobEffectInstance instance = new MobEffectInstance(
                effectHolder.get(),
                buff.duration() * 20,
                buff.amplifier(),
                false,
                buff.particles(),
                true
        );

        player.addEffect(instance);

        LOGGER.info(
                "Applied {} level {} to {} for {} seconds",
                buff.id(),
                buff.amplifier() + 1,
                player.getName().getString(),
                buff.duration()
        );
    }
}
