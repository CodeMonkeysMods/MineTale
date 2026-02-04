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
import net.minecraft.world.item.crafting.CookingBookCategory;
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
    private CookingBookCategory category = CookingBookCategory.MISC;
    private int cookTime = 200;
    @Nullable private String group;

   /**
 * Creates a MapCodec that serializes and deserializes WorkbenchRecipe instances bound to the given recipe type and serializer.
 *
 * The codec encodes the recipe's ingredients, results, and cookTime (default 200) and constructs a WorkbenchRecipe using the provided type and serializer.
 *
 * @param type the RecipeType associated with the encoded WorkbenchRecipe
 * @param serializer the RecipeSerializer used to (de)serialize the WorkbenchRecipe
 * @return a MapCodec for WorkbenchRecipe that reads/writes ingredients, results, and cookTime and produces WorkbenchRecipe instances tied to the given type and serializer
 */
public static final MapCodec<WorkbenchRecipe> CODEC(RecipeType<WorkbenchRecipe> type, RecipeSerializer<WorkbenchRecipe> serializer) {
    return RecordCodecBuilder.mapCodec(inst -> inst.group(
        Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(WorkbenchRecipe::ingredients),
        ItemStack.STRICT_CODEC.listOf().fieldOf("results").forGetter(WorkbenchRecipe::results),
        Codec.INT.optionalFieldOf("cookTime", 200).forGetter(WorkbenchRecipe::cookTime),
        CookingBookCategory.CODEC.optionalFieldOf("category", CookingBookCategory.MISC).forGetter(WorkbenchRecipe::category)
    ).apply(inst, (ingredients, results, cookTime, category) -> 
        new WorkbenchRecipe(ingredients, results, cookTime, type, serializer, category)
    ));
}

    /**
     * Create a new WorkbenchRecipeBuilder configured for a specific recipe type and its serializer.
     *
     * @param type       the target RecipeType for the recipes produced by this builder (e.g., furnace, campfire)
     * @param serializer the RecipeSerializer used to serialize the produced WorkbenchRecipe instances
     */
    public WorkbenchRecipeBuilder(RecipeType<WorkbenchRecipe> type, RecipeSerializer<WorkbenchRecipe> serializer) {
        this.type = type;
        this.serializer = serializer;
    }

    /**
     * Create a new WorkbenchRecipeBuilder configured with the given recipe type and serializer.
     *
     * @param type the target RecipeType for the WorkbenchRecipe
     * @param serializer the RecipeSerializer used to serialize the WorkbenchRecipe
     * @return a WorkbenchRecipeBuilder configured with the provided type and serializer
     */
    public static WorkbenchRecipeBuilder create(RecipeType<WorkbenchRecipe> type, RecipeSerializer<WorkbenchRecipe> serializer) {
        return new WorkbenchRecipeBuilder(type, serializer);
    }
    
    /**
     * Adds an input ingredient to the recipe being built.
     *
     * @param ingredient the ingredient to add as an input
     * @return           this builder instance for method chaining
     */
    public WorkbenchRecipeBuilder input(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }

    public WorkbenchRecipeBuilder category(CookingBookCategory category) {
        this.category = category;
        return this;
    }

    /**
     * Adds an output item stack to the recipe.
     *
     * @param stack the output ItemStack to add
     * @return this builder instance for chaining
     */
    public WorkbenchRecipeBuilder output(ItemStack stack) {
        this.results.add(stack);
        return this;
    }

    /**
     * Set the recipe cook time in ticks.
     *
     * @param ticks the cook time in game ticks
     * @return the builder instance
     */
    public WorkbenchRecipeBuilder time(int ticks) {
        this.cookTime = ticks;
        return this;
    }

    /**
     * Adds an advancement criterion to be required for unlocking the built recipe.
     *
     * @param name      the unique key for the criterion in the recipe's advancement
     * @param criterion the criterion to add
     * @return          this builder instance for method chaining
     */
    @Override
    public WorkbenchRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    /**
     * Sets the recipe's group identifier.
     *
     * @param group an optional group name to categorize the recipe, or {@code null} to unset grouping
     * @return the same builder instance
     */
    @Override
    public WorkbenchRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    /**
     * Retrieves the recipe's first output item.
     *
     * @return the Item of the first result, or {@link Items#AIR} if no results are present.
     */
    @Override
    public Item getResult() {
        return results.isEmpty() ? Items.AIR : results.get(0).getItem();
    }

    /**
     * Saves this builder's recipe to the given exporter under the specified identifier.
     *
     * @param exporter the RecipeOutput that will receive the recipe
     * @param id the identifier to use for the saved recipe
     */
    public void save(RecipeOutput exporter, Identifier id) {
        this.save(exporter, ResourceKey.create(Registries.RECIPE, id));
    }

    /**
     * Registers the built WorkbenchRecipe with the provided RecipeOutput and generates its advancement.
     *
     * Builds an advancement that awards the recipe when unlocked, constructs a WorkbenchRecipe using the
     * builder's collected ingredients, results, cookTime, type, and serializer, and submits both the
     * recipe and its advancement to the RecipeOutput using the given resource key. The advancement
     * identifier is created by prefixing the recipe location with "recipes/".
     *
     * @param recipeOutput the exporter that will receive the recipe and its advancement
     * @param resourceKey the registry key identifying the recipe (used as the recipe id and for advancement rewards)
     */
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
                this.serializer,
                this.category
        );

        // 4. Accept the recipe into the generator
        recipeOutput.accept(resourceKey, recipe, advancement.build(location.withPrefix("recipes/")));
    }
}