package utils;

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


    /**
     * Method that recursively reveals Tiles, if tile is 0.
     */
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

    /**
     * Sets revealed status to given boolean, if tile is not flagged
     * @param revealed given boolean
     * @return true only if mine was revealed and is a mine
     */
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

    /**
     * Sets flagged status to given boolean if not revealed
     * @param flagged given boolean
     */
    public void setFlagged(boolean flagged) {
        if (this.revealed)
            return;
        this.flagged = flagged;
    }


    /**
     * Method to use in solver, return utils.TileInfoNull object only when tile isn't revealed or flagged
     *
     * @return utils.TileInfo object containing information available to the solver. utils.TileInfoNull when tile is unreavealed and unflagged (meaning player has no info about it).
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Set<Tile> getNeighbours() {
        return neighbours;
    }

    public int getMinesInNeighbourhood() {
        return minesInNeighbourhood;
    }
}
