package com.tcm.MineTale.block.workbenches.menu;

import org.jspecify.annotations.Nullable;

import com.tcm.MineTale.block.workbenches.entity.AbstractWorkbenchEntity;
import com.tcm.MineTale.recipe.WorkbenchRecipeInput;
import com.tcm.MineTale.registry.ModMenuTypes;
import com.tcm.MineTale.registry.ModRecipes;
import com.tcm.MineTale.util.Constants;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;

public class CampfireWorkbenchMenu extends AbstractWorkbenchContainerMenu {
    private static final int containerDataSize = 4;

    @Nullable
    private final AbstractWorkbenchEntity blockEntity;
    
    /**
     * Creates a CampfireWorkbenchMenu initialised with the default internal inventory and data containers.
     *
     * The menu uses a 7-slot internal container and a data container sized by {@code containerDataSize}.
     *
     * @param syncId          the synchronisation id for this menu
     * @param playerInventory the player's inventory interacting with this menu
     */
    public CampfireWorkbenchMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(7), new SimpleContainerData(containerDataSize), null);
    }

    /**
     * Creates a CampfireWorkbenchMenu bound to the supplied player inventory, container and container data.
     *
     * @param syncId the synchronisation id used for client–server container syncing
     * @param playerInventory the player's inventory
     * @param container the backing container that provides the workbench slots
     * @param data the container data used to synchronise numeric state
     * @param blockEntity the associated workbench block entity, or {@code null} if the menu is not bound to a block
     */
    public CampfireWorkbenchMenu(int syncId, Inventory playerInventory, Container container, ContainerData data, @Nullable AbstractWorkbenchEntity blockEntity) {
        super(ModMenuTypes.CAMPFIRE_WORKBENCH_MENU, syncId, container, data, containerDataSize, playerInventory, Constants.INPUT_START + 1, 6, ModRecipes.CAMPFIRE_TYPE);
        this.blockEntity = blockEntity;
    }

    @Override
    public @Nullable AbstractWorkbenchEntity getBlockEntity() {
        return this.blockEntity;
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {
        // This is vital for the recipe book to "see" what is currently in your furnace.
        // It allows the book to calculate if you have enough items to craft more.
        if (this.container instanceof StackedContentsCompatible compatible) {
            compatible.fillStackedContents(stackedItemContents);
        }
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }
    
    @Override
    public WorkbenchRecipeInput createRecipeInput() {
        // We grab the items currently sitting in the container at indices 0 and 1
        // These correspond to the "Left" and "Right" input slots added in your constructor
        return new WorkbenchRecipeInput(
            this.container.getItem(Constants.INPUT_START), 
            this.container.getItem(this.inputEnd)
        );
    }
}