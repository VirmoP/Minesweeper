package solvers;

import java.util.*;
import utils.*;

public class CSPSolver implements SolverInterface {


    int minesLeft;

    /**
     * Method that acts on a game object. Change happens to tiles and their revealed status.
     *
     * @param game given utils.Game object
     * @return boolean of solved status. If game is solved true, otherwise false.
     */
    @Override
    public SolveInfo solve(Game game) {
        minesLeft = game.getMinecount();
        int iteration = 0;
        int backtrackiteration = 0;
        Map<Set<Tile>, Integer> constraints = new HashMap<>();
        Tile[][] tiles = game.getBoard().board;


        tiles[game.getStarty()][game.getStartx()].setRevealed(true);

        for (Tile[] row: tiles) {
            for (Tile tile : row) {
                TileInfo info = tile.getInformation();
                if (info.getClass() != TileInfoNull.class && !info.flagged)
                    constraints.put(info.neighbours, info.minesInNeighbourhood);
            }
        }

        boolean somethingChanged = true;

        //Constructing and solving constraint problems
        while (somethingChanged) {
            while (somethingChanged) {
                while (somethingChanged) {
                    iteration++;

                    if (constraints.isEmpty())
                        break;

                    if (iteration > 500)
                        return new SolveInfo(false, constraints);


                    Map<Set<Tile>, Integer> tempconstraints;

                    //do simple changes
                    tempconstraints = removeUnnecessary(constraints);

                    somethingChanged = !tempconstraints.equals(constraints);

                    constraints = tempconstraints;

                    tempconstraints = simpleFlagAndReveal(constraints);


                    if (!somethingChanged)
                        somethingChanged = !tempconstraints.equals(constraints);

                    constraints = tempconstraints;
                    if (minesLeft <= 0) {
                        return new SolveInfo(true, constraints);
                    }
                }
                Map<Set<Tile>, Integer> tempconstraints;

                //if nothing changed do more complex simplification
                tempconstraints = simplify(constraints);

                somethingChanged = !tempconstraints.equals(constraints);

                constraints = tempconstraints;
            }
            backtrackiteration++;
            Map<Set<Tile>, Integer> tempconstraints;

            //if nothing to simplify try all possible solutions and check if any tiles are mines or safe in all solutions
            tempconstraints = backtrackingSetup(constraints);

            somethingChanged = !tempconstraints.equals(constraints);
            if (backtrackiteration < 2)
                somethingChanged = true;

            constraints = tempconstraints;
        }

        return new SolveInfo(false, constraints);
    }


    /**
     * Method that sets up the recursive backtracking.
     * @param constraints given constraints
     * @return new constraints
     */
    public Map<Set<Tile>, Integer> backtrackingSetup(Map<Set<Tile>, Integer> constraints){
        Map<Tile, Integer> values = new HashMap<>();

        Set<Tile> allTilesSet = new HashSet<>();
        for (Set<Tile> constraint: constraints.keySet()){
            allTilesSet.addAll(constraint);
        }
        Tile[] allTiles = allTilesSet.toArray(new Tile[0]);

        //Artificial constraint to make sure that the recursion won't get too much to handle
        if (allTiles.length > 20)
            return constraints;

        for (Tile tile: allTiles){
            values.put(tile, 0);
        }

        //map refers to valuation of tiles in constraints
        List<Map<Tile, Integer>> allMaps;
        List<Map<Tile, Integer>> allCorrectMaps = new ArrayList<>();

        //get all maps
        allMaps = backtracking(0, allTiles, values, constraints);

        //sort maps to be correct
        for (Map<Tile, Integer> map: allMaps){
            if (checkIfCorrect(map, constraints))
                allCorrectMaps.add(map);
        }

        //Check if anything was found
        if (allCorrectMaps.isEmpty())
            return constraints;

        //find all tiles where value is the same in every map
        //if such is found make new constraint to reveal that later
        for (Tile tile: allTiles) {
            boolean tileFits = true;
            int firstvalue = allCorrectMaps.get(0).get(tile);
            for (Map<Tile, Integer> info : allCorrectMaps) {
                if (info.get(tile) != firstvalue)
                    tileFits = false;
            }
            if (tileFits){
                Set<Tile> temp = new HashSet<>();
                temp.add(tile);
                constraints.put(temp, firstvalue);
            }
        }

        return constraints;
    }


    /**
     * Recursive method for finding all possible solutions to constraints
     * @param i tile to change
     * @param allTiles array of all tiles
     * @param info chosen value for tiles
     * @param constraints constraints to check for
     * @return List of all possible solutions
     */
    public List<Map<Tile, Integer>> backtracking(int i, Tile[] allTiles, Map<Tile, Integer> info, Map<Set<Tile>, Integer> constraints){

        if (i >= allTiles.length || !checkIfTooManyMines(info, constraints)){
            List<Map<Tile, Integer>> results = new ArrayList<>();
            results.add(info);
            return results;
        }

        Tile tile = allTiles[i];

        Map<Tile, Integer> info0 = new HashMap<>(info);
        info0.put(tile, 0);
        List<Map<Tile, Integer>> result0 = backtracking(i+1, allTiles, info0, constraints);

        Map<Tile, Integer> info1 = new HashMap<>(info);
        info1.put(tile, 1);
        List<Map<Tile, Integer>> result1 = backtracking(i+1, allTiles, info1, constraints);
        result0.addAll(result1);
        return result0;

    }

