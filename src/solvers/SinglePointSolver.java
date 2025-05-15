package solvers;

import java.util.HashSet;
import java.util.Set;
import utils.*;

public class SinglePointSolver implements SolverInterface {

    public Set<Tile> tobeWorkedTiles = new HashSet<>();
    public Set<Tile> flaggedTiles = new HashSet<>();
    public Set<Tile> workedTiles = new HashSet<>();

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

        //keep track of tiles that can change and only check if can change on those
        tobeWorkedTiles.add(tiles[game.getStarty()][game.getStartx()]);

        boolean notStuck = true;
        int iteration = 0;

        while (notStuck && iteration < tiles.length*tiles[0].length){
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


    /**
     * Method that checks if tile satisfies certain simple constraints and if so does the necessary action
     * @param tile given utils.Tile
     * @return boolean if something was done
     */
    public boolean handleTile(Tile tile){
        TileInfo info = tile.getInformation();

        //if no info about tile or it is flagged nothing can be done
        if (info.getClass() == TileInfoNull.class || info.flagged){
            return false;
        }

        Set<Tile> unrevealed = new HashSet<>();
        Set<Tile> flagged = new HashSet<>();

        TileInfo neighbourinfo;
        for (Tile neighbour : info.neighbours){
            neighbourinfo = neighbour.getInformation();
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

        //if flags equal to mines in neighbourhood all unrevealed and not flagged tiles can be revealed
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

        //if mines in neighbourhood is equal to flags and unrevealed then all neighbours can be flagged
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
