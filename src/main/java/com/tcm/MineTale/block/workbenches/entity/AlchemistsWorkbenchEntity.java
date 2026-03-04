package com.tcm.MineTale.block.workbenches.entity;

import com.mojang.serialization.Codec;
import com.tcm.MineTale.block.workbenches.menu.AlchemistsWorkbenchMenu;
import com.tcm.MineTale.recipe.WorkbenchRecipe;
import com.tcm.MineTale.registry.ModBlockEntities;
import com.tcm.MineTale.registry.ModRecipes;
import com.tcm.MineTale.util.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AlchemistsWorkbenchEntity extends AbstractWorkbenchEntity {
    protected final ContainerData data = new ContainerData() {
        /**
         * Provide a data value for container UI synchronization by index.
         *
         * @param index the data index to query
         * @return `0` for all indices
         */
        @Override
        public int get(int index) {
            return switch (index) {
                default -> 0;
            };
        }

        /**
         * No-op setter; client-side attempts to set workbench data are ignored because state is server-driven.
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
     * Creates a new AlchemistsWorkbenchEntity at the given world position with the specified block state.
     *
     * Initialises the workbench's tier to 1 and enables pulling from nearby inventories.
     *
     * @param blockPos   the world position of this block entity
     * @param blockState the block state for this block entity
     */
    public AlchemistsWorkbenchEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.ALCHEMISTS_WORKBENCH_BE, blockPos, blockState);

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
     * Restore this workbench's persisted state, applying defaults for missing values.
     *
     * Reads and restores three keys from the provided input:
     * - "WorkbenchTier": stored as an int into {@code tier}, defaults to {@code 1} if absent;
     * - "ScanRadius": stored as a double into {@code scanRadius}, defaults to {@code 0.0} if absent;
     * - "Inventory": read as a list of {@code ItemStack} and copies items into the internal inventory up to its capacity.
     *
     * @param valueInput source of persisted values
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
     * Create the server-side container menu for this workbench and synchronise nearby state to the opening player.
     *
     * @param syncId the window id for client–server menu synchronization
     * @param playerInventory the opening player's inventory
     * @param player the player who opened the menu; if a ServerPlayer, nearby state will be synced to them before the menu is returned
     * @return the container menu bound to this workbench's inventory and synced data
     */
    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        // 1. Trigger the sync to the client before returning the menu
        if (player instanceof ServerPlayer serverPlayer) {
            this.syncNearbyToPlayer(serverPlayer);
        }

        // // 2. Return the menu as usual
        return new AlchemistsWorkbenchMenu(syncId, playerInventory, this.data, this);
    }
    
    /**
     * Identifies the recipe type used to find and match recipes for this workbench.
     *
     * @return the RecipeType for workbench recipes
     */
    @Override
    public RecipeType<WorkbenchRecipe> getWorkbenchRecipeType() {
        return ModRecipes.ALCHEMISTS_TYPE;
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