package com.accbdd.complicated_bees.bees.effect;

import com.accbdd.complicated_bees.util.BlockPosBoxIterator;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EssayistEffect extends BeeEffect {
    public static Random rand = new Random();

    @Override
    public void runEffect(BlockEntity apiary, ItemStack queen, int cycleProgress) {
        if (cycleProgress == 0) {
            BlockPosBoxIterator iter = getBlockIterator(apiary, queen);
            List<ChiseledBookShelfBlockEntity> shelves = new ArrayList<>();
            while (iter.hasNext()) {
                var pos = iter.next();
                if (apiary.getLevel().getBlockEntity(pos) instanceof ChiseledBookShelfBlockEntity bookshelf) {
                    shelves.add(bookshelf);
                }
            }
            if (!shelves.isEmpty()) {
                ChiseledBookShelfBlockEntity shelf = shelves.get(rand.nextInt(shelves.size()));
                for (int i = 0; i < 6; i++) {
                    if (shelf.canPlaceItem(i, generateBook())) {
                        shelf.setItem(i, generateBook());
                        break;
                    }
                }
            }
        }
    }

    private ItemStack generateBook() {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        book.addTagElement("title", StringTag.valueOf("Bee Book"));
        book.addTagElement("author", StringTag.valueOf("BEES"));
        ListTag pages = new ListTag();
        pages.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("buzzBUZZBuzz"))));
        book.addTagElement("pages", pages);
        return book;
    }
}
