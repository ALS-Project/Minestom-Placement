package fr.bretzel.minestomplacement;

import fr.als.core.block.blockstate.BlockState;
import fr.als.core.block.blockstate.state.Facing;
import fr.als.core.block.blockstate.state.IntegerState;
import fr.als.core.utils.ALSBlock;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class SnowLayerPlacement extends ALSBlockPlacement {

    private static final IntegerState LAYERS = IntegerState.Of("layers");

    public SnowLayerPlacement() {
        super(Block.SNOW);
    }

    @Override
    public boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl) {
        var block = instance.getBlock(blockPosition);
        return (block.isAir() || block == block()) && blockFace == Facing.UP;
    }

    @Override
    public boolean canUpdate(Instance instance, Point blockPosition, BlockState blockState) {
        return false;
    }

    @Override
    public void update(Instance instance, Point blockPosition, BlockState blockState) {
    }

    @Override
    public void place(Instance instance, BlockState blockState, Facing blockFace, Point blockPosition, Player pl) {
        var selfBlock = new ALSBlock(instance, blockPosition);
        var downBlock = selfBlock.down();

        if (blockFace == Facing.UP) {
            if (downBlock.block() == block()) {
                var downState = downBlock.state();
                var layers = downState.get(LAYERS);

                if (layers < 8) {
                    blockState.withBlock(Block.AIR);
                    layers = layers + 1;
                    downState.set(LAYERS, layers);
                    instance.setBlock(downBlock.position(), downState.block());
                }

            } else {
                blockState.set(LAYERS, 1);
            }
        }
    }
}
