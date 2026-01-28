package com.tcm.MineTale.registry;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.block.workbenches.menu.FurnaceWorkbenchMenu;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {

    public static final MenuType<FurnaceWorkbenchMenu> FURNACE_WORKBENCH_MENU = register(
        "furnace_workbench_menu", 
        FurnaceWorkbenchMenu::new
    );
    
    public static void initialize() {
        // Just used to trigger the static registration
        System.out.println("Registered Mod Menus for " + MineTale.MOD_ID);
    }

    private static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuType.MenuSupplier<T> factory) {
        Identifier id = Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name);
        return Registry.register(
            BuiltInRegistries.MENU, 
            id, 
            new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS)
        );
    }
}
