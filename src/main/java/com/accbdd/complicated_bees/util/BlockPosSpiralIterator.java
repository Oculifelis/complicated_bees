package com.accbdd.complicated_bees.util;

import com.google.common.collect.AbstractIterator;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockPosSpiralIterator extends AbstractIterator<BlockPos> {

    private final BlockPos center;
    private final int hRadius, vRadius;
    private int shellIndex, currentShell;
    private List<BlockPos> currentShellPoints;

    public BlockPosSpiralIterator(BlockPos center, int hRadius, int vRadius) {
        this.center = center;
        this.hRadius = hRadius;
        this.vRadius = vRadius;
        this.currentShellPoints = generateShellPoints(currentShell);
    }

    @Nullable
    @Override
    protected BlockPos computeNext() {
        while (currentShell <= hRadius && currentShell <= vRadius) {
            BlockPos current = nextPos();
            if (current.getY() >= center.getY()-vRadius && current.getY() <= center.getY()+vRadius
                    && current.getX() >= center.getX()-hRadius && current.getX() <= center.getX()+hRadius
                    && current.getZ() >= center.getZ()-hRadius && current.getZ() <= center.getZ()+hRadius) {
                return current;
            }
        }
        return this.endOfData();
    }

    protected BlockPos nextPos() {
        BlockPos offset = currentShellPoints.get(shellIndex++);
        if (shellIndex >= currentShellPoints.size()) {
            currentShell++;
            shellIndex = 0;
            if (currentShell <= hRadius || currentShell <= vRadius) {
                currentShellPoints = generateShellPoints(currentShell);
            }
        }
        return center.offset(offset);
    }

    private List<BlockPos> generateShellPoints(int shellRadius) {
        List<BlockPos> points = new ArrayList<>();
        if (shellRadius == 0) {
            points.add(new BlockPos(0, 0, 0));
            return points;
        }

        for (int x = -shellRadius; x <= shellRadius; x++) {
            for (int y = -shellRadius; y <= shellRadius; y++) {
                for (int z = -shellRadius; z <= shellRadius; z++) {
                    if (Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(z))) == shellRadius) {
                        points.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return points;
    }
}
