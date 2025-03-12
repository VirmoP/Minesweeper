import java.util.HashSet;
import java.util.Set;

public class AutomatonSolver implements SolverInterface{

    public Set<Tile> tobeWorkedTiles = new HashSet<>();
    public Set<Tile> flaggedTiles = new HashSet<>();
    public Set<Tile> workedTiles = new HashSet<>();

    /**
     * Method that acts on a game object. Change happens to tiles and their revealed status.
     *
     * @param game given Game object
     * @return boolean of solved status. If game is solved true, otherwise false.
     */
    @Override
    public SolveInfo solve(Game game) {
        Tile[][] tiles = game.getBoard().board;
        tiles[game.getStarty()][game.getStartx()].revealWithNeighbours();

        tobeWorkedTiles.add(tiles[game.getStarty()][game.getStartx()]);

        boolean notStuck = true;
        int iteration = 0;

        while (notStuck && iteration < 10000){
            iteration++;
            notStuck = false;
            Set<Tile> revealedcopy = new HashSet<>(tobeWorkedTiles);
            for (Tile tile: revealedcopy){
                if (handleTile(tile))
                    notStuck = true;
            }

            if (flaggedTiles.size() == game.getMinecount())
                return new SolveInfo(true, null);
        }


        return new SolveInfo(false, null);
    }



    public boolean handleTile(Tile tile){
        TileInfo info = tile.getInformation();

        if (info.getClass() == TileInfoNull.class || info.flagged){
            return false;
        }

        Set<Tile> unrevealed = new HashSet<>();
        Set<Tile> flagged = new HashSet<>();

        for (Tile neighbour : info.neighbours){
            TileInfo neighbourinfo = neighbour.getInformation();
            if (neighbourinfo.getClass() == TileInfoNull.class){
                unrevealed.add(neighbour);
            } else if (neighbourinfo.flagged) {
                flagged.add(neighbour);
                flaggedTiles.add(neighbour);
            } else {
                if (!workedTiles.contains(neighbour)) {
                    tobeWorkedTiles.add(neighbour);
                }
            }
        }

        if (info.minesInNeighbourhood == flagged.size()) {
            for (Tile neighbour : unrevealed) {
                neighbour.setRevealed(true);
                if (!workedTiles.contains(neighbour))
                    tobeWorkedTiles.add(neighbour);
            }
            workedTiles.add(tile);
            tobeWorkedTiles.remove(tile);
            return true;
        }

        if (info.minesInNeighbourhood == flagged.size() + unrevealed.size()) {
            for (Tile neighbour : unrevealed) {
                neighbour.setFlagged(true);
                flaggedTiles.add(neighbour);
            }
            workedTiles.add(tile);
            tobeWorkedTiles.remove(tile);
            return true;
        }
        return false;
    }

}
