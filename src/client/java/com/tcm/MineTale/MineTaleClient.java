package com.tcm.MineTale;

import com.tcm.MineTale.block.workbenches.screen.FurnaceWorkbenchScreen;

import java.util.List;

import com.tcm.MineTale.block.workbenches.screen.CampfireWorkbenchScreen;
import com.tcm.MineTale.registry.ModMenuTypes;
import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.registry.ModRecipes;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.crafting.RecipeBookCategories;

public class MineTaleClient implements ClientModInitializer {

	

	/**
	 * Registers client-side screen factories for custom workbench menu types.
	 *
	 * Binds the furnace and campfire workbench menu types to their corresponding screen constructors
	 * so the client can create the appropriate GUI when those menus are opened.
	 */
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModMenuTypes.FURNACE_WORKBENCH_MENU, FurnaceWorkbenchScreen::new);
		MenuScreens.register(ModMenuTypes.CAMPFIRE_WORKBENCH_MENU, CampfireWorkbenchScreen::new);
	}
}