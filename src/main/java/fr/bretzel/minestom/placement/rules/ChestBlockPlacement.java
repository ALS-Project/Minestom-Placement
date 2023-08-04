package fr.bretzel.minestom.placement.rules;

import fr.bretzel.minestom.placement.PlacementRule;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.ChestType;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.BlockPlacementRule;

public class ChestBlockPlacement extends PlacementRule {
    public ChestBlockPlacement(Block block) {
        super(block);
    }

    public static Facing getDirectionToAttached(BlockState state) {
        var direction = state.get(Facing.class);
        return state.get(ChestType.class) == ChestType.LEFT ? direction.rotateY() : direction.rotateYCCW();
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
        var instance = updateState.instance();
        var blockPosition = updateState.blockPosition();
        var selfBlock = new BlockUtils((Instance) instance, blockPosition);
        var facing = blockState.get(Facing.class);

        var chestType = blockState.get(ChestType.SINGLE);

        if (chestType == ChestType.SINGLE) {
            switch (facing) {
                case NORTH, SOUTH -> {
                    var eastBlock = selfBlock.east();
                    var westBlock = selfBlock.west();

                    var eastState = eastBlock.state();
                    var westState = westBlock.state();

                    if (isSameChest(eastBlock)) {
                        if (eastState.get(Facing.class) == facing && eastState.get(ChestType.class) != ChestType.SINGLE) {
                            var eastType = eastState.get(ChestType.class);
                            var facing1 = getDirectionToAttached(eastState);
                            if (eastBlock.relative(facing1).position().equals(blockPosition)) {
                                blockState.set(eastType.opposite());
                            }
                        }
                    }

                    if (isSameChest(westBlock)) {
                        if (westState.get(Facing.class) == facing && westState.get(ChestType.class) != ChestType.SINGLE) {
                            var westTpe = westState.get(ChestType.class);
                            var facing1 = getDirectionToAttached(westState);
                            if (westBlock.relative(facing1).position().equals(blockPosition)) {
                                blockState.set(westTpe.opposite());
                            }
                        }
                    }
                }
                case EAST, WEST -> {
                    var northBlock = selfBlock.north();
                    var southBlock = selfBlock.south();

                    var northState = northBlock.state();
                    var southState = southBlock.state();

                    if (isSameChest(northBlock)) {
                        if (northState.get(Facing.class) == facing && northState.get(ChestType.class) != ChestType.SINGLE) {
                            var eastType = northState.get(ChestType.class);
                            var facing1 = getDirectionToAttached(northState);
                            if (northBlock.relative(facing1).position().equals(blockPosition)) {
                                blockState.set(eastType.opposite());
                            }
                        }
                    }

                    if (isSameChest(southBlock)) {
                        if (southState.get(Facing.class) == facing && southState.get(ChestType.class) != ChestType.SINGLE) {
                            var westTpe = southState.get(ChestType.class);
                            var facing1 = getDirectionToAttached(southState);
                            if (southBlock.relative(facing1).position().equals(blockPosition)) {
                                blockState.set(westTpe.opposite());
                            }
                        }
                    }
                }
            }
        } else {
            var attachedFacing = getDirectionToAttached(blockState);
            var attachedBlock = selfBlock.relative(attachedFacing);

            if (!isSameChest(attachedBlock))
                blockState.set(ChestType.SINGLE);
        }
    }

    @Override
    public void place(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        var instance = placementState.instance();
        var blockPosition = placementState.placePosition();
        var chestType = ChestType.SINGLE;

        var sneaking = placementState.isPlayerShifting();
        var facing = blockState.get(Facing.class);
        var alsBlock = new BlockUtils((Instance) instance, blockPosition);

        if (!sneaking) {
            if (facing == getDirectionToAttach(alsBlock, facing.rotateY())) {
                chestType = ChestType.LEFT;
            } else if (facing == getDirectionToAttach(alsBlock, facing.rotateYCCW())) {
                chestType = ChestType.RIGHT;
            }
        }

        blockState.set(chestType);
    }

    private Facing getDirectionToAttach(BlockUtils block, Facing directional) {
        var relative = block.relative(directional);
        var blockstate = relative.state();
        return isSameChest(relative.block()) && blockstate.get(ChestType.class) == ChestType.SINGLE ? blockstate.get(Facing.class) : null;
    }

    public boolean isSameChest(Block block) {
        return block == block();
    }

    public boolean isSameChest(BlockUtils block) {
        return isSameChest(block.block());
    }
}
