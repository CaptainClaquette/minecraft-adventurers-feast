package fr.adventurersfeast.client;

import fr.adventurersfeast.catalog.FoodDefinition;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;

import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class FoodTooltipHandler {

    public void onTooltip(ItemTooltipEvent event) {

        String itemId = BuiltInRegistries.ITEM
            .getKey(event.getItemStack().getItem())
            .toString();

        FoodDefinition food = ClientCatalog.getFoods().get(itemId);

        if (food == null || food.buffs().isEmpty()) {
            return;
        }

        event.getToolTip().add(
            Component.literal("Adventurer's Feast")
                .withStyle(ChatFormatting.GOLD)
        );

        food.buffs().stream()
            .map(TooltipFormatter::format)
            .forEach(event.getToolTip()::add);
    }
}