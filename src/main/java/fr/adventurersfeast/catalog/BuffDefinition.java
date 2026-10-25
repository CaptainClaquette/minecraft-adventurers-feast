package fr.adventurersfeast.catalog;

public record BuffDefinition(
        String type,
        String id,
        int duration,
        int amplifier,
        boolean particles) {

}