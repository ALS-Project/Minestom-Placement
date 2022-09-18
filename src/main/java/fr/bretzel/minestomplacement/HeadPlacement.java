package fr.bretzel.minestomplacement;

import fr.als.core.block.blockstate.BlockState;
import fr.als.core.block.blockstate.state.Facing;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class HeadPlacement extends ALSBlockPlacement {
    private final Block wall_head;

    public HeadPlacement(Block block, Block wall_head) {
        super(block);

        this.wall_head = wall_head;
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
        if (wall_head == Block.PLAYER_WALL_HEAD) {
            blockState.setBlockHandler(MinecraftServer.getBlockManager().getHandlerOrDummy("minecraft:skull"));
            blockState.setBlockNbt(pl.getItemInMainHand());
        }

        if (blockFace != Facing.UP && blockFace != Facing.DOWN) {
            blockState.withBlock(wall_head);
            blockState.set(blockFace);
        }
    }
}
