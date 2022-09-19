package fr.bretzel.minestomplacement.placement;

import fr.bretzel.minestomstates.BlockState;
import fr.bretzel.minestomstates.state.Facing;
import fr.bretzel.minestomstates.state.PlantHalf;
import fr.als.core.utils.ALSBlock;
import fr.bretzel.minestomplacement.ALSBlockPlacement;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.Arrays;
import java.util.List;

public class DoublePlantPlacement extends ALSBlockPlacement {
    private static final List<Block> blockList = Arrays.asList(Block.SUNFLOWER, Block.LILAC, Block.ROSE_BUSH, Block.PEONY, Block.TALL_GRASS, Block.LARGE_FERN);

    public DoublePlantPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl) {
        var self = new ALSBlock(instance, blockPosition);
        return blockPosition.y() < 255 && self.block().isAir() && self.up().block().isAir() || !(blockFace == Facing.DOWN);
    }

    @Override
    public boolean canUpdate(Instance instance, Point blockPosition, BlockState blockState) {
        return false;
    }

    @Override
    public void update(Instance instance, Point blockPosition, BlockState blockState) {

    }

    @Override
    public void place(Instance instance, BlockState selfState, Facing blockFace, Point blockPosition, Player pl) {
        var downBlock = new ALSBlock(instance, blockPosition).down();
        var plantHalf = PlantHalf.LOWER;

        if (downBlock.equals(selfState.block()))
            plantHalf = PlantHalf.UPPER;

        selfState.set(plantHalf);
    }

    public static boolean isDoublePlant(Block block) {
        return blockList.contains(block);
    }
}
