package com.tcm.MineTale.recipe;

import java.util.List;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tcm.MineTale.registry.ModBlocks;
import com.tcm.MineTale.registry.ModRecipeDisplay;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public record WorkbenchRecipeDisplay(List<SlotDisplay> ingredients, 
    SlotDisplay result, 
    SlotDisplay craftingStation) implements RecipeDisplay {

    public static final MapCodec<WorkbenchRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(WorkbenchRecipeDisplay::ingredients),
        SlotDisplay.CODEC.fieldOf("result").forGetter(WorkbenchRecipeDisplay::result),
        SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(WorkbenchRecipeDisplay::craftingStation) // Just one fieldOf
    ).apply(inst, WorkbenchRecipeDisplay::new));

    public WorkbenchRecipeDisplay(WorkbenchRecipe recipe) {
        this(
            recipe.ingredients().stream().map(Ingredient::display).toList(),
            new SlotDisplay.ItemStackSlotDisplay(recipe.results().isEmpty() ? ItemStack.EMPTY : recipe.results().get(0)),
            new SlotDisplay.ItemStackSlotDisplay(new ItemStack(ModBlocks.FURNACE_WORKBENCH_BLOCK_T1))
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

    @Override
    public SlotDisplay craftingStation() {
        // Replace 'ModBlocks.YOUR_WORKBENCH' with your actual block item
        return new SlotDisplay.ItemStackSlotDisplay(new ItemStack(ModBlocks.FURNACE_WORKBENCH_BLOCK_T1));
    }
}