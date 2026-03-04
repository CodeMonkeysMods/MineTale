package com.tcm.MineTale.util;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum CoopPart implements StringRepresentable {
    // 18 Parts: 3 Wide (x) x 2 Deep (z) x 3 High (y)
    
    // --- BOTTOM LAYER (y=0) ---
    BOTTOM_FRONT_LEFT("bottom_front_left", 0, 0, 0),
    BOTTOM_FRONT_CENTER("bottom_front_center", 1, 0, 0),
    BOTTOM_FRONT_RIGHT("bottom_front_right", 2, 0, 0),
    BOTTOM_BACK_LEFT("bottom_back_left", 0, 1, 0),
    BOTTOM_BACK_CENTER("bottom_back_center", 1, 1, 0),
    BOTTOM_BACK_RIGHT("bottom_back_right", 2, 1, 0),

    // --- MIDDLE LAYER (y=1) ---
    MIDDLE_FRONT_LEFT("middle_front_left", 0, 0, 1),
    MIDDLE_FRONT_CENTER("middle_front_center", 1, 0, 1),
    MIDDLE_FRONT_RIGHT("middle_front_right", 2, 0, 1),
    MIDDLE_BACK_LEFT("middle_back_left", 0, 1, 1),
    MIDDLE_BACK_CENTER("middle_back_center", 1, 1, 1),
    MIDDLE_BACK_RIGHT("middle_back_right", 2, 1, 1),

    // --- TOP LAYER (y=2) ---
    TOP_FRONT_LEFT("top_front_left", 0, 0, 2),
    TOP_FRONT_CENTER("top_front_center", 1, 0, 2),
    TOP_FRONT_RIGHT("top_front_right", 2, 0, 2),
    TOP_BACK_LEFT("top_back_left", 0, 1, 2),
    TOP_BACK_CENTER("top_back_center", 1, 1, 2),
    TOP_BACK_RIGHT("top_back_right", 2, 1, 2);

    private final String name;
    private final int xOffset;
    private final int zOffset;
    private final int yOffset;

    CoopPart(String name, int x, int z, int y) {
        this.name = name;
        this.xOffset = x;
        this.zOffset = z;
        this.yOffset = y;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public int getXOffset() { return xOffset; }
    public int getYOffset() { return yOffset; }
    public int getZOffset() { return zOffset; }

    /**
     * Retrieves the CoopPart corresponding to the given grid offsets.
     * The grid is structured as 3x2x3 (width x depth x height), 
     * corresponding to the x, z, and y axes respectively.
     *
     * @param x The width offset
     * @param z The depth offset
     * @param y The height offset
     * @return The matching CoopPart
     * @throws IllegalArgumentException if no part exists at the specified coordinates
     */
    public static CoopPart getPartFromCoords(int x, int z, int y) {
        for (CoopPart part : values()) {
            if (part.xOffset == x && part.zOffset == z && part.yOffset == y) {
                return part;
            }
        }
        
        throw new IllegalArgumentException(
            String.format("No CoopPart found at coordinates: x=%d, z=%d, y=%d", x, z, y)
        );
    }
}