import java.util.HashSet;
import java.util.Set;

public class Tile {
    final int x;
    final int y;
    private final boolean isMine;
    private boolean flagged;
    private boolean revealed;
    int minesInNeighbourhood;
    Set<Tile> neighbours = new HashSet<>();

    //JButton button;


    public Tile(int minesInNeighbourhood, int x, int y) {
        this.isMine = (minesInNeighbourhood == 9);
        this.flagged = false;
        this.revealed = false;
        this.minesInNeighbourhood = minesInNeighbourhood;
        this.x = x;
        this.y = y;
    }



    public void revealWithNeighbours(){
        if (isRevealed())
            return;
        setRevealed(true);
        if (minesInNeighbourhood == 0) {
            for (Tile neighbour : neighbours) {
                neighbour.revealWithNeighbours();
            }
        }
    }

    public boolean setRevealed(boolean revealed) {
        if (this.flagged)
            return false;
        this.revealed = revealed;
        return this.isMine();
    }

    public boolean isMine() {
        return isMine;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setFlagged(boolean flagged) {
        if (this.revealed)
            return;
        this.flagged = flagged;
    }


    /**
     * Method to use in solver, return TileInfoNull object only when tile isn't revealed or flagged
     *
     * @return TileInfo object containing information available to the solver. TileInfoNull when tile is unreavealed and unflagged (meaning playerhas no info about it).
     */
    public TileInfo getInformation(){
        if (!isRevealed() && !isFlagged()){
            return new TileInfoNull();
        }
        return new TileInfo(x,y, flagged, minesInNeighbourhood, this, neighbours);
    }

    @Override
    public String toString() {
        return this.x + ";" + this.y + "-N:"+this.minesInNeighbourhood+",R:"+this.revealed+",F:"+this.flagged;
    }
}
