package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.states.state.Part;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

public class BedPlacement extends PlacementRule {

    public BedPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        var instance = (Instance) placementState.instance();
        var blockUtils = new BlockUtils(instance, placementState.placePosition());
        return blockUtils.block().isAir() && blockUtils.relative(Facing.fromYaw(placementState.playerPosition().yaw())).block().isAir();
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
        var instance = (Instance) placementState.instance();
        var blockPosition = placementState.placePosition();
        var footBlock = new BlockUtils(instance, blockPosition);
        var playerFacing = Facing.fromYaw(placementState.playerPosition().yaw());
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
