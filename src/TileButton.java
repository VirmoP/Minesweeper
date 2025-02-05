import javax.swing.*;

public class TileButton extends JButton {
    Tile tile;

    /**
     * Creates a button with no set text or icon.
     */
    public TileButton(Tile tile) {
        this.tile = tile;
    }
}
