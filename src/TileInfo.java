import java.util.Set;

public class TileInfo {
    public final int x;
    public final int y;
    public boolean flagged;
    public int minesInNeighbourhood;

    public Tile tile;
    public Set<Tile> neighbours;

    public TileInfo(int x, int y, boolean flagged, int minesInNeighbourhood, Tile tile, Set<Tile> neighbours) {
        this.x = x;
        this.y = y;
        this.flagged = flagged;
        this.minesInNeighbourhood = minesInNeighbourhood;
        this.tile = tile;
        this.neighbours = neighbours;
    }
}
