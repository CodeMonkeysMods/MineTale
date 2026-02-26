package com.tcm.MineTale.registry;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.block.workbenches.entity.ArmorersWorkbenchEntity;
import com.tcm.MineTale.block.workbenches.entity.CampfireWorkbenchEntity;
import com.tcm.MineTale.block.workbenches.entity.FurnaceWorkbenchEntity;
import com.tcm.MineTale.block.workbenches.entity.WorkbenchWorkbenchEntity;
import com.tcm.MineTale.block.workbenches.entity.FarmersWorkbenchEntity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType<CampfireWorkbenchEntity> CAMPFIRE_WORKBENCH_BE = register(
        "campfire_workbench_be", 
        CampfireWorkbenchEntity::new,
        ModBlocks.CAMPFIRE_WORKBENCH_BLOCK
    );

    public static final BlockEntityType<WorkbenchWorkbenchEntity> WORKBENCH_WORKBENCH_BE = register(
        "workbench_workbench_be", 
        WorkbenchWorkbenchEntity::new,
        ModBlocks.WORKBENCH_WORKBENCH_BLOCK
    );

    public static final BlockEntityType<ArmorersWorkbenchEntity> ARMORERS_WORKBENCH_BE = register(
        "armorers_workbench_be",
        ArmorersWorkbenchEntity::new,
        ModBlocks.ARMORERS_WORKBENCH_BLOCK
    );
    
    public static final BlockEntityType<FarmersWorkbenchEntity> FARMERS_WORKBENCH_BE = register(
        "farmers_workbench_be",
        FarmersWorkbenchEntity::new,
        ModBlocks.FARMERS_WORKBENCH_BLOCK
    );

    /**
     * Register a BlockEntityType for the given furnace tier and store it in the tier map.
     *
     * @param tier  the furnace tier whose BlockEntityType will be registered
     * @param block the block to associate with the registered BlockEntityType; must not be null
     * @return      the registered BlockEntityType for the specified furnace tier
     * @throws IllegalStateException if {@code block} is null
     */
    private static BlockEntityType<FurnaceWorkbenchEntity> registerTier(ModTiers.FurnaceTier tier, Block block) {
        // If 'block' is null here, the game WILL crash later. 
        // We can check it now to prove the theory:
        if (block == null) {
            throw new IllegalStateException("Block for tier " + tier.id() + " is null during BE registration!");
        }

        BlockEntityType<FurnaceWorkbenchEntity> type = register(
            tier.id() + "_furnace_be",
            (pos, state) -> new FurnaceWorkbenchEntity(tier, pos, state, 2, 6),
            block
        );
        ModTiers.TIER_MAP.put(tier, type);
        return type;
    }

    public static BlockEntityType<FurnaceWorkbenchEntity> FURNACE_WORKBENCH_BE_T1;
    public static BlockEntityType<FurnaceWorkbenchEntity> FURNACE_WORKBENCH_BE_T2;

    /**
     * Logs a confirmation that the mod's block entity types have been registered.
     *
     * Prints "Registered Mod Entities for {modId}" to standard output, where `{modId}` is the mod's identifier.
     */
    public static void initialize() {
        FURNACE_WORKBENCH_BE_T1 = registerTier(ModTiers.TIER_1, ModBlocks.FURNACE_WORKBENCH_BLOCK_T1);
        FURNACE_WORKBENCH_BE_T2 = registerTier(ModTiers.TIER_2, ModBlocks.FURNACE_WORKBENCH_BLOCK_T2);
        System.out.println("Registered Mod Entities for " + MineTale.MOD_ID);
    }
    
    // Helper registration method to keep it clean
    private static <T extends BlockEntity> BlockEntityType<T> register(
		String name,
		FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
		Block... blocks
    ) {
        Identifier id = Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}