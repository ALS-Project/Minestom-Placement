package fr.bretzel.minestomplacement;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockManager;

public class BlockPlacementManager {

    public static void register() {
        BlockManager blockManager = MinecraftServer.getBlockManager();

        for (Block block : Block.values()) {
            var blockPlacement = new MultipleBlockPlacement(block);
            var blockName = block.name().toLowerCase();

            if (block.properties().containsKey("facing"))
                blockPlacement.addBlockPlacement(new FacingPlacement(block));

            if (block.properties().containsKey("axis"))
                blockPlacement.addBlockPlacement(new AxisPlacement(block));

            if (block.properties().containsKey("waterlogged"))
                blockPlacement.addBlockPlacement(new DisableWaterLogPlacement(block));

            if (block.properties().containsKey("rotation"))
                blockPlacement.addBlockPlacement(new RotationPlacement(block));

            if (DoublePlantPlacement.isDoublePlant(block))
                blockPlacement.addBlockPlacement(new DoublePlantPlacement(block));

            if (StairsPlacement.isStairs(block))
                blockPlacement.addBlockPlacement(new StairsPlacement(block));

            if (CardinalPlacement.isCardinalBlock(block))
                blockPlacement.addBlockPlacement(new CardinalPlacement(block));

            if (block == Block.GRINDSTONE)
                blockPlacement.addBlockPlacement(GrindstonePlacement::new);

            if (block == Block.REDSTONE_WIRE)
                blockPlacement.addBlockPlacement(RedstonePlacement::new);

            if (block == Block.BAMBOO)
                blockPlacement.addBlockPlacement(BambooPlacement::new);

            if (block == Block.BELL)
                blockPlacement.addBlockPlacement(BellPlacement::new);

            if (block == Block.TWISTING_VINES)
                blockPlacement.addBlockPlacement(TwistingVines::new);

            if (block == Block.WEEPING_VINES)
                blockPlacement.addBlockPlacement(WeepingVines::new);

            if (block == Block.SNOW)
                blockPlacement.addBlockPlacement(SnowLayerPlacement::new);

            if (block == Block.TURTLE_EGG)
                blockPlacement.addBlockPlacement(TurtleEggPlacement::new);

            if (block == Block.PLAYER_HEAD)
                blockPlacement.addBlockPlacement(new HeadPlacement(block, Block.PLAYER_WALL_HEAD));

            if (block == Block.ZOMBIE_HEAD)
                blockPlacement.addBlockPlacement(new HeadPlacement(block, Block.ZOMBIE_WALL_HEAD));

            if (block == Block.CREEPER_HEAD)
                blockPlacement.addBlockPlacement(new HeadPlacement(block, Block.CREEPER_WALL_HEAD));

            if (block == Block.DRAGON_HEAD)
                blockPlacement.addBlockPlacement(new HeadPlacement(block, Block.DRAGON_WALL_HEAD));

            if (block == Block.SKELETON_SKULL)
                blockPlacement.addBlockPlacement(new HeadPlacement(block, Block.SKELETON_WALL_SKULL));

            if (block == Block.LANTERN || block == Block.SOUL_LANTERN)
                blockPlacement.addBlockPlacement(new LanternPlacement(block));

            if (block == Block.TORCH)
                blockPlacement.addBlockPlacement(new TorchPlacement(block, Block.WALL_TORCH));

            if (block == Block.REDSTONE_TORCH)
                blockPlacement.addBlockPlacement(new TorchPlacement(block, Block.REDSTONE_WALL_TORCH));

            if (block == Block.SOUL_TORCH)
                blockPlacement.addBlockPlacement(new TorchPlacement(block, Block.SOUL_WALL_TORCH));

            if (block == Block.CHEST || block == Block.TRAPPED_CHEST)
                blockPlacement.addBlockPlacement(new ChestBlockPlacement(block));

            if (blockName.contains("wall") && !blockName.contains("skull") && !blockName.contains("torch") && !blockName.contains("head") && !blockName.contains("wall_sign"))
                blockPlacement.addBlockPlacement(new WallPlacement(block));

            if (blockName.contains("slab"))
                blockPlacement.addBlockPlacement(new SlabPlacement(block));

            if (blockName.contains("anvil"))
                blockPlacement.addBlockPlacement(new AnvilPlacement(block));

            if (blockName.contains("bed") && block != Block.BEDROCK)
                blockPlacement.addBlockPlacement(new BedPlacement(block));

            if (blockName.contains("trapdoor"))
                blockPlacement.addBlockPlacement(new TrapdoorPlacement(block));

            if (blockName.contains("door") && !blockName.contains("trapdoor"))
                blockPlacement.addBlockPlacement(new DoorPlacement(block));

            if (blockName.contains("button") || block == Block.LEVER)
                blockPlacement.addBlockPlacement(new ButtonAndLeverPlacement(block));

            if (blockName.contains("fence_gate"))
                blockPlacement.addBlockPlacement(new FenceGatePlacement(block));

            if (blockPlacement.getPlacementRules().size() <= 0)
                blockPlacement.addBlockPlacement(new UnknownPlacement(block));

            blockManager.registerBlockPlacementRule(blockPlacement);
        }
    }
}