    /**
     * Method that checks if info map is possible in constraints
     * @param info given info map to check
     * @param constraints constraints to adhere to
     * @return boolean true if given map is possible, false if too many mines somewhere
     */
    public boolean checkIfTooManyMines(Map<Tile, Integer> info, Map<Set<Tile>, Integer> constraints){
        for (Set<Tile> constraint : constraints.keySet()){
            int sum = 0;
            for (Tile tile: constraint){
                sum += info.get(tile);
            }

            if (sum > constraints.get(constraint) || sum > minesLeft)
                return false;
        }

        return true;
    }

    /**
     * Method that checks if info map is possible in constraints
     * @param info given info map to check
     * @param constraints constraints to adhere to
     * @return boolean true if given map is possible, false if too many or too few mines somewhere
     */
    public boolean checkIfCorrect(Map<Tile, Integer> info, Map<Set<Tile>, Integer> constraints){
        for (Set<Tile> constraint : constraints.keySet()){
            int sum = 0;
            for (Tile tile: constraint){
                sum += info.get(tile);
            }
            if (sum != constraints.get(constraint) || sum > minesLeft)
                return false;
        }

        return true;
    }

    /**
     * Checks if any constraint is a subset of other constraints, if it is then the larger one can be simplified
     * @param constraints constraints to simplify
     * @return simplified constraints
     */
    public Map<Set<Tile>, Integer> simplify(Map<Set<Tile>, Integer> constraints){
        Map<Set<Tile>, Integer> tempconstraints = new HashMap<>();

        for (Set<Tile> constraint1: constraints.keySet()){
            //If set has less than 3 elements then it cant have a non-trivial subset
            if (constraint1.size() < 3) {
                tempconstraints.put(constraint1, constraints.get(constraint1));
                continue;
            }
            for (Set<Tile> constraint2: constraints.keySet()){
                if (constraint1.containsAll(constraint2) && !constraint1.equals(constraint2)){
                    Set<Tile> tempconstraint = new HashSet<>(constraint1);
                    tempconstraint.removeAll(constraint2);
                    tempconstraints.put(tempconstraint, constraints.get(constraint1) - constraints.get(constraint2));
                }
            }
            tempconstraints.put(constraint1, constraints.get(constraint1));
        }
        return tempconstraints;
    }


    /**
     * Method for simple constraint solution, if constraint is equal to zero then can reveal all tiles in constraint, if contraint is equal to number of tiles all can be flagged
     * @param constraints given constraint to simplify
     * @return simplified constraints
     */
    public Map<Set<Tile>, Integer> simpleFlagAndReveal(Map<Set<Tile>, Integer> constraints){
        int flags = 0;
        Map<Set<Tile>, Integer> tempconstraints = new HashMap<>();

        for (Set<Tile> constraint : constraints.keySet()){
            if (constraint.isEmpty())
                continue;
            if (constraint.size() == constraints.get(constraint)){
                for (Tile tile: constraint) {
                    TileInfo info = tile.getInformation();
                    if (info.getClass() == TileInfoNull.class) {
                        tile.setFlagged(true);
                        flags++;
                    }
                }
            } else if (constraints.get(constraint) == 0) {
                for (Tile tile: constraint)
                    tile.setRevealed(true);
                for (Tile tile: constraint) {
                    TileInfo info = tile.getInformation();
                    Set<Tile> tempneighbours = new HashSet<>();
                    int tempint = 0;
                    for (Tile neighbour : info.neighbours){
                        TileInfo neighbourinfo = neighbour.getInformation();
                        if (neighbourinfo.getClass() == TileInfoNull.class){
                            tempneighbours.add(neighbour);
                        } else if (neighbourinfo.flagged) {
                            tempint++;
                        }
                    }
                    tempconstraints.put(tempneighbours, info.minesInNeighbourhood- tempint);
                }
            }else {
                tempconstraints.put(constraint, constraints.get(constraint));
            }
        }

        minesLeft -= flags;

        return tempconstraints;
    }


    /**
     * Method that removes known mines from constraints and removes contraint if it is empty
     * @param constraints constraints to simplify
     * @return simplified constraints
     */
    public Map<Set<Tile>, Integer> removeUnnecessary(Map<Set<Tile>, Integer> constraints){
        Map<Set<Tile>, Integer> tempconstraints = new HashMap<>();

        for (Set<Tile> constraint : constraints.keySet()){
            if (constraint.isEmpty())
                continue;
            Set<Tile> temp = new HashSet<>();
            Integer tempint = 0;

            for (Tile tile: constraint){

                TileInfo tileInfo = tile.getInformation();

                if (tileInfo.getClass() == TileInfoNull.class){
                    temp.add(tile);
                    continue;
                }
                if (tileInfo.flagged){
                    tempint++;
                }
            }
            if (temp.isEmpty())
                continue;
            tempconstraints.put( temp, (constraints.get(constraint)-tempint) );
        }
        return tempconstraints;
    }

}
