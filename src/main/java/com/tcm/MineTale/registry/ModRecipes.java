package com.tcm.MineTale.registry;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.recipe.WorkbenchRecipe;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipes {

    // 1. Define the Types (The "Where")
    public static final RecipeType<WorkbenchRecipe> CAMPFIRE_TYPE = createType("campfire_recipe_type");
    public static final RecipeType<WorkbenchRecipe> WORKBENCH_TYPE = createType("workbench_recipe_type");
    public static final RecipeType<WorkbenchRecipe> FURNACE_T1_TYPE = createType("furnace_t1_recipe_type");
    
    // 2. Define the Serializers (The "How")
    // We pass the specific Type into the Serializer's constructor
    public static final RecipeSerializer<WorkbenchRecipe> FURNACE_SERIALIZER = 
        new WorkbenchRecipe.Serializer(FURNACE_T1_TYPE);
        
    public static final RecipeSerializer<WorkbenchRecipe> CAMPFIRE_SERIALIZER = 
        new WorkbenchRecipe.Serializer(CAMPFIRE_TYPE);

    public static final RecipeSerializer<WorkbenchRecipe> WORKBENCH_SERIALIZER =
        new WorkbenchRecipe.Serializer(WORKBENCH_TYPE);

    /**
     * Registers the mod's recipe types and their serializers into the game's built-in registries.
     *
     * Specifically registers the furnace (FURNACE_T1_TYPE), campfire (CAMPFIRE_TYPE),
     * and workbench (WORKBENCH_TYPE) recipe types with their corresponding serializers.
     */
    public static void initialize() {
        // Register the Furnace-flavored version
        register(FURNACE_T1_TYPE.toString(), FURNACE_T1_TYPE, FURNACE_SERIALIZER);
        
        // Register the Campfire-flavored version
        register(CAMPFIRE_TYPE.toString(), CAMPFIRE_TYPE, CAMPFIRE_SERIALIZER);

        register(WORKBENCH_TYPE.toString(), WORKBENCH_TYPE, WORKBENCH_SERIALIZER);
    }

    /**
     * Registers a recipe type and its serializer in the game's recipe registries under this mod's namespace.
     *
     * @param name       the registry path name to use (combined with the mod ID)
     * @param type       the recipe type to register
     * @param serializer the recipe serializer to register
     */
    private static void register(String name, RecipeType<?> type, RecipeSerializer<?> serializer) {
        Registry.register(BuiltInRegistries.RECIPE_TYPE, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name), type);
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name), serializer);
    }

    /**
     * Create a RecipeType identified by the given name.
     *
     * @param name the identifier to associate with the recipe type; returned by its {@code toString()}
     * @return a RecipeType whose {@code toString()} returns the provided name
     */
    private static <T extends Recipe<?>> RecipeType<T> createType(String name) {
        return new RecipeType<>() { @Override public String toString() { return name; } };
    }
}