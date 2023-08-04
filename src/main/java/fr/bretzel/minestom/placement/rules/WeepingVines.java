package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

public class WeepingVines extends PlacementRule {

    public WeepingVines() {
        super(Block.WEEPING_VINES);
    }


    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        return placementState.blockFace() == BlockFace.BOTTOM;
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

        var selfBlock = new BlockUtils(instance, blockPosition);
        var upBlock = selfBlock.up();

        if (upBlock.block() == Block.WEEPING_VINES) {
            instance.setBlock(upBlock.position(), Block.WEEPING_VINES_PLANT);
        }
    }
}
