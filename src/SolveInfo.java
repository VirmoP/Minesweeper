import java.util.Map;
import java.util.Set;

public class SolveInfo {
    boolean solved;
    Map<Set<Tile>, Integer> constraints;

    public SolveInfo(boolean solved, Map<Set<Tile>, Integer> constraints) {
        this.solved = solved;
        this.constraints = constraints;
    }
}
