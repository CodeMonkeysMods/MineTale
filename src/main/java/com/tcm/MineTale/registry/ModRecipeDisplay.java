package com.tcm.MineTale.registry;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.recipe.WorkbenchRecipeDisplay;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
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
		// Explicitly define the constructor mapping to avoid the Function3 error
		(ingredients, result, craftingStation) -> new WorkbenchRecipeDisplay(ingredients, result, craftingStation)
	);

    public static final RecipeDisplay.Type<WorkbenchRecipeDisplay> WORKBENCH_TYPE = 
        new RecipeDisplay.Type<>(WorkbenchRecipeDisplay.CODEC, STREAM_CODEC);

    public static final RecipeBookCategory CAMPFIRE_SEARCH = new RecipeBookCategory();

    public static void initialize() {
        // Register the Display TYPE
        Registry.register(
            BuiltInRegistries.RECIPE_DISPLAY, 
            Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "workbench_recipe_display"), 
            WORKBENCH_TYPE
        );

        // Register the Category
        Registry.register(
            BuiltInRegistries.RECIPE_BOOK_CATEGORY, 
            Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "campfire_recipe_book_category"), 
            CAMPFIRE_SEARCH
        );
    }
}
