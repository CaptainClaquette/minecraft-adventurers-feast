
package fr.adventurersfeast.client;

import fr.adventurersfeast.catalog.BuffDefinition;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Locale;

public final class TooltipFormatter {

    private TooltipFormatter() {}

    public static Component format(BuffDefinition buff) {

        if (!"effect".equals(buff.type())) {
            return Component.empty();
        }

        Identifier id = Identifier.tryParse(buff.id());

        if (id == null) {
            return Component.empty();
        }

        var effect = BuiltInRegistries.MOB_EFFECT.get(id);

        if (effect.isEmpty()) {
            return Component.empty();
        }

        Component name = effect.get().value().getDisplayName();

        String level = toRoman(buff.amplifier() + 1);
        String duration = formatDuration(buff.duration());

        return Component.empty()
            .append(name)
            .append(" " + level + " (" + duration + ")")
            .withStyle(ChatFormatting.BLUE);
    }

    private static String formatDuration(int seconds) {
        return String.format(
            Locale.ROOT,
            "%d:%02d",
            seconds / 60,
            seconds % 60
        );
    }

    private static String toRoman(int level) {
        return switch (level) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> Integer.toString(level);
        };
    }
}
