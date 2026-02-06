package com.tcm.MineTale;

import com.tcm.MineTale.block.workbenches.screen.FurnaceWorkbenchScreen;
import com.tcm.MineTale.block.workbenches.screen.WorkbenchWorkbenchScreen;
import com.tcm.MineTale.block.workbenches.screen.CampfireWorkbenchScreen;
import com.tcm.MineTale.registry.ModMenuTypes;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class MineTaleClient implements ClientModInitializer {
	/**
	 * Register client-side screen factories for custom workbench menu types.
	 *
	 * Binds ModMenuTypes.FURNACE_WORKBENCH_MENU to FurnaceWorkbenchScreen,
	 * ModMenuTypes.CAMPFIRE_WORKBENCH_MENU to CampfireWorkbenchScreen, and
	 * ModMenuTypes.WORKBENCH_WORKBENCH_MENU to WorkbenchWorkbenchScreen so the client
	 * can create the appropriate GUI when those menus open.
	 */
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModMenuTypes.FURNACE_WORKBENCH_MENU, FurnaceWorkbenchScreen::new);
		MenuScreens.register(ModMenuTypes.CAMPFIRE_WORKBENCH_MENU, CampfireWorkbenchScreen::new);
		MenuScreens.register(ModMenuTypes.WORKBENCH_WORKBENCH_MENU, WorkbenchWorkbenchScreen::new);
	}
}