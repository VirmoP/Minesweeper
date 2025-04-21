package utils;

import javax.swing.*;

public class TileButton extends JButton {
    public Tile tile;

    /**
     * Creates a button with no set text or icon.
     */
    public TileButton(Tile tile) {
        this.tile = tile;
    }
}
