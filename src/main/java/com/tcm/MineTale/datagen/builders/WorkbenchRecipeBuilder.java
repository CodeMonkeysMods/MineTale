package com.tcm.MineTale.datagen.builders;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tcm.MineTale.recipe.WorkbenchRecipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class WorkbenchRecipeBuilder implements RecipeBuilder {
    private final RecipeType<WorkbenchRecipe> type;
    private final RecipeSerializer<WorkbenchRecipe> serializer; // Add this
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final List<ItemStack> results = new ArrayList<>();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private int cookTime = 200;
    @Nullable private String group;

   public static final MapCodec<WorkbenchRecipe> CODEC(RecipeType<WorkbenchRecipe> type, RecipeSerializer<WorkbenchRecipe> serializer) {
    return RecordCodecBuilder.mapCodec(inst -> inst.group(
        Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(WorkbenchRecipe::ingredients),
        ItemStack.STRICT_CODEC.listOf().fieldOf("results").forGetter(WorkbenchRecipe::results),
        Codec.INT.optionalFieldOf("cookTime", 200).forGetter(WorkbenchRecipe::cookTime)
    ).apply(inst, (ingredients, results, cookTime) -> 
        new WorkbenchRecipe(ingredients, results, cookTime, type, serializer)
    ));
}

    // Use a constructor that defines the target block type (Furnace vs Campfire)
    public WorkbenchRecipeBuilder(RecipeType<WorkbenchRecipe> type, RecipeSerializer<WorkbenchRecipe> serializer) {
        this.type = type;
        this.serializer = serializer;
    }

    public static WorkbenchRecipeBuilder create(RecipeType<WorkbenchRecipe> type, RecipeSerializer<WorkbenchRecipe> serializer) {
        return new WorkbenchRecipeBuilder(type, serializer);
    }
    
    // Logic for multiple inputs
    public WorkbenchRecipeBuilder input(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }

    // Logic for multiple outputs
    public WorkbenchRecipeBuilder output(ItemStack stack) {
        this.results.add(stack);
        return this;
    }

    public WorkbenchRecipeBuilder time(int ticks) {
        this.cookTime = ticks;
        return this;
    }

    @Override
    public WorkbenchRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public WorkbenchRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return results.isEmpty() ? Items.AIR : results.get(0).getItem();
    }

    public void save(RecipeOutput exporter, Identifier id) {
        this.save(exporter, ResourceKey.create(Registries.RECIPE, id));
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> resourceKey) {
        // 1. Extract the Location from the Key for advancement naming
        Identifier location = resourceKey.identifier();

        // 2. Build Advancements (Required for Recipe Book)
        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        
        // Add your criteria here (omitted for brevity)

        // 3. Create the Recipe Instance
        WorkbenchRecipe recipe = new WorkbenchRecipe(
                List.copyOf(ingredients),
                List.copyOf(results),
                cookTime,
                this.type,
                this.serializer
        );

        // 4. Accept the recipe into the generator
        recipeOutput.accept(resourceKey, recipe, advancement.build(location.withPrefix("recipes/")));
    }
}
