package solvers;

import java.util.*;
import utils.*;

public class AutomatonSolverWithGuess implements SolverInterface {

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


        tobeWorkedTiles.add(tiles[game.getStarty()][game.getStartx()]);

        boolean notStuck = true;
        int iteration = 0;

        //TODO guessing starts at the first tiles.
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

            if (!notStuck){
                if (guess())
                    notStuck = true;
            }
        }


        return new SolveInfo(false, null);
    }

    /**
     * Method that reveals tile with the lowest probability (very basic calculation) of mine
     * @return bool if guess happened or not
     */
    public boolean guess(){
        Set<Tile> revealedcopy = new HashSet<>(tobeWorkedTiles);

        Map<Tile, Float> guessCanditates = new HashMap<>();

        for (Tile tile : revealedcopy){
            TileInfo info = tile.getInformation();

            if (info.getClass() == TileInfoNull.class || info.flagged){
                continue;
            }

            Set<Tile> unrevealed = new HashSet<>();
            Set<Tile> flagged = new HashSet<>();

            for (Tile neighbour : info.neighbours){
                TileInfo neighbourinfo = neighbour.getInformation();
                if (neighbourinfo.getClass() == TileInfoNull.class){
                    unrevealed.add(neighbour);
                } else if (neighbourinfo.flagged) {
                    flagged.add(neighbour);
                }
            }

            for (Tile unrevealedTile : unrevealed){
                if (guessCanditates.containsKey(unrevealedTile)){
                    float prevValue = guessCanditates.get(unrevealedTile);
                    guessCanditates.put(unrevealedTile, prevValue + (tile.getMinesInNeighbourhood() - flagged.size()) / ((float) unrevealed.size()));
                }else
                    guessCanditates.put(unrevealedTile, (tile.getMinesInNeighbourhood() - flagged.size()) / ((float) unrevealed.size()));
            }
        }
        Map.Entry<Tile, Float> min = null;
        for (Map.Entry<Tile, Float> entry : guessCanditates.entrySet()){
            if (min == null || min.getValue() > entry.getValue())
                min = entry;
        }

        if (min != null) {
            System.out.println(min.getKey().getX() + ", " + min.getKey().getY());

            min.getKey().setRevealed(true);
            System.out.println(min.getKey().getMinesInNeighbourhood());

            if (min.getKey().getInformation().minesInNeighbourhood == 9)
                return false;

            tobeWorkedTiles.add(min.getKey());

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
