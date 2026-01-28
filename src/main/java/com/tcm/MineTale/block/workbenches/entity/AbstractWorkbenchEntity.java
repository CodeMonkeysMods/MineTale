package com.tcm.MineTale.block.workbenches.entity;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractWorkbenchEntity extends BlockEntity implements MenuProvider {
    protected int tier = 1;
    protected double scanRadius = 5.0;

    /**
     * Creates a new workbench block entity instance.
     *
     * @param type  the BlockEntityType for this entity
     * @param pos   the world position of the block entity
     * @param state the block state at the position
     */
    public AbstractWorkbenchEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
 * Gets the workstation's current tier.
 *
 * @return the current tier value
 */
    public int getTier() { return tier; }
    /**
 * Sets the workstation's tier and marks the block entity as changed.
 *
 * @param tier the new tier value for this workstation
 */
public void setTier(int tier) { this.tier = tier; setChanged(); }

    /**
     * Collects nearby inventory-containing block entities within the configured scan radius and vertical range.
     *
     * Scans a square area centered on this entity from -scanRadius..+scanRadius on X/Z and -2..+2 on Y, gathers any
     * block entities implementing `Container`, and returns them sorted by increasing distance to this entity.
     *
     * @return a list of nearby `Container` instances sorted by proximity; an empty list if none are found or if the world (`level`) is null
     */
    public List<Container> getNearbyInventories() {
        List<Container> inventories = new ArrayList<>();
        if (level == null) {
            return inventories;
        } 
        BlockPos.betweenClosed(
            worldPosition.offset((int)-scanRadius, -2, (int)-scanRadius),
            worldPosition.offset((int)scanRadius, 2, (int)scanRadius)
        ).forEach(pos -> {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof Container container) {
                inventories.add(container);
            }
        });
        
        // Prioritization: Sort by proximity to prevent "chest prioritization" issues
        inventories.sort((a, b) -> {
            double distA = ((BlockEntity)a).getBlockPos().distSqr(this.worldPosition);
            double distB = ((BlockEntity)b).getBlockPos().distSqr(this.worldPosition);
            return Double.compare(distA, distB);
        });
        
        return inventories;
    }

    /**
     * Attempt to recycle an ItemStack into component items and distribute those components to nearby inventories or drop them at the workbench location.
     *
     * <p>The base implementation is a no-op; subclasses should override to perform actual recycling. Implementations are expected to insert resulting items into nearby Container block entities when possible and otherwise spawn the items at this entity's world position.</p>
     *
     * @param stack the ItemStack to recycle
     */
    public void attemptRecycle(ItemStack stack) {
        // Logic to break down stack.getItem() and return components 
        // to nearby chests or drop them at worldPosition.
    }

    /**
     * Create the container menu presented to the player when they open this workbench.
     *
     * @param syncId         the window synchronization id provided by the client
     * @param playerInventory the player's inventory
     * @param player         the player opening the menu
     * @return               the created AbstractContainerMenu, or `null` if no menu should be opened
     */
    @Nullable
    @Override
    public abstract AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player);

    /**
     * Provides the display name for this workbench block.
     *
     * @return a translatable Component created from the block's description ID
     */
    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    
}