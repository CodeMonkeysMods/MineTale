package com.tcm.MineTale.block.workbenches.entity;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.tcm.MineTale.block.workbenches.menu.FarmersWorkbenchMenu;
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

public class FarmersWorkbenchEntity extends AbstractWorkbenchEntity {
    protected final ContainerData data = new ContainerData() {
        /**
         * Provide the container's UI-synchronized integer value for the specified data index.
         *
         * This workbench exposes four data slots (indices 0–3); all slots are currently unused and always return 0.
         *
         * @param index the data index to read (expected range: 0–3)
         * @return 0 for the requested index
         */
        @Override
        public int get(int index) {
            return switch (index) {
                default -> 0;
            };
        }

        /**
         * Ignore attempts to modify the workbench's container data from the client; changes are a no-op.
         *
         * @param index the data slot index (ignored)
         * @param value the value provided (ignored)
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
     * Create a FarmersWorkbenchEntity at the given world position and block state.
     *
     * Initializes the workbench tier to 1 and enables pulling from nearby inventories.
     *
     * @param blockPos   the world position of this block entity
     * @param blockState the block state for this block entity
     */
    public FarmersWorkbenchEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.FARMERS_WORKBENCH_BE, blockPos, blockState);

        this.tier = 1;
        this.canPullFromNearby = true;
    }

    /**
     * Writes this workbench's persistent state to the provided output, including the workbench tier,
     * scan radius, and full inventory (stored under the keys "WorkbenchTier", "ScanRadius", and "Inventory").
     *
     * @param valueOutput the output used to serialize this entity's fields
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
     * Create the server-side menu for a player to interact with this workbench and synchronize nearby data to that player.
     *
     * @param syncId the window id used to synchronize the menu with the client
     * @param playerInventory the opening player's inventory
     * @param player the player who opened the menu
     * @return the FarmersWorkbenchMenu bound to this workbench's inventory and synchronization data
     */
    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        // 1. Trigger the sync to the client before returning the menu
        if (player instanceof ServerPlayer serverPlayer) {
            this.syncNearbyToPlayer(serverPlayer);
        }

        // // 2. Return the menu as usual
        return new FarmersWorkbenchMenu(syncId, playerInventory, this.data, this);
    }
    
    /**
     * Identifies the recipe type used to find and match recipes for this workbench.
     *
     * @return the RecipeType for workbench recipes
     */
    @Override
    public RecipeType<WorkbenchRecipe> getWorkbenchRecipeType() {
        return ModRecipes.FARMERS_TYPE;
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