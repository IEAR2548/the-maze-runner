package th.ac.kmutt.cpe.algorithm.suntalumiti.algo.classical;

import th.ac.kmutt.cpe.algorithm.suntalumiti.ISolver;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Cell;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Maze;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.PathResult;

import java.util.*;

public class AStarSolver implements ISolver {
    private static final int[][] DIRECTIONS = {
        {0, 1}, {0, -1}, {1, 0}, {-1, 0}
    };
    
    @Override
    public String getName() {
        return "A* Search Algorithm";
    }

    @Override
    public PathResult solve(Maze maze) {
        long startTime = System.currentTimeMillis(); 
        Cell start = maze.getStartCell();
        Cell goal = maze.getGoalCell();
        if (start == null || goal == null) {
            return createFailureResult(getName(), startTime);
        }
        PriorityQueue<AStarNode> openList = new PriorityQueue<>();
        Map<Cell, Integer> gCostMap = new HashMap<>();
        int hStart = calculateManhattanDistance(start, goal);
        AStarNode startNode = new AStarNode(start, null, 0, hStart);
        openList.add(startNode);
        gCostMap.put(start, 0);
        AStarNode finalNode = null;
        while (!openList.isEmpty()) {
            AStarNode current = openList.poll();
            Cell currentCell = current.cell;
            if (currentCell.equals(goal)) {
                finalNode = current;
                break;
            }
            for (int[] direction : DIRECTIONS) {
                int nextR = currentCell.getRow() + direction[0];
                int nextC = currentCell.getCol() + direction[1];
                if (!maze.isValid(nextR, nextC)) {
                    continue;
                }
                Cell neighborCell = maze.getCell(nextR, nextC);
                int tentativeGCost = current.gCost + neighborCell.getTimeCost();
                if (tentativeGCost < gCostMap.getOrDefault(neighborCell, Integer.MAX_VALUE)) {
                    gCostMap.put(neighborCell, tentativeGCost);
                    int hNeighbor = calculateManhattanDistance(neighborCell, goal);
                    AStarNode neighborNode = new AStarNode(
                        neighborCell, 
                        current, 
                        tentativeGCost, 
                        hNeighbor
                    );
                    openList.add(neighborNode);
                }
            }
        }
        long endTime = System.currentTimeMillis();    
        if (finalNode != null) {
            return reconstructPath(finalNode, startTime, endTime);
        } else {
            return createFailureResult(getName(), startTime);
        }
    }
    
    private int calculateManhattanDistance(Cell current, Cell goal) {
        return Math.abs(current.getRow() - goal.getRow()) + Math.abs(current.getCol() - goal.getCol());
    }

    private PathResult reconstructPath(AStarNode finalNode, long startTime, long endTime) {
        List<Cell> path = new LinkedList<>();
        AStarNode current = finalNode;
        while (current != null) {
            path.add(0, current.cell); 
            current = current.parent;
        }
        return new PathResult(
            path, 
            finalNode.gCost, 
            getName(), 
            endTime - startTime
        );
    }

    private PathResult createFailureResult(String algorithmName, long startTime) {
        long endTime = System.currentTimeMillis();
        return new PathResult(
            Collections.emptyList(), 
            Integer.MAX_VALUE, 
            algorithmName, 
            endTime - startTime
        );
    }
}