package com.tcm.MineTale.block.workbenches.menu;

import org.jspecify.annotations.Nullable;

import com.tcm.MineTale.block.workbenches.entity.AbstractFurnaceWorkbenchEntity;
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

public class FurnaceWorkbenchMenu extends AbstractWorkbenchContainerMenu {
    private static final int containerDataSize = 4;

    @Nullable
    private final AbstractFurnaceWorkbenchEntity blockEntity;

    /**
     * Constructs a client-side FurnaceWorkbenchMenu backed by a new empty internal container and default container data.
     *
     * @param syncId          the window synchronization id assigned by the client
     * @param playerInventory the player's inventory to attach to this menu
     */
    public FurnaceWorkbenchMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(7), new SimpleContainerData(containerDataSize), null);
    }

    /**
     * Creates a FurnaceWorkbenchMenu bound to the provided player inventory, container, and optional block entity.
     *
     * @param syncId        window sync id used by the client-server menu sync
     * @param playerInventory the player's inventory
     * @param container     the backing container holding slot items for this menu
     * @param data          container data used for syncing menu state (e.g., progress fields)
     * @param blockEntity   the associated AbstractFurnaceWorkbenchEntity instance, or {@code null} for client-side construction
     */
    public FurnaceWorkbenchMenu(int syncId, Inventory playerInventory, Container container, ContainerData data, @Nullable AbstractFurnaceWorkbenchEntity blockEntity) {
        super(ModMenuTypes.FURNACE_WORKBENCH_MENU, syncId, container, data, containerDataSize, playerInventory, Constants.INPUT_START + 1, 6, ModRecipes.FURNACE_T1_TYPE);
        this.blockEntity = blockEntity;
    }

    @Override
    public @Nullable AbstractFurnaceWorkbenchEntity getBlockEntity() {
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