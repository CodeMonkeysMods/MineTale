package com.tcm.MineTale.block.workbenches.entity;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.tcm.MineTale.block.workbenches.menu.CampfireWorkbenchMenu;
import com.tcm.MineTale.registry.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class CampfireWorkbenchEntity extends AbstractWorkbenchEntity {
    private final SimpleContainer inventory = new SimpleContainer(7);

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
     * Creates a CampfireWorkbenchEntity at the given position with the provided block state.
     *
     * Initializes the entity's scan radius to 6.0 and tier to 1.
     *
     * @param blockPos   the world position of this block entity
     * @param blockState the block state for this block entity
     */
    public CampfireWorkbenchEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CAMPFIRE_WORKBENCH_BE, blockPos, blockState);

        this.scanRadius = 0.0; 
        this.tier = 1;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        // boolean changed = false;
        // ItemStack fuel = inventory.getItem(FUEL_SLOT);
        // List<ItemStack> inputs = List.of(inventory.getItem(INPUT_1), inventory.getItem(INPUT_2));
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
    }

    /**
     * Restores workbench-specific state from persistent storage and applies defaults when keys are absent.
     *
     * Delegates to the superclass load logic, then reads:
     * - "WorkbenchTier" (int) into {@code tier}, defaulting to {@code 1} if missing.
     * - "ScanRadius" (double) into {@code scanRadius}, defaulting to {@code 5.0} if missing.
     */
    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        // read() returns an Optional
        this.tier = valueInput.read("WorkbenchTier", Codec.INT).orElse(1);
        this.scanRadius = valueInput.read("ScanRadius", Codec.DOUBLE).orElse(0.0);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new CampfireWorkbenchMenu(syncId, playerInventory, this.inventory, this.data);
    }
}