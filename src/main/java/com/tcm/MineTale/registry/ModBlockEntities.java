package com.tcm.MineTale.registry;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.block.workbenches.entity.CampfireWorkbenchEntity;
import com.tcm.MineTale.block.workbenches.entity.FurnaceWorkbenchEntity;

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

    public static final BlockEntityType<FurnaceWorkbenchEntity> FURNACE_WORKBENCH_BE = register(
        "furnace_workbench_be", 
        FurnaceWorkbenchEntity::new,
        ModBlocks.FURNACE_WORKBENCH_BLOCK
    );


    public static void initialize() {
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
