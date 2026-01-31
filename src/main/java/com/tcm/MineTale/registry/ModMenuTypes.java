package com.tcm.MineTale.registry;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.block.workbenches.menu.FurnaceWorkbenchMenu;
import com.tcm.MineTale.block.workbenches.menu.CampfireWorkbenchMenu;

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
    
    public static final MenuType<CampfireWorkbenchMenu> CAMPFIRE_WORKBENCH_MENU = register(
        "campfire_workbench_menu", 
        CampfireWorkbenchMenu::new
    );
    
    /**
     * Triggers static registration of the mod's menu types.
     *
     * <p>Forces the class's static initializers to run and prints a registration confirmation
     * message to standard output.
     */
    public static void initialize() {
        // Just used to trigger the static registration
        System.out.println("Registered Mod Menus for " + MineTale.MOD_ID);
    }

    /**
     * Register a MenuType under this mod's namespace and return the registered type.
     *
     * @param name    the path component used to build the registry Identifier (namespace is MineTale.MOD_ID)
     * @param factory a supplier that constructs instances of the menu type
     * @param <T>     the concrete AbstractContainerMenu subtype
     * @return        the MenuType instance registered in BuiltInRegistries.MENU
     */
    private static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuType.MenuSupplier<T> factory) {
        Identifier id = Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name);
        return Registry.register(
            BuiltInRegistries.MENU, 
            id, 
            new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS)
        );
    }
}