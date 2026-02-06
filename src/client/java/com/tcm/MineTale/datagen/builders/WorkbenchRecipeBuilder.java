package com.tcm.MineTale.datagen.builders;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.Nullable;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.recipe.WorkbenchRecipe;
import com.tcm.MineTale.registry.ModRecipeDisplay;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

public class WorkbenchRecipeBuilder implements RecipeBuilder {
    private final RecipeType<WorkbenchRecipe> type;
    private final RecipeSerializer<WorkbenchRecipe> serializer; // Add this
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final List<ItemStack> results = new ArrayList<>();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private CraftingBookCategory category = CraftingBookCategory.MISC;
    private Identifier bookCategory = BuiltInRegistries.RECIPE_BOOK_CATEGORY
        .getKey(ModRecipeDisplay.CAMPFIRE_SEARCH);
    private int cookTime = 200;
    @Nullable private String group;

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
     * Adds an input ingredient multiple times to represent a required count.
     *
     * @param ingredient the ingredient to add
     * @param count      how many of this ingredient are required
     * @return           this builder instance
     */
    public WorkbenchRecipeBuilder input(Ingredient ingredient, int count) {
        for (int i = 0; i < count; i++) {
            this.ingredients.add(ingredient);
        }
        return this;
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

    /**
     * Adds an item input with a specific count.
     */
    public WorkbenchRecipeBuilder input(ItemLike item, int count) {
        Ingredient ingredient = Ingredient.of(item);
        for (int i = 0; i < count; i++) {
            this.ingredients.add(ingredient);
        }
        return this;
    }

    /**
     * Adds a tag input with a specific count.
     * Requires a RegistryLookup (usually available in your RecipeProvider).
     */
    public WorkbenchRecipeBuilder input(TagKey<Item> tag, HolderLookup.Provider registries, int count) {
        // 1. Get the lookup for the Item registry from the provider
        var itemLookup = registries.lookupOrThrow(Registries.ITEM);
        
        // 2. Now you can use getOrThrow with the TagKey
        Ingredient ingredient = Ingredient.of(itemLookup.getOrThrow(tag));
        
        for (int i = 0; i < count; i++) {
            this.ingredients.add(ingredient);
        }
        return this;
    }



    public WorkbenchRecipeBuilder category(CraftingBookCategory category) {
        this.category = category;
        return this;
    }

    public WorkbenchRecipeBuilder bookCategory(RecipeBookCategory category) {
        Identifier id = BuiltInRegistries.RECIPE_BOOK_CATEGORY.getKey(category);
        if (id != null) {
            this.bookCategory = id;
        }
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
    public void save(RecipeOutput exporter, String name) {
        this.save(exporter, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name)));
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
        for (Map.Entry<String, Criterion<?>> entry : this.criteria.entrySet()) {
            advancement.addCriterion(entry.getKey(), entry.getValue());
        }

        // 3. Create the Recipe Instance
        WorkbenchRecipe recipe = new WorkbenchRecipe(
                List.copyOf(ingredients),
                List.copyOf(results),
                cookTime,
                this.type,
                this.serializer,
                this.category,
                this.bookCategory
        );

        // 4. Accept the recipe into the generator
        recipeOutput.accept(resourceKey, recipe, advancement.build(location.withPrefix("recipes/")));
    }
}