package com.tcm.MineTale.block.workbenches.entity;

import com.mojang.serialization.Codec;
import com.tcm.MineTale.block.workbenches.menu.BlacksmithsWorkbenchMenu;
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

public class BlacksmithsWorkbenchEntity extends AbstractWorkbenchEntity {
    protected final ContainerData data = new ContainerData() {
        /**
         * Provide the internal sync data value for the given index; this workbench does not expose any data.
         *
         * @param index data index to retrieve; ignored by this implementation
         * @return 0 for any index
         */
        @Override
        public int get(int index) {
            return switch (index) {
                default -> 0;
            };
        }

        /**
         * No-op setter: workbench data is server-controlled and must not be modified client-side.
         *
         * @param index the data index (ignored)
         * @param value the value to assign (ignored)
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
     * Initialises a BlacksmithsWorkbenchEntity at the given world position and block state.
     *
     * The created entity has its workbench tier set to 1 and is enabled to pull items from nearby inventories.
     *
     * @param blockPos   the world position of this block entity
     * @param blockState the block state for this block entity
     */
    public BlacksmithsWorkbenchEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.BLACKSMITHS_WORKBENCH_BE, blockPos, blockState);

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
     * Restore the workbench's persisted state from the provided input source.
     *
     * Reads and applies the following keys if present: "WorkbenchTier" into {@code tier},
     * "ScanRadius" into {@code scanRadius}, and "Inventory" into the internal inventory
     * (up to the inventory's capacity). Absent keys leave the corresponding fields at their defaults.
     *
     * @param valueInput source used to read persisted values
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
     * @param syncId the window id used to identify the menu instance
     * @param playerInventory the opening player's inventory
     * @param player the player who opened the menu
     * @return the BlacksmithsWorkbenchMenu bound to this workbench and its synced container data
     */
    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        // 1. Trigger the sync to the client before returning the menu
        if (player instanceof ServerPlayer serverPlayer) {
            this.syncNearbyToPlayer(serverPlayer);
        }

        // // 2. Return the menu as usual
        return new BlacksmithsWorkbenchMenu(syncId, playerInventory, this.data, this);
    }
    
    /**
     * Identifies the recipe type used to find and match recipes for this workbench.
     *
     * @return the RecipeType for workbench recipes
     */
    @Override
    public RecipeType<WorkbenchRecipe> getWorkbenchRecipeType() {
        return ModRecipes.BLACKSMITHS_TYPE;
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