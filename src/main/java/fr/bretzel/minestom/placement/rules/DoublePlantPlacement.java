package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.PlantHalf;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

import java.util.Arrays;
import java.util.List;

public class DoublePlantPlacement extends PlacementRule {
    private static final List<Block> blockList = Arrays.asList(Block.SUNFLOWER, Block.LILAC, Block.ROSE_BUSH, Block.PEONY, Block.TALL_GRASS, Block.LARGE_FERN);

    public DoublePlantPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        var instance = placementState.instance();
        var blockPosition = placementState.placePosition();
        var blockFace = placementState.blockFace();
        var self = new BlockUtils((Instance) instance, blockPosition);
        return blockPosition.y() < 255 && self.block().isAir() && self.up().block().isAir() || !(blockFace == BlockFace.BOTTOM);
    }

    @Override
    public boolean canUpdate(BlockState blockState, BlockPlacementRule.UpdateState updateState) {
        return false;
    }

    @Override
    public void update(BlockState blockState, BlockPlacementRule.UpdateState updateState) {

    }

    @Override
    public void place(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        var instance = placementState.instance();
        var blockPosition = placementState.placePosition();
        var downBlock = new BlockUtils((Instance) instance, blockPosition).down();
        var plantHalf = PlantHalf.LOWER;

        if (downBlock.equals(blockState.block()))
            plantHalf = PlantHalf.UPPER;

        blockState.set(plantHalf);
    }

    public static boolean isDoublePlant(Block block) {
        return blockList.contains(block);
    }
}
