package fr.bretzel.minestomplacement.placement;

import fr.bretzel.minestomstates.BlockState;
import fr.bretzel.minestomstates.state.Facing;
import fr.bretzel.minestomstates.state.IntegerState;
import fr.als.core.utils.math.MathsUtils;
import fr.bretzel.minestomplacement.ALSBlockPlacement;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class RotationPlacement extends ALSBlockPlacement {

    private final IntegerState ROTATION = IntegerState.Of("rotation", 0, 0, 15);

    public RotationPlacement(Block block) {
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
        var yaw = pl.getPosition().yaw();

        if (!block().name().contains("skull")) {
            yaw = yaw + 180;
        }

        blockState.set(ROTATION, MathsUtils.floor((yaw * 16.0F / 360.0F) + 0.5D) & 15);

        if (block().name().toLowerCase().contains("sign")) {
            blockState.setBlockHandler(MinecraftServer.getBlockManager().getHandlerOrDummy("minecraft:sign"));
            blockState.setBlockNbt(pl.getItemInMainHand());
        }
    }
}
