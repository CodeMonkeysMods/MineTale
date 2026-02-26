package com.tcm.MineTale;

import com.tcm.MineTale.block.workbenches.screen.FurnaceWorkbenchScreen;
import com.tcm.MineTale.block.workbenches.screen.WorkbenchWorkbenchScreen;
import com.tcm.MineTale.network.ClientboundNearbyInventorySyncPacket;

import java.util.List;

import com.tcm.MineTale.block.workbenches.menu.AbstractWorkbenchContainerMenu;
import com.tcm.MineTale.block.workbenches.screen.ArmorersWorkbenchScreen;
import com.tcm.MineTale.block.workbenches.screen.BuildersWorkbenchScreen;
import com.tcm.MineTale.block.workbenches.screen.CampfireWorkbenchScreen;
import com.tcm.MineTale.block.workbenches.screen.FarmersWorkbenchScreen;
import com.tcm.MineTale.registry.ModBlocks;
import com.tcm.MineTale.registry.ModMenuTypes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.item.ItemStack;

public class MineTaleClient implements ClientModInitializer {
	/**
	 * Initialises client-side handlers for the MineTale mod.
	 *
	 * Registers screen factories for custom workbench menu types, configures render
	 * layers for furnace workbench blocks, and registers a global network receiver
	 * that applies nearby inventory items to an open workbench menu.
	 *
	 * The network receiver schedules work on the client thread and retries application
	 * for up to 10 client ticks if the expected workbench menu is not yet open; if
	 * synchronization still fails it logs a failure message.
	 */
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModMenuTypes.FURNACE_WORKBENCH_MENU, FurnaceWorkbenchScreen::new);
		MenuScreens.register(ModMenuTypes.CAMPFIRE_WORKBENCH_MENU, CampfireWorkbenchScreen::new);
		MenuScreens.register(ModMenuTypes.WORKBENCH_WORKBENCH_MENU, WorkbenchWorkbenchScreen::new);
		MenuScreens.register(ModMenuTypes.ARMORERS_WORKBENCH_MENU, ArmorersWorkbenchScreen::new);
		MenuScreens.register(ModMenuTypes.FARMERS_WORKBENCH_MENU, FarmersWorkbenchScreen::new);
		MenuScreens.register(ModMenuTypes.BUILDERS_WORKBENCH_MENU, BuildersWorkbenchScreen::new);

		BlockRenderLayerMap.putBlock(ModBlocks.FURNACE_WORKBENCH_BLOCK_T1, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(ModBlocks.FURNACE_WORKBENCH_BLOCK_T2, ChunkSectionLayer.CUTOUT);

		ClientPlayNetworking.registerGlobalReceiver(ClientboundNearbyInventorySyncPacket.TYPE, (payload, context) -> {
			List<ItemStack> items = payload.items();
			
			// We create a task that can re-run itself if the menu isn't ready yet
			context.client().execute(new Runnable() {
				int retries = 0;

				@Override
				public void run() {
					if (context.client().player != null && context.client().player.containerMenu instanceof AbstractWorkbenchContainerMenu menu) {
						applyItemsToMenu(menu, items, context.client().screen);
					} else if (retries < 10) { // Try for up to 10 frames (~0.5 seconds)
						retries++;
						// Re-submit to the next tick
						context.client().execute(this);
					} else {
						System.out.println("CLIENT: Failed to sync nearby items after 10 retries.");
					}
				}
			});
		});
	}

	// Helper method to keep things clean
	private static void applyItemsToMenu(AbstractWorkbenchContainerMenu menu, List<ItemStack> items, Screen screen) {
		System.out.println("CLIENT: Successfully applied " + items.size() + " stacks to the Workbench Menu.");
		menu.setNetworkedNearbyItems(items);
		if (screen instanceof RecipeUpdateListener listener) {
			listener.recipesUpdated();
		}
	}
}