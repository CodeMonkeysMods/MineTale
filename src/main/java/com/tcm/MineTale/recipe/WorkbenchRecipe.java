package com.tcm.MineTale.recipe;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CookingBookCategory;
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
    CookingBookCategory category
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
        if (ingredients.isEmpty()) return false;

        // Use .getItem() to get the actual stack from the input
        ItemStack stackA = input.inputA();
        ItemStack stackB = input.inputB();

        // Check if slot 0 matches the first ingredient
        boolean slot0Matches = ingredients.get(0).test(stackA);

        if (ingredients.size() > 1) {
            // Recipe needs two items
            return slot0Matches && ingredients.get(1).test(stackB);
        } else {
            // Recipe only needs one item, so Slot B MUST be empty
            return slot0Matches && stackB.isEmpty();
        }
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
        // Using null as we are using a custom workbench
        return null;
    }

    /**
     * Provides recipe displays for the recipe book UI.
     *
     * @return an empty list, indicating no recipe displays are provided for the recipe book
     */
    @Override
    public List<RecipeDisplay> display() {
        // Used for the recipe book UI display
        return List.of();
    }

    // --- SERIALIZER ---

    public static class Serializer implements RecipeSerializer<WorkbenchRecipe> {
        private final RecipeType<WorkbenchRecipe> recipeType;
        private final MapCodec<WorkbenchRecipe> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, WorkbenchRecipe> streamCodec;

        /**
         * Creates a serializer for WorkbenchRecipe and initializes its data and binary codecs.
         *
         * The constructed codec validates that the recipe contains 1 or 2 ingredients, 1 to 4 result stacks,
         * and provides a default cookTime of 200 when absent. The stream codec mirrors the same fields
         * for binary (network/packet) serialization.
         *
         * @param recipeType the RecipeType associated with recipes produced/deserialized by this serializer
         */
        public Serializer(RecipeType<WorkbenchRecipe> recipeType) {
            this.recipeType = recipeType;

            this.codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.listOf()
                    .validate(list -> list.size() >= 1 && list.size() <= 2 
                        ? DataResult.success(list) 
                        : DataResult.error(() -> "Ingredients must be 1 or 2"))
                    .fieldOf("ingredients").forGetter(WorkbenchRecipe::ingredients),
                ItemStack.STRICT_CODEC.listOf()
                    .validate(list -> list.size() >= 1 && list.size() <= 4 
                        ? DataResult.success(list) 
                        : DataResult.error(() -> "Results must be between 1 and 4"))
                    .fieldOf("results").forGetter(WorkbenchRecipe::results),
                Codec.INT.optionalFieldOf("cookTime", 200).forGetter(WorkbenchRecipe::cookTime),
                CookingBookCategory.CODEC.optionalFieldOf("category", CookingBookCategory.MISC).forGetter(WorkbenchRecipe::category)
            ).apply(inst, (ing, res, time, cat) -> new WorkbenchRecipe(ing, res, time, this.recipeType, this, cat)));

            this.streamCodec = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), WorkbenchRecipe::ingredients,
                ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), WorkbenchRecipe::results,
                ByteBufCodecs.VAR_INT, WorkbenchRecipe::cookTime,
                CookingBookCategory.STREAM_CODEC, WorkbenchRecipe::category,
                (ing, res, time, cat) -> new WorkbenchRecipe(ing, res, time, this.recipeType, this, cat)
            );
        }

        /**
         * Provides the MapCodec used to serialize and deserialize WorkbenchRecipe instances.
         *
         * @return the MapCodec for encoding and decoding WorkbenchRecipe objects
         */
        @Override
        public MapCodec<WorkbenchRecipe> codec() {
            return codec;
        }

        /**
         * Provides the binary stream codec used to serialize and deserialize WorkbenchRecipe instances.
         *
         * @return the StreamCodec that encodes and decodes WorkbenchRecipe objects to and from a RegistryFriendlyByteBuf
         */
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, WorkbenchRecipe> streamCodec() {
            return streamCodec;
        }
    }
}