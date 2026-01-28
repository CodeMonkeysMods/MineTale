package com.tcm.MineTale;

import com.tcm.MineTale.block.workbenches.screen.FurnaceWorkbenchScreen;
import com.tcm.MineTale.registry.ModMenuTypes;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class MineTaleClient implements ClientModInitializer {
	/**
	 * Registers the screen factory for the furnace workbench menu so the client can create FurnaceWorkbenchScreen instances for that menu type.
	 */
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModMenuTypes.FURNACE_WORKBENCH_MENU, FurnaceWorkbenchScreen::new);
	}
}