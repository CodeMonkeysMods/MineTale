package com.tcm.MineTale.datagen;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.registry.ModBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class ModModelProvider extends FabricModelProvider {
    /**
     * Creates a ModModelProvider using the given Fabric data output.
     *
     * @param output the FabricDataOutput used to write generated model and blockstate data
     */
    public ModModelProvider(FabricDataOutput output) { super(output); }

    // Recreate the rotation logic locally since the base one is private
    private static final PropertyDispatch<VariantMutator> WORKBENCH_ROTATION = PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
            .select(Direction.NORTH, BlockModelGenerators.NOP)
            .select(Direction.EAST, BlockModelGenerators.Y_ROT_90)
            .select(Direction.SOUTH, BlockModelGenerators.Y_ROT_180)
            .select(Direction.WEST, BlockModelGenerators.Y_ROT_270);

    /**
     * Registers block state and model definitions for the mod's log blocks.
     *
     * This configures horizontal and vertical log models for each custom log block and, for
     * WILD_WISTERIA_LOG, also registers the corresponding wood model (WILD_WISTERIA_WOOD).
     *
     * @param blockStateModelGenerator the generator used to create block state and model entries
     */
    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.woodProvider(ModBlocks.AMBER_LOG).logWithHorizontal(ModBlocks.AMBER_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.ASH_LOG).logWithHorizontal(ModBlocks.ASH_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.ASPEN_LOG).logWithHorizontal(ModBlocks.ASPEN_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.AZURE_LOG).logWithHorizontal(ModBlocks.AZURE_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.BAMBOO_LOG).logWithHorizontal(ModBlocks.BAMBOO_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.BAMBOO_LOG_DECO).logWithHorizontal(ModBlocks.BAMBOO_LOG_DECO);
        blockStateModelGenerator.woodProvider(ModBlocks.BANYAN_LOG).logWithHorizontal(ModBlocks.BANYAN_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.BEECH_LOG).logWithHorizontal(ModBlocks.BEECH_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.BOTTLETREE_LOG).logWithHorizontal(ModBlocks.BOTTLETREE_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.BURNT_LOG).logWithHorizontal(ModBlocks.BURNT_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.CAMPHOR_LOG).logWithHorizontal(ModBlocks.CAMPHOR_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.CEDAR_LOG).logWithHorizontal(ModBlocks.CEDAR_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.CRYSTALWOOD_LOG).logWithHorizontal(ModBlocks.CRYSTALWOOD_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.DRY_LOG).logWithHorizontal(ModBlocks.DRY_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.BLUE_FIG_LOG).logWithHorizontal(ModBlocks.BLUE_FIG_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.FIRE_LOG).logWithHorizontal(ModBlocks.FIRE_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.GUMBOAB_LOG).logWithHorizontal(ModBlocks.GUMBOAB_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.ICE_LOG).logWithHorizontal(ModBlocks.ICE_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.MAPLE_LOG).logWithHorizontal(ModBlocks.MAPLE_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.PALM_TREE_LOG).logWithHorizontal(ModBlocks.PALM_TREE_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.PALO_LOG).logWithHorizontal(ModBlocks.PALO_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.PETRIFIED_LOG).logWithHorizontal(ModBlocks.PETRIFIED_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.POISONED_LOG).logWithHorizontal(ModBlocks.POISONED_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.REDWOOD_LOG).logWithHorizontal(ModBlocks.REDWOOD_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.SALLOW_LOG).logWithHorizontal(ModBlocks.SALLOW_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.SPIRAL_LOG).logWithHorizontal(ModBlocks.SPIRAL_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.STORMBARK_LOG).logWithHorizontal(ModBlocks.STORMBARK_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.STRIPPED_LOG).logWithHorizontal(ModBlocks.STRIPPED_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.WINDWILLOW_LOG).logWithHorizontal(ModBlocks.WINDWILLOW_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.WILD_WISTERIA_LOG).logWithHorizontal(ModBlocks.WILD_WISTERIA_LOG).wood(ModBlocks.WILD_WISTERIA_WOOD);

        registerLargeWorkbench(blockStateModelGenerator, ModBlocks.FURNACE_WORKBENCH_BLOCK_T1);
    }

    private void registerLargeWorkbench(BlockModelGenerators generator, Block block) {
        // 1. Get the base identifier (e.g., minetale:block/furnace_workbench_block_t1)
        Identifier blockId = ModelLocationUtils.getModelLocation(block);

        // 2. Build the references to your manual JSON files
        // .withSuffix() creates: minetale:block/furnace_workbench_block_t1_bottom_left, etc.
        Identifier bottomLeft  = blockId.withSuffix("_bottom_left");
        Identifier bottomRight = blockId.withSuffix("_bottom_right");
        Identifier topLeft     = blockId.withSuffix("_top_left");
        Identifier topRight    = blockId.withSuffix("_top_right");
        Identifier inventory   = blockId.withSuffix("_inventory");

        // 3. Dispatch to Blockstate (Tells the game which model to show for each state)
        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
            .with(PropertyDispatch.initial(BlockStateProperties.DOUBLE_BLOCK_HALF, BlockStateProperties.CHEST_TYPE)
                .select(DoubleBlockHalf.LOWER, ChestType.LEFT,   BlockModelGenerators.plainVariant(bottomLeft))
                .select(DoubleBlockHalf.LOWER, ChestType.RIGHT,  BlockModelGenerators.plainVariant(bottomRight))
                .select(DoubleBlockHalf.UPPER, ChestType.LEFT,   BlockModelGenerators.plainVariant(topLeft))
                .select(DoubleBlockHalf.UPPER, ChestType.RIGHT,  BlockModelGenerators.plainVariant(topRight))
                // Support the 'SINGLE' state as a fallback
                .select(DoubleBlockHalf.LOWER, ChestType.SINGLE, BlockModelGenerators.plainVariant(bottomLeft))
                .select(DoubleBlockHalf.UPPER, ChestType.SINGLE, BlockModelGenerators.plainVariant(topLeft))
            )
            .with(WORKBENCH_ROTATION)
        );

        // 4. Map the Item in your hand to the inventory JSON
        generator.registerSimpleItemModel(block, inventory);
    }

    /**
     * Registers item models for the mod; currently left empty (no item models are generated).
     *
     * @param itemModelGenerators generator used to register item models
     */
    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {

    }
}