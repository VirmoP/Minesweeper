import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SinglePointSolver implements SolverInterface{
    /**
     * Method that acts on a game object. Change happens to tiles and their revealed status.
     *
     * @param game given Game object
     * @return boolean of solved status. If game is solved true, otherwise false.
     */
    @Override
    public SolveInfo solve(Game game) {

        Set<Tile> S = new HashSet<>();
        S.add(game.getBoard().board[game.getStarty()][game.getStartx()]);
        System.out.println(S);
        boolean notLost = true;
        while (notLost){
            Tile tile = null;
            if (!S.isEmpty()){
                for (Tile tilepick : S){
                    tile = tilepick;
                    break;
                }
            }
            /*else {
                //How to do random choice
                tile = pickRandomTile(game);
            }

             */
            //Gameover statement
            if (S.isEmpty())
                notLost = false;

            assert tile != null;
            tile.setRevealed(true);

            S.remove(tile);
            TileInfo info = tile.getInformation();
            if (info.minesInNeighbourhood == 0){
                S.addAll(info.neighbours);
            }else {
                for (Tile neighbour : info.neighbours){
                    TileInfo neighbourinfo = neighbour.getInformation();
                    if (neighbourinfo.getClass() == TileInfoNull.class)
                        continue;
                    int[] values = countAdjacent(neighbour);
                    if (values[1] == neighbourinfo.minesInNeighbourhood)
                        S.add(neighbour);
                    if (values[0] + values[1] == neighbourinfo.minesInNeighbourhood)
                        S.add(neighbour);
                }
            }
        }
        return new SolveInfo(true, null);
    }

    public int[] countAdjacent(Tile tile){
        int flagged = 0;
        int unmarked = 0;
        TileInfo tileInfo = tile.getInformation();
        for (Tile neighbour : tileInfo.neighbours){
            TileInfo neighbourinfo = neighbour.getInformation();
            if (neighbourinfo.getClass() == TileInfoNull.class){
                unmarked++;
            } else if (tileInfo.flagged) {
                flagged++;
            }
        }
        int[] values = new int[2];
        values[0] = unmarked;
        values[1] = flagged;
        return values;
    }

    public Tile pickRandomTile(Game game){
        Set<Tile> randomChoice = new HashSet<>();
        for (Tile[] row : game.getBoard().board){
            for (Tile tile1 : row){
                TileInfo tileInfo = tile1.getInformation();
                if (tileInfo.getClass() == TileInfoNull.class && !tileInfo.flagged)
                    randomChoice.add(tile1);
            }
        }
        int k = 0;
        int randomint = new Random().nextInt(randomChoice.size());
        for (Tile choice : randomChoice){
            if (k == randomint)
                return choice;
            k++;
        }
        return null;
    }
}
