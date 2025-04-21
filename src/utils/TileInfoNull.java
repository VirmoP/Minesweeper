package utils;

import java.util.HashSet;

/**
 * Utility to avoid returns of zero
 */
public class TileInfoNull extends TileInfo{
    public TileInfoNull() {
        super(0,0,false,0,new Tile(0,0,0), new HashSet<>());

    }
}
