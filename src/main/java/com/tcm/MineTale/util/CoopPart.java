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

    /**
     * Initialise the enum constant with its serialized name and 3D grid offsets.
     *
     * @param name the serialized name for this part
     * @param x the x-axis offset (width; 0 = left)
     * @param z the z-axis offset (depth; 0 = front)
     * @param y the y-axis offset (height; 0 = bottom)
     */
    CoopPart(String name, int x, int z, int y) {
        this.name = name;
        this.xOffset = x;
        this.zOffset = z;
        this.yOffset = y;
    }

    /**
     * Gets the serialized name for this enum constant.
     *
     * @return the serialized name associated with this part
     */
    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    /**
 * The part's x-axis offset within the structure's width.
 *
 * @return the x-axis offset (0 to 2) indicating the part's horizontal position
 */
public int getXOffset() { return xOffset; }
    /**
 * The vertical offset within the three-layer coop grid.
 *
 * @return the y-axis offset (height layer) where 0 is bottom, 1 is middle and 2 is top
 */
public int getYOffset() { return yOffset; }
    /**
 * The depth offset of this coop part within the grid.
 *
 * @return the z-axis offset (0 = front, 1 = back)
 */
public int getZOffset() { return zOffset; }

    /**
     * Retrieves the CoopPart corresponding to the supplied grid offsets.
     *
     * Offsets map to a 3×2×3 (width × depth × height) grid along the x, z and y axes.
     *
     * @param x the width offset (0–2)
     * @param z the depth offset (0–1)
     * @param y the height offset (0–2)
     * @return the matching CoopPart
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