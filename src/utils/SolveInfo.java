package utils;

import java.util.Map;
import java.util.Set;

/**
 * Utily class for moving info about solution of board
 */
public class SolveInfo {
    public boolean solved;
    public Map<Set<Tile>, Integer> constraints;

    public SolveInfo(boolean solved, Map<Set<Tile>, Integer> constraints) {
        this.solved = solved;
        this.constraints = constraints;
    }
}
