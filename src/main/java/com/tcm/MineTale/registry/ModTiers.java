package com.tcm.MineTale.registry;

import java.util.HashMap;
import java.util.Map;

import com.tcm.MineTale.block.workbenches.entity.FurnaceWorkbenchEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModTiers {
    public record FurnaceTier(
        int id, 
        int cookTime,
        double scanRadius
    ) {}

    public static final Map<FurnaceTier, BlockEntityType<FurnaceWorkbenchEntity>> TIER_MAP = new HashMap<>();

    public static FurnaceTier getTierFromInt(int id) {
        return TIER_MAP.keySet().stream()
            .filter(t -> t.id() == id)
            .findFirst()
            .orElse(TIER_1);
    }

    public static final FurnaceTier TIER_1 = new FurnaceTier(1, 200, 8.0);
    public static final FurnaceTier TIER_2 = new FurnaceTier(2, 100, 8.0);
}