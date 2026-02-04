package com.tcm.MineTale.util;

/**
 * Constants defining the slot layout for the machine inventory.
 */
public final class Constants {

    /**
     * Prevents instantiation of this utility class.
     *
     * @throws UnsupportedOperationException always thrown to enforce non-instantiability
     */
    private Constants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Input & Fuel
    public static final int FUEL_SLOT = 0;
    public static final int INPUT_START = 1;
}