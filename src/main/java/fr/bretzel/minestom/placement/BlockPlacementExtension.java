package fr.bretzel.minestom.placement;

import net.minestom.server.extensions.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockPlacementExtension extends Extension {

    private final Logger LOGGER = LoggerFactory.getLogger(BlockPlacementExtension.class);

    @Override
    public void initialize() {
        LOGGER.info("Starting to register all block placement !");
        BlockPlacementManager.register();
        LOGGER.info("End !");
    }

    @Override
    public void terminate() {

    }
}
