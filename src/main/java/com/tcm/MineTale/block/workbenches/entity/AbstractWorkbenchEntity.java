package com.tcm.MineTale.block.workbenches.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.tcm.MineTale.recipe.WorkbenchRecipe;
import com.tcm.MineTale.recipe.WorkbenchRecipeInput;
import com.tcm.MineTale.util.Constants;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractWorkbenchEntity extends BlockEntity implements MenuProvider {
    protected int tier = 1;
    protected double scanRadius = 5.0;

    // Slot Mapping: 0-1 Inputs, 2 Fuel, 3-6 Outputs
    protected final SimpleContainer inventory = new SimpleContainer(7);
    protected int progress = 0;
    protected int maxProgress = 200;

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
     * Subclasses (Campfire/Furnace) must define which recipe type they look for.
     */
    public abstract RecipeType<WorkbenchRecipe> getWorkbenchRecipeType();

    public static void tick(Level level, BlockPos pos, BlockState state, AbstractWorkbenchEntity entity) {
    // 1. Create the input wrapper using the internal SimpleContainer
    // Slot 1 = Input A, Slot 2 = Input B
    WorkbenchRecipeInput input = new WorkbenchRecipeInput(
        entity.inventory.getItem(Constants.INPUT_1), 
        entity.inventory.getItem(Constants.INPUT_2)
    );

    // DEBUG 1: Is the machine even seeing the pork?
    if (!entity.inventory.getItem(Constants.INPUT_1).isEmpty()) {
        System.out.println("Slot 1 (Input) contains: " + entity.inventory.getItem(Constants.INPUT_1).getItem().toString());
    }

    if (!entity.inventory.getItem(Constants.FUEL_SLOT).isEmpty()) {
        System.out.println("Slot 0 (Fuel) contains: " + entity.inventory.getItem(Constants.FUEL_SLOT).getItem().toString());
    }

    // 2. Fetch the RecipeManager from the server
    if (level.getServer() == null) return;
    var recipeManager = level.getServer().getRecipeManager();

    // 3. Lookup the recipe using our explicit generic types
    Optional<RecipeHolder<WorkbenchRecipe>> recipeHolder = recipeManager
        .getRecipeFor(entity.getWorkbenchRecipeType(), input, level);

    if (recipeHolder.isPresent()) {
        WorkbenchRecipe recipe = recipeHolder.get().value();
        entity.maxProgress = recipe.cookTime();

        if (!entity.hasFuel()) {
            System.out.println("DEBUG: Failed because hasFuel() is false.");
        }
        if (!entity.canFitOutputs(recipeHolder.get().value().results())) {
            System.out.println("DEBUG: Failed because outputs are full.");
        }

        boolean hasFuel = entity.hasFuel();
        boolean canFit = entity.canFitOutputs(recipe.results());

        if (hasFuel && canFit) {
            entity.progress++;
            // Only print every 20 ticks (1 second) to avoid console spam
            if (entity.progress % 20 == 0) {
                System.out.println("DEBUG: Cooking... Progress is now " + entity.progress);
            }
            
            setChanged(level, pos, state);

            if (entity.progress >= entity.maxProgress) {
                System.out.println("DEBUG: Progress complete! Triggering craft().");
                entity.craft(recipe);
                entity.progress = 0;
            }
        } else {
            // This tells us exactly WHY it stopped
            if (!hasFuel) {
                System.out.println("DEBUG: Cooking stalled - NO FUEL (Check LIT state or Fuel Slot)");
            }
            if (!canFit) {
                System.out.println("DEBUG: Cooking stalled - NO SPACE in output slots (3-6)");
            }
        }
    } else {
        if (!input.getItem(0).isEmpty()) {
            System.out.println("DEBUG: No recipe found for this input type: " + entity.getWorkbenchRecipeType().toString());
        }
        // Reset progress if ingredients are removed
        if (entity.progress > 0) {
            entity.progress = 0;
            setChanged(level, pos, state);
        }
    }
}

    public ItemStack getItem(int slot) { return this.inventory.getItem(slot); }

    public boolean canFitOutputs(List<ItemStack> results) {
        for (ItemStack result : results) {
            // If we can't find a home for even one of the results, return false
            if (findOutputSlot(result, Constants.OUTPUT_START, Constants.OUTPUT_END) == -1) {
                return false;
            }
        }
        return true;
    }

    public int findOutputSlot(ItemStack result, int start, int end) {
        for (int i = start; i <= end; i++) {
            ItemStack stack = getItem(i);
            if (stack.isEmpty()) return i;
            
            // 1.21.1 Check: Same item + same components + space for more
            if (ItemStack.isSameItemSameComponents(stack, result) && 
                stack.getCount() + result.getCount() <= stack.getMaxStackSize()) {
                return i;
            }
        }
        return -1;
    }

    public int getContainerSize() {
        return this.inventory.getContainerSize();
    }

    public boolean isEmpty() {
        return this.inventory.isEmpty();
    }

    protected void craft(WorkbenchRecipe recipe) {
        // 1. Consume 1 from each ingredient slot (Slots 1 and 2)
        this.removeItem(Constants.INPUT_1, 1);
        this.removeItem(Constants.INPUT_2, 1);

        // 2. Distribute results from the recipe
        for (ItemStack result : recipe.results()) {
            if (result.isEmpty()) continue;
            
            // Use the constants for the output range (3 to 6)
            int slot = findOutputSlot(result, Constants.OUTPUT_START, Constants.OUTPUT_END);
            
            if (slot != -1) {
                ItemStack existing = getItem(slot);
                if (existing.isEmpty()) {
                    setItem(slot, result.copy());
                } else {
                    existing.grow(result.getCount());
                    // Crucial: SimpleContainer needs to know the stack changed
                    this.setChanged();
                }
            }
        }
    }

    public ItemStack removeItem(int slot, int amount) {
        // SimpleContainer has its own removeItem logic built-in
        ItemStack result = this.inventory.removeItem(slot, amount);
        if (!result.isEmpty()) {
            this.setChanged();
        }
        return result;
    }

    public void setItem(int slot, ItemStack stack) {
        // Use setItem(), not set()
        this.inventory.setItem(slot, stack);
        
        // Check max stack size
        if (!stack.isEmpty() && stack.getCount() > stack.getMaxStackSize()) {
            stack.setCount(stack.getMaxStackSize());
        }
        this.setChanged();
    }

    // Subclasses handle fuel logic (Campfires might return true always, Furnaces check slot 2)
    protected abstract boolean hasFuel();

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
     * Finds the first output slot that can accept the given result.
     *
     * @param result the item stack to place into an output slot
     * @return the index of the first suitable output slot between OUTPUT_START and OUTPUT_END, or -1 if none is available
     */
    public int findOutputSlot(ItemStack result, SimpleContainer inventory, int start, int end) {
        for (int i = start; i <= end; i++) {
            ItemStack out = inventory.getItem(i);
            if (out.isEmpty() || (ItemStack.isSameItem(out, result)
                && out.getCount() + result.getCount() <= out.getMaxStackSize())) {
                return i;
            }
        }
        return -1;
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