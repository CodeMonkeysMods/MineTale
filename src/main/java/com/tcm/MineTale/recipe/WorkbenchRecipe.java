package com.tcm.MineTale.recipe;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tcm.MineTale.registry.ModRecipeDisplay;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;

public record WorkbenchRecipe(
    List<Ingredient> ingredients, 
    List<ItemStack> results, 
    int cookTime,
    RecipeType<WorkbenchRecipe> recipeType,
    RecipeSerializer<WorkbenchRecipe> recipeSerializer,
    CraftingBookCategory category,
    Identifier bookCategory
) implements Recipe<WorkbenchRecipeInput> {

    /**
     * Determines whether the given crafting input matches this recipe's ingredient requirements.
     *
     * @param input the two-slot workbench input to test
     * @param level the world context (unused for matching but provided by the recipe API)
     * @return `true` if the input satisfies this recipe's ingredients; `false` otherwise.
     *         For recipes with one ingredient the second slot must be empty; for recipes with two
     *         ingredients both slots must match their respective ingredients. An empty ingredient
     *         list always fails to match.
     */
    @Override
    public boolean matches(WorkbenchRecipeInput input, Level level) {
        if (input == null || ingredients.isEmpty()) return false;

        ItemStack slotA = input.inputA();
        ItemStack slotB = input.inputB();
        Ingredient recipeIngredient = ingredients.get(0);

        // If the recipe only has 1 ingredient (like your pork recipe)
        if (ingredients.size() == 1) {
            // Check if the ingredient matches either slot AND the other slot is empty
            boolean matchesA = recipeIngredient.test(slotA) && slotB.isEmpty();
            boolean matchesB = recipeIngredient.test(slotB) && slotA.isEmpty();
            
            return matchesA || matchesB;
        } 
        
        if (ingredients.size() == 2) {
            Ingredient secondIngredient = ingredients.get(1);
            return (recipeIngredient.test(slotA) && secondIngredient.test(slotB)) ||
                (recipeIngredient.test(slotB) && secondIngredient.test(slotA));
        }

        return false;
    }

    /**
     * Produce the recipe's resulting ItemStack for the given input.
     *
     * @param input the recipe input containing the crafting slot ItemStacks
     * @param provider a registry/lookup provider (passed through by caller; not used)
     * @return `ItemStack.EMPTY` if no results are defined, otherwise a defensive copy of the first result
     */
    @Override
    public ItemStack assemble(WorkbenchRecipeInput input, HolderLookup.Provider provider) {
        // Return a copy of the first result for vanilla compatibility
        return results.isEmpty() ? ItemStack.EMPTY : results.get(0).copy();
    }

    /**
     * Retrieves the serializer associated with this WorkbenchRecipe.
     *
     * @return the RecipeSerializer used to serialize and deserialize this recipe
     */
    @Override
    public RecipeSerializer<? extends Recipe<WorkbenchRecipeInput>> getSerializer() {
        return this.recipeSerializer;
    }

    /**
     * Retrieves the recipe type associated with this workbench recipe.
     *
     * @return the RecipeType instance representing this recipe's type
     */
    @Override
    public RecipeType<? extends Recipe<WorkbenchRecipeInput>> getType() {
        return this.recipeType;
    }

    /**
     * Provide placement guidance for the recipe book based on this recipe's ingredients.
     *
     * @return a PlacementInfo describing how the ingredients should be arranged in the recipe UI
     */
    @Override
    public PlacementInfo placementInfo() {
        // Tells the recipe book how to place these items
        return PlacementInfo.create(this.ingredients);
    }

    /**
     * Indicates the recipe is not associated with any vanilla recipe book category.
     *
     * @return `null` to indicate the recipe should not appear in the vanilla recipe book
     */
    @Override
    public RecipeBookCategory recipeBookCategory() {
        return BuiltInRegistries.RECIPE_BOOK_CATEGORY
        .get(this.bookCategory) // Returns Optional<Holder.Reference<RecipeBookCategory>>
        .map(Holder::value)     // Extracts the RecipeBookCategory from the Holder
        .orElse(ModRecipeDisplay.CAMPFIRE_SEARCH); // Fallback if ID is missing or invalid
    }

    @Override
    public CraftingBookCategory category() {
        return this.category; 
    }

    /**
     * Provides a display representation for the recipe book UI.
     *
     * @return a list containing a WorkbenchRecipeDisplay instance representing this recipe
     */
    @Override
    public List<RecipeDisplay> display() {
        // Every time the client asks for this recipe's "looks", 
        // we provide our custom display.
        return List.of(new WorkbenchRecipeDisplay(this));
    }

    // --- SERIALIZER ---
    public record SizedIngredient(Ingredient ingredient, int count) {
        public static final Codec<SizedIngredient> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(SizedIngredient::ingredient),
            Codec.INT.optionalFieldOf("count", 1).forGetter(SizedIngredient::count)
        ).apply(inst, SizedIngredient::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SizedIngredient> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, SizedIngredient::ingredient,
            ByteBufCodecs.VAR_INT, SizedIngredient::count,
            SizedIngredient::new
        );
    }

    public static class Serializer implements RecipeSerializer<WorkbenchRecipe> {
        private final RecipeType<WorkbenchRecipe> recipeType;
        private final MapCodec<WorkbenchRecipe> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, WorkbenchRecipe> streamCodec;

        /**
         * Creates a serializer for WorkbenchRecipe and constructs its JSON and network codecs.
         *
         * Initializes the serializer with the given recipe type and builds the MapCodec used for JSON
         * serialization (assigned to `codec`) and the StreamCodec used for network serialization
         * (assigned to `streamCodec`), covering the recipe's ingredients, results, cook time,
         * crafting book category, and book category identifier.
         *
         * @param recipeType the RecipeType instance that identifies the kind of WorkbenchRecipe this serializer handles
         */
        public Serializer(RecipeType<WorkbenchRecipe> recipeType) {
            this.recipeType = recipeType;

            // MAP CODEC (For JSON)
            this.codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(WorkbenchRecipe::ingredients),
                ItemStack.STRICT_CODEC.listOf().fieldOf("results").forGetter(WorkbenchRecipe::results),
                Codec.INT.optionalFieldOf("cookTime", 200).forGetter(WorkbenchRecipe::cookTime),
                CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC).forGetter(WorkbenchRecipe::category),
                Identifier.CODEC.fieldOf("book_category").forGetter(WorkbenchRecipe::bookCategory)
            ).apply(inst, (ing, res, time, cat, book) -> 
                new WorkbenchRecipe(ing, res, time, this.recipeType, this, cat, book)));

            // STREAM CODEC (For Network)
            // We use ByteBufCodecs.registry to force everything into RegistryFriendlyByteBuf
            this.streamCodec = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), WorkbenchRecipe::ingredients,
                ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), WorkbenchRecipe::results,
                ByteBufCodecs.VAR_INT.cast(), WorkbenchRecipe::cookTime,
                CraftingBookCategory.STREAM_CODEC.cast(), WorkbenchRecipe::category,
                Identifier.STREAM_CODEC.cast(), WorkbenchRecipe::bookCategory,
                (ing, res, time, cat, book) -> 
                    new WorkbenchRecipe(ing, res, time, this.recipeType, this, cat, book)
            );
        }

        /**
         * Provides the JSON map codec for serializing and deserializing this recipe.
         *
         * @return the MapCodec used for JSON (map) serialization of WorkbenchRecipe instances
         */
        @Override
        public MapCodec<WorkbenchRecipe> codec() { return codec; }

        /**
         * Provides the network stream codec used for serializing and deserializing WorkbenchRecipe instances.
         *
         * @return the StreamCodec that encodes and decodes WorkbenchRecipe objects to and from a RegistryFriendlyByteBuf
         */
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, WorkbenchRecipe> streamCodec() { return streamCodec; }
    }
}