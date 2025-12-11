package th.ac.kmutt.cpe.algorithm.suntalumiti;

import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Maze;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.PathResult;

/**
 * Interface for all pathfinding algorithms (A*, Dijkstra, Genetic).
 */
public interface ISolver {
    
    /**
     * Solves the maze and finds the minimum time cost path.
     * @param maze The Maze object to solve.
     * @return A PathResult object containing the best path and cost.
     */
    PathResult solve(Maze maze);
    
    /**
     * Gets the name of the algorithm.
     * @return The name string.
     */
    String getName();
}
