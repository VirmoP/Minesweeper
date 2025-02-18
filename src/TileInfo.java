import java.util.ArrayList;

public class TileInfo {
    final int x;
    final int y;
    boolean flagged;
    int minesInNeighbourhood;

    Tile tile;
    ArrayList<Tile> neighbours;

    public TileInfo(int x, int y, boolean flagged, int minesInNeighbourhood, Tile tile, ArrayList<Tile> neighbours) {
        this.x = x;
        this.y = y;
        this.flagged = flagged;
        this.minesInNeighbourhood = minesInNeighbourhood;
        this.tile = tile;
        this.neighbours = neighbours;
    }
}
