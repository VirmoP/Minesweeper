package solvers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import utils.*;

public class AutomatonSolver implements SolverInterface {

    private final Set<Tile> flaggedSet = new HashSet<>();

    /**
     * Method that acts on a game object. Change happens to tiles and their revealed status.
     *
     * @param game given utils.Game object
     * @return boolean of solved status. If game is solved true, otherwise false.
     */
    @Override
    public SolveInfo solve(Game game) {
        Tile[][] tiles = game.getBoard().board;
        tiles[game.getStarty()][game.getStartx()].revealWithNeighbours();

        Set<Tile> allTiles = new HashSet<>();

        for (Tile[] row: tiles)
            allTiles.addAll(List.of(row));

        boolean notStuck = true;
        int iteration = 0;

        //calls hande tile on every tile until nothing changes or iteration count is over the number of tiles
        while (notStuck && iteration < tiles.length*tiles[0].length){
            iteration++;
            notStuck = false;


            for (Tile tile: allTiles){
                if (handleTile(tile))
                    notStuck = true;
            }

            if (flaggedSet.size() == game.getMinecount()) {
                return new SolveInfo(true, null);
            }
        }

        return new SolveInfo(false, null);
    }


    /**
     * Method that checks if tile satisfies certain simple constraints and if so does the necessary action
     * @param tile given utils.Tile
     * @return boolean if something was done
     */
    public boolean handleTile(Tile tile) {
        TileInfo info = tile.getInformation();

        //if no info about tile or it is flagged nothing can be done
        if (info.getClass() == TileInfoNull.class || info.flagged) {
            return false;
        }

        Set<Tile> unrevealed = new HashSet<>();
        Set<Tile> flagged = new HashSet<>();


        TileInfo neighbourinfo;
        for (Tile neighbour : info.neighbours) {
            neighbourinfo = neighbour.getInformation();
            if (neighbourinfo.getClass() == TileInfoNull.class) {
                unrevealed.add(neighbour);
            } else if (neighbourinfo.flagged) {
                flagged.add(neighbour);
                flaggedSet.add(neighbour);
            }
        }


        //if flags equal to mines in neighbourhood all unrevealed and not flagged tiles can be revealed
        if (info.minesInNeighbourhood == flagged.size()){
            for (Tile tile1: unrevealed){
                tile1.setRevealed(true);
            }
            return true;
        }

        //if mines in neighbourhood is equal to flags and unrevealed then all neighbours can be flagged
        if (info.minesInNeighbourhood == flagged.size() + unrevealed.size()){
            for (Tile tile2: unrevealed){
                tile2.setFlagged(true);
                flaggedSet.add(tile2);
            }
            return true;
        }
        return false;
    }
}
