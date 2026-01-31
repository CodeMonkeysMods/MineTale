package com.tcm.MineTale.util;

/**
 * Constants defining the slot layout for the machine inventory.
 */
public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Input & Fuel
    public static final int FUEL_SLOT = 0;
    public static final int INPUT_1 = 1;
    public static final int INPUT_2 = 2;

    // Output range
    public static final int OUTPUT_START = 3;
    public static final int OUTPUT_END = 6;
    
    // Derived constant for convenience
    public static final int TOTAL_SLOTS = 7; 
}