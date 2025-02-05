import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Tile {
    private final boolean isMine;
    private boolean flagged;
    private boolean revealed;
    int minesInNeighbourhood;
    ArrayList<Tile> neighbours = new ArrayList<>() ;

    JButton button;


    public Tile(int minesInNeighbourhood) {
        this.isMine = (minesInNeighbourhood == 9);
        this.flagged = false;
        this.revealed = false;
        this.minesInNeighbourhood = minesInNeighbourhood;
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

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
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
        this.flagged = flagged;
    }
}
