package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class FacingPlacement extends PlacementRule {
    private final List<Block> needToBeInverted =
            Arrays.asList(Block.HOPPER, Block.LECTERN, Block.REPEATER, Block.COMPARATOR, Block.CHEST, Block.ENDER_CHEST,
                    Block.TRAPPED_CHEST, Block.END_PORTAL_FRAME);

    private final List<Block> onlyDirectional =
            Arrays.asList(Block.DISPENSER, Block.PISTON, Block.STICKY_PISTON, Block.DROPPER, Block.OBSERVER);

    public FacingPlacement(@NotNull Block block) {
        super(block);
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
        var facing = blockFace;
        var playerPosition = placementState.playerPosition();

        if (!hasUpAndDown(blockState) && (blockFace == BlockFace.BOTTOM || blockFace == BlockFace.TOP) || (onlyDirectional.contains(block()) && playerPosition.pitch() < 45.5))
            facing = Facing.fromYaw(playerPosition.yaw()).getMinestomBlockFace();

        if (needToBeInverted.contains(block()) || onlyDirectional.contains(block()))
            facing = facing.getOppositeFace();

        blockState.set(Facing.parse(facing));
    }

    public boolean hasUpAndDown(BlockState blockState) {
        var facingValue = blockState.getAllStateValue(Facing.class);
        return facingValue.stream().anyMatch(facing -> facing == Facing.UP || facing == Facing.DOWN);
    }
}
