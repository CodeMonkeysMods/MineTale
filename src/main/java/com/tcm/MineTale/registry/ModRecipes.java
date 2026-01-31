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
    public static final RecipeType<WorkbenchRecipe> FURNACE_TYPE = createType("furnace_alloying");
    public static final RecipeType<WorkbenchRecipe> CAMPFIRE_TYPE = createType("campfire_alloying");
    
    // 2. Define the Serializers (The "How")
    // We pass the specific Type into the Serializer's constructor
    public static final RecipeSerializer<WorkbenchRecipe> FURNACE_SERIALIZER = 
        new WorkbenchRecipe.Serializer(FURNACE_TYPE);
        
    public static final RecipeSerializer<WorkbenchRecipe> CAMPFIRE_SERIALIZER = 
        new WorkbenchRecipe.Serializer(CAMPFIRE_TYPE);

    public static void initialize() {
        // Register the Furnace-flavored version
        register("furnace_alloying", FURNACE_TYPE, FURNACE_SERIALIZER);
        
        // Register the Campfire-flavored version
        register("campfire_alloying", CAMPFIRE_TYPE, CAMPFIRE_SERIALIZER);
    }

    private static void register(String name, RecipeType<?> type, RecipeSerializer<?> serializer) {
        Registry.register(BuiltInRegistries.RECIPE_TYPE, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name), type);
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name), serializer);
    }

    private static <T extends Recipe<?>> RecipeType<T> createType(String name) {
        return new RecipeType<>() { @Override public String toString() { return name; } };
    }
}
