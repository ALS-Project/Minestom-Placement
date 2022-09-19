package fr.bretzel.minestomplacement.placement;

import fr.bretzel.minestomplacement.ALSBlockPlacement;
import fr.bretzel.minestomstates.BlockState;
import fr.bretzel.minestomstates.state.Facing;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.block.BlockUtils;

public class BambooPlacement extends ALSBlockPlacement {
    public BambooPlacement() {
        super(Block.BAMBOO);
    }

    @Override
    public boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl) {
        var bellowBlock = instance.getBlock(blockPosition.sub(0, 1, 0));
        var block = instance.getBlock(blockPosition);
        return (bellowBlock.isSolid() || bellowBlock == Block.BAMBOO_SAPLING) && (block.isAir());
    }

    @Override
    public boolean canUpdate(Instance instance, Point blockPosition, BlockState blockState) {
        return true;
    }

    @Override
    public void update(Instance instance, Point blockPosition, BlockState blockState) {
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
    public void place(Instance instance, BlockState blockState, Facing blockFace, Point blockPosition, Player pl) {
        var blockUtils = new BlockUtils(instance, blockPosition);
        var below = blockUtils.below();

        if (below.getBlock() != Block.BAMBOO && below.getBlock() != Block.BAMBOO_SAPLING)
            blockState.withBlock(Block.BAMBOO_SAPLING);
        else if (below.getBlock() == Block.BAMBOO_SAPLING)
            instance.setBlock(blockPosition.sub(0, 1, 0), Block.BAMBOO);

    }
}
