package com.tcm.MineTale.block.workbenches.entity;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.tcm.MineTale.block.workbenches.menu.CampfireWorkbenchMenu;
import com.tcm.MineTale.recipe.WorkbenchRecipe;
import com.tcm.MineTale.registry.ModBlockEntities;
import com.tcm.MineTale.registry.ModRecipes;
import com.tcm.MineTale.util.Constants;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class CampfireWorkbenchEntity extends AbstractWorkbenchEntity {
    private int cookTime;
    private int cookTimeTotal = 200; 
    private int fuelTime;

    protected final ContainerData data = new ContainerData() {
        /**
         * Retrieves an internal data value by index for UI synchronization.
         *
         * @param index the data index: 0 = remaining fuel time, 1 = fuel total (constant 100),
         *              2 = current cook progress, 3 = total cook time
         * @return the value associated with {@code index}, or 0 for any other index
         */
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> fuelTime;
                case 1 -> 100; // Fuel total
                case 2 -> cookTime;
                case 3 -> cookTimeTotal;
                default -> 0;
            };
        }

        /**
         * Sets an internal workbench data field identified by index.
         *
         * Supported indices:
         * <ul>
         *   <li>0 — sets {@code fuelTime}</li>
         *   <li>2 — sets {@code cookTime}</li>
         * </ul>
         * Other indices are ignored.
         *
         * @param index the data index to set
         * @param value the value to assign to the indexed field
         */
        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> fuelTime = value;
                case 2 -> cookTime = value;
            }
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
     * Creates a CampfireWorkbenchEntity for the specified world position and block state.
     *
     * Sets the entity's scanRadius to 0.0 and tier to 1.
     *
     * @param blockPos   the world position of this block entity
     * @param blockState the block state for this block entity
     */
    public CampfireWorkbenchEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CAMPFIRE_WORKBENCH_BE, blockPos, blockState);

        this.scanRadius = 0.0; 
        this.tier = 1;
    }

    /**
     * Performs server-side per-tick updates for this workbench.
     *
     * <p>This method is a no-op on the client and exits immediately; server-side update logic
     * should be implemented here.
     */
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;
    }

    /**
     * Determines whether the provided item stack is a supported food.
     *
     * @param stack the item stack to test
     */
    // private boolean isCookable(List<ItemStack> stacks) { return stack.is(ItemTags.MEAT); }

    /**
     * Determines whether the given item stack represents a fuel item.
     *
     * @param stack the item stack to inspect
     */
    // private boolean isFuel(ItemStack stack) { return stack.is(ItemTags.LOGS_THAT_BURN) || stack.is(Items.STICK); }

    /**
     * Persist entity-specific state into the provided ValueOutput.
     *
     * Stores the workbench's tier as "WorkbenchTier" and its scan radius as "ScanRadius"
     * using type-safe Codecs.
     *
     * @param valueOutput the output writer used to serialize this entity's fields
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
     * Create the server-side container menu for the campfire workbench UI.
     *
     * @param syncId         window id used to synchronize the menu with the client
     * @param playerInventory the opening player's inventory
     * @param player         the player who opened the menu
     * @return               a CampfireWorkbenchMenu tied to this workbench's inventory and sync data
     */
    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new CampfireWorkbenchMenu(syncId, playerInventory, this.inventory, this.data, this);
    }

    /**
     * Specifies which recipe type this workbench uses.
     *
     * @return the campfire workbench recipe type used to look up and match recipes
     */
    @Override
    public RecipeType<WorkbenchRecipe> getWorkbenchRecipeType() {
        return ModRecipes.CAMPFIRE_TYPE;
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