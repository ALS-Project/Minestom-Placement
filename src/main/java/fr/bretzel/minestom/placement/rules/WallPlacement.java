package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.BlockStateManager;
import fr.bretzel.minestom.states.FenceGateState;
import fr.bretzel.minestom.states.WallState;
import fr.bretzel.minestom.states.state.BooleanState;
import fr.bretzel.minestom.states.state.Directional;
import fr.bretzel.minestom.states.state.WallHeight;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

import java.util.stream.Stream;

public class WallPlacement extends PlacementRule {

    public WallPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        return true;
    }

    @Override
    public boolean canUpdate(BlockState blockState, BlockPlacementRule.UpdateState updateState) {
        return true;
    }

    @Override
    public void update(BlockState blockState, BlockPlacementRule.UpdateState updateState) {
        updateState(blockState, (Instance) updateState.instance(), updateState.blockPosition());
    }

    @Override
    public void place(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        updateState(blockState, (Instance) placementState.instance(), placementState.placePosition());
    }

    public void updateState(BlockState blockState, Instance instance, Point blockPosition) {
        var selfBlock = new BlockUtils(instance, blockPosition);
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

    public WallHeight getWallHeight(boolean connected, Directional directional, BlockUtils selfBlock) {
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
