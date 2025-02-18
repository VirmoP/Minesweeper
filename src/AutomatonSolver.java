import java.util.HashSet;
import java.util.Set;

public class AutomatonSolver implements SolverInterface{

    public Set<Tile> revealedTiles = new HashSet<>();
    public Set<Tile> flaggedTiles = new HashSet<>();

    /**
     * Method that acts on a game object. Change happens to tiles and their revealed status.
     *
     * @param game given Game object
     * @return boolean of solved status. If game is solved true, otherwise false.
     */
    @Override
    public boolean solve(Game game) {
        Tile[][] tiles = game.getBoard().board;
        tiles[game.getStarty()][game.getStartx()].revealWithNeighbours();

        revealedTiles.add(tiles[game.getStarty()][game.getStartx()]);

        boolean notStuck = true;
        int iteration = 0;

        while (notStuck && iteration < 10000){
            iteration++;
            notStuck = false;
            Set<Tile> revealedcopy = new HashSet<>(revealedTiles);
            for (Tile tile: revealedcopy){
                boolean i = handleTile(tile);
                if (i)
                    notStuck = true;
            }
            System.out.println(revealedTiles.size());
            if (flaggedTiles.size() == game.getMinecount())
                return true;
        }


        return false;
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
                revealedTiles.add(neighbour);
            }
        }

        if (info.minesInNeighbourhood == flagged.size()) {
            for (Tile neighbour : unrevealed) {
                neighbour.setRevealed(true);
                revealedTiles.add(neighbour);
            }
            return true;
        }

        if (info.minesInNeighbourhood == flagged.size() + unrevealed.size()) {
            for (Tile neighbour : unrevealed) {
                neighbour.setFlagged(true);
                flaggedTiles.add(neighbour);
            }
            return true;
        }
        return false;
    }

}
