package th.ac.kmutt.cpe.algorithm.suntalumiti;

import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Maze;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.PathResult;

public interface ISolver {
    PathResult solve(Maze maze);
    String getName();
}
