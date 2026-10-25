package fr.adventurersfeast.catalog;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;

import org.slf4j.Logger;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.core.registries.BuiltInRegistries;

public class CatalogLoader implements ResourceManagerReloadListener {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();

    private static final Path CONFIG_DIRECTORY = FMLPaths.CONFIGDIR
            .get()
            .resolve("adventurersfeast")
            .resolve("food_buffs");

    private volatile Map<String, FoodDefinition> foods = Map.of();

    @Override
    public void onResourceManagerReload(ResourceManager manager) {

        Map<String, FoodDefinition> loaded = new HashMap<>();

        // 1. Catalogues intégrés au mod et datapacks
        loadDatapacks(manager, loaded);

        // 2. Configurations locales prioritaires
        loadConfigFiles(loaded);

        // 3. Publication atomique du catalogue
        foods = Map.copyOf(loaded);

        LOGGER.info(
                "Adventurer's Feast loaded {} food definitions",
                foods.size());
    }

    private void loadDatapacks(
            ResourceManager manager,
            Map<String, FoodDefinition> loaded) {
        var resources = manager.listResources(
                "food_buffs",
                location -> location.getNamespace().equals("adventurersfeast")
                        && location.getPath().endsWith(".json"));

        resources.forEach((location, resource) -> {
            try (Reader reader = resource.openAsReader()) {

                loadFood(reader, location.toString(), loaded);

            } catch (Exception e) {
                LOGGER.error(
                        "Failed to load datapack food definition {}",
                        location,
                        e);
            }
        });
    }

    private void loadConfigFiles(
            Map<String, FoodDefinition> loaded) {
        try {
            Files.createDirectories(CONFIG_DIRECTORY);

            try (Stream<Path> files = Files.walk(CONFIG_DIRECTORY)) {

                files.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".json"))
                        .sorted()
                        .forEach(path -> {

                            try (Reader reader = Files.newBufferedReader(path)) {

                                loadFood(reader, path.toString(), loaded);

                            } catch (Exception e) {
                                LOGGER.error(
                                        "Failed to load config food definition {}",
                                        path,
                                        e);
                            }
                        });
            }

        } catch (Exception e) {
            LOGGER.error(
                    "Failed to read Adventurer's Feast config directory",
                    e);
        }
    }

    private void loadFood(
            Reader reader,
            String source,
            Map<String, FoodDefinition> loaded) {
        JsonElement json = JsonParser.parseReader(reader);

        FoodDefinition food = GSON.fromJson(
                json,
                FoodDefinition.class);

        if (food == null || !validId(food.item())) {
            LOGGER.warn("Invalid food definition in {}", source);
            return;
        }

        List<BuffDefinition> buffs = food.buffs().stream()
                .filter(this::validBuff)
                .toList();

        boolean override = loaded.containsKey(food.item());

        loaded.put(
                food.item(),
                new FoodDefinition(
                        food.item(),
                        buffs));

        LOGGER.debug(
                "{} food definition {} from {}",
                override ? "Overridden" : "Loaded",
                food.item(),
                source);
    }

    private boolean validId(String id) {
        return id != null && Identifier.tryParse(id) != null;
    }

    private boolean validBuff(BuffDefinition buff) {

        if (buff == null
                || !"effect".equals(buff.type())
                || !validId(buff.id())
                || buff.duration() <= 0
                || buff.amplifier() < 0) {
            return false;
        }

        Identifier id = Identifier.tryParse(buff.id());

        if (BuiltInRegistries.MOB_EFFECT.get(id).isEmpty()) {
            LOGGER.debug("Skipping unavailable effect: {}", buff.id());
            return false;
        }

        return true;
    }

    public Map<String, FoodDefinition> getFoods() {
        return foods;
    }
}