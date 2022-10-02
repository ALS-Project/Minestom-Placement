package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.placement.PlacementRule;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockHandler;

public class HeadPlacement extends PlacementRule {
    private final Block wall_head;
    private final BlockHandler skullHandler = MinecraftServer.getBlockManager().getHandler("minecraft:skull");

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
        if (blockFace != Facing.UP && blockFace != Facing.DOWN) {
            blockState.withBlock(wall_head);
            blockState.set(blockFace);
        }

        if (block() == Block.PLAYER_HEAD || block() == Block.PLAYER_WALL_HEAD) {
            blockState.setBlockHandler(skullHandler);
            blockState.setBlockNbt(pl.getItemInMainHand());
        }
    }
}
