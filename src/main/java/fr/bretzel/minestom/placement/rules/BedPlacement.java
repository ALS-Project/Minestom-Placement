package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.states.state.Part;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class BedPlacement extends PlacementRule {

    public BedPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl) {
        var blockUtils = new BlockUtils(instance, blockPosition);
        return blockUtils.block().isAir() && blockUtils.relative(Facing.fromYaw(pl.getPosition().yaw())).block().isAir();
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
        var footBlock = new BlockUtils(instance, blockPosition);
        var playerFacing = Facing.fromYaw(pl.getPosition().yaw());
        var headBlock = footBlock.relative(playerFacing);

        if (footBlock.block().isAir() && headBlock.block().isAir()) {
            blockState.set(Part.FOOT);
            BlockState headStates = headBlock.state();
            headStates.withBlock(block());
            headStates.set(Part.HEAD);
            headStates.set(playerFacing);
            blockState.set(playerFacing);
            instance.setBlock(headBlock.position(), headStates.block());
        }
    }
}
