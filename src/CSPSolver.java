import java.util.*;

public class CSPSolver implements SolverInterface{



    int minesLeft;

    /**
     * Method that acts on a game object. Change happens to tiles and their revealed status.
     *
     * @param game given Game object
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
        TileInfo firstinfo = tiles[game.getStarty()][game.getStartx()].getInformation();

        constraints.put(firstinfo.neighbours, firstinfo.minesInNeighbourhood);
        //System.out.println(constraints);
        boolean somethingChanged = true;

        while (somethingChanged) {
            while (somethingChanged) {
                while (somethingChanged) {
                    while (somethingChanged) {

                        if (constraints.isEmpty())
                            break;

                        System.out.println(iteration++);
                        System.out.println("-------------------");
                        if (iteration > 500)
                            return new SolveInfo(false, constraints);


                        Map<Set<Tile>, Integer> tempconstraints;


                        tempconstraints = removeUnnecessary(constraints);

                        somethingChanged = !tempconstraints.equals(constraints);

                        constraints = tempconstraints;

                        //System.out.println("teine" + constraints);

                        tempconstraints = simpleFlagAndReveal(constraints);

                        //System.out.println(minesLeft + " mines");

                        if (!somethingChanged)
                            somethingChanged = !tempconstraints.equals(constraints);

                        constraints = tempconstraints;
                        if (minesLeft <= 0) {
                            return new SolveInfo(true, constraints);
                        }

                        if (!somethingChanged) {
                            System.out.println("*********");
                            System.out.println(constraints);
                            System.out.println("**********");
                        }
                    }
                    System.out.println("simplify");
                    Map<Set<Tile>, Integer> tempconstraints;

                    tempconstraints = simplify(constraints);

                    somethingChanged = !tempconstraints.equals(constraints);

                    constraints = tempconstraints;
                }
                System.out.println("Backtracking " + backtrackiteration++);

                Map<Set<Tile>, Integer> tempconstraints;

                tempconstraints = backtrackingSetup(constraints);

                somethingChanged = !tempconstraints.equals(constraints);
                if (backtrackiteration < 2)
                    somethingChanged = true;

                constraints = tempconstraints;
                //System.out.println(constraints);
            }
            Set<Tile> unmarked = new HashSet<>();
            for (Tile[] row: tiles){
                for (Tile tile: row){
                    if (tile.getInformation().getClass() == TileInfoNull.class)
                        unmarked.add(tile);
                }
            }



        }
        System.out.println("==========");

        System.out.println(constraints);

        System.out.println("==========");

        return new SolveInfo(false, constraints);
    }


    public Map<Set<Tile>, Integer> backtrackingSetup(Map<Set<Tile>, Integer> constraints){
        Map<Tile, Integer> values = new HashMap<>();

        Set<Tile> allTilesSet = new HashSet<>();
        for (Set<Tile> constraint: constraints.keySet()){
            allTilesSet.addAll(constraint);
        }
        Tile[] allTiles = allTilesSet.toArray(new Tile[0]);

        //Artificial constraint to make sure that the recursion wont get too much to handle
        if (allTiles.length > 10)
            return constraints;

        for (Tile tile: allTiles){
            values.put(tile, 0);
        }

        List<Map<Tile, Integer>> allMaps;
        List<Map<Tile, Integer>> allCorrectMaps = new ArrayList<>();

        allMaps = backtracking(0, allTiles, values, constraints);

        for (Map<Tile, Integer> map: allMaps){
            if (checkIfCorrect(map, constraints))
                allCorrectMaps.add(map);
        }


        //Check if anything was found
        if (allCorrectMaps.isEmpty())
            return constraints;



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

/*
    public Set<Set<Set<Tile>>> turnIntoSubsets(Map<Set<Tile>, Integer> constraints){

        Set<Tile> allTiles = new HashSet<>();

        for (Set<Tile> constraint: constraints.keySet())
            allTiles.addAll(constraint);

        Set<Set<Set<Tile>>> subsets = new HashSet<>();

        for (Tile tile: allTiles){
            Set<Set<Tile>> subset = new HashSet<>();
            for (Set<Tile> constraint: constraints.keySet()){
                if (constraint.contains(tile))
                    subset.add(constraint);
            }
            subsets.add(subset);
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            Set<Set<Set<Tile>>> tempsubsets = new HashSet<>();

            for (Set<Set<Tile>> subset : subsets) {
                Set<Set<Tile>> tempsubset = new HashSet<>(subset);
                for (Set<Set<Tile>> subset1 : subsets) {
                    for (Set<Tile> set : subset1)
                        if (subset.contains(set)) {
                            tempsubset.addAll(subset1);
                            changed = true;
                        }
                }
                tempsubsets.add(tempsubset);
            }
            subsets = tempsubsets;
        }

        return subsets;
    }

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
