package net.minecraft.client.gui.screens.recipebook;

import net.minecraft.world.inventory.Slot;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public class GhostSlotsProxy {
    public static void setInputProxy(GhostSlots ghostSlots, Slot slot, ContextMap contextMap, SlotDisplay display) {
        // Because this class is in the same package, it can see protected methods!
        ghostSlots.setInput(slot, contextMap, display);
    }

    public static void setResultProxy(GhostSlots ghostSlots, Slot slot, ContextMap contextMap, SlotDisplay display) {
        ghostSlots.setResult(slot, contextMap, display);
    }
}