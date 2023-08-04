package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.minestom.server.utils.block.BlockUtils;

public class BambooPlacement extends PlacementRule {
    public BambooPlacement() {
        super(Block.BAMBOO);
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        var instance = placementState.instance();
        var blockPosition = placementState.placePosition();
        var bellowBlock = instance.getBlock(blockPosition.sub(0, 1, 0));
        var block = instance.getBlock(blockPosition);
        return (bellowBlock.isSolid() || bellowBlock == Block.BAMBOO_SAPLING) && (block.isAir());
    }

    @Override
    public boolean canUpdate(BlockState blockState, BlockPlacementRule.UpdateState updateState) {
        return true;
    }

    @Override
    public void update(BlockState blockState, BlockPlacementRule.UpdateState updateState) {
        var instance = (Instance) updateState.instance();
        var blockPosition = updateState.blockPosition();

        var upper = new BlockUtils(instance, blockPosition);
        var bellow = new BlockUtils(instance, blockPosition).below();

        if (bellow.getBlock().isAir() || !bellow.getBlock().isSolid()) {
            while (upper.getBlock() == Block.BAMBOO) {
                instance.setBlock(blockPosition, Block.AIR);
                blockPosition.add(0, 1, 0);
                upper.above();
            }
        }
    }

    @Override
    public void place(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        var instance = (Instance) placementState.instance();
        var blockPosition = placementState.placePosition();

        var blockUtils = new BlockUtils(instance, blockPosition);
        var below = blockUtils.below();

        if (below.getBlock() != Block.BAMBOO && below.getBlock() != Block.BAMBOO_SAPLING)
            blockState.withBlock(Block.BAMBOO_SAPLING);
        else if (below.getBlock() == Block.BAMBOO_SAPLING)
            instance.setBlock(blockPosition.sub(0, 1, 0), Block.BAMBOO);
    }
}
