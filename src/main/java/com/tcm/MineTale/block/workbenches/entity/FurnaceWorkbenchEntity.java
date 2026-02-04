package com.tcm.MineTale.block.workbenches.entity;

import org.jspecify.annotations.Nullable;

import com.tcm.MineTale.block.workbenches.menu.FurnaceWorkbenchMenu;
import com.tcm.MineTale.registry.ModTiers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;

public class FurnaceWorkbenchEntity extends AbstractFurnaceWorkbenchEntity {
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
                case 0 -> getFuelTime();
                case 1 -> 100; // Fuel total
                case 2 -> getCookTime();
                case 3 -> getCookTimeTotal();
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
                case 0 -> setFuelTime(value);
                case 2 -> setCookTime(value);
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
     * Creates a new FurnaceWorkbenchEntity with dynamic tier data.
     *
     * @param tier  the record containing speed, radius, and other stats
     * @param pos   the block position of the entity
     * @param state the block state at that position
     */
    public FurnaceWorkbenchEntity(ModTiers.FurnaceTier tier, BlockPos pos, BlockState state) {
        // Dynamically fetch the BlockEntityType from your registry map using the tier key
        super(ModTiers.TIER_MAP.get(tier), pos, state);
        this.tier = tier.id();
        
        // You can now set scanRadius dynamically from the record 
        // or keep it a default value.
        this.scanRadius = tier.scanRadius(); 

        this.setCookTimeTotal(tier.cookTime());
    }

    /**
         * Create a container menu that allows a player to interact with this furnace workbench.
         *
         * @param syncId          window id used to synchronize the menu between client and server
         * @param playerInventory the player's inventory view passed into the menu
         * @param player          the player opening the menu
         * @return                the workbench's {@link AbstractContainerMenu}, or {@code null} if a menu cannot be created
         */
    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new FurnaceWorkbenchMenu(syncId, playerInventory, this.inventory, this.data);
    }
}