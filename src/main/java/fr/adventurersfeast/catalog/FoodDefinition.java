package fr.adventurersfeast.catalog;

import java.util.List;

public record FoodDefinition(
    String item,
    List<BuffDefinition> buffs
) {
    public FoodDefinition {
        buffs = buffs == null
            ? List.of()
            : List.copyOf(buffs);
    }
}