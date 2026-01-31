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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;

// public record WorkbenchRecipe(
//     List<Ingredient> ingredients, 
//     List<ItemStack> results, 
//     int cookTime,
//     RecipeType<WorkbenchRecipe> recipeType,
//     RecipeSerializer<WorkbenchRecipe> recipeSerializer // Added property
// ) implements Recipe<RecipeInput> {
//     public static final int DEFAULT_COOK_TIME = 200;
//     public static final int MAX_INPUTS = 2;
//     public static final int MAX_OUTPUTS = 4;

//     public WorkbenchRecipe(List<Ingredient> ingredients, List<ItemStack> results, int cookTime, RecipeType<WorkbenchRecipe> recipeType, RecipeSerializer<WorkbenchRecipe> recipeSerializer) {
//         this.ingredients = ingredients;
//         this.results = results;
//         this.cookTime = cookTime;
//         this.recipeType = recipeType;
//         this.recipeSerializer = recipeSerializer;
//     }

//     public NonNullList<Ingredient> getIngredients() {
//         NonNullList<Ingredient> list = NonNullList.create();
//         list.addAll(this.ingredients);
//         return list;
//     }

//     @Override
//     public boolean matches(RecipeInput input, Level level) {
//         if (level.isClientSide()) return false;
//         if (ingredients.isEmpty()) return false;
        
//         // Match slot 0
//         boolean slot0Matches = ingredients.get(0).test(input.getItem(0));
        
//         // Match slot 1 if the recipe has a second ingredient
//         if (ingredients.size() > 1) {
//             return slot0Matches && ingredients.get(1).test(input.getItem(1));
//         }
        
//         // If recipe only has 1 ingredient, slot 1 must be empty
//         return slot0Matches && input.getItem(1).isEmpty();
//     }

//     @Override
//     public ItemStack assemble(RecipeInput input, HolderLookup.Provider provider) {
//         return results.isEmpty() ? ItemStack.EMPTY : results.get(0).copy();
//     }

//     @Override
//     public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
//         return this.recipeSerializer;
//     }

//     @Override
//     public RecipeType<? extends Recipe<RecipeInput>> getType() {
//         return this.recipeType;
//     }

//     @Override
//     public PlacementInfo placementInfo() {
//         // This tells the recipe book how to place ingredients in your block's slots
//         return PlacementInfo.create(this.ingredients);
//     }

//     @Override
//     public RecipeBookCategory recipeBookCategory() {
//         // Return null if you aren't using the vanilla recipe book categories
//         return null; 
//     }

//     @Override
//     public List<RecipeDisplay> display() {
//         return List.of(); 
//     }

//     // --- Serializer Class ---
//     public static class Serializer implements RecipeSerializer<WorkbenchRecipe> {
//         private final RecipeType<WorkbenchRecipe> recipeType;
//         private final MapCodec<WorkbenchRecipe> codec;
//         private final StreamCodec<RegistryFriendlyByteBuf, WorkbenchRecipe> streamCodec;

//         public Serializer(RecipeType<WorkbenchRecipe> recipeType) {
//             this.recipeType = recipeType;
            
//             // Note: We pass 'this' as the serializer into the constructor
//             this.codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
//                 Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(WorkbenchRecipe::ingredients),
//                 ItemStack.STRICT_CODEC.listOf().fieldOf("results").forGetter(WorkbenchRecipe::results),
//                 Codec.INT.optionalFieldOf("cookTime", 200).forGetter(WorkbenchRecipe::cookTime)
//             ).apply(inst, (ing, res, time) -> new WorkbenchRecipe(ing, res, time, this.recipeType, this)));

//             this.streamCodec = StreamCodec.composite(
//                 Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), WorkbenchRecipe::ingredients,
//                 ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), WorkbenchRecipe::results,
//                 ByteBufCodecs.VAR_INT, WorkbenchRecipe::cookTime,
//                 (ing, res, time) -> new WorkbenchRecipe(ing, res, time, this.recipeType, this)
//             );
//         }

//         @Override public MapCodec<WorkbenchRecipe> codec() { return codec; }
//         @Override public StreamCodec<RegistryFriendlyByteBuf, WorkbenchRecipe> streamCodec() { return streamCodec; }
//     }
// }

public record WorkbenchRecipe(
    List<Ingredient> ingredients, 
    List<ItemStack> results, 
    int cookTime,
    RecipeType<WorkbenchRecipe> recipeType,
    RecipeSerializer<WorkbenchRecipe> recipeSerializer
) implements Recipe<WorkbenchRecipeInput> {

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

    @Override
    public ItemStack assemble(WorkbenchRecipeInput input, HolderLookup.Provider provider) {
        // Return a copy of the first result for vanilla compatibility
        return results.isEmpty() ? ItemStack.EMPTY : results.get(0).copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<WorkbenchRecipeInput>> getSerializer() {
        return this.recipeSerializer;
    }

    @Override
    public RecipeType<? extends Recipe<WorkbenchRecipeInput>> getType() {
        return this.recipeType;
    }

    @Override
    public PlacementInfo placementInfo() {
        // Tells the recipe book how to place these items
        return PlacementInfo.create(this.ingredients);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        // Using null as we are using a custom workbench
        return null;
    }

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
                Codec.INT.optionalFieldOf("cookTime", 200).forGetter(WorkbenchRecipe::cookTime)
            ).apply(inst, (ing, res, time) -> new WorkbenchRecipe(ing, res, time, this.recipeType, this)));

            this.streamCodec = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), WorkbenchRecipe::ingredients,
                ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), WorkbenchRecipe::results,
                ByteBufCodecs.VAR_INT, WorkbenchRecipe::cookTime,
                (ing, res, time) -> new WorkbenchRecipe(ing, res, time, this.recipeType, this)
            );
        }

        @Override
        public MapCodec<WorkbenchRecipe> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, WorkbenchRecipe> streamCodec() {
            return streamCodec;
        }
    }
}