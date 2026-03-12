package com.tcm.MineTale.util;

import com.tcm.MineTale.MineTale;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {


        /**
         * Create a TagKey for a Block in the mod's namespace.
         *
         * @param name the tag path (name) within the mod namespace
         * @return the Block TagKey for the specified tag name in the mod namespace
         */
        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> WOOD_REPAIR = createTag("wood_repair");
        public static final TagKey<Item> MOSS = createTag("moss");
        public static final TagKey<Item> MILK_BUCKETS = createTag("milk_buckets");
    }

    /**
     * Creates an item tag key using the mod's namespace and the given path.
     *
     * @param name the path component of the tag identifier within the mod namespace
     * @return the item TagKey for the identifier formed from the mod ID and the provided name
     */
    private static TagKey<Item> createTag(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name));
    }
}
