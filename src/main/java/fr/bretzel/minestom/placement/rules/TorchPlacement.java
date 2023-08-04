package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

public class TorchPlacement extends PlacementRule {

    private final Block toPlace;

    public TorchPlacement(Block block, Block toPlace) {
        super(block);
        this.toPlace = toPlace;
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        return true;
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
        var blockFace = placementState.blockFace();

        if (blockFace == BlockFace.BOTTOM || blockFace == BlockFace.TOP)
            return;

        blockState.withBlock(toPlace);
        blockState.set(Facing.parse(blockFace));
    }
}
