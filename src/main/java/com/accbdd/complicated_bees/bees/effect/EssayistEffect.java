package com.accbdd.complicated_bees.bees.effect;

import com.accbdd.complicated_bees.util.BlockPosBoxIterator;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EssayistEffect extends BeeEffect {
    public static Random rand = new Random();

    @Override
    public void runEffect(BlockEntity apiary, ItemStack queen, int cycleProgress) {
        if (cycleProgress == 0 & rand.nextFloat() < 0.05f) {
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
        book.set(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContent(
                Filterable.passThrough("Book"),
                "a bee",
                0,
                List.of(Filterable.passThrough(generatePage())),
                true
        ));
        return book;
    }

    private static Component generatePage() {
        StringBuilder content = new StringBuilder();
        while (content.length() < 266) {
            content.append(randomBuzz());
        }
        return Component.literal(content.substring(0, 266));
    }

    private static String randomBuzz() {
        StringBuilder buzz = new StringBuilder();
        buzz.append(rand.nextBoolean() ? 'B' : 'b');
        for (int i = 1; i < rand.nextInt(4, 9); i++) {
            if (i == 1) {
                buzz.append('u');
            } else {
                buzz.append(rand.nextBoolean() ? 'z' : 'Z');
            }
        }
        return buzz.toString();
    }
}
