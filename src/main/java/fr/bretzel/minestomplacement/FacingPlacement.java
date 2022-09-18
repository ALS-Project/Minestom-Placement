package fr.bretzel.minestomplacement;

import fr.als.core.block.blockstate.BlockState;
import fr.als.core.block.blockstate.state.Facing;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class FacingPlacement extends ALSBlockPlacement {
    private final List<Block> needToBeInverted =
            Arrays.asList(Block.HOPPER, Block.LECTERN, Block.REPEATER, Block.COMPARATOR, Block.CHEST, Block.ENDER_CHEST,
                    Block.TRAPPED_CHEST, Block.END_PORTAL_FRAME);

    private final List<Block> onlyDirectional =
            Arrays.asList(Block.DISPENSER, Block.PISTON, Block.STICKY_PISTON, Block.DROPPER, Block.OBSERVER);

    public FacingPlacement(@NotNull Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl) {
        return true;
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
        var facing = blockFace;

        if (!hasUpAndDown(blockState) && (blockFace == Facing.DOWN || blockFace == Facing.UP) || (onlyDirectional.contains(block()) && pl.getPosition().pitch() < 45.5))
            facing = Facing.fromYaw(pl.getPosition().yaw());

        if (needToBeInverted.contains(block()) || onlyDirectional.contains(block()))
            facing = facing.opposite();

        blockState.set(facing);
    }

    public boolean hasUpAndDown(BlockState blockState) {
        var facingValue = blockState.getAllStateValue(Facing.class);
        return facingValue.stream().anyMatch(facing -> facing == Facing.UP || facing == Facing.DOWN);
    }

}
