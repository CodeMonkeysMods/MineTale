package com.tcm.MineTale.block.workbenches.entity;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.tcm.MineTale.block.workbenches.menu.WorkbenchWorkbenchMenu;
import com.tcm.MineTale.recipe.WorkbenchRecipe;
import com.tcm.MineTale.registry.ModBlockEntities;
import com.tcm.MineTale.registry.ModRecipes;
import com.tcm.MineTale.util.Constants;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class WorkbenchWorkbenchEntity extends AbstractWorkbenchEntity {
    protected final ContainerData data = new ContainerData() {
        /**
         * Retrieves an internal data value by index for UI synchronization.
         *
         * @param index There is no cook time or anything for this block as it doesnt use it
         * @return the value associated with {@code index}, or 0 for any other index
         */
        @Override
        public int get(int index) {
            return switch (index) {
                default -> 0;
            };
        }

        /**
         * No-op for this workbench; data is server-driven and not set client-side.
         *
         * `@param` index the data index to set
         * `@param` value the value to assign (ignored)
         */
        @Override
        public void set(int index, int value) {
            // Not required on WorkbenchEntity
        }

        /**
         * The number of data values exposed by this ContainerData.
         *
         * @return the number of data entries (4)
         */
        @Override
        public int getCount() {
            return 4;
        }
    };

    /**
     * Creates a WorkbenchWorkbenchEntity for the specified world position and block state.
     *
     * Sets the entity's scanRadius to 0.0 and tier to 1.
     *
     * @param blockPos   the world position of this block entity
     * @param blockState the block state for this block entity
     */
    public WorkbenchWorkbenchEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.WORKBENCH_WORKBENCH_BE, blockPos, blockState);

        this.tier = 1;
        this.canPullFromNearby = true;
    }

    /**
     * Persist this workbench's state to the given ValueOutput.
     *
     * Stores "WorkbenchTier" (int), "ScanRadius" (double), and the full inventory under "Inventory"
     * using type-safe Codecs.
     *
     * @param valueOutput the writer used to serialize this entity's fields
     */
    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        // store() uses Codecs for type safety
        valueOutput.store("WorkbenchTier", Codec.INT, this.tier);
        valueOutput.store("ScanRadius", Codec.DOUBLE, this.scanRadius);

        // Convert the SimpleContainer to a List of ItemStacks for the Codec
        // Or use the built-in NBT helper if your framework supports it
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            stacks.add(inventory.getItem(i));
        }

        // CHANGE: Use OPTIONAL_CODEC instead of CODEC
        valueOutput.store("Inventory", ItemStack.OPTIONAL_CODEC.listOf(), stacks);
    }

    /**
     * Restores workbench-specific state from persistent storage, applying defaults when keys are absent.
     *
     * Delegates to the superclass load logic, then:
     * - reads "WorkbenchTier" (int) into {@code tier}, defaulting to {@code 1} if missing;
     * - reads "ScanRadius" (double) into {@code scanRadius}, defaulting to {@code 0.0} if missing;
     * - reads "Inventory" as a list of {@code ItemStack} and populates the internal inventory up to its capacity.
     */
    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        // read() returns an Optional
        this.tier = valueInput.read("WorkbenchTier", Codec.INT).orElse(1);
        this.scanRadius = valueInput.read("ScanRadius", Codec.DOUBLE).orElse(0.0);

        // Read the inventory list back
        valueInput.read("Inventory", ItemStack.OPTIONAL_CODEC.listOf()).ifPresent(stacks -> {
            for (int i = 0; i < stacks.size() && i < inventory.getContainerSize(); i++) {
                inventory.setItem(i, stacks.get(i));
            }
        });
    }

    /**
     * Creates the server-side container menu for this workbench's UI.
     *
     * @param syncId the window id used to synchronize the menu with the client
     * @param playerInventory the opening player's inventory
     * @param player the player who opened the menu
     * @return a WorkbenchWorkbenchMenu bound to this workbench's inventory and synced data
     */
    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        // 1. Trigger the sync to the client before returning the menu
        if (player instanceof ServerPlayer serverPlayer) {
            this.syncNearbyToPlayer(serverPlayer);
        }

        // // 2. Return the menu as usual
        return new WorkbenchWorkbenchMenu(syncId, playerInventory, this.data, this);
    }
    
    /**
     * Identifies the recipe type used to find and match recipes for this workbench.
     *
     * @return the RecipeType for workbench recipes
     */
    @Override
    public RecipeType<WorkbenchRecipe> getWorkbenchRecipeType() {
        return ModRecipes.WORKBENCH_TYPE;
    }

    /**
     * Determines whether the workbench currently has fuel available.
     *
     * Checks that the entity is in a loaded level and that the configured fuel slot contains an item.
     *
     * @return `true` if the entity is in a loaded level and the fuel slot contains an item, `false` otherwise.
     */
    @Override
    protected boolean hasFuel() {
        if (this.level == null) return false;
        
        // Check if block is lit
        // BlockState state = this.level.getBlockState(this.worldPosition);
        // boolean isLit = state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT);
        
        boolean hasFuelItem = !this.getItem(Constants.FUEL_SLOT).isEmpty();
        
        return hasFuelItem;
    }
}