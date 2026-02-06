package com.tcm.MineTale.registry;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.recipe.WorkbenchRecipeDisplay;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public class ModRecipeDisplay {
    public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, WorkbenchRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
		SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), WorkbenchRecipeDisplay::ingredients,
		SlotDisplay.STREAM_CODEC, WorkbenchRecipeDisplay::result,
		SlotDisplay.STREAM_CODEC, WorkbenchRecipeDisplay::craftingStation,
        ByteBufCodecs.registry(Registries.RECIPE_TYPE), WorkbenchRecipeDisplay::recipeType,
		// Explicitly define the constructor mapping to avoid the Function3 error
		(ingredients, result, craftingStation, recipeType) -> 
            new WorkbenchRecipeDisplay(ingredients, result, craftingStation, recipeType)
	);

    public static final RecipeDisplay.Type<WorkbenchRecipeDisplay> WORKBENCH_TYPE = 
        new RecipeDisplay.Type<>(WorkbenchRecipeDisplay.CODEC, STREAM_CODEC);

    public static final RecipeBookCategory CAMPFIRE_SEARCH = registerCategory("campfire_recipe_book_category");

    public static final RecipeBookCategory WORKBENCH_SEARCH = registerCategory("workbench_recipe_book_category");

    public static final RecipeBookCategory FURNACE_T1_SEARCH = registerCategory("furnace_t1_recipe_book_category");

    /**
     * Registers the workbench recipe display type into the built-in recipe display registry.
     *
     * The registration uses this mod's ID combined with the path "workbench_recipe_display" as the identifier.
     */
    public static void initialize() {
        // Register the Display TYPE
        Registry.register(
            BuiltInRegistries.RECIPE_DISPLAY, 
            Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "workbench_recipe_display"), 
            WORKBENCH_TYPE
        );
    }

    private static RecipeBookCategory registerCategory(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name);
        RecipeBookCategory category = new RecipeBookCategory();
        
        // Register it in the game's internal registry
        return Registry.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY, id, category);
    }
}