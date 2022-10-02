package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.states.state.PlantHalf;
import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.Arrays;
import java.util.List;

public class DoublePlantPlacement extends PlacementRule {
    private static final List<Block> blockList = Arrays.asList(Block.SUNFLOWER, Block.LILAC, Block.ROSE_BUSH, Block.PEONY, Block.TALL_GRASS, Block.LARGE_FERN);

    public DoublePlantPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl) {
        var self = new BlockUtils(instance, blockPosition);
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
        var downBlock = new BlockUtils(instance, blockPosition).down();
        var plantHalf = PlantHalf.LOWER;

        if (downBlock.equals(selfState.block()))
            plantHalf = PlantHalf.UPPER;

        selfState.set(plantHalf);
    }

    public static boolean isDoublePlant(Block block) {
        return blockList.contains(block);
    }
}
