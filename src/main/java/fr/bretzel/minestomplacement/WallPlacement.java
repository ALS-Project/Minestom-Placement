package fr.bretzel.minestomplacement;

import fr.als.core.block.blockstate.BlockState;
import fr.als.core.block.blockstate.BlockStateManager;
import fr.als.core.block.blockstate.FenceGateState;
import fr.als.core.block.blockstate.WallState;
import fr.als.core.block.blockstate.state.BooleanState;
import fr.als.core.block.blockstate.state.Directional;
import fr.als.core.block.blockstate.state.Facing;
import fr.als.core.block.blockstate.state.WallHeight;
import fr.als.core.utils.ALSBlock;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.stream.Stream;

public class WallPlacement extends ALSBlockPlacement {

    public WallPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(Instance instance, Facing blockFace, Point blockPosition, BlockState blockState, Player pl) {
        return true;
    }

    @Override
    public boolean canUpdate(Instance instance, Point blockPosition, BlockState blockState) {
        return true;
    }

    @Override
    public void update(Instance instance, Point blockPosition, BlockState blockState) {
        updateState(blockState, instance, blockPosition);
    }

    @Override
    public void place(Instance instance, BlockState blockState, Facing blockFace, Point blockPosition, Player pl) {
        updateState(blockState, instance, blockPosition);
    }

    public void updateState(BlockState blockState, Instance instance, Point blockPosition) {
        var selfBlock = new ALSBlock(instance, blockPosition);
        var northBlock = selfBlock.north();
        var southBlock = selfBlock.south();
        var westBlock = selfBlock.west();
        var eastBlock = selfBlock.east();
        var upBlock = selfBlock.up();

        var northState = northBlock.state();
        var southState = southBlock.state();
        var westState = westBlock.state();
        var eastState = eastBlock.state();
        var upState = upBlock.state();

        var northConnect = shouldConnect(northState, isSolidBlock(northBlock.block()), Directional.SOUTH);
        var eastConnect = shouldConnect(eastState, isSolidBlock(eastBlock.block()), Directional.WEST);
        var southConnect = shouldConnect(southState, isSolidBlock(southBlock.block()), Directional.NORTH);
        var westConnect = shouldConnect(westState, isSolidBlock(westBlock.block()), Directional.EAST);

        blockState.set(Directional.SOUTH, getWallHeight(southConnect, Directional.SOUTH, selfBlock));
        blockState.set(Directional.EAST, getWallHeight(eastConnect, Directional.EAST, selfBlock));
        blockState.set(Directional.WEST, getWallHeight(westConnect, Directional.WEST, selfBlock));
        blockState.set(Directional.NORTH, getWallHeight(northConnect, Directional.NORTH, selfBlock));

        blockState.set(BooleanState.Waterlogged(false));

        blockState.set(BooleanState.Of(Directional.UP, needUp(blockState, upState)));
    }

    public WallHeight getWallHeight(boolean connected, Directional directional, ALSBlock selfBlock) {
        if (connected) {
            var upBlock = selfBlock.up();
            if (upBlock.block().isSolid() && !isWall(upBlock.state()))
                return WallHeight.TALL;
            else if (isWall(upBlock.state())) {
                var upState = upBlock.state();
                if (upState.get(directional, WallHeight.class) != WallHeight.NONE)
                    return WallHeight.TALL;
                else
                    return WallHeight.LOW;
            } else
                return WallHeight.LOW;
        } else
            return WallHeight.NONE;
    }

    public boolean shouldConnect(BlockState state, boolean sideColid, Directional directional) {
        var block = state.block();
        var flag = isFenceGate(block) && ((FenceGateState) BlockStateManager.get(block)).isParallel(state, directional);
        return isWall(state) || !cannotAttach(block) && sideColid || isPane(block) || flag;
    }

    public boolean needUp(BlockState state, BlockState upState) {
        if (isWall(upState) && upState.get(BooleanState.Of("up"))) {
            return true;
        } else {

            var connected = (int) Stream.of(Directional.axis)
                    .map(directional -> state.get(directional, WallHeight.class))
                    .filter(wallHeight -> wallHeight != WallHeight.NONE)
                    .count();

            if (connected == 0 || connected == 1 || connected == 3) {
                return true;
            } else if (connected == 2 || connected == 4) {
                if (connected == 2) {
                    var east = state.get(Directional.EAST, WallHeight.class) != WallHeight.NONE;
                    var west = state.get(Directional.WEST, WallHeight.class) != WallHeight.NONE;
                    var north = state.get(Directional.NORTH, WallHeight.class) != WallHeight.NONE;
                    var south = state.get(Directional.SOUTH, WallHeight.class) != WallHeight.NONE;

                    return !(east == west) || !(north == south);
                } else {
                    if (isWall(upState)) {
                        return upState.get(BooleanState.Of("up"));
                    } else {
                        return isSolidBlock(upState.block());
                    }
                }
            } else {
                return true;
            }
        }
    }

    public boolean cannotAttach(Block blockIn) {
        return blockIn.name().contains("leaves") || blockIn == Block.BARRIER || blockIn == Block.CARVED_PUMPKIN || blockIn == Block.JACK_O_LANTERN
                || blockIn == Block.MELON || blockIn == Block.PUMPKIN || blockIn.name().contains("shulker_block");
    }

    public boolean isSolidBlock(Block block) {
        return block.isSolid();
    }

    public boolean isFenceGate(Block block) {
        return block.name().contains("fence_gate");
    }

    public boolean isWall(BlockState state) {
        return state instanceof WallState;
    }

    public boolean isPane(Block block) {
        return block.name().contains("pane") || block == Block.IRON_BARS;
    }
}
