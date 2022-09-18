package fr.bretzel.minestomplacement;

import fr.als.core.block.blockstate.BlockState;
import fr.als.core.block.blockstate.state.ChestType;
import fr.als.core.block.blockstate.state.Facing;
import fr.als.core.utils.ALSBlock;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class ChestBlockPlacement extends ALSBlockPlacement {
    public ChestBlockPlacement(Block block) {
        super(block);
    }

    public static Facing getDirectionToAttached(BlockState state) {
        var direction = state.get(Facing.class);
        return state.get(ChestType.class) == ChestType.LEFT ? direction.rotateY() : direction.rotateYCCW();
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
    public void update(Instance instance, Point blockPosition, BlockState state) {
        var selfBlock = new ALSBlock(instance, blockPosition);
        var facing = state.get(Facing.class);

        var chestType = state.get(ChestType.SINGLE);

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
                                state.set(eastType.opposite());
                            }
                        }
                    }

                    if (isSameChest(westBlock)) {
                        if (westState.get(Facing.class) == facing && westState.get(ChestType.class) != ChestType.SINGLE) {
                            var westTpe = westState.get(ChestType.class);
                            var facing1 = getDirectionToAttached(westState);
                            if (westBlock.relative(facing1).position().equals(blockPosition)) {
                                state.set(westTpe.opposite());
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
                                state.set(eastType.opposite());
                            }
                        }
                    }

                    if (isSameChest(southBlock)) {
                        if (southState.get(Facing.class) == facing && southState.get(ChestType.class) != ChestType.SINGLE) {
                            var westTpe = southState.get(ChestType.class);
                            var facing1 = getDirectionToAttached(southState);
                            if (southBlock.relative(facing1).position().equals(blockPosition)) {
                                state.set(westTpe.opposite());
                            }
                        }
                    }
                }
            }
        } else {
            var attachedFacing = getDirectionToAttached(state);
            var attachedBlock = selfBlock.relative(attachedFacing);

            if (!isSameChest(attachedBlock))
                state.set(ChestType.SINGLE);
        }
    }

    @Override
    public void place(Instance instance, BlockState blockState, Facing blockFace, Point blockPosition, Player pl) {
        var chestType = ChestType.SINGLE;
        var sneaking = pl.isSneaking();
        var facing = blockState.get(Facing.class);
        var alsBlock = new ALSBlock(instance, blockPosition);

        if (!sneaking) {
            if (facing == getDirectionToAttach(alsBlock, facing.rotateY())) {
                chestType = ChestType.LEFT;
            } else if (facing == getDirectionToAttach(alsBlock, facing.rotateYCCW())) {
                chestType = ChestType.RIGHT;
            }
        }

        blockState.set(chestType);
    }

    private Facing getDirectionToAttach(ALSBlock block, Facing directional) {
        var relative = block.relative(directional);
        var blockstate = relative.state();
        return isSameChest(relative.block()) && blockstate.get(ChestType.class) == ChestType.SINGLE ? blockstate.get(Facing.class) : null;
    }

    public boolean isSameChest(Block block) {
        return block == block();
    }

    public boolean isSameChest(ALSBlock block) {
        return isSameChest(block.block());
    }
}
