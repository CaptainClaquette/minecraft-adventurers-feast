package fr.adventurersfeast.client;

import fr.adventurersfeast.catalog.FoodDefinition;

import java.util.Map;

public final class ClientCatalog {

    private static volatile Map<String, FoodDefinition> foods = Map.of();

    private ClientCatalog() {
    }

    public static Map<String, FoodDefinition> getFoods() {
        return foods;
    }

    public static void replace(Map<String, FoodDefinition> definitions) {
        foods = Map.copyOf(definitions);
    }

    public static void clear() {
        foods = Map.of();
    }
}