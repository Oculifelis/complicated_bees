package com.accbdd.complicated_bees.compat.jei.ingredient;

import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.Services;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.stream.Collectors;

public final class BlockListFactory {
    private static final Logger LOGGER = LogManager.getLogger();

    public static List<Block> create(BlockHelper blockHelper) {

        final List<Block> blockList = new ArrayList<>();
        final Set<Object> blockUidSet = new HashSet<>();

//        Minecraft minecraft = Minecraft.getInstance();
//
//        ClientLevel level = minecraft.level;
//        if (level == null) {
//            throw new NullPointerException("minecraft.level must be set before JEI fetches ingredients");
//        }
//        RegistryAccess registryAccess = level.registryAccess();


        addItemsFromRegistries(blockHelper, blockList, blockUidSet);


        return blockList;
    }

    private static void addItemsFromRegistries(
            BlockHelper blockHelper,
            List<Block> blockList,
            Set<Object> blockUidSet
    ) {
        {
            List<Block> blocks = ForgeRegistries.BLOCKS.getValues().stream().toList();

            int added = 0;
            for (Block block : blocks) {
                Object blockKey = safeGetUid(blockHelper, block);
                if (blockKey != null && blockUidSet.add(blockKey)) {
                    blockList.add(block);
                    added++;
                }
            }

            LOGGER.debug(
                    "Added {}/{} new items from the item registry (this is run because ShowHiddenItems is set to true in JEI's config)",
                    added,
                    blocks.size()
            );
        }

        {
            List<Block> blocks = ForgeRegistries.BLOCKS.getValues().stream().toList();

            int added = 0;
            for (Block block : blocks) {
                String blockKey = safeGetUid(blockHelper, block);
                if (blockKey != null && blockUidSet.add(blockKey)) {
                    blockList.add(block);
                    added++;
                }
            }

            LOGGER.debug(
                    "Added {}/{} new blocks from the block registry.",
                    added,
                    blocks.size()
            );
        }
    }

    @Nullable
    private static String safeGetUid(BlockHelper blockHelper, Block block) {
        return blockHelper.getUniqueId(block, UidContext.Ingredient);
    }
}
