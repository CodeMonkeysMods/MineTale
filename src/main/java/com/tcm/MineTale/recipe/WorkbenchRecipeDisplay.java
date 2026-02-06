package com.tcm.MineTale.recipe;

import java.util.List;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tcm.MineTale.registry.ModBlocks;
import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.registry.ModRecipes;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public record WorkbenchRecipeDisplay(
    List<SlotDisplay> ingredients, 
    SlotDisplay result, 
    SlotDisplay craftingStation,
    RecipeType<?> recipeType
) implements RecipeDisplay {

    public static final MapCodec<WorkbenchRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(WorkbenchRecipeDisplay::ingredients),
        SlotDisplay.CODEC.fieldOf("result").forGetter(WorkbenchRecipeDisplay::result),
        SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(WorkbenchRecipeDisplay::craftingStation),
        BuiltInRegistries.RECIPE_TYPE.byNameCodec().fieldOf("recipe_type").forGetter(WorkbenchRecipeDisplay::recipeType)
    ).apply(inst, WorkbenchRecipeDisplay::new));

    // Updated Helper Constructor
    public WorkbenchRecipeDisplay(WorkbenchRecipe recipe) {
        this(
            recipe.ingredients().stream().map(Ingredient::display).toList(),
            new SlotDisplay.ItemStackSlotDisplay(recipe.results().isEmpty() ? ItemStack.EMPTY : recipe.results().get(0)),
            // Dynamic icon: If it's a campfire recipe, show the campfire item in the book
            new SlotDisplay.ItemStackSlotDisplay(new ItemStack(
                recipe.getType() == ModRecipes.CAMPFIRE_TYPE ? ModBlocks.CAMPFIRE_WORKBENCH_BLOCK : ModBlocks.FURNACE_WORKBENCH_BLOCK_T1
            )),
            recipe.getType()
        );
    }

    @Override
    public List<SlotDisplay> ingredients() {
        return this.ingredients;
    }

    @Override
    public SlotDisplay result() {
        return this.result;
    }

    @Override
    public RecipeDisplay.Type<WorkbenchRecipeDisplay> type() {
        // We will register this next
        return ModRecipeDisplay.WORKBENCH_TYPE;
    }

    public RecipeType<?> getRecipeType() {
        return this.recipeType;
    }

    @Override
    public SlotDisplay craftingStation() {
        // Replace 'ModBlocks.YOUR_WORKBENCH' with your actual block item
        return new SlotDisplay.ItemStackSlotDisplay(new ItemStack(ModBlocks.FURNACE_WORKBENCH_BLOCK_T1));
    }
}