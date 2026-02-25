package com.tcm.MineTale.datagen.recipes;

import com.tcm.MineTale.datagen.builders.WorkbenchRecipeBuilder;
import com.tcm.MineTale.registry.ModItems;
import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.registry.ModRecipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;

public class ArmorRecipes {
    public static void buildRecipes(RecipeProvider provider, RecipeOutput exporter, HolderLookup.Provider lookup) {
        // Copper Armor
        new WorkbenchRecipeBuilder(ModRecipes.ARMORERS_TYPE, ModRecipes.ARMORERS_SERIALIZER)
            .input(Items.COPPER_INGOT, 11)
            .input(ModItems.PLANT_FIBER, 4)
            .output(Items.COPPER_CHESTPLATE)
            .time(3)
            .unlockedBy("has_copper_ingot", provider.has(Items.COPPER_INGOT))
            .bookCategory(ModRecipeDisplay.ARMORERS_SEARCH)
            .save(exporter, "copper_cuirass");
        new WorkbenchRecipeBuilder(ModRecipes.ARMORERS_TYPE, ModRecipes.ARMORERS_SERIALIZER)
            .input(Items.COPPER_INGOT, 6)
            .input(ModItems.PLANT_FIBER, 2)
            .output(Items.COPPER_HELMET)
            .time(3)
            .unlockedBy("has_copper_ingot", provider.has(Items.COPPER_INGOT))
            .bookCategory(ModRecipeDisplay.ARMORERS_SEARCH)
            .save(exporter, "copper_helmet");
        new WorkbenchRecipeBuilder(ModRecipes.ARMORERS_TYPE, ModRecipes.ARMORERS_SERIALIZER)
            .input(Items.COPPER_INGOT, 9)
            .input(ModItems.PLANT_FIBER, 3)
            .output(Items.COPPER_LEGGINGS)
            .time(3)
            .unlockedBy("has_copper_ingot", provider.has(Items.COPPER_INGOT))
            .bookCategory(ModRecipeDisplay.ARMORERS_SEARCH)
            .save(exporter, "copper_greaves");
        // new WorkbenchRecipeBuilder(ModRecipes.ARMORERS_TYPE, ModRecipes.ARMORERS_SERIALIZER)
        //     .input(Items.COPPER_INGOT, 5)
        //     .input(ModItems.PLANT_FIBER, 1)
        //     .output(Items.COPPER_LEGGINGS)
        //     .time(3)
        //     .unlockedBy("has_copper_ingot", provider.has(Items.COPPER_INGOT))
        //     .save(exporter, "copper_gauntlets");
        // new WorkbenchRecipeBuilder(ModRecipes.ARMORERS_TYPE, ModRecipes.ARMORERS_SERIALIZER)
        //     .input(Items.COPPER_INGOT, 2)
        //     .input(ModItems.PLANT_FIBER, 3)
        //     .input(ItemTags.LOGS, lookup)
        //     .output(ModItems.COPPER_SHIELD)
        //     .time(3)
        //     .unlockedBy("has_copper_ingot", provider.has(Items.COPPER_INGOT))
        //     .save(exporter, "COPPER_SHIELD");

        // Wood Armor
        // TODO: WoodenArmor Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.ARMORERS_TYPE, ModRecipes.ARMORERS_SERIALIZER)
        //     .input(ItemTags.LOGS, lookup, 15)
        //     .input(ModItems.PLANT_FIBER, 3)
        //     .output(Items.COPPER_CHESTPLATE)
        //     .time(3)
        //     .unlockedBy("has_copper_ingot", provider.has(Items.COPPER_INGOT))
        //     .save(exporter, "copper_cuirass");
        // new WorkbenchRecipeBuilder(ModRecipes.ARMORERS_TYPE, ModRecipes.ARMORERS_SERIALIZER)
        //     .input(Items.COPPER_INGOT, 6)
        //     .input(ModItems.PLANT_FIBER, 2)
        //     .output(Items.COPPER_HELMET)
        //     .time(3)
        //     .unlockedBy("has_copper_ingot", provider.has(Items.COPPER_INGOT))
        //     .save(exporter, "copper_helmet");
        // new WorkbenchRecipeBuilder(ModRecipes.ARMORERS_TYPE, ModRecipes.ARMORERS_SERIALIZER)
        //     .input(Items.COPPER_INGOT, 9)
        //     .input(ModItems.PLANT_FIBER, 3)
        //     .output(Items.COPPER_LEGGINGS)
        //     .time(3)
        //     .unlockedBy("has_copper_ingot", provider.has(Items.COPPER_INGOT))
        //     .save(exporter, "copper_greaves");
        // new WorkbenchRecipeBuilder(ModRecipes.ARMORERS_TYPE, ModRecipes.ARMORERS_SERIALIZER)
        //     .input(Items.COPPER_INGOT, 5)
        //     .input(ModItems.PLANT_FIBER, 1)
        //     .output(Items.COPPER_LEGGINGS)
        //     .time(3)
        //     .unlockedBy("has_copper_ingot", provider.has(Items.COPPER_INGOT))
        //     .save(exporter, "copper_gauntlets");


    }
}
